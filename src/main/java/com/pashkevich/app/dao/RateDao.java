package com.pashkevich.app.dao;

import com.pashkevich.app.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Created by Vlad on 03.05.17.
 */
@Repository
@RepositoryRestResource(path = "rate", collectionResourceRel = "rate")
public interface RateDao extends JpaRepository<Rate, Long> {
    Rate findByUserIdAndPageId(Long userId, Long pageId);

    @Query("SELECT SUM(r.rate) FROM Rate r WHERE r.pageId = :pageId")
    Integer getRateByPageId(@Param("pageId") Long pageId);
}
