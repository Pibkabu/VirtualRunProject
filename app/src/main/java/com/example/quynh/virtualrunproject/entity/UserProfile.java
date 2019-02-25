package com.example.quynh.virtualrunproject.entity;

import java.sql.Timestamp;

/**
 * Created by quynh on 2/16/2019.
 */

public class UserProfile {
    private int userId;
    private String displayName;
    private String firstName;
    private String lastName;
    private Timestamp dob;
    private boolean gender;
    private String phone;
    private String address;

    public UserProfile() {
    }

    public UserProfile(int userId, String displayName, String firstName, String lastName, Timestamp dob, boolean gender, String phone, String address) {
        this.userId = userId;
        this.displayName = displayName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

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

    public Timestamp getDOB() {
        return dob;
    }

    public void setDOB(Timestamp dob) {
        this.dob = dob;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

