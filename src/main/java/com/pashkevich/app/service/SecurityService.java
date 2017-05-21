package com.pashkevich.app.service;

public interface SecurityService {

    String findLoggedInUsername();

    boolean autoLogin(String username, String password);

    boolean isUserAuthor(String username);

    boolean canEdit(String username);

    boolean isLogged();

    boolean isAdmin();
}
