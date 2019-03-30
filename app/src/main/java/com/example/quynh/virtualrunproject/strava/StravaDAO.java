package com.example.quynh.virtualrunproject.strava;

/**
 * Created by quynh on 3/30/2019.
 */

public class StravaDAO {
    private Athlete athlete;
    private StravaAccessToken access_token;

    public StravaDAO() {
    }

    public StravaDAO(Athlete athlete, StravaAccessToken access_token) {
        this.athlete = athlete;
        this.access_token = access_token;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public StravaAccessToken getAccess_token() {
        return access_token;
    }

    public void setAccess_token(StravaAccessToken access_token) {
        this.access_token = access_token;
    }
}
