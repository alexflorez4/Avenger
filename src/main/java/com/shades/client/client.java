package com.shades.client;

import com.shades.services.AzProcess;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class client {

    static Logger logger = Logger.getLogger(client.class);

    public static void main(String[] args) {

        logger.info("In main class.");
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");

        try {
            AzProcess azProcess = container.getBean(AzProcess.class);

            File azInventory = new File("C:\\Users\\alexf\\Documents\\Development\\Projects\\Shades\\azInventory.xlsx");
            azProcess.updateInventory(azInventory);

        }catch (Exception e){
            System.out.println("Exception: \n\n");
            System.out.println(e);
        }
    }
}
