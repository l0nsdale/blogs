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
 * Created by Vlad on 21.03.17.
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        Thread mail = new Thread(new Runnable() {
            @Override
            public void run() {
                User user = event.getUser();
                String token = UUID.randomUUID().toString();
                userService.createVerificationToken(token, user);
                String recipientAddress = user.getEmail();
                String subject = messageSource.getMessage("reg.confirm", null, event.getLocale());
                String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
                SimpleMailMessage email = new SimpleMailMessage();
                email.setFrom(env.getProperty("smtp.username"));
                email.setTo(recipientAddress);
                email.setSubject(subject);
                email.setText(messageSource.getMessage("reg.go.url", null, event.getLocale()) + confirmationUrl);
                mailSender.send(email);
            }
        });
        mail.start();
    }
}
