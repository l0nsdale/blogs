package com.pashkevich.app.model;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Indexed
@Table(name = "user_pages")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String pageName;

    @Column(name="value", columnDefinition="TEXT")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String value;

    @Column(name="originalValue", columnDefinition="TEXT")
    private String originalValue;

    private long idBlog;

    private String username;

    private Date createdAt;

    @ManyToMany
    @JoinTable(name = "page_tags", joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
}