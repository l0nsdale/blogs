package com.pashkevich.app.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.pashkevich.app.constants.Constants.Views.HOME_PAGE;

/**
 * Created by Vlad on 06.03.17.
 */

@Controller
public class HomeController {

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home() {
        System.out.println("1. " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return HOME_PAGE;
    }

}
