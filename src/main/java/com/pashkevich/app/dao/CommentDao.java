package com.pashkevich.app.dao;

import com.pashkevich.app.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Vlad on 20.05.17.
 */
public interface CommentDao extends JpaRepository<Comment, Long> {
    List<Comment> findByPageIdOrderByCreatedAtDesc(Long pageId);
}
