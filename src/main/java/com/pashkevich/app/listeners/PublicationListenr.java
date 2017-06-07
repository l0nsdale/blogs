package com.pashkevich.app.listeners;

import com.pashkevich.app.model.Subscriber;
import com.pashkevich.app.model.User;
import com.pashkevich.app.service.SecurityService;
import com.pashkevich.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by Vlad on 20.05.17.
 */

@Component
public class PublicationListenr implements ApplicationListener<OnPagePublicationEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onApplicationEvent(OnPagePublicationEvent onPagePublicationEvent) {
        this.notificationSubscribers(onPagePublicationEvent);
    }

    private void notificationSubscribers(final OnPagePublicationEvent event) {
        User user = event.getUser();
        Set<Subscriber> subscribers = user.getSubscribers();
        for (Subscriber subscriber : subscribers) {
            if (subscriber.isNotificationemail()) {
                User usubscriber  = userService.getUser(subscriber.getUserId());
                String recipientAddress = usubscriber.getEmail();
                String subject = "New post!";
                String urlPost = event.getUrlPost();
                SimpleMailMessage email = new SimpleMailMessage();
                email.setFrom(env.getProperty("smtp.username"));
                email.setTo(recipientAddress);
                email.setSubject(subject);
                email.setText("Hello, "+ user.getUsername() + " write new post. Check this blog " + urlPost + ".html");
                mailSender.send(email);
            }
        }
    }
}
