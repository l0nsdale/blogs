package com.pashkevich.app.service;

import com.pashkevich.app.model.Comment;
import com.pashkevich.app.model.Page;
import com.pashkevich.app.model.Tag;
import com.pashkevich.app.web.dto.PageForm;

import java.util.List;

/**
 * Created by Vlad on 12.05.17.
 */
public interface PageService {
    List<PageForm> getNewPages();
    List<PageForm> getPopularPages();
    List<PageForm> generatePageForms(List<Page> pages);
    List<Page> getFavouritePages();
    Comment sendComment(String comment, Long pageId);
    List<Comment> getComments(Long pageId);
    List<String> getAvailableTags();
}
