package com.pashkevich.app.listeners;

import com.pashkevich.app.model.User;
import com.pashkevich.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Vlad on 20.05.17.
 */
@Component
public class ReparePasswordListener implements ApplicationListener<OnReparePasswordEvent> {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onApplicationEvent(OnReparePasswordEvent onReparePasswordEvent) {
        this.reparePassword(onReparePasswordEvent);
    }

    public void reparePassword(OnReparePasswordEvent event) {
        User user = event.getUser();
        String recipientAddress = user.getEmail();
        String token = UUID.randomUUID().toString();
        userService.createRepareToken(token, user);
        String subject = "Repare password";
        String confirmationUrl = event.getUrlPost() + "/reparepassword.html?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(env.getProperty("smtp.username"));
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Please, to repare password go to this url: " + confirmationUrl);
        mailSender.send(email);
    }
}
