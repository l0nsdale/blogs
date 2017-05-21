package com.pashkevich.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String findLoggedInUsername() {
        if (isLogged()) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return null;
    }

    @Override
    public boolean isLogged() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            if (SecurityContextHolder.getContext().
                    getAuthentication().getAuthorities().size() > 0) {
                if (((GrantedAuthority) SecurityContextHolder.getContext().
                        getAuthentication().getAuthorities().toArray()[0]).getAuthority()
                        .equals("ROLE_ANONYMOUS")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(authenticationToken);
            if (authenticationToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return true;
            }
        } catch (Exception e) {
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
//            SecurityContextHolder.getContext().setAuthentication(null);
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isUserAuthor(String username) {
        return username.equals(findLoggedInUsername());
    }

    @Override
    public boolean canEdit(String username) {
        return false;
    }

    @Override
    public boolean isAdmin() {
        if (isLogged()) {
            if (((GrantedAuthority) SecurityContextHolder.getContext().
                    getAuthentication().getAuthorities().toArray()[0]).getAuthority()
                    .equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
