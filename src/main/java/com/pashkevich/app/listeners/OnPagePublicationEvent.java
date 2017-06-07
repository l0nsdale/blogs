package com.pashkevich.app.listeners;

import com.pashkevich.app.model.User;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Vlad on 20.05.17.
 */
public class OnPagePublicationEvent extends ApplicationEvent {

    private User user;
    private String urlPost;

    public OnPagePublicationEvent(final User user, final String urlPost) {
        super(user);
        this.user = user;
        this.urlPost = urlPost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUrlPost() {
        return urlPost;
    }

    public void setUrlPost(String urlPost) {
        this.urlPost = urlPost;
    }
}
