package com.shades.utilities;


public class Enumerations {

    public enum Sellers{

        Welse(1, "Welse"), Shades(2, "Shades"), Sunsky(3, "Sunsky"), Wayuu(4, "Wayuu");

        Sellers(int sellerId, String sellerName) {
            this.sellerId = sellerId;
            this.sellerName = sellerName;
        }

        int sellerId;
        String sellerName;

        public int getSellerId() {
            return sellerId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public static String getSellerName(int id){

            switch (id){
                case 1:
                    return Welse.getSellerName();
                case 2:
                    return Shades.getSellerName();
                case 3:
                    return Sunsky.getSellerName();
                case 4:
                    return Wayuu.getSellerName();
                default:
                    return "Supplier Not found";
            }
        }
    }

    public enum Markets{
        Amazon(100, "Amazon"), Ebay(101, "Ebay");

        Markets(int marketId, String marketName) {
            this.marketId = marketId;
            this.marketName = marketName;
        }

        int marketId;
        String marketName;

        public int getMarketId() {
            return marketId;
        }

        public String getMarketName() {
            return marketName;
        }

        public static String getMarketName(int id){

            switch (id){
                case 100:
                    return Amazon.getMarketName();
                case 101:
                    return Ebay.getMarketName();
                default:
                    return "Market Not found";
            }
        }
    }

    public enum Suppliers {

        AZEnum(500, "AZ Trading"), FXEnum(501, "Fragrance X"), TDEnum(502, "Teledynamics"), Shades(599, "Shades");

        int supplierId;
        String supplierName;

        Suppliers(int id, String name){
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
                case 599:
                    return Shades.getSupplierName();
                default:
                    return "Supplier Not found";
            }
        }
    }
}
