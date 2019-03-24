package com.example.quynh.virtualrunproject.entity;

/**
 * Created by quynh on 3/24/2019.
 */

public class UserHost {
    private UserAndRaceMaped userAndRaceMaped;

    public UserHost(UserAndRaceMaped userAndRaceMaped) {
        this.userAndRaceMaped = userAndRaceMaped;
    }

    public UserHost() {
    }

    public UserAndRaceMaped getUserAndRaceMaped() {
        return userAndRaceMaped;
    }

    public void setUserAndRaceMaped(UserAndRaceMaped userAndRaceMaped) {
        this.userAndRaceMaped = userAndRaceMaped;
    }
}
