package com.shades.services.fragx;

import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.BadItemIdException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Helpers.Constants;
import com.shades.services.fragx.Interfaces.IFrgxListingApiClient;
import com.shades.services.fragx.Models.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * API client that handles product information requests.
 */
public class FrgxListingApiClient extends FrgxApiClient implements IFrgxListingApiClient {
    /**
     * Initializes a new product info API client.
     * @param accessId Api access ID found in FragranceX Api documentation
     * @param accessKey Api access key found in FragranceX Api documentation
     * @throws IOException throws IOException
     */
    public FrgxListingApiClient(String accessId, String accessKey) throws IOException, BadAccessIdOrKeyException {
        super(accessId, accessKey);
    }

    /**
     * Initializes a new product info API client.  This constructor should only be
     * used after a constructor with API access Id and API access key.
     * @throws BadAccessIdOrKeyException Access Id or Access key is wrong
     */
    public FrgxListingApiClient() throws BadAccessIdOrKeyException {
        super();
    }

    /**
     * Gets product information by item number.
     * @param id Item number of the product you would like to retrieve.
     * @return Product with matching item number.
     */
    @Override
    public Product getProductById(String id) throws BadAccessIdOrKeyException, BadItemIdException, HttpErrorException {
    	if(id == null || id.equals(""))
    		throw (new BadItemIdException("Item Id can't be empty!"));
        return getProduct(Constants.FRGXAPI_BASE_URL + Constants.FRGXAPI_PRODUCT_ID + id);
    }

    /**
     * Gets product information by UPC.
     * @param upcCode UPC of the product you would like to retrieve.
     * @return Product with matching UPC.
     */
    @Override
    public Product getProductByUPC(String upcCode) throws BadAccessIdOrKeyException, BadItemIdException, HttpErrorException {
    	if(upcCode == null || upcCode.equals(""))
    		throw (new BadItemIdException("UPC can't be empty!"));
        return getProduct(Constants.FRGXAPI_BASE_URL + Constants.FRGXAPI_UPC + upcCode);
    }

    /**
     * Passes URL to the helper class and parses the returned value from the helper class.
     * @param url URL of the HTTP request
     * @return Product that matches your request
     */
    //private Product getProduct(String url){
    private Product getProduct(String url) throws BadAccessIdOrKeyException, BadItemIdException, HttpErrorException {
        Product res = null;
        try {
            String result = _frgxApicallHelper.GetApi(url);
            if(result.equals("HTTP/1.1 500 Internal Server Error")){
                throw (new BadItemIdException("The ItemId is Wrong!"));
            }
            JsonElement json = new JsonParser().parse(result);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            res = gson.fromJson(json.toString(),Product.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    
    /**
     * Gets the list of all products.  Limited to 8 requests per day.
     * @return productList : A list of all products.
     */
    @Override
    public List<Product> getAllProductList() throws BadAccessIdOrKeyException, HttpErrorException {
        return getList(Constants.FRGXAPI_BASE_URL + Constants.FRGXAPI_LIST);
    }

    /**
     *Gets the list of all products by brand name (not case-sensitive) that are in stock.
     * @param brand  the name of the brand you would like to search by.
     * @return product All products with matching brand name.
     */
    @Override
    public List<Product> getProductByBrand(String brand) throws BadAccessIdOrKeyException, HttpErrorException {

        brand = UrlEscapers.urlFragmentEscaper().escape(brand);

        return getList(Constants.FRGXAPI_BASE_URL + Constants.FRGXAPI_GET_BY_BRAND + brand);
    }


    /**
     * Gets the list of all products by brand name (not case-sensitive)
     * @param brand The name of the brand you would like to search by.
     * @param instock If true, displays all in-stock products.
     * @return Gets the list of products that have matching brand name.
     */
    @Override
    public List<Product> getProductByBrand(String brand, boolean instock) throws BadAccessIdOrKeyException, HttpErrorException {
        brand = UrlEscapers.urlFragmentEscaper().escape(brand);
        return getList(Constants.FRGXAPI_BASE_URL + Constants.FRGXAPI_GET_BY_BRAND + brand + "?instockOnly=" + instock);
    }

    /**
     * Gets the list of products with a matching parent code.
     * @param parentCode Parent code of the product
     * @return List of products with matching parent code
     */
    @Override
    public List<Product> getProductByParentCode(String parentCode) throws BadAccessIdOrKeyException, HttpErrorException {
        return getList(Constants.FRGXAPI_BASE_URL + Constants.FRGXAPI_LIST + parentCode);
    }

    private List<Product> getList(String url) throws BadAccessIdOrKeyException, HttpErrorException {
        List<Product> res = new ArrayList<>();
        String result = "";
        try {
            result = _frgxApicallHelper.GetApi(url);
            JsonArray json = new JsonParser().parse(result).getAsJsonArray();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Iterator it = json.iterator();
            while(it.hasNext()){
                res.add(gson.fromJson(it.next().toString(),Product.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(java.lang.IllegalStateException e){
            System.out.println(result);
        }
        return res;
    }
}