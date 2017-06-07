package com.pashkevich.app.web.controller;

import com.pashkevich.app.dao.SearchDao;
import com.pashkevich.app.listeners.OnPagePublicationEvent;
import com.pashkevich.app.model.Blog;
import com.pashkevich.app.model.Page;
import com.pashkevich.app.service.PageService;
import com.pashkevich.app.service.SecurityService;
import com.pashkevich.app.service.UserService;
import com.pashkevich.app.utils.UrlUtils;
import com.pashkevich.app.utils.theme.Theme;
import com.pashkevich.app.utils.theme.ThemeBuilder;
import com.pashkevich.app.web.dto.PageForm;
import com.pashkevich.app.web.dto.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.pashkevich.app.constants.Constants.Redirect.TO_HOME;
import static com.pashkevich.app.constants.Constants.Views.*;

/**
 * Created by Vlad on 18.03.17.
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AjaxController ajaxController;

    @Autowired
    private PageService pageService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "{username}/home", method = RequestMethod.GET)
    public String home_user(@PathVariable String username, Model model, HttpServletRequest request) {
        List<Blog> blogs = userService.getBlogs(username);
        model.addAttribute("user", userService.findByUsername(username));
        model.addAttribute("blogs", blogs);
        model.addAttribute("usernameNav", username);
        if (securityService.isUserAuthor(username)) {
            model.addAttribute("canEdit", true);
        } else {
            model.addAttribute("notificationfeed", userService.isNotificateFeed(username));
            model.addAttribute("notificationemail", userService.isNotificateEmail(username));
        }
        return USER_PROFILE;
    }

    @RequestMapping(value = "{username}/newblog", method = RequestMethod.GET)
    public String newBlog(@PathVariable String username, Model model) {
        model.addAttribute("usernameNav", username);
        if (securityService.isUserAuthor(username)) {
            return NEW_BLOG;
        }
        return HOME_PAGE;
    }

    @RequestMapping(value = "{username}/newblog", method = RequestMethod.POST)
    public String newBlogCreate(@ModelAttribute("blog") Blog blog,
                                @PathVariable String username, HttpServletRequest request) {
        userService.saveBlog(blog);
        return "redirect:home";
    }

    @RequestMapping(value = "{username}/{blogId}/edit", method = RequestMethod.GET)
    public String editBlog(@PathVariable String username, @PathVariable long blogId, Model model) {
        if (securityService.isUserAuthor(username)) {
            Blog blog = userService.getBlog(blogId);
            if (blog.getTypeTheme().equals("black")) {
                model.addAttribute("blackTheme", true);
                model.addAttribute("standartTheme", false);
            } else {
                model.addAttribute("blackTheme", false);
                model.addAttribute("standartTheme", true);
            }
            model.addAttribute("blog", blog);
            model.addAttribute("usernameNav", username);
            model.addAttribute("blogIdNav", blogId);
            model.addAttribute("blogNameNav", blog.getBlogName());
        }
        return BLOG_EDIT;
    }

    @RequestMapping(value = "{username}/{blogId}/edit", method = RequestMethod.POST)
    public String doEditBlog(@ModelAttribute Blog blogDto, @PathVariable String username, @PathVariable long blogId) {
        if (securityService.isUserAuthor(username)) {
            Blog blog = userService.getBlog(blogId);
            blog.setDescription(blogDto.getDescription());
            blog.setTypeTheme(blogDto.getTypeTheme());
            userService.saveBlog(blog);
        }
        return "redirect:/" + username + "/home";
    }

    @RequestMapping(value = {"{username}/{blogId}/home", "{username}/{blogId}/home.html"})
    public String blogHome(@PathVariable String username, @PathVariable long blogId,
                           Model model) {
        Blog blog = userService.getBlog(blogId);
        List<PageForm> pages = pageService.generatePageForms(userService.getPages(blogId));
        Theme theme = ThemeBuilder.getThemeStandart();
        if (blog.getTypeTheme().equals("black")) {
            theme = ThemeBuilder.getThemeBlack();
        }
        if (securityService.isUserAuthor(username)) {
            model.addAttribute("canEdit", true);
        }
        model.addAttribute("theme", theme);
        model.addAttribute("blog", blog);
        model.addAttribute("username", username);
        model.addAttribute("blogName", blog.getBlogName());
        model.addAttribute("pages", pages);
        model.addAttribute("idBlog", blogId);
        model.addAttribute("usernameNav", username);
        model.addAttribute("blogIdNav", blogId);
        model.addAttribute("blogNameNav", blog.getBlogName());
        return BLOG_HOME;
    }

    @RequestMapping(value = "{username}/{blogId}/newpage")
    public String newPage(@PathVariable String username, @PathVariable long blogId, Model model) {
        if (securityService.isUserAuthor(username)) {
            Blog blog = userService.getBlog(blogId);
            model.addAttribute("idBlog", blogId);
            model.addAttribute("blog", blog);
            model.addAttribute("usernameNav", username);
            model.addAttribute("blogIdNav", blogId);
            model.addAttribute("blogNameNav", blog.getBlogName());
        }
        return NEW_PAGE;
    }

    @RequestMapping(value = "{username}/{blogId}/newpage", method = RequestMethod.POST)
    public String createNewPage(@ModelAttribute("page") Page page, @RequestParam("content") List<String> list,
                                @PathVariable String username, @PathVariable long blogId, @RequestParam("tages") String tages,
                                WebRequest request, @RequestParam("background") MultipartFile background) {
        if (securityService.isUserAuthor(username)) {
            userService.savePage(page, tages, background);
            eventPublisher.publishEvent(new OnPagePublicationEvent(userService.findByUsername(username),
                    UrlUtils.getPageUrl(request, username, blogId)));
        }
        return "redirect:home";
    }

    @RequestMapping(value = "{username}/{blogId}/{pageId}")
    public String userPage(@PathVariable String username, @PathVariable long blogId, @PathVariable long pageId,
                           Model model, WebRequest request) {
        Blog blog = userService.getBlog(blogId);
        Page page = userService.getPage(pageId);
        Theme theme = ThemeBuilder.getThemeStandart();
        if (blog.getTypeTheme().equals("black")) {
            theme = ThemeBuilder.getThemeBlack();
        }
        model.addAttribute("usernameNav", username);
        model.addAttribute("blogIdNav", blogId);
        model.addAttribute("pageIdNav", pageId);
        model.addAttribute("blog", userService.getBlog(blogId));
        model.addAttribute("idPage", page.getId());
        model.addAttribute("pageName", page.getPageName());
        model.addAttribute("page", page);
        model.addAttribute("pages", userService.getPages(blogId));
        model.addAttribute("theme", theme);
        model.addAttribute("rating", ajaxController.getLikes(pageId, request));
        model.addAttribute("blogNameNav", blog.getBlogName());
        model.addAttribute("pageNameNav", page.getPageName());
        model.addAttribute("comments", pageService.getComments(pageId));
        return USER_PAGE;
    }

    @RequestMapping(value = "/reparepassword.html")
    public String repareGet(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return REPARE_PASSWORD;
    }

    @RequestMapping(value = "/reparepassword", method = RequestMethod.POST)
    public String reparePost(@RequestParam("token") String token, @RequestParam("password") String password) {
        if (userService.reparePassword(token, password)) {
            if (securityService.isLogged()) {
                securityService.logout();
            }
            return "redirect:login";
        }
        return "redirect:registration";
    }

    @RequestMapping(value = "{username}/{blogId}/{pageId}/edit")
    public String editPage(@PathVariable String username, @PathVariable long blogId, @PathVariable long pageId, Model model) {
        Blog blog = userService.getBlog(blogId);
        Page page = userService.getPage(pageId);
        Theme theme = ThemeBuilder.getThemeStandart();
        if (blog.getTypeTheme().equals("black")) {
            theme = ThemeBuilder.getThemeBlack();
        }
        model.addAttribute("blog", userService.getBlog(blogId));
        model.addAttribute("idPage", page.getId());
        model.addAttribute("pageName", page.getPageName());
        model.addAttribute("page", page);
        model.addAttribute("theme", theme);
        return EDIT_PAGE;
    }

    @RequestMapping(value = "/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }
}
