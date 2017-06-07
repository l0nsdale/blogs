package com.pashkevich.app.service;

import com.pashkevich.app.dao.SearchDao;
import com.pashkevich.app.model.Blog;
import com.pashkevich.app.model.Page;
import com.pashkevich.app.model.Tag;
import com.pashkevich.app.model.User;
import com.pashkevich.app.utils.UrlUtils;
import com.pashkevich.app.web.dto.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Vlad on 21.05.17.
 */

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public List<SearchResult> findBySearch(String search, WebRequest request) {
        List<SearchResult> results = new ArrayList<>();
        List<User> users = searchDao.findUser(search);
        List<Page> pages = searchDao.findPage(search);
        List<Blog> blogs = searchDao.findBlog(search);
        List<Tag> tags = searchDao.findTag(search);
        for (User user : users) {
            SearchResult result = new SearchResult();
            result.setType("user");
            result.setName(user.getUsername());
            result.setUrl(UrlUtils.getAppUrl(request) + "/" + user.getUsername() + "/home");
            results.add(result);
        }
        for (Page page : pages) {
            SearchResult result = new SearchResult();
            result.setType("page");
            result.setName(page.getPageName());
            result.setUrl(UrlUtils.getAppUrl(request) + "/" + page.getUsername() + "/" + page.getIdBlog() + "/" +
                page.getId());
            results.add(result);
        }
        for (Blog blog : blogs) {
            SearchResult result = new SearchResult();
            result.setType("blog");
            result.setName(blog.getBlogName());
            result.setUrl(UrlUtils.getAppUrl(request) + "/" + blog.getUsername() + "/" + blog.getId() + "/" +
                    "home");
            results.add(result);
        }
        for (Tag tag : tags) {
            SearchResult result = new SearchResult();
            Set<Page> pageList = tag.getPages();
            for (Page page : pageList) {
                result.setType("page");
                result.setName(page.getPageName());
                result.setUrl(UrlUtils.getAppUrl(request) + "/" + page.getUsername() + "/" + page.getIdBlog() + "/" +
                        page.getId());
                results.add(result);
            }
        }
        return results;
    }
}
