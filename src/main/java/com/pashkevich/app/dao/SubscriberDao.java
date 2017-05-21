package com.pashkevich.app.dao;

import com.pashkevich.app.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vlad on 20.05.17.
 */
public interface SubscriberDao extends JpaRepository<Subscriber, Long> {
    Subscriber findByUserId(Long userId);
}
