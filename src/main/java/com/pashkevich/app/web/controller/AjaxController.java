package com.pashkevich.app.web.controller;

import com.pashkevich.app.model.Comment;
import com.pashkevich.app.model.Page;
import com.pashkevich.app.model.Blog;
import com.pashkevich.app.model.Tag;
import com.pashkevich.app.service.PageService;
import com.pashkevich.app.service.SecurityService;
import com.pashkevich.app.service.UserService;
import com.pashkevich.app.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static com.pashkevich.app.constants.Constants.Response.*;

@Controller
public class AjaxController {

    @Autowired
    private UserService userService;

    @Autowired
    private PageService pageService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/resendConfirmation", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity resendConfirmation(@RequestParam String username, WebRequest request) {
        if (!userService.resendMessage(username, request.getLocale(), UrlUtils.getAppUrl(request))) {
            return BAD;
        }
        return OK;
    }

    @RequestMapping(value = "/deleteuser", method = RequestMethod.POST)
    public @ResponseBody long deleteUser(@RequestParam String username) {
        if (securityService.isAdmin()) {
            return userService.deleteUser(username);
        }
        return 0L;
    }

    @RequestMapping(value = "/edituser", method = RequestMethod.POST)
    public @ResponseBody long editUser(@RequestParam String username, @RequestParam String email,
                                       @RequestParam Long id, @RequestParam String firstName,
                                       @RequestParam String lastName, @RequestParam String password) {
        if (securityService.isAdmin()) {
            return userService.editUser(username, password, firstName, lastName, email);
        }
        return 0L;
    }

    @RequestMapping(value = "/deleteblog", method = RequestMethod.POST)
    public @ResponseBody long deleteBlog(@RequestParam long idBlog) {
        Blog blog = userService.getBlog(idBlog);
        String username = blog.getUsername();
        if (securityService.isUserAuthor(username) || securityService.isAdmin()) {
            userService.deleteBlog(blog);
        }
        return idBlog;
    }

    @RequestMapping(value = "/deletepage", method = RequestMethod.POST)
    public @ResponseBody long deletePage(@RequestParam long idPage) {
        Page page = userService.getPage(idPage);
        String username = page.getUsername();
        if (securityService.isUserAuthor(username) || securityService.isAdmin()){
            userService.deletePage(page);
        }
        return idPage;
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity like(@RequestParam Long idPage, WebRequest request) {
        Page page = userService.getPage(idPage);
        if (!securityService.isUserAuthor(page.getUsername())) {
            if (userService.tryLike(page)) {
                return OK;
            }
        }
        return BAD;
    }

    @RequestMapping(value = "/likes", method = RequestMethod.POST)
    public @ResponseBody int getLikes(@RequestParam Long idPage, WebRequest request) {
        return userService.getLikes(idPage);
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity  subscribe(@RequestParam String username, @RequestParam boolean notification_feed,
                                                   @RequestParam boolean notification_email) {
        if (userService.subscribe(username, notification_feed, notification_email)) {
            return OK;
        }
        return BAD;
    }

    @RequestMapping(value = "/reparePassword", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity reparePassword(@RequestParam String username, WebRequest request) {
        if (!userService.resendRepareMessage(UrlUtils.getAppUrl(request), username)) {
            return BAD;
        }
        return OK;
    }

    @RequestMapping(value = "/sendComment", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> reparePassword(@RequestParam String value, @RequestParam Long idPage) {
        Comment comment = pageService.sendComment(value, idPage);
        if (comment == null) {
            return BAD;
        }
        String json = "{\"name\": \""+comment.getUsername()+"\"," +
                "\"createdAt\": \""+comment.getCreatedAt()+"\"}";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(json, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/getTags", method = RequestMethod.GET)
    public @ResponseBody List<String> getAvailableTags() {
        return pageService.getAvailableTags();
    }

    @RequestMapping(value = "/blockuser", method = RequestMethod.POST)
    public @ResponseBody long blockUser(@RequestParam String username) {
        if (securityService.isAdmin()) {
            return userService.blockUser(username);
        }
        return 0L;
    }
}
