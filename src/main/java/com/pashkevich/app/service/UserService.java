package com.pashkevich.app.service;

import com.pashkevich.app.model.Page;
import com.pashkevich.app.model.Blog;
import com.pashkevich.app.model.User;

import java.util.List;
import java.util.Locale;

public interface UserService {

    void save(User user);

    boolean resendMessage(String username, Locale locale, String appUrl);

    boolean isAccountEnabled(String username);

    User findByUsername(String username);

    void createVerificationToken(String token, User user);

    boolean enableAccount(String token);

    boolean isExistUsername(String username);

    boolean isExistEmail(String email);

    List<Blog> getBlogs(String username);

    Blog getBlog(Long idBlog);

    boolean saveBlog(Blog blog);

    boolean deleteBlog(Blog blog);

    List<Page> getPages(Long idBlog);

    boolean savePage(Page page, String tages);

    Page getPage(long idPage);

    boolean deletePage(Page page);

    boolean tryLike(Page page);

    int getLikes(Long pageId);
}
