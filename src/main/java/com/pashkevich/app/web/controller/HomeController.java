package com.pashkevich.app.web.controller;

import com.pashkevich.app.service.PageService;
import com.pashkevich.app.service.SearchService;
import com.pashkevich.app.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import static com.pashkevich.app.constants.Constants.Views.HOME_PAGE;

/**
 * Created by Vlad on 06.03.17.
 */

@Controller
public class HomeController {

    @Autowired
    private PageService pageService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home() {
        System.out.println("1. " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return HOME_PAGE;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String feed(Model model){
        model.addAttribute("newposts", pageService.getNewPages());
        model.addAttribute("popularposts", pageService.getPopularPages());
        if (securityService.isLogged()) {
            model.addAttribute("favouriteposts", pageService.getFavouritePages());
        }
        return "feed";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("search") String search, WebRequest request, Model model){
        model.addAttribute("searchs", searchService.findBySearch(search, request));
        return "search";
    }

}
