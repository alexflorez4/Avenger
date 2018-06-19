package com.shades.client;


import com.shades.services.fragx.FragxService;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class client2 {

    static Logger logger = Logger.getLogger(client2.class);

    public static void main(String[] args) {

        logger.info("In main class.");
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");

        //FragxProcess fragxProcess = container.getBean(FragxProcess.class);
        FragxService service = container.getBean(FragxService.class);
        service.updateInventory();



        //fragxProcess.testMethod();
    }
}
