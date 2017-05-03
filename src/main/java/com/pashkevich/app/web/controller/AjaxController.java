package com.pashkevich.app.web.controller;

import com.pashkevich.app.model.Page;
import com.pashkevich.app.model.Blog;
import com.pashkevich.app.service.SecurityService;
import com.pashkevich.app.service.UserService;
import com.pashkevich.app.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class AjaxController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/resendConfirmation", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity resendConfirmation(@RequestParam String username, WebRequest request) {
        if (!userService.resendMessage(username, request.getLocale(), UrlUtils.getAppUrl(request))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteblog", method = RequestMethod.POST)
    public @ResponseBody long deleteBlog(@RequestParam long idBlog) {
        Blog blog = userService.getBlog(idBlog);
        String username = blog.getUsername();
        if (securityService.isUserAuthor(username)) {
            userService.deleteBlog(blog);
        }
        return idBlog;
    }

    @RequestMapping(value = "/deletepage", method = RequestMethod.POST)
    public @ResponseBody long deletePage(@RequestParam long idPage) {
        Page page = userService.getPage(idPage);
        String username = page.getUsername();
        if (securityService.isUserAuthor(username)){
            userService.deletePage(page);
        }
        return idPage;
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity like(@RequestParam Long idPage, WebRequest request) {
        Page page = userService.getPage(idPage);
        if (!securityService.isUserAuthor(page.getUsername())) {
            if (userService.tryLike(page)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/likes", method = RequestMethod.POST)
    public @ResponseBody int getLikes(@RequestParam Long idPage, WebRequest request) {
        return userService.getLikes(idPage);
    }
}
