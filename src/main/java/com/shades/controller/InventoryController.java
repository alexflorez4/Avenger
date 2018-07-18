package com.shades.controller;

import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;
import com.shades.services.az.AzProcess;
import com.shades.services.fragx.FragxService;
import com.shades.services.misc.AppServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
        /*UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("************************" + userDetails.getUsername());
        System.out.println("Authorities: " + userDetails.getAuthorities().size());
        request.getSession().setAttribute("roles", String.valueOf(userDetails.getAuthorities().size()));
        request.getSession().setAttribute("user",userDetails.getUsername());*/
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

        try {
            azProcess.updateInventory(serverFile);
        } catch (Exception e) {
            logger.error("Exception thrown " + e.getMessage());
            e.printStackTrace();
        }
        String status = "success";
        return new ModelAndView("inventoryUpdate", "status", status);
    }

    @RequestMapping(value = "/pages/processFragXInventory", method = RequestMethod.POST)
    public ModelAndView updateFragXInventory(){
        logger.info("Updating FragranceX inventory at " + System.nanoTime());
        fragxService.updateInventory();
        String fragStatus = "success";
        return new ModelAndView("inventoryUpdate", "fragStatus", fragStatus);
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
            @RequestParam("market") Integer marketId,
            @RequestParam("seller") String sellerName){

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
            appServices.processNewSingleOrder(order, sellerName);

        } catch (Exception e) { //// TODO: 6/19/2018 Handle errors
            logger.info("Transaction failed");
            return new ModelAndView("placeholder");
        }

        return new ModelAndView("index", "", null);
    }

    @RequestMapping("/express")
    public ModelAndView displayExpressUpload(){
        return new ModelAndView("express");
    }

    @RequestMapping("/myPendingOrders")
    public ModelAndView displayMyPendingOrders() throws ShadesException {
        List<OrderEntity> pendingOrders = appServices.getSellerPendingOrders();
        return new ModelAndView("pendingOrders", "orders", pendingOrders);
    }

    @RequestMapping(value = "/processExpress", method = RequestMethod.POST)
    public ModelAndView orderExpress(@RequestParam("ordersFile") MultipartFile file){

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "uploads");

        if(!dir.exists())
            dir.mkdirs();

        File serverFile = new File(dir.getAbsolutePath() + File.separator + file);
        Set<OrderEntity> failingOrders = new HashSet<>();
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            byte[] bytes = file.getBytes();
            stream.write(bytes);
            stream.close();
            failingOrders =  appServices.processExpressOrder(serverFile);
        } catch (ShadesException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(failingOrders.isEmpty()){
            return new ModelAndView("express", "OrderSuccess", true);
        }
        return new ModelAndView("express", "failingOrdersSet", failingOrders);
    }

    @RequestMapping("/viewOrder/{id}")
    public ModelAndView viewSingleOrder(@PathVariable int id){
        System.out.println("Order id: " + id);
        return new ModelAndView("viewOrder");
    }

}
