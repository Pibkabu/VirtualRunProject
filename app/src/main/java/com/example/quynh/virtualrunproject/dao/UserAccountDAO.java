package com.example.quynh.virtualrunproject.dao;

import com.example.quynh.virtualrunproject.entity.UserAccount;

import java.util.List;

/**
 * Created by quynh on 4/13/2019.
 */

public class UserAccountDAO {
    private List<UserAccount> accounts;

    public UserAccountDAO() {
    }

    public UserAccountDAO(List<UserAccount> accounts) {
        this.accounts = accounts;
    }

    public List<UserAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UserAccount> accounts) {
        this.accounts = accounts;
    }
}
