package com.shades.controller;

import com.shades.exceptions.ShadesException;
import com.shades.services.AzProcess;
import com.shades.utilities.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;


@Controller
public class InventoryController {

    private static final Logger logger = Logger.getLogger(InventoryController.class);

    @Autowired
    private AzProcess azProcess;

    @RequestMapping("/")
    public String actionsManager(){
        return "index";
    }

    @RequestMapping(value = "/pages/processAZImportFile", method = RequestMethod.POST)
    public ModelAndView processAZInventoryUpdate(@RequestParam("azFile") MultipartFile file){

        logger.info("Controller accessed.");

        String rootPath = System.getProperty("catalina.home");
        logger.info("System property, catalina.home: " + rootPath);


        File dir = new File(rootPath + File.separator + "uploads");

        if(!dir.exists())
            dir.mkdirs();

        File serverFile = new File(dir.getAbsolutePath() + File.separator + file);
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            byte[] bytes = file.getBytes();
            stream.write(bytes);
            stream.close();
            logger.info("Server File Location: "  + serverFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        File azInventory = null;
        try {
            //azInventory = Utils.multipartToFile(serverFile);
            azProcess.updateInventory(serverFile);
        } catch (Exception e) {
            logger.error("Exception thrown " + e.getMessage());
            e.printStackTrace();
        }
        return new ModelAndView("placeholder");
    }
}
