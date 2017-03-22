package com.pashkevich.app.web.dto;

import javax.validation.constraints.Pattern;

import static com.pashkevich.app.constants.Constants.Regex.*;

/**
 * Created by Vlad on 18.03.17.
 */

public class UserForm {
    @Pattern(regexp = USERNAME)
    private String username;
    @Pattern(regexp = PASSWORD)
    private String password;
    @Pattern(regexp = FIRST_NAME)
    private String firstName;
    @Pattern(regexp = LAST_NAME)
    private String lastName;
    @Pattern(regexp = EMAIL)
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
