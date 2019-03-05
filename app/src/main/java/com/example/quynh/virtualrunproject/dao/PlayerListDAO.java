package com.example.quynh.virtualrunproject.dao;

import com.example.quynh.virtualrunproject.entity.Player;

import java.util.List;

/**
 * Created by quynh on 3/4/2019.
 */

public class PlayerListDAO {
    private List<Player> players;

    public PlayerListDAO(List<Player> players) {
        super();
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
