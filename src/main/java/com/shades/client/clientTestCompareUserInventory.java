package com.shades.client;

import Entities.InventoryEntity;
import com.shades.services.misc.AppServices;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.List;

public class clientTestCompareUserInventory {

    static Logger logger = Logger.getLogger(clientTestCompareUserInventory.class);

    public static void main(String[] args) {

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");

        try {
            AppServices appServices = container.getBean(AppServices.class);
            File updatedTrackings = new File("C:\\Users\\alexf\\Downloads\\Sample Report Artur_2.xlsx");
            List<InventoryEntity> results = appServices.compareUserInventory(updatedTrackings);
            System.out.println(results.size());

        }catch (Exception e){
            System.out.println("Exception: \n\n");
            System.out.println(e);
        }
    }
}
