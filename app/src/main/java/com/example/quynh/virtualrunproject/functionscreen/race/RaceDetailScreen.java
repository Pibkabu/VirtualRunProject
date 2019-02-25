package com.example.quynh.virtualrunproject.functionscreen.race;
import com.example.quynh.virtualrunproject.R;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.helper.PictureResizerHandler;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RaceDetailScreen extends AppCompatActivity implements View.OnClickListener{

    private TextView toolbarTitle;
    private ImageView raceImage;
    private RecyclerView playerIcons;
    private TextView title;
    private TextView numberOfPlayer;
    private TextView countDownDays, countDownHours, countDownMins, countDownSecs;
    private TextView raceTime ;
    private ReadMoreTextView description;
    private Button joinRaceBtn;
    private CountDownTimer countDownTimer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_detail);

        setupView();
        setupRaceInfo();
        setupAction();
    }

    private void setupRaceInfo() {
        Intent intent = getIntent();
        Race race = intent.getParcelableExtra("race");
        toolbarTitle.setText(race.getName());
        title.setText(race.getName());
        numberOfPlayer.setText(race.getTotalPlayer() + " Runners have joined the race");

        Date startDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", intent.getStringExtra("startTime"));
        Date endDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", intent.getStringExtra("endTime"));
        String startTime = DateFormatHandler.dateToString("dd MMM", startDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", startDate) + ") Vietnam time";
        String endTime = DateFormatHandler.dateToString("dd MMM", endDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", endDate) + ") Vietnam time";
        raceTime.setText(startTime + " to " + endTime);
        description.setText(race.getDescription());

        Calendar calendar = Calendar.getInstance();
        long countDownTime = startDate.getTime() - calendar.getTimeInMillis();
        if(countDownTime > 0){
            countDownTimer = new CountDownTimer(countDownTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.DAYS.toMillis(days);
                    countDownDays.setText(String.valueOf(days));

                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);
                    countDownHours.setText(String.valueOf(hours));

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                    countDownMins.setText(String.valueOf(minutes));

                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    countDownSecs.setText(String.valueOf(seconds));
                }

                @Override
                public void onFinish() {

                }
            };
            countDownTimer.start();
        }


    }

    private void setupView() {
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        raceImage = (ImageView) findViewById(R.id.race_head_image);
        playerIcons = (RecyclerView) findViewById(R.id.players_icon);
        title = (TextView) findViewById(R.id.race_detail_title);
        numberOfPlayer = (TextView) findViewById(R.id.number_of_players_detail);
        countDownDays = (TextView) findViewById(R.id.countdown_days);
        countDownHours = (TextView) findViewById(R.id.countdown_hours);
        countDownMins = (TextView) findViewById(R.id.countdown_mins);
        countDownSecs = (TextView) findViewById(R.id.countdown_secs);
        raceTime = (TextView) findViewById(R.id.race_time);
        description = (ReadMoreTextView) findViewById(R.id.race_description);
        joinRaceBtn = (Button) findViewById(R.id.race_join_btn);


        raceImage.setImageDrawable(PictureResizerHandler.resizeImage(R.drawable.dummy_picture, this));
    }

    private void setupAction() {
        joinRaceBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.race_join_btn:
                //join the race || cancel race
                break;
        }
    }
}
