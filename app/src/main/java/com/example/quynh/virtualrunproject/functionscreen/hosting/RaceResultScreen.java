package com.example.quynh.virtualrunproject.functionscreen.hosting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.RaceResultAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.PlayerListDAO;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.functionscreen.race.RaceDetailScreen;
import com.example.quynh.virtualrunproject.services.PlayerServices;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

public class RaceResultScreen extends AppCompatActivity {

    private ImageView image;
    private TextView raceName;
    private ImageView backBtn;
    private RecyclerView recyclerView;
    private RaceResultAdapter adapter;
    private List<Player> records;
    private Race race;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_result_screen);

        setupView();
        setupInfo();
        setupAction();
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        raceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RaceResultScreen.this, RaceDetailScreen.class);
                Gson gson = new Gson();
                intent.putExtra("raceString", gson.toJson(race));
                startActivity(intent);
            }
        });
    }

    private void setupInfo() {
        Intent intent = getIntent();
        final Gson gson = new Gson();
        race = gson.fromJson(intent.getStringExtra("raceString"), Race.class);

        raceName.setText(race.getName());
        Glide.with(this).load(race.getRaceImage()).into(image);

        //Players Records
        PlayerServices.getFinishResult(race.getRaceId(), this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                PlayerListDAO dao = gson.fromJson(response.toString(), PlayerListDAO.class);
                records = dao.getPlayers();
                adapter = new RaceResultAdapter(records, RaceResultScreen.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(RaceResultScreen.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);
            }
        });
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Kết Quả Đường Chạy");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        image = (ImageView) findViewById(R.id.race_head_image);
        raceName = (TextView) findViewById(R.id.race_name);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }
}
