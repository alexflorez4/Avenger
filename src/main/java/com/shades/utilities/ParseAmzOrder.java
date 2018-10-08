package com.shades.utilities;

import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParseAmzOrder {

    private static int nextShadesOrderId;

    public List<OrderEntity> parse(File file, int sellerId, int nextOrderId) throws ShadesException {

        nextShadesOrderId = nextOrderId;

        List<OrderEntity> orders = new ArrayList<>();
        Document doc;

        try {

            doc = Jsoup.parse(file, "UTF-8");
            Elements content = doc.getElementsByAttributeValueStarting("id", "myo-packing-slip-");

            for(Element nextElm : content){

                OrderEntity order = new OrderEntity();

                //Seller Id:
                order.setSellerId(sellerId);
                order.setSellerName(Enumerations.Sellers.getSellerName(sellerId));

                //Market Id:
                order.setMarketId(100);

                //Amazon order id:
                Elements orderId = nextElm.getElementsByAttributeValueMatching("class", "a-section myo-orderId");

                if(StringUtils.isBlank(orderId.text()))
                    continue;

                String amzOrderId = StringUtils.strip(orderId.text(), "Order ID:");
                order.setMarketOrderId(amzOrderId);

                Elements shippingInfo = nextElm.getElementsByClass("a-section a-spacing-none a-padding-mini table-border");
                processElementsShippingInfo(order, shippingInfo);


                //Quantity:
                Elements tableBodyElement = nextElm.getElementsByTag("tbody");

                Element tableOrderDetails = tableBodyElement.get(0);
                Elements productDetailsTableRows = tableOrderDetails.children();

                //Process single order
                if(productDetailsTableRows.size() == 2){
                    Element orderDetail = productDetailsTableRows.get(1);
                    processSingleOrderDetails(order, orderDetail, orders);
                }else {

                    //Table header
                    Iterator productDetailsIterator = productDetailsTableRows.iterator();
                    productDetailsIterator.next();

                    //First Order
                    Element firstOrderDetail = (Element) productDetailsIterator.next();
                    OrderEntity firstMultiOrder = new OrderEntity(order);
                    firstMultiOrder.setObservations("MULTI-ORDER: " + order.getMarketOrderId());
                    processSingleOrderDetails(firstMultiOrder, firstOrderDetail, orders);

                    //Next Orders
                    while (productDetailsIterator.hasNext()){
                        Element orderDetail = (Element) productDetailsIterator.next();
                        OrderEntity multiOrder = new OrderEntity(order);
                        multiOrder.setObservations("MULTI-ORDER: " + order.getMarketOrderId());
                        multiOrder.setMarketOrderId(null);
                        processSingleOrderDetails(multiOrder, orderDetail, orders);
                    }
                }
            }
        } catch (IOException e) {
            throw new ShadesException("Error parsing orders in bulk.");
        }

        return orders;
    }

    private void processElementsShippingInfo(OrderEntity order, Elements shippingElms) throws ShadesException {

        String orderAddressHtml = shippingElms.html();
        String addRem = StringUtils.substringAfter(orderAddressHtml, "<span id=\"myo-order-details-buyer-address\" class=\"myo-wrap\">");

        //Buyer name:
        String buyerName = StringUtils.substringBefore(addRem, "<br>").trim();
        order.setBuyerName(buyerName);
        addRem = StringUtils.substringAfter(addRem, "<br>");

        //Street and City
        String streetAndCity = StringUtils.substringBefore(addRem, ",<span");
        String [] streetAndCityArr = StringUtils.splitByWholeSeparator(streetAndCity, "<br>");

        if(streetAndCityArr.length == 2){
            order.setStreet(streetAndCityArr[0].trim());
            order.setCity(streetAndCityArr[1].trim());
        }else if(streetAndCityArr.length == 3){
            order.setStreet(streetAndCityArr[0].trim());
            order.setStreet2(streetAndCityArr[1].trim());
            order.setCity(streetAndCityArr[2].trim());
        }else {
            throw new ShadesException("Error parsing address in street and city");
        }
        addRem = StringUtils.substringAfter(addRem, "<span class=\"a-letter-space\"></span>").trim();

        //State
        String state = StringUtils.substringBefore(addRem, "<span class=\"a-letter-space\"></span>").trim();
        order.setState(state);
        addRem = StringUtils.substringAfter(addRem, "<span class=\"a-letter-space\"></span>");

        String zipAndCountry = StringUtils.substringBefore(addRem, "</span>").trim();
        String [] zipAndCountryArr = StringUtils.splitByWholeSeparator(zipAndCountry, "<br>");

        if(zipAndCountryArr.length == 1){
            order.setZipCode(zipAndCountryArr[0].trim());
            order.setCountry("United States");
        }else if(zipAndCountryArr.length == 2){
            order.setZipCode(zipAndCountryArr[0].trim());
            order.setCountry(zipAndCountryArr[1].trim());
        }else{
            System.out.println("");
            throw new ShadesException("Error parsing address, zip & country. In order # " + order.getMarketOrderId());
        }

        addRem = StringUtils.substringAfter(addRem, "Seller Name:");
        String [] otherShippingDetails = StringUtils.substringsBetween(addRem, "<span>", "</span>");

        //Service
        order.setShippingService(otherShippingDetails[1]);
    }

    private void processSingleOrderDetails(OrderEntity order, Element orderDetail, List<OrderEntity> orders) {

        //Shades Order Id
        order.setOrderId(nextShadesOrderId);

        String detail = orderDetail.html();

        //Quantity
        String quantity = StringUtils.substringBetween(detail, "<td class=\"a-text-center table-border\">", "</td>");
        order.setQuantity(Integer.valueOf(quantity));

        //SKU
        String detRem = StringUtils.substringAfter(detail, "SKU:");
        String sku = StringUtils.substringBetween(detRem, "<span>", "</span>").trim();
        order.setSku(Utils.parseSku(sku));

        //ASIN
        detRem = StringUtils.substringAfter(detRem, "ASIN:");
        String asin = StringUtils.substringBetween(detRem, "<span>", "</span>").trim();
        order.setAsin(asin);

        //Market Listing ID
        detRem = StringUtils.substringAfter(detRem, "Listing ID:");
        String marketListingId = StringUtils.substringBetween(detRem, "<span id=\"myo-order-details-product-listing-id\">", "</span>").trim();
        order.setMarketListingId(marketListingId);

        //Market Sold Amount:
        detRem = StringUtils.substringAfter(detRem, "Item total");
        String total = StringUtils.substringBetween(detRem, "<span id=\"myo-order-details-item-total\" class=\"a-text-bold\">", "</span>");
        total = StringUtils.removeFirst(total, "[^A-Za-z0-9.]").trim();
        total = StringUtils.remove(total, "CAD");
        order.setMarketSoldAmount(Double.valueOf(total));

        //Supplier
        //Enumerations.Suppliers suppEnum = (Enumerations.Suppliers) Utils.supplierChecker(sku);
        //order.setSupplierId(suppEnum.getSupplierId());

        orders.add(order);
        nextShadesOrderId++;
    }

}
