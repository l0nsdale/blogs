package com.pashkevich.app.service;

import com.pashkevich.app.dao.*;
import com.pashkevich.app.model.*;
import com.pashkevich.app.utils.DateUtils;
import com.pashkevich.app.web.dto.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Vlad on 12.05.17.
 */

@Service
public class PageServiceImpl implements PageService{

    @Autowired
    private PageDao pageDao;

    @Autowired
    private RateDao rateDao;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriberDao subscriberDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TagDao tagDao;

    @Override
    public List<PageForm> getNewPages() {
        return generatePageForms(getNewPagesEntity());
    }

    @Override
    public List<PageForm> generatePageForms(List<Page> pages) {
        List<PageForm> pagesForms = new ArrayList<>();
        for (Page page : pages) {
            PageForm pageForm = new PageForm();
            String value = page.getValue();
            pageForm.setUsername(page.getUsername());
            pageForm.setPageName(page.getPageName());
            pageForm.setTags(page.getTags());
            pageForm.setCreatedAt(DateUtils.getDateString(page.getCreatedAt()));
            pageForm.setIdBlog(page.getIdBlog());
            pageForm.setOriginalValue(page.getOriginalValue());
            pageForm.setValue(value);
            pageForm.setId(page.getId());
            String preview = value.substring(0, Math.min(value.length(), 100));
            StringBuilder previewBuilder = new StringBuilder(preview);
            for (int i = previewBuilder.length() - 1; i > 0; i--) {
                if (previewBuilder.charAt(i) != '>') {
                    previewBuilder.deleteCharAt(i);
                } else {
                    i = 0;
                }
            }
            pageForm.setPreview(previewBuilder.toString());
            pagesForms.add(pageForm);
        }
        return pagesForms;
    }

    private List<Page> getNewPagesEntity() {
        return pageDao.findFirst10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<PageForm> getPopularPages() {
        List<Long> rates = rateDao.getTopRatePages();
        List<Page> pages = new ArrayList<>();
        for (Long pageId : rates.subList(0, Math.min(rates.size(), 10))) {
            pages.add(pageDao.getOne(pageId));
        }
        return generatePageForms(pages);
    }

    @Override
    public List<Page> getFavouritePages() {
        User currentUser = userService.findByUsername(securityService.findLoggedInUsername());
        Subscriber subscriber = subscriberDao.findByUserId(currentUser.getId());
        List<Page> pages = new ArrayList<>();
        if (subscriber != null) {
            Set<User> users = subscriber.getUsers();
            for (User user : users) {
                List<Page> pagesForUser = pageDao.findByUsername(user.getUsername());
                pages.addAll(pagesForUser);
            }
        }
        return pages;
    }

    @Override
    public Comment sendComment(String value, Long pageId) {
        try {
            if (securityService.findLoggedInUsername() != null) {
                Comment comment = new Comment();
                comment.setUsername(securityService.findLoggedInUsername());
                comment.setValue(value);
                comment.setPageId(pageId);
                comment.setCreatedAt(new Date());
                commentDao.save(comment);
                return comment;
            }
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
        return null;
    }

    @Override
    public List<Comment> getComments(Long pageId) {
        return commentDao.findByPageIdOrderByCreatedAtDesc(pageId);
    }

    @Override
    public List<String> getAvailableTags() {
        List<String> tagsStr = new ArrayList<>();
        List<Tag> tags = tagDao.findAll();
        for (Tag tag : tags) {
            tagsStr.add(tag.getTag());
        }
        return tagsStr;
    }
}
