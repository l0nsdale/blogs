package com.pashkevich.app.web.controller;

import com.pashkevich.app.listeners.OnRegistrationCompleteEvent;
import com.pashkevich.app.model.User;
import com.pashkevich.app.service.SecurityService;
import com.pashkevich.app.service.UserService;
import com.pashkevich.app.web.dto.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

import static com.pashkevich.app.constants.Constants.Redirect.TO_HOME;
import static com.pashkevich.app.constants.Constants.Views.LOGIN_PAGE;
import static com.pashkevich.app.constants.Constants.Views.REGISTRATION_PAGE;

/**
 * Created by Vlad on 22.03.17.
 */

@Controller
public class AuthController {

    @Autowired
    public ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(UserForm userForm) {
        return LOGIN_PAGE;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginSubmit(@Valid @ModelAttribute UserForm userForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return LOGIN_PAGE;
        }
        if (userService.isAccountEnabled(userForm.getUsername())) {
            securityService.autoLogin(userForm.getUsername(), userForm.getPassword());
            return TO_HOME;
        }
        model.addAttribute("enabled", "Please, activate your account");
        return LOGIN_PAGE;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(UserForm userForm) {
        return REGISTRATION_PAGE;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationSubmit(@Valid @ModelAttribute UserForm userForm, BindingResult result, WebRequest request) {
        if (result.hasErrors()) {
            return REGISTRATION_PAGE;
        }
        User user = new User();
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setUsername(userForm.getUsername());
        user.setPassword(userForm.getPassword());
        user.setEnabled(false);
        userService.save(user);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), request.getContextPath()));
        return LOGIN_PAGE;
    }

    @RequestMapping(value = "/registrationConfirm.html")
    public String registrationConfirm(@RequestParam("token") String token, UserForm userForm){
        if (userService.enableAccount(token)) {
            return LOGIN_PAGE;
        }
        return TO_HOME;
    }
}
