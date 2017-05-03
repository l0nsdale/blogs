package com.pashkevich.app.dao;

import com.pashkevich.app.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogDao extends JpaRepository<Blog, Long> {
    List<Blog> findByUsername(String username);
    long findByBlogName(String blogName);
    Blog findByIdAndUsername(long id, String username);
    void deleteBlogeByBlogName(String blogName);
    void deleteBlogsByUsername(String username);
}