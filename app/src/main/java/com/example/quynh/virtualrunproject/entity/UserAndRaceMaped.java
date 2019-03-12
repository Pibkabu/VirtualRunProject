package com.example.quynh.virtualrunproject.entity;

/**
 * Created by quynh on 3/3/2019.
 */

public class UserAndRaceMaped {
    private int userId;
    private int raceId;

    public UserAndRaceMaped(int userId, int raceId) {
        this.userId = userId;
        this.raceId = raceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }
}
