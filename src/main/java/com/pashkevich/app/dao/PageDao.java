package com.pashkevich.app.dao;

import java.util.List;

import com.pashkevich.app.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageDao extends JpaRepository<Page, Long>{
    List<Page> findByIdBlogAndUsername(long idBlog, String username);
    List<Page> findByIdBlog(long idBlog);
    List<Page> findByIdBlogOrderByCreatedAtDesc(long idBlog);
    List<Page> findByUsername(String username);
    Page findByIdAndIdBlog(long id,long idBlog);
    Page findByIdAndUsername(long id, String username);
    void deletePagesByUsername(String username);
    void deletePagesByIdBlog(long idBlog);
}