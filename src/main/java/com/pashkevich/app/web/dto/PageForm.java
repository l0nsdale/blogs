package com.pashkevich.app.web.dto;

import com.pashkevich.app.model.Tag;

import java.util.Date;
import java.util.Set;

/**
 * Created by Vlad on 12.05.17.
 */
public class PageForm {

    private Long id;
    private String pageName;

    private String value;

    private String originalValue;

    private long idBlog;

    private String username;

    private String createdAt;

    private Set<Tag> tags;

    private String preview;

    private String colorText;

    private String colorBackround;

    private String pageTags;

    public String getPageTags() {
        return pageTags;
    }

    public void setPageTags(String pageTags) {
        this.pageTags = pageTags;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }
    public String getPageName() {
        return pageName;
    }
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public long getIdBlog() {
        return idBlog;
    }
    public void setIdBlog(long idBlog) {
        this.idBlog = idBlog;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public String getColorBackround() {
        return colorBackround;
    }

    public void setColorBackround(String colorBackround) {
        this.colorBackround = colorBackround;
    }
}
