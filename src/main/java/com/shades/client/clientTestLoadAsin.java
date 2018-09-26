package com.shades.client;

import Entities.InventoryEntity;
import com.shades.services.misc.AppServices;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.List;

public class clientTestLoadAsin {

    static Logger logger = Logger.getLogger(clientTestLoadAsin.class);

    public static void main(String[] args) {

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");

        try {
            AppServices appServices = container.getBean(AppServices.class);
            File asinFile = new File("C:\\Users\\alexf\\Documents\\EA Group\\Welse\\sharew\\All+Listings+Report+09-05-2018_sku_asin.xlsx");
            appServices.uploadAsin(asinFile);

        }catch (Exception e){
            System.out.println("Exception: \n\n");
            System.out.println(e);
        }
    }
}
