package com.pashkevich.app.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Vlad on 20.05.17.
 */
@Entity
@Table(name = "subscribers")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @ManyToMany(mappedBy = "subscribers")
    private Set<User> users;

    private boolean notificationfeed;

    private boolean notificationemail;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean isNotificationfeed() {
        return notificationfeed;
    }

    public void setNotificationfeed(boolean notificationfeed) {
        this.notificationfeed = notificationfeed;
    }

    public boolean isNotificationemail() {
        return notificationemail;
    }

    public void setNotificationemail(boolean notificationemail) {
        this.notificationemail = notificationemail;
    }
}
