package com.pashkevich.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String blogName;

    @Column(name="description", columnDefinition="TEXT")
    private String description;

    private String typeTheme;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getBlogName() {
        return blogName;
    }
    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTypeTheme() {
        return typeTheme;
    }
    public void setTypeTheme(String typeTheme) {
        this.typeTheme = typeTheme;
    }
}