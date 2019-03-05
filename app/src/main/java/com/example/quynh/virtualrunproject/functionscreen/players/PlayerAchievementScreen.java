package com.example.quynh.virtualrunproject.functionscreen.players;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.PlayerAchievementAdapter;
import com.example.quynh.virtualrunproject.dao.PlayerListDAO;
import com.example.quynh.virtualrunproject.entity.Player;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PlayerAchievementScreen extends AppCompatActivity {

    private RecyclerView achievementsList;
    private PlayerAchievementAdapter adapter;
    private List<Player> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_achievement);
        setupView();
        setupAction();
    }

    private void setupView() {
        achievementsList = (RecyclerView) findViewById(R.id.achievements_list);
        records = new ArrayList<>();
        Intent intent = getIntent();
        Gson gson = new Gson();
        records = gson.fromJson(intent.getStringExtra("playerRecords"), PlayerListDAO.class).getPlayers();
        adapter = new PlayerAchievementAdapter(records, this);
        achievementsList.setLayoutManager(new LinearLayoutManager(this));
        achievementsList.setAdapter(adapter);
        achievementsList.setNestedScrollingEnabled(false);
    }

    private void setupAction() {

    }
}
