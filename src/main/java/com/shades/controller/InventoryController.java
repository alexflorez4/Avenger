package com.shades.controller;

import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;
import com.shades.services.fragx.FragxService;
import com.shades.services.misc.AppServices;
import com.shades.utilities.Utils;
import com.shades.views.InventoryAllProducts;
import com.shades.views.InventoryUserReport;
import com.shades.views.InvoiceOrders;
import com.shades.views.StagedOrdersExcel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class InventoryController {

    private static final Logger logger = Logger.getLogger(InventoryController.class);

    @Autowired
    private FragxService fragxService;

    @Autowired
    private AppServices appServices;

    @RequestMapping("/")
    public String actionsManager(){
        logger.info("Passing through controller!!!");
        return "index";
    }

    @RequestMapping(value = "/pages/inventoryUpdate", method = RequestMethod.POST)
    public ModelAndView updateInventory(@RequestParam("azFile") MultipartFile file, @RequestParam("supplier") Integer supplierId){

        if(supplierId == 0){
            return new ModelAndView("inventoryUpdate", "status", "Please select a supplier");
        }

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "uploads");
        String status;

        if(!dir.exists())
            dir.mkdirs();

        File serverFile = new File(dir.getAbsolutePath() + File.separator + file);
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            byte[] bytes = file.getBytes();
            stream.write(bytes);
            stream.close();
            appServices.updateInventory(serverFile, supplierId);
        } catch (Exception e) {
            status = "Error. " + e;
            return new ModelAndView("inventoryUpdate", "status", status);
        }

        status = "success";
        return new ModelAndView("inventoryUpdate", "status", status);
    }

    @RequestMapping(value = "/pages/processFragXInventory", method = RequestMethod.POST)
    public ModelAndView updateFragXInventory(){
        logger.info("Updating FragranceX inventory at " + System.nanoTime());
        String fragStatus;
        try {
            fragxService.updateInventory();
            fragStatus = "success";
        } catch (ShadesException e) {
            fragStatus = "Error updating inventory. Cause: " + e;
        }

        return new ModelAndView("inventoryUpdate", "fragStatus", fragStatus);
    }


    @RequestMapping("/orders")
    public ModelAndView viewCart()
    {
        logger.info("Requesting Orders");
        List<String> sku = appServices.allProductsSet();
        return new ModelAndView("orderManual","sku",sku);
    }

    @RequestMapping(value = "/singleOrder", method = RequestMethod.POST)
    public ModelAndView newSingleOrder(@RequestParam("orderNo") Integer orderNo, @RequestParam("reference") String reference,
            @RequestParam("itemQuantity") Integer quantity, @RequestParam("bName") String buyerName, @RequestParam("bAddress") String buyerAddress,
            @RequestParam("bApt") String apartment, @RequestParam("bCiti") String city, @RequestParam("bState") String state,
            @RequestParam("bZip") String zipCode, @RequestParam("bCountry") String country, @RequestParam("bNotes") String notes,
            @RequestParam("market") Integer marketId){

        OrderEntity order = new OrderEntity();
        order.setOrderId(orderNo);
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

        String status;
        try {
            appServices.processNewSingleOrder(order);

        } catch (ShadesException e) {
            status = "Error: " + e.getMessage();
            return new ModelAndView("orderManual", "status", status);
        }
        status = "success";
        return new ModelAndView("orderManual", "status", status);
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

    @RequestMapping("/myCompletedOrders")
    public ModelAndView displayMyCompletedOrders() throws ShadesException {
        List<OrderEntity> completedOrders = appServices.getSellerCompletedOrders();
        return new ModelAndView("completedOrders", "orders", completedOrders);
    }

    @RequestMapping("/completedOrdersAdmin")
    public ModelAndView displayCompletedOrdersAdmin() throws ShadesException {
        List<OrderEntity> completedOrders = appServices.getAllCompletedOrders();
        return new ModelAndView("completedOrders", "orders", completedOrders);
    }

    @RequestMapping("/allNewOrdersAdmin")
    public ModelAndView displayAllNewOrders() throws ShadesException {
        List<OrderEntity> orders = appServices.getAllNewOrders();
        return new ModelAndView("newOrdersAdmin", "orders", orders);
    }

    @RequestMapping("/stagedOrdersAdmin")
    public ModelAndView displayStagedOrders() throws ShadesException {
        List<OrderEntity> orders = appServices.getStagedOrders();
        return new ModelAndView("stagedOrdersAdmin", "orders", orders);
    }

    @RequestMapping("/downloadStagedOrder")
    public ModelAndView downloadStagedOrders() throws ShadesException {
        List<OrderEntity> orders = appServices.getStagedOrders();
        return new ModelAndView(new StagedOrdersExcel(), "orders", orders);
    }

    @RequestMapping("/generateInvoice")
    public ModelAndView downloadSellerInvoices(@RequestParam("from") String from, @RequestParam("to") String to) throws ShadesException {
        //List<OrderEntity> orders = appServices.getStagedOrders();
        System.out.println("From: " + from + " - to " + to);
        List<OrderEntity> orders = appServices.getOrdersForInvoice(from + " 00:00:00", to + " 23:59:59");
        return new ModelAndView(new InvoiceOrders(), "orders", orders);
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

    @RequestMapping(value = "/uploadTrackingIds", method = RequestMethod.POST)
    public ModelAndView trackingUpload(@RequestParam("ordersFile") MultipartFile file){

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "uploads");

        if(!dir.exists())
            dir.mkdirs();

        File serverFile = new File(dir.getAbsolutePath() + File.separator + file);

        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            byte[] bytes = file.getBytes();
            stream.write(bytes);
            stream.close();
            appServices.updateTrackingIds(serverFile);

        } catch (Exception e) {
            return new ModelAndView("stagedOrdersAdmin", "status", "Error." + e);
        }
        List<OrderEntity> orders = appServices.getStagedOrders();
        return new ModelAndView("stagedOrdersAdmin", "orders", orders);
    }

    @RequestMapping(value = "/pages/checkMyInventory", method = RequestMethod.POST)
    public ModelAndView checkUserInventory(@RequestParam("myInventory") MultipartFile file){

        File serverFile;
        List<InventoryEntity> items;
        try {
            serverFile = Utils.saveMultipartFileToServer(file);
            items = appServices.compareUserInventory(serverFile);
        } catch (IOException e) {
            return new ModelAndView("inventoryUser", "status", "Error." + e);
        }

        return new ModelAndView(new InventoryUserReport(), "items", items);
    }

    @RequestMapping(value = "/pages/allInventoryComparison", method = RequestMethod.POST)
    public ModelAndView compareAllInventory(@RequestParam("myInventory2") MultipartFile file){

        File serverFile;
        List<InventoryEntity> items;
        try {
            serverFile = Utils.saveMultipartFileToServer(file);
            items = appServices.compareAllUserInventory(serverFile);
        } catch (IOException e) {
            return new ModelAndView("inventoryUser", "status", "Error." + e);
        }

        return new ModelAndView(new InventoryUserReport(), "items", items);
    }

    @RequestMapping("/pages/downloadAllInventory")
    public ModelAndView downloadAllInventory() throws ShadesException {
        List<InventoryEntity> items = appServices.getAllInventory();
        return new ModelAndView(new InventoryAllProducts(), "items", items);
    }

    @RequestMapping("/stageOrder")
    public ModelAndView stageOrder(@RequestParam("orderToStage") String [] orderToStage){
        System.out.println("**" + orderToStage);
        for(String temp : orderToStage){
            System.out.println(temp);
        }
        appServices.stageOrders(orderToStage);
        List<OrderEntity> orders = appServices.getAllNewOrders();
        return new ModelAndView("newOrdersAdmin", "orders", orders);
    }

    @RequestMapping(value = "/editOrder/{id}", method = RequestMethod.GET)
    public ModelAndView viewSingleOrder(@PathVariable String id){
        System.out.println("Order id: " + id);
        OrderEntity order = appServices.getOrderById(Integer.valueOf(id));
        return new ModelAndView("editOrder", "order", order);
    }
}
