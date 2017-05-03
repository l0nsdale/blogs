package com.pashkevich.app.dao;

import com.pashkevich.app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vlad on 03.05.17.
 */
public interface TagDao extends JpaRepository<Tag, Long> {
    Tag findByTag(String tag);
}
