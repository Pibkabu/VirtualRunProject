package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;

/**
 * Created by quynh on 2/23/2019.
 */

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.MySpinnerAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.services.UserProfileServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.example.quynh.virtualrunproject.userlogintracker.UserProfilePrefs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProfileChangeScreen extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener{

    private EditText txtEmail, txtDisplayName, txtFirstName, txtLastName, txtPhone, txtAddress;
    private Spinner gender;
    private ImageView backBtn, pictureChangeBtn;
    private Button update, updateCancel;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;
    private TextView txtDateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change_screen);

        accountPrefs = new UserAccountPrefs(this);
        profilePrefs = new UserProfilePrefs(this);
        setupView();
        setupAction();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Thay Đổi Thông Tin");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        pictureChangeBtn = (ImageView) findViewById(R.id.picture_change_btn);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtDisplayName = (EditText) findViewById(R.id.txtDisplayName);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        gender = (Spinner) findViewById(R.id.gender);
        update = (Button) findViewById(R.id.update);
        updateCancel = (Button) findViewById(R.id.update_cancel);
        txtDateOfBirth = (TextView) findViewById(R.id.txtDateOfBirth);
        PushDownAnim.setPushDownAnimTo(txtDateOfBirth);
        PushDownAnim.setPushDownAnimTo(update);
        PushDownAnim.setPushDownAnimTo(txtDateOfBirth);
        setupSpinner();
        setupInfo();
    }

    private void setupInfo() {
        Gson gson = new Gson();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        UserProfile profile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);

        txtEmail.setText(account.getEmail());
        txtDisplayName.setText(profile.getDisplayName());
        txtFirstName.setText(profile.getFirstName());
        txtLastName.setText(profile.getLastName());
        txtPhone.setText(profile.getPhone());
        txtAddress.setText(profile.getAddress());

        Date date = DateFormatHandler.stringToDate("yyyy-MM-dd", profile.getDOB().toString());
        txtDateOfBirth.setText(DateFormatHandler.dateToString("yyyy-MM-dd", date));
    }

    private void setupSpinner() {
        Gson gson = new Gson();
        UserProfile profile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);
        MySpinnerAdapter adapter = new MySpinnerAdapter(this, getResources().getStringArray(R.array.gender), R.layout.spinner_items_normal);
        gender.setAdapter(adapter);
        if(profile.isGender()){
            gender.setSelection(adapter.getPosition("Male"));
        }else{
            gender.setSelection(adapter.getPosition("Female"));
        }
    }

    private void setupAction() {
        backBtn.setOnClickListener(this);
        pictureChangeBtn.setOnClickListener(this);
        update.setOnClickListener(this);
        updateCancel.setOnClickListener(this);
        txtDateOfBirth.setOnClickListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener dateSetListener){
        //Date date = DateFormatHandler.stringToDate("dd/MM/yyyy", time);
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
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

    private void update(){
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        final UserProfile profile = new UserProfile();
        profile.setUserId(account.getUserId());
        profile.setDisplayName(txtDisplayName.getText().toString());
        profile.setFirstName(txtFirstName.getText().toString());
        profile.setLastName(txtLastName.getText().toString());
        profile.setPhone(txtPhone.getText().toString());
        String DOB = txtDateOfBirth.getText().toString();
        Date date = DateFormatHandler.stringToDate("yyyy-MM-dd", DOB);
        Timestamp timestamp = new Timestamp(date.getTime());
        profile.setDOB(timestamp);
        if(gender.getSelectedItem().toString().equalsIgnoreCase("female")){
            profile.setGender(false);
        }else{
            profile.setGender(true);
        }
        profile.setAddress(txtAddress.getText().toString());
        UserProfileServices.updateUserProfile(profile, ProfileChangeScreen.this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                if(gson.fromJson(response.toString(), UserProfile.class).getUserId() != 0){
                    profilePrefs.saveUserProfile(gson.toJson(profile));
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
            case R.id.picture_change_btn:
                break;
            case R.id.txtDateOfBirth:
                showDatePickerDialog(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if((month + 1) >= 10){
                            txtDateOfBirth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        }else{
                            txtDateOfBirth.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
                        }
                    }
                });
                break;
            case R.id.update:
                update();
                break;
            case R.id.update_cancel:
                finish();
                break;
        }
    }
}
