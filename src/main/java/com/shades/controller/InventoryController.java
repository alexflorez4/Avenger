package com.shades.controller;

import com.shades.services.AzProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class InventoryController {

    @Autowired
    private AzProcess azProcess;


    @RequestMapping("/")
    public String actionsManager(){
        return "index";
    }

    @RequestMapping(value = "/pages/processAZImportFile", method = RequestMethod.POST)
    public ModelAndView processAZInventoryUpdate(@RequestParam("azFile") MultipartFile file){

        azProcess.updateInventory(file);

        System.out.println("*****************  PROCESSING FILE  ******************");
        System.out.println(file.getName() + " " + file.getSize());

        return new ModelAndView("placeholder");
    }
}
