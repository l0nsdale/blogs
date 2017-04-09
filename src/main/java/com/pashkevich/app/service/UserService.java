package com.pashkevich.app.service;

import com.pashkevich.app.model.User;

public interface UserService {

    void save(User user);

    boolean isAccountEnabled(String username);

    User findByUsername(String username);

    void createVerificationToken(String token, User user);

    boolean enableAccount(String token);

    boolean isExistUsername(String username);

    boolean isExistEmail(String email);
}
