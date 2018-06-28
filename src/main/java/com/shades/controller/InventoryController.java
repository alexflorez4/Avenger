package com.shades.controller;

import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;
import com.shades.services.az.AzProcess;
import com.shades.services.fragx.FragxService;
import com.shades.services.misc.AppServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.List;


@Controller
public class InventoryController {

    private static final Logger logger = Logger.getLogger(InventoryController.class);

    @Autowired
    private AzProcess azProcess;

    @Autowired
    private FragxService fragxService;

    @Autowired
    private AppServices appServices;

    @RequestMapping("/")
    public String actionsManager(){
        logger.info("Passing through controller!!!");
        return "index";
    }

    @RequestMapping(value = "/pages/processAZImportFile", method = RequestMethod.POST)
    public ModelAndView processAZInventoryUpdate(@RequestParam("azFile") MultipartFile file){

        logger.info("Controller accessed.");

        String rootPath = System.getProperty("catalina.home");
        logger.info("System property, catalina.home: " + rootPath);


        File dir = new File(rootPath + File.separator + "uploads");

        if(!dir.exists())
            dir.mkdirs();

        File serverFile = new File(dir.getAbsolutePath() + File.separator + file);
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            byte[] bytes = file.getBytes();
            stream.write(bytes);
            stream.close();
            logger.info("Server File Location: "  + serverFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        File azInventory = null;
        try {
            //azInventory = Utils.multipartToFile(serverFile);
            azProcess.updateInventory(serverFile);
        } catch (Exception e) {
            logger.error("Exception thrown " + e.getMessage());
            e.printStackTrace();
        }
        return new ModelAndView("inventoryUpdate");
    }

    @RequestMapping(value = "/pages/processFragXInventory", method = RequestMethod.POST)
    public ModelAndView updateFragXInventory(){
        logger.info("Updating FragranceX inventory at " + System.nanoTime());
        fragxService.updateInventory();
        return new ModelAndView("pages/inventoryUpdate");
    }


    @RequestMapping("/orders")
    public ModelAndView viewCart()
    {
        logger.info("Requesting Orders");
        List<String> sku = appServices.allProductsSet();
        return new ModelAndView("orderManual","sku",sku);
    }

    @RequestMapping("/singleOrder")
    public ModelAndView newSingleOrder(
            @RequestParam("reference") String reference,
            @RequestParam("itemQuantity") Integer quantity,
            @RequestParam("bName") String buyerName,
            @RequestParam("bAddress") String buyerAddress,
            @RequestParam("bApt") String apartment,
            @RequestParam("bCiti") String city,
            @RequestParam("bState") String state,
            @RequestParam("bZip") String zipCode,
            @RequestParam("bCountry") String country,
            @RequestParam("bNotes") String notes,
            @RequestParam("market") Integer marketId){

        OrderEntity order = new OrderEntity();
        order.setSku(reference);
        order.setQuantity(quantity);
        order.setBuyerName(buyerName);
        order.setStreet(buyerAddress);
        order.setStreet2(apartment);
        order.setCity(city);
        order.setState(state);
        order.setZipCode(zipCode);
        order.setCountry(country);
        order.setObservations(notes);
        order.setMarketId(marketId);

        try {
            appServices.processNewSingleOrder(order);

        } catch (ShadesException e) { //// TODO: 6/19/2018 Handle errors
            e.printStackTrace();
        }

        return new ModelAndView("index", "", null);
    }
}
