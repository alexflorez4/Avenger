package com.shades.client;

import Entities.OrderEntity;
import com.shades.services.misc.AppServices;
import com.shades.utilities.ParseAmzOrder;
import com.shades.utilities.Utils;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.File;
import java.util.List;

public class ParseOrderTest {

    public static void main(String[] args) {

        try {
            ParseAmzOrder bean = new ParseAmzOrder();

            //File file = new File("C:\\Users\\alexf\\Documents\\Development\\Projects\\Shades\\Issues\\2 skus.html");
            File file = new File("C:\\Users\\alexf\\Documents\\Development\\Projects\\Shades\\Issues\\bug 1.html");
            List<OrderEntity> results = bean.parse(file, 1, 599);
            System.out.println(results);

        }catch (Exception e){
            System.out.println("Exception: \n\n");
            System.out.println(e);
        }
    }
}
