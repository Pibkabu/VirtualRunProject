package com.example.quynh.virtualrunproject.functionscreen.hosting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.services.RaceServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by quynh on 3/31/2019.
 */

public class EditRaceInfoScreen extends AppCompatActivity implements View.OnClickListener{

    private EditText raceName, raceDistance, raceDescription, raceRegulation;
    private TextView pictureName, raceStartTime, raceEndTime;
    private LinearLayout choosePictureBtn;
    private Button confirmEditRaceBtn;
    private ImageView backBtn;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String raceImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_raceinfo_screen_activity);

        setupView();
        setupRaceInfo();
        setupAction();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Chỉnh sửa thông tin đường chạy");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        choosePictureBtn = (LinearLayout) findViewById(R.id.choose_picture_btn);
        choosePictureBtn.setVisibility(View.GONE);
        TextView choosePicture = (TextView) findViewById(R.id.choose_picture);
        choosePicture.setVisibility(View.GONE);

        raceName = (EditText) findViewById(R.id.race_name);
        raceDistance = (EditText) findViewById(R.id.race_distance);
        raceDescription = (EditText) findViewById(R.id.race_description);
        raceRegulation = (EditText) findViewById(R.id.race_regulation);
        raceStartTime = (TextView) findViewById(R.id.race_start_time);
        raceEndTime = (TextView) findViewById(R.id.race_end_time);
        pictureName = (TextView) findViewById(R.id.picture_name);
        choosePictureBtn = (LinearLayout) findViewById(R.id.choose_picture_btn);
        confirmEditRaceBtn = (Button) findViewById(R.id.save_btn);

        PushDownAnim.setPushDownAnimTo(confirmEditRaceBtn);
    }

    private void setupRaceInfo(){
        Intent intent = getIntent();
        Gson gson = new Gson();
        Race race = gson.fromJson(intent.getStringExtra("race"), Race.class);
        raceName.setText(race.getName());
        raceDistance.setText(String.valueOf(race.getDistance()));
        raceDescription.setText(race.getDescription());
        raceRegulation.setText(race.getRegulation());

        Date date = DateFormatHandler.stringToDate("yyyy-MM-dd", race.getStartTime().toString());
        raceStartTime.setText(DateFormatHandler.dateToString("dd/MM/yyyy", date));

        date = DateFormatHandler.stringToDate("yyyy-MM-dd", race.getEndTime().toString());
        raceEndTime.setText(DateFormatHandler.dateToString("dd/MM/yyyy", date));
    }

    private void setupAction() {
        backBtn.setOnClickListener(this);
        confirmEditRaceBtn.setOnClickListener(this);
        choosePictureBtn.setOnClickListener(this);
        raceStartTime.setOnClickListener(this);
        raceEndTime.setOnClickListener(this);
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener dateSetListener, String time){
        Date date = DateFormatHandler.stringToDate("dd/MM/yyyy", time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private boolean checkPickedDate(){
        Date startTime = DateFormatHandler.stringToDate("dd/MM/yyyy", raceStartTime.getText().toString());
        Date endTime = DateFormatHandler.stringToDate("dd/MM/yyyy", raceEndTime.getText().toString());
        Calendar calendar = Calendar.getInstance();
        if(startTime.getTime() < calendar.getTimeInMillis() || endTime.getTime() < startTime.getTime()){
            return false;
        }
        return true;
    }

    private void editRace(){
        Gson gson = new Gson();
        Intent intent = getIntent();
        Race race = gson.fromJson(intent.getStringExtra("race"), Race.class);
        Date date = DateFormatHandler.stringToDate("dd/MM/yyyy", raceStartTime.getText().toString());
        race.setStartTime(new Timestamp(date.getTime()));
        date = DateFormatHandler.stringToDate("dd/MM/yyyy", raceEndTime.getText().toString());
        race.setEndTime(new Timestamp(date.getTime()));
        race.setName(raceName.getText().toString());
        race.setDistance(Double.valueOf(raceDistance.getText().toString()));
        race.setRegulation(raceRegulation.getText().toString());
        race.setDescription(raceDescription.getText().toString());
        //race.setRaceImage(raceImage);
        RaceServices.editRaceInfo(race, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                if(response != null){
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.choose_picture_btn:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.race_start_time:
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        raceStartTime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                };
                showDatePickerDialog(dateSetListener, raceStartTime.getText().toString());
                break;
            case R.id.race_end_time:
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        raceEndTime.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                };
                showDatePickerDialog(dateSetListener, raceEndTime.getText().toString());
                break;
            case R.id.save_btn:
                if(raceName.getText().toString().equalsIgnoreCase("")){
                    raceName.setError("Thông tin bắt buộc");
                }else if(raceDistance.getText().toString().equalsIgnoreCase("")){
                    raceDistance.setError("Thông tin bắt buộc");
                }else if(raceRegulation.getText().toString().equalsIgnoreCase("")){
                    raceRegulation.setError("Thông tin bắt buộc");
                }else if(raceDescription.getText().toString().equalsIgnoreCase("")){
                    raceDescription.setError("Thông tin bắt buộc");
                }else if(!checkPickedDate()){
                    Toast.makeText(this, "Bạn cần chọn lại thời gian phù hợp", Toast.LENGTH_LONG).show();
                }else{
                    editRace();
                }
                break;
        }
    }
}
