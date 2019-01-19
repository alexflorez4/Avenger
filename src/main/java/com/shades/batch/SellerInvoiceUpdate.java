package com.shades.batch;

import com.shades.services.misc.FileServices;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class SellerInvoiceUpdate {

    static Logger LOG = Logger.getLogger(SellerInvoiceUpdate.class);

    public static void main(String[] args) {

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");

        try {
            FileServices bean = container.getBean(FileServices.class);
            File invoice = new File("C:/Users/alexf/Downloads/6026_test.xls");
            bean.updateInvoiceToAccount(invoice);

        }catch (Exception e){
            LOG.error("Method fail to " + e.getMessage());
        }
    }
}
