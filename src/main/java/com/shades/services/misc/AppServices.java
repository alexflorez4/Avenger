package com.shades.services.misc;


import com.shades.dao.InventoryDao;
import com.shades.services.az.AzProcess;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
@Service
public class AppServices {

    private static Logger logger = Logger.getLogger(AzProcess.class);

    @Autowired
    private InventoryDao inventoryDao;

    public List<String> allProductsSet(){
        return inventoryDao.getAllProductsSet();
    }
}
