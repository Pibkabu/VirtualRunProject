package com.example.quynh.virtualrunproject.functionscreen.adminscreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.AdminRacesAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.functionscreen.hosting.RaceResultScreen;
import com.example.quynh.virtualrunproject.functionscreen.race.RaceDetailScreen;
import com.google.gson.Gson;

import java.util.List;

public class AllRaceScreen extends AppCompatActivity {

    private ImageView backBtn;
    private RecyclerView recyclerView;
    private List<Race> races;
    private AdminRacesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_race_screen);

        setupView();
        setupAction();
        setupRaceList();
    }

    private void setupRaceList() {
        Intent intent = getIntent();
        final Gson gson = new Gson();
        RacesListDAO dao = gson.fromJson(intent.getStringExtra("races"), RacesListDAO.class);
        races = dao.getRaces();
        adapter = new AdminRacesAdapter(races);
        adapter.setOnButtonClickRecyclerViewAdapter(new OnButtonClickRecyclerViewAdapter() {
            @Override
            public void OnButtonClick(int position) {
                Intent intent1 = new Intent(AllRaceScreen.this, RaceDetailScreen.class);
                intent1.putExtra("raceString", gson.toJson(races.get(position)));
                startActivity(intent1);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(AllRaceScreen.this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Đường Chạy");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }
}
