package com.pashkevich.app.dao;

import com.pashkevich.app.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vlad on 22.03.17.
 */
public interface TokenDao extends JpaRepository<VerificationToken, Long>{
    VerificationToken findByToken(String token);
}
