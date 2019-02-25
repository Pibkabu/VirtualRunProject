package com.example.quynh.virtualrunproject.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by quynh on 2/8/2019.
 */

public class Race implements Parcelable{
    private int raceId;
    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp endTime;
    private String name;
    private double distance;
    private String regulation;
    private String description;
    private int totalPlayer;

    public Race() {
    }

    public Race(int raceId, Timestamp createTime, Timestamp startTime, Timestamp endTime, String name, double distance, String regulation, String description, int totalPlayer) {
        this.raceId = raceId;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.distance = distance;
        this.regulation = regulation;
        this.description = description;
        this.totalPlayer = totalPlayer;
    }

    protected Race(Parcel in) {
        raceId = in.readInt();
        name = in.readString();
        distance = in.readDouble();
        regulation = in.readString();
        description = in.readString();
        totalPlayer = in.readInt();
    }

    public static final Creator<Race> CREATOR = new Creator<Race>() {
        @Override
        public Race createFromParcel(Parcel in) {
            return new Race(in);
        }

        @Override
        public Race[] newArray(int size) {
            return new Race[size];
        }
    };

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(int totalPlayer) {
        this.totalPlayer = totalPlayer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(raceId);
        dest.writeString(name);
        dest.writeDouble(distance);
        dest.writeString(regulation);
        dest.writeString(description);
        dest.writeInt(totalPlayer);
    }
}
