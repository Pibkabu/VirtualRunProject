package com.example.quynh.virtualrunproject.functionscreen.hosting;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.DonateAccount;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.helper.PictureResizeHandler;
import com.example.quynh.virtualrunproject.services.DonateAccountServices;
import com.example.quynh.virtualrunproject.services.RaceServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class CreateRaceScreen extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private EditText raceName, raceDistance, raceDescription, raceRegulation;
    private TextView pictureName, raceStartTime, raceEndTime;
    private LinearLayout choosePictureBtn;
    private Button confirmCreateRaceBtn;
    private ImageView backBtn;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String raceImage;

    private CheckBox checkboxDonation, checkboxPassword;
    private EditText accountName, accountNumber, racePassword;

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
        checkboxPassword.setOnCheckedChangeListener(this);
        checkboxDonation.setOnCheckedChangeListener(this);
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
        checkboxDonation = (CheckBox) findViewById(R.id.checkbox_donation);
        checkboxPassword = (CheckBox) findViewById(R.id.checkbox_password);
        accountName = (EditText) findViewById(R.id.account_name);
        accountNumber = (EditText) findViewById(R.id.account_number);
        racePassword = (EditText) findViewById(R.id.race_password);
        confirmCreateRaceBtn = (Button) findViewById(R.id.save_btn);

        PushDownAnim.setPushDownAnimTo(choosePictureBtn);
        PushDownAnim.setPushDownAnimTo(confirmCreateRaceBtn);
        PushDownAnim.setPushDownAnimTo(raceStartTime);
        PushDownAnim.setPushDownAnimTo(raceEndTime);

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                Display display = this.getWindowManager().getDefaultDisplay();
                double deviceWidth = display.getWidth();

                double imageHeight = bitmap.getHeight();
                double imageWidth = bitmap.getWidth();

                double ratio = deviceWidth / imageWidth;
                int newImageHeight = (int) (imageHeight * ratio);

                Bitmap test = PictureResizeHandler.getResizedBitmap(bitmap, newImageHeight, (int) deviceWidth);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                test.compress(Bitmap.CompressFormat.PNG, 100, baos);
                raceImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
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
        if(startTime.getTime() < calendar.getTimeInMillis() || endTime.getTime() < startTime.getTime() || endTime.getTime() == startTime.getTime()){
            return false;
        }
        return true;
    }

    private boolean checkDonate(){
        if(checkboxDonation.isChecked()){
            if(accountName.getText().toString().equalsIgnoreCase("")){
                accountName.setError("This does not filled yet");
                return false;
            }else if(accountNumber.getText().toString().equalsIgnoreCase("")){
                accountNumber.setError("This does not filled yet");
                return false;
            }
        }
        return true;
    }

    private boolean checkPassword(){
        if(checkboxPassword.isChecked()){
            if(racePassword.getText().toString().equalsIgnoreCase("")){
                racePassword.setError("This does not filled yet");
                return false;
            }
        }
        return true;
    }

    private void addDonateAccount(int raceId){
        DonateAccount account = new DonateAccount();
        account.setRaceId(raceId);
        account.setAccountName(accountName.getText().toString());
        account.setAccountNumber(accountNumber.getText().toString());

        DonateAccountServices.addDonateAccount(account, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                if(gson.fromJson(response.toString(), DonateAccount.class).getRaceId() != 0){
                    Toast.makeText(CreateRaceScreen.this, "Tài khoản quyên góp đã thêm thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createHostingRace(){
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
        race.setRaceImage(raceImage);

        if(checkboxPassword.isChecked()){
            race.setRacePassword(racePassword.getText().toString());
        }else{
            race.setRacePassword("");
        }

        UserAccountPrefs accountPrefs = new UserAccountPrefs(this);
        final Gson gson = new Gson();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        RaceServices.createRace(race, account.getUserId(), this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                if(response != null){
                    Race race1 = gson.fromJson(response.toString(), Race.class);
                    if(race1.getRaceId() != 0){
                        if(checkboxDonation.isChecked()){
                            addDonateAccount(race1.getRaceId());
                        }
                        setResult(RESULT_OK);
                        finish();
                    }
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
                }else if(pictureName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(this, "Bận cần chọn ảnh cho đường chạy", Toast.LENGTH_LONG).show();
                }else if(!checkPickedDate()){
                    Toast.makeText(this, "Bạn cần chọn lại thời gian phù hợp", Toast.LENGTH_LONG).show();
                }else if(!checkDonate()){
                    Toast.makeText(this, "Bạn cần điền đầy đủ thông tin số tài khoản", Toast.LENGTH_LONG).show();
                }else if(!checkPassword()){
                    Toast.makeText(this, "Bạn cần bổ sung mật khẩu", Toast.LENGTH_LONG).show();
                }else{
                    createHostingRace();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkbox_donation:
                if(isChecked){
                    accountName.setEnabled(true);
                    accountNumber.setEnabled(true);
                }else{
                    accountName.setEnabled(false);
                    accountNumber.setEnabled(false);
                }
                break;
            case R.id.checkbox_password:
                if(isChecked){
                    racePassword.setEnabled(true);
                }else{
                    racePassword.setEnabled(false);
                }
                break;
        }
    }
}
