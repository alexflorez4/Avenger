package com.shades.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InventoryController {

    @SuppressWarnings("SpringMVCViewInspection")
    @RequestMapping("/")
    public String actionsManager(){
        return "index";
    }
}
