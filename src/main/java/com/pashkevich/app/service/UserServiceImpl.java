package com.pashkevich.app.service;

import com.pashkevich.app.dao.*;
import com.pashkevich.app.listeners.OnRegistrationCompleteEvent;
import com.pashkevich.app.model.*;
import com.pashkevich.app.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private PageDao pageDao;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getOne(1L));
        user.setRoles(roles);
        userDao.save(user);
    }

    @Override
    public boolean isAccountEnabled(String username) {
        return findByUsername(username).isEnabled();
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void createVerificationToken(String token, User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setDateExpired(DateUtils.getNextDayDate());
        tokenDao.save(verificationToken);
    }

    @Override
    public boolean resendMessage(String username, Locale locale, String appUrl) {
        if (isExistUsername(username)) {
            User user = findByUsername(username);
            tokenDao.deleteByUserId(user.getId());
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, locale, appUrl));
            return true;
        }
        return false;
    }

    @Override
    public boolean enableAccount(String token) {
        try {
            VerificationToken verificationToken = tokenDao.findByToken(token);
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userDao.save(user);
            tokenDao.delete(verificationToken);
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isExistUsername(String username) {
        if (userDao.findByUsername(username) != null)
            return true;
        return false;
    }

    @Override
    public boolean isExistEmail(String email) {
        if (userDao.findByEmail(email) != null)
            return true;
        return false;
    }

    @Override
    public List<Blog> getBlogs(String username) {
        return blogDao.findByUsername(username);
    }

    @Override
    public Blog getBlog(Long idBlog) {
        return blogDao.getOne(idBlog);
    }

    @Override
    public boolean saveBlog(Blog blog) {
        blogDao.save(blog);
        return true;
    }

    @Override
    public boolean deleteBlog(Blog blog) {
        blogDao.delete(blog);
        return true;
    }

    @Override
    public List<Page> getPages(Long idBlog) {
        return pageDao.findByIdBlog(idBlog);
    }

    @Override
    public boolean savePage(Page page) {
        page.setCreatedAt(new Date());
        pageDao.save(page);
        return true;
    }

    @Override
    public Page getPage(long idPage) {
        return pageDao.getOne(idPage);
    }

    @Override
    public boolean deletePage(Page page) {
        pageDao.delete(page);
        return true;
    }
}
