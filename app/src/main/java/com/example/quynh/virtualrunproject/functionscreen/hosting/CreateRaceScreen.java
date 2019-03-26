package com.example.quynh.virtualrunproject.functionscreen.hosting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class CreateRaceScreen extends AppCompatActivity implements View.OnClickListener{

    private EditText raceName, raceDistance, raceDescription, raceRegulation;
    private TextView pictureName, raceStartTime, raceEndTime;
    private LinearLayout choosePictureBtn;
    private Button confirmCreateRaceBtn;
    private ImageView backBtn;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_race_screen);

        setupView();
        setupAction();
    }

    private void setupAction() {
        backBtn.setOnClickListener(this);
        confirmCreateRaceBtn.setOnClickListener(this);
        choosePictureBtn.setOnClickListener(this);
        raceStartTime.setOnClickListener(this);
        raceEndTime.setOnClickListener(this);
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Tạo đường chạy");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);

        raceName = (EditText) findViewById(R.id.race_name);
        raceDistance = (EditText) findViewById(R.id.race_distance);
        raceDescription = (EditText) findViewById(R.id.race_description);
        raceRegulation = (EditText) findViewById(R.id.race_regulation);
        raceStartTime = (TextView) findViewById(R.id.race_start_time);
        raceEndTime = (TextView) findViewById(R.id.race_end_time);
        pictureName = (TextView) findViewById(R.id.picture_name);
        choosePictureBtn = (LinearLayout) findViewById(R.id.choose_picture_btn);
        confirmCreateRaceBtn = (Button) findViewById(R.id.confirm_create_race_btn);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        raceStartTime.setText(day + "/" + month + "/" + year);
        raceEndTime.setText(day + "/" + month + "/" + year);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            File f = new File(picturePath);
            String imageName = f.getName();
            pictureName.setText(imageName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.choose_picture_btn:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
            case R.id.confirm_create_race_btn:

                break;
        }
    }
}
