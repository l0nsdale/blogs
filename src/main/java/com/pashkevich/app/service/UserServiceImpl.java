package com.pashkevich.app.service;

import com.pashkevich.app.dao.*;
import com.pashkevich.app.listeners.OnRegistrationCompleteEvent;
import com.pashkevich.app.listeners.OnReparePasswordEvent;
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

    @Autowired
    private TagDao tagDao;

    @Autowired
    private RateDao rateDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SubscriberDao subscriberDao;

    @Autowired
    private CommentDao commentDao;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getOne(2L));
        user.setRoles(roles);
        saveUser(user);
    }

    private void saveUser(User user) {
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
        return pageDao.findByIdBlogOrderByCreatedAtDesc(idBlog);
    }

    @Override
    public boolean savePage(Page page, String tages) {

        page.setCreatedAt(new Date());
        Set<Tag> tags = new HashSet<>();
        for (String tagStr : tages.split(",")) {
            Tag tag;
            tag = tagDao.findByTag(tagStr);
            if (tag == null) {
                tag = new Tag();
                tag.setTag(tagStr);
                tagDao.save(tag);
            }
            tags.add(tag);
        }
        page.setTags(tags);
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

    @Override
    public boolean tryLike(Page page) {
        User user = userDao.findByUsername(securityService.findLoggedInUsername());
        if (rateDao.findByUserIdAndPageId(user.getId(), page.getId()) == null) {
            Rate rate = new Rate();
            rate.setBall(1);
            rate.setUserId(user.getId());
            rate.setPageId(page.getId());
            rateDao.save(rate);
            return true;
        }
        return false;
    }

    @Override
    public int getLikes(Long pageId) {
        Integer likes = rateDao.getRateByPageId(pageId);
        if (likes == null) {
            return 0;
        }
        return likes;
    }

    @Override
    public boolean subscribe(String username, boolean notification_feed, boolean notification_email) {
        try {
            User user = findByUsername(username);
            User usubscriber = findByUsername(securityService.findLoggedInUsername());
            Subscriber subscriber = new Subscriber();
            subscriber.setUserId(usubscriber.getId());
            subscriber.setId(usubscriber.getId());
            subscriber.setNotificationfeed(notification_feed);
            subscriber.setNotificationemail(notification_email);
            Set<Subscriber> subscribers = user.getSubscribers();
            boolean can = true;
            for (Subscriber subcriberTemp : subscribers) {
                if (subcriberTemp.getUserId() == subscriber.getUserId()) {
                    if (subcriberTemp.isNotificationemail() == subscriber.isNotificationemail() &&
                            subcriberTemp.isNotificationfeed() == subscriber.isNotificationfeed()) {
                        can = false;
                    } else {
                        subscribers.remove(subcriberTemp);
                        subscriberDao.delete(subcriberTemp);
                    }
                    break;
                }
            }
            if (can) {
                subscribers.add(subscriber);
                user.setSubscribers(subscribers);
                saveUser(user);
                return true;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    @Override
    public User getUser(Long userId) {
        return userDao.getOne(userId);
    }

    @Override
    public boolean isNotificateFeed(String username) {
        User user = findByUsername(username);
        User currentUser = findByUsername(securityService.findLoggedInUsername());
        Set<Subscriber> subscribers = user.getSubscribers();
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getUserId()  == currentUser.getId()) {
                return subscriber.isNotificationfeed();
            }
        }
        return false;
    }

    @Override
    public boolean isNotificateEmail(String username) {
        User user = findByUsername(username);
        User currentUser = findByUsername(securityService.findLoggedInUsername());
        Set<Subscriber> subscribers = user.getSubscribers();
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getUserId()  == currentUser.getId()) {
                return subscriber.isNotificationemail();
            }
        }
        return false;
    }

    @Override
    public void createRepareToken(String token, User user) {
        createVerificationToken(token, user);
    }

    @Override
    public boolean reparePassword(String token, String password) {
        try {
            VerificationToken verificationToken = tokenDao.findByToken(token);
            User user = verificationToken.getUser();
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userDao.save(user);
            tokenDao.delete(verificationToken);
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean resendRepareMessage(String url, String username) {
        User user;
        if (securityService.isLogged()) {
            user = findByUsername(securityService.findLoggedInUsername());
        } else  {
            user = findByUsername(username);
        }
        try {
            tokenDao.deleteByUserId(user.getId());
            eventPublisher.publishEvent(new OnReparePasswordEvent(user, url));
            return true;
        } catch (Exception e) {
            e.getMessage();
        }
        return false;
    }

    @Override
    public Long deleteUser(String username) {
        try {
            User user = findByUsername(username);
            userDao.delete(user);
            return user.getId();
        } catch (Exception e) {
            e.getMessage();
            return 0L;
        }
    }

    @Override
    public Long editUser(String username, String password, String firstName, String lastName, String email) {
        User user = findByUsername(username);
        boolean edit = false;
        if (!user.getUsername().equals(username)) {
            user.setUsername(username);
            edit = true;
        }
        if (!user.getFirstName().equals(firstName)) {
            user.setFirstName(firstName);
            edit = true;
        }
        if (!user.getLastName().equals(lastName)) {
            user.setLastName(lastName);
            edit = true;
        }
        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
            edit = true;
        }
        if (!password.isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
            edit = true;
        }
        if (edit == true) {
            userDao.save(user);
        }
        return user.getId();
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
}
