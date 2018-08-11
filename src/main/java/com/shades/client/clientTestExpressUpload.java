package com.shades.client;

import Entities.OrderEntity;
import com.shades.services.misc.AppServices;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.List;

public class clientTestExpressUpload {

    static Logger logger = Logger.getLogger(clientTestExpressUpload.class);

    public static void main(String[] args) {

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");

        try {
            AppServices appServices = container.getBean(AppServices.class);
            //File amzOrders = new File("C:\\Users\\alexf\\Documents\\EA Group\\Welse\\sharew\\order052518.html");
            //appServices.processExpressOrder(amzOrders);
            //List<OrderEntity> orders = appServices.getSellerPendingOrders();
            //System.out.println(orders.size());

            File updatedTrackings = new File("C:\\Users\\alexf\\Documents\\Development\\Projects\\Shades\\Issues\\avenger 08092018.xlsx");
            appServices.updateTrackingIds(updatedTrackings);


            //File userInventory = new File("C:\\Users\\alexf\\Documents\\Development\\Projects\\Shades\\Inventory Comparison.xlsx");
            //appServices.compareUserInventory(userInventory);

            //File azInventory = new File("C:\\Users\\alexf\\Downloads\\AZ08062018.xlsx");
            //appServices.updateInventory(azInventory, 500);
        }catch (Exception e){
            System.out.println("Exception: \n\n");
            System.out.println(e);
        }
    }
}
