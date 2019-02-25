package com.example.quynh.virtualrunproject.dao;

import com.example.quynh.virtualrunproject.entity.Race;

import java.util.List;

/**
 * Created by quynh on 2/8/2019.
 */

public class RacesListDAO {
    private List<Race> races;

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }
}
