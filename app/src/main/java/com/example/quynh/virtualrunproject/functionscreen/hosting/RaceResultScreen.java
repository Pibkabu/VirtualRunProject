package com.example.quynh.virtualrunproject.functionscreen.hosting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.entity.Race;
import com.google.gson.Gson;

public class RaceResultScreen extends AppCompatActivity {

    private ImageView image;
    private TextView raceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_result_screen);

        setupView();
        setupRaceInfo();
    }

    private void setupRaceInfo() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        Race race = gson.fromJson(intent.getStringExtra("raceString"), Race.class);

        raceName.setText(race.getName());
        Glide.with(this).load(race.getRaceImage()).into(image);
    }

    private void setupView() {
        image = (ImageView) findViewById(R.id.race_head_image);
        raceName = (TextView) findViewById(R.id.race_name);
    }
}
