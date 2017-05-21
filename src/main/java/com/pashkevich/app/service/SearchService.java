package com.pashkevich.app.service;

import com.pashkevich.app.web.dto.SearchResult;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * Created by Vlad on 21.05.17.
 */
public interface SearchService {
    List<SearchResult> findBySearch(String search, WebRequest request);
}
