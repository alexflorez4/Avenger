package com.shades.client;


import Entities.InventoryEntity;
import Entities.OrderEntity;
import com.shades.exceptions.ShadesException;
import com.shades.services.fragx.FragxService;
import com.shades.services.misc.AppServices;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class client2 {

    static Logger logger = Logger.getLogger(client2.class);

    public static void main(String[] args) throws ShadesException {

        String s = "MTPSD_312";
        String s1 = StringUtils.removePattern(s, "[_]+\\d++");
        System.out.println(s1);

        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("/application.xml");
        AppServices service = container.getBean(AppServices.class);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setSku("400028");
        orderEntity.setQuantity(2);
        service.processNewSingleOrder(orderEntity);
    }
}
