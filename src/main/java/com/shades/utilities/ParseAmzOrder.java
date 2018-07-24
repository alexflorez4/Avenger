package com.shades.utilities;

import Entities.OrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.shades.utilities.ParseAmzOrder.EnumSuppliers.AZEnum;
import static com.shades.utilities.ParseAmzOrder.EnumSuppliers.FXEnum;
import static com.shades.utilities.ParseAmzOrder.EnumSuppliers.TDEnum;

public class ParseAmzOrder {

    private static final String TELE_DYN = "([A-Za-z]{2,3})([-])(\\d+\\w+)(//s)";
    private static final String FRAGX = "[0-9]{6}";

    public enum EnumSuppliers {

        AZEnum(500, "AZ Trading"), FXEnum(501, "Fragrance X"), TDEnum(502, "Teledynamics");

        int supplierId;
        String supplierName;

        EnumSuppliers(int id, String name){
            this.supplierId = id;
            this.supplierName = name;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public static String getSupplierName(int id){

            switch (id){
                case 500:
                    return AZEnum.getSupplierName();
                case 501:
                    return FXEnum.getSupplierName();
                case 502:
                    return TDEnum.getSupplierName();
                default:
                    return "Supplier Not found";
            }
        }
    }

    public List<OrderEntity> parse(File file, int sellerId, int nextOrderId){

        List<OrderEntity> orders = new ArrayList<>();
        Document doc;

        try {

            doc = Jsoup.parse(file, "UTF-8");
            Elements content = doc.getElementsByAttributeValueStarting("id", "myo-packing-slip-");

            for(Element nextElm : content){

                OrderEntity order = new OrderEntity();

                //Seller Id:
                order.setSellerId(sellerId);

                //Market Id:
                order.setMarketId(100);

                //Amazon order id:
                Elements orderId = nextElm.getElementsByAttributeValueMatching("class", "a-section myo-orderId");


                if(StringUtils.isBlank(orderId.text()))
                    continue;

                order.setOrderId(nextOrderId);
                nextOrderId++;

                String amzOrderId = StringUtils.strip(orderId.text(), "Order ID:");
                order.setMarketOrderId(amzOrderId);

                Elements addressElm = nextElm.getElementsByAttributeValueMatching("class", "a-section myo-address");

                String orderAddressHtml = addressElm.html();
                String addRem = StringUtils.substringAfter(orderAddressHtml, "<span id=\"myo-order-details-buyer-address\" class=\"myo-wrap\">");

                //Buyer name:
                String buyerName = StringUtils.substringBefore(addRem, "<br>").trim();
                order.setBuyerName(buyerName);
                addRem = StringUtils.substringAfter(addRem, "<br>");

                //Street
                String address = StringUtils.substringBefore(addRem, "<br>").trim();
                order.setStreet(address);
                addRem = StringUtils.substringAfter(addRem, "<br>");

                //City
                String city = StringUtils.substringBefore(addRem, "<span").trim().replace(",","");
                city = StringUtils.replace(city, "<br>", "-");
                order.setCity(city);
                addRem = StringUtils.substringAfter(addRem, "<span class=\"a-letter-space\">");

                //Street2
                String addressOther = StringUtils.substringBefore(addRem, "</span>").trim();
                order.setStreet2(addressOther);
                addRem = StringUtils.substringAfter(addRem, "</span>");

                //State
                String state = StringUtils.substringBefore(addRem, "<span").trim();
                order.setState(state);
                addRem = StringUtils.substringAfter(addRem, "<span class=\"a-letter-space\">");

                //Other
                String stateOther = StringUtils.substringBefore(addRem, "</span").trim();
                order.setOther(stateOther);
                addRem = StringUtils.substringAfter(addRem, "</span>").trim();

                String zipCode;
                String country;

                if(addRem.contains("<br>")){
                    zipCode = StringUtils.substringBefore(addRem, "<br>").trim();
                    addRem = StringUtils.substringAfter(addRem, "<br>");
                    country = StringUtils.substringBefore(addRem, "</span>").trim();
                }else{
                    zipCode = StringUtils.substringBefore(addRem, "</span>").trim();
                    country = "United States";
                }

                //Zip code
                order.setZipCode(zipCode);

                //Country
                order.setCountry(country);

                //Quantity:
                Elements qty = nextElm.getElementsByAttributeValueMatching("class", "a-text-center table-border");
                String quantity = qty.first().text();
                order.setQuantity(Integer.valueOf(quantity));

                Elements orderDetails = nextElm.getElementsByAttributeValueMatching("class", "a-row");
                List<String> details = orderDetails.eachText();

                //Sku:
                String sku = details.stream().filter(s-> s.contains("SKU:")).reduce("", String::concat).trim();
                sku = StringUtils.removeAll(StringUtils.strip(sku, "SKU:").trim(), "(\\s+)(\\d+)|(\\s+)([Xx])(\\d+)").trim();
                sku = StringUtils.removePattern(sku, "[_]+\\d++").trim();
                order.setSku(sku);

                //ASIN
                String asin = details.stream().filter(s-> s.contains("ASIN:")).reduce("", String::concat).trim();
                asin = StringUtils.removeAll(asin, "ASIN:").trim();
                order.setAsin(asin);

                //Used for supplier name - String listingId = details.stream().filter(s-> s.contains("Listing ID:")).reduce("", String::concat).trim();

                //Market Sold Amount
                String total = details.stream().filter(s-> s.contains("Grand total: $")).reduce("", String::concat).trim();
                total = StringUtils.replaceAll(total, "Grand total:", "").trim();
                total = StringUtils.removeFirst(total, "[^A-Za-z0-9.]").trim();
                order.setMarketSoldAmount(Double.valueOf(total));

                EnumSuppliers suppEnum = (EnumSuppliers) supplierChecker(sku);
                int supplierId = suppEnum.getSupplierId();
                order.setSupplierId(supplierId);

                orders.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static Enum<EnumSuppliers> supplierChecker(String sku2Check){

        if(Pattern.compile(FRAGX).matcher(sku2Check).matches()){
            return FXEnum;
        }else if(Pattern.compile(TELE_DYN).matcher(sku2Check).matches()){
            return TDEnum;
        }else{
            return AZEnum;
        }
    }
}
