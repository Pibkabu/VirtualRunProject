package com.example.quynh.virtualrunproject.entity;

/**
 * Created by quynh on 4/11/2019.
 */

public class DonateAccount {
    private int raceId;
    private String accountNumber;
    private String accountName;

    public DonateAccount() {
    }

    public DonateAccount(int raceId, String accountNumber, String accountName) {
        this.raceId = raceId;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
