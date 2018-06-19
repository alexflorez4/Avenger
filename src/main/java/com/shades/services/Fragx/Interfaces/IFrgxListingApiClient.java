package com.shades.services.fragx.Interfaces;

import com.shades.services.fragx.Exceptions.BadAccessIdOrKeyException;
import com.shades.services.fragx.Exceptions.BadItemIdException;
import com.shades.services.fragx.Exceptions.HttpErrorException;
import com.shades.services.fragx.Models.Product;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Interface for FrgxProductInfoApiClient
 */
public interface IFrgxListingApiClient {
    Product getProductById(String id) throws BadAccessIdOrKeyException, BadItemIdException, HttpErrorException;
    Product getProductByUPC(String upcCode) throws BadAccessIdOrKeyException, BadItemIdException, HttpErrorException;
    List<Product> getAllProductList() throws BadAccessIdOrKeyException, HttpErrorException;
    List<Product> getProductByBrand(String brand) throws URISyntaxException, BadAccessIdOrKeyException, HttpErrorException;
    List<Product> getProductByBrand(String brand, boolean instock) throws BadAccessIdOrKeyException, HttpErrorException;
    List<Product> getProductByParentCode(String parentCode) throws BadAccessIdOrKeyException, HttpErrorException;
}
