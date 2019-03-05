package com.example.quynh.virtualrunproject.entity;

/**
 * Created by quynh on 3/3/2019.
 */

public class Player {
    UserAndRaceMaped userAndRaceMaped;
    private double travelTime;
    private double travelDistance;
    private int rankInRace;

    public Player() {
    }

    public Player(UserAndRaceMaped userAndRaceMaped, double travelTime, double travelDistance, int rankInRace) {
        this.userAndRaceMaped = userAndRaceMaped;
        this.travelTime = travelTime;
        this.travelDistance = travelDistance;
        this.rankInRace = rankInRace;
    }

    public UserAndRaceMaped getUserAndRaceMaped() {
        return userAndRaceMaped;
    }

    public void setUserAndRaceMaped(UserAndRaceMaped userAndRaceMaped) {
        this.userAndRaceMaped = userAndRaceMaped;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    public double getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(double travelDistance) {
        this.travelDistance = travelDistance;
    }

    public int getRankInRace() {
        return rankInRace;
    }

    public void setRankInRace(int rankInRace) {
        this.rankInRace = rankInRace;
    }
}
