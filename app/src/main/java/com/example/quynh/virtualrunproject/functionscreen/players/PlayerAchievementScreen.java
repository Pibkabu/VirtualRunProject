package com.example.quynh.virtualrunproject.functionscreen.players;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.PlayerAchievementAdapter;
import com.example.quynh.virtualrunproject.dao.PlayerListDAO;
import com.example.quynh.virtualrunproject.entity.Player;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerAchievementScreen extends AppCompatActivity {

    private RecyclerView achievementsList;
    private PlayerAchievementAdapter adapter;
    private List<Player> records;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_achievement);
        setupView();
        setupAction();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Thành Tích");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        achievementsList = (RecyclerView) findViewById(R.id.achievements_list);
        records = new ArrayList<>();
        Intent intent = getIntent();
        Gson gson = new Gson();
        records = gson.fromJson(intent.getStringExtra("playerRecords"), PlayerListDAO.class).getPlayers();
        List<Player> completedRecords = new ArrayList<>();
        for (Player player : records){
            if(!(player.getRankInRace() == 0)){
                completedRecords.add(player);
            }
        }
        adapter = new PlayerAchievementAdapter(completedRecords, this);
        achievementsList.setLayoutManager(new LinearLayoutManager(this));
        achievementsList.setAdapter(adapter);
        achievementsList.setNestedScrollingEnabled(false);
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
