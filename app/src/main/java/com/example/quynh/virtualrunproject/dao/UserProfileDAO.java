package com.example.quynh.virtualrunproject.dao;

import com.example.quynh.virtualrunproject.entity.UserProfile;

import java.util.List;

/**
 * Created by quynh on 4/21/2019.
 */

public class UserProfileDAO {
    private List<UserProfile> profiles;

    public UserProfileDAO(List<UserProfile> profiles) {
        this.profiles = profiles;
    }

    public List<UserProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<UserProfile> profiles) {
        this.profiles = profiles;
    }
}
