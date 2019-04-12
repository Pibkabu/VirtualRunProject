package com.example.quynh.virtualrunproject.entity;

/**
 * Created by quynh on 2/16/2019.
 */

public class UserAccount {
    private int userId;
    private String email;
    private String password;
    private boolean accountRole;

    public UserAccount() {
    }

    public UserAccount(int userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountRole() {
        return accountRole;
    }

    public void setAccountRole(boolean accountRole) {
        this.accountRole = accountRole;
    }
}
