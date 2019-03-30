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

import org.json.JSONObject;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class CreateRaceScreen extends AppCompatActivity implements View.OnClickListener{

    private EditText raceName, raceDistance, raceDescription, raceRegulation;
    private TextView pictureName, raceStartTime, raceEndTime;
    private LinearLayout choosePictureBtn;
    private Button confirmCreateRaceBtn;
    private ImageView backBtn;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String raceImage;

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

            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                //raceImage = Base64.encodeToString(b, Base64.DEFAULT);
                File f = new File(picturePath);
                String imageName = f.getName();
                pictureName.setText(imageName);
                Log.d("TestPicturePath", "pictureParh: " + picturePath);
            }catch (Exception e){
                Log.e("CreateRaceScreen", "onActivityResult: ", e);
            }

        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.choose_picture_btn:
                //Intent intent = new Intent();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
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
            case R.id.confirm_create_race_btn:
                if(raceName.getText().toString().equalsIgnoreCase("")){
                    raceName.setError("This does not filled yet");
                }else if(raceDistance.getText().toString().equalsIgnoreCase("")){
                    raceDistance.setError("This does not filled yet");
                }else if(raceRegulation.getText().toString().equalsIgnoreCase("")){
                    raceRegulation.setError("This does not filled yet");
                }else if(raceDescription.getText().toString().equalsIgnoreCase("")){
                    raceDescription.setError("This does not filled yet");
                }else if(pictureName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(this, "You need to choose the race's picture", Toast.LENGTH_LONG).show();
                }else if(!checkPickedDate()){
                    Toast.makeText(this, "You need to choose a legal date", Toast.LENGTH_LONG).show();
                }else{
                    Race race = new Race();
                    race.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                    Date date = DateFormatHandler.stringToDate("dd/MM/yyyy", raceStartTime.getText().toString());
                    race.setStartTime(new Timestamp(date.getTime()));
                    date = DateFormatHandler.stringToDate("dd/MM/yyyy", raceEndTime.getText().toString());
                    race.setEndTime(new Timestamp(date.getTime()));
                    race.setName(raceName.getText().toString());
                    race.setDistance(Double.valueOf(raceDistance.getText().toString()));
                    race.setRegulation(raceRegulation.getText().toString());
                    race.setDescription(raceDescription.getText().toString());
                    //race.setRaceImage(raceImage);

                    UserAccountPrefs accountPrefs = new UserAccountPrefs(this);
                    Gson gson = new Gson();
                    UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
                    RaceServices.createRace(race, account.getUserId(), this, new OnReceiveResponse() {
                        @Override
                        public void onReceive(JSONObject response) {
                            if(response != null){
                                finish();
                            }
                        }
                    });
                }
                break;
        }
    }
}
