package com.shades.client;


import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;
import com.shades.services.fragx.FragxService;
import com.shades.services.misc.AppServices;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FragXUpdateInventory {

    static Logger logger = Logger.getLogger(FragXUpdateInventory.class);

    public static void main(String[] args) throws ShadesException {

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");
        FragxService service = container.getBean(FragxService.class);
        service.updateInventory();

    }
}
