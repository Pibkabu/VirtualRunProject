package com.example.quynh.virtualrunproject.customGUI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
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

/**
 * Created by quynh on 2/19/2019.
 */

public class ProfileRegisterDialog extends Dialog implements TextView.OnEditorActionListener{

    private EditText txtEmail, txtDisplayName, txtFirstName, txtLastName, txtPhone, txtAddress;
    private Spinner gender;
    private Button submitFormBtn;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;
    private TextView txtDateOfBirth;
    private Context context;


    public ProfileRegisterDialog(Context context) {
        super(context);
        this.context = context;
        accountPrefs = new UserAccountPrefs(context);
        profilePrefs = new UserProfilePrefs(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_register_form);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setupView();
        setupAction();
    }

    private void setupView() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtDisplayName = (EditText) findViewById(R.id.txtDisplayName);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        gender = (Spinner) findViewById(R.id.gender);
        submitFormBtn = (Button) findViewById(R.id.submit_form_btn);
        txtDateOfBirth = (TextView) findViewById(R.id.txtDateOfBirth);
        PushDownAnim.setPushDownAnimTo(txtDateOfBirth);

        Gson gson = new Gson();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        txtEmail.setText(account.getEmail());
        setupSpinner();
    }

    private void setupSpinner() {
        MySpinnerAdapter adapter = new MySpinnerAdapter(context, context.getResources().getStringArray(R.array.gender), R.layout.spinner_items_normal);
        gender.setAdapter(adapter);
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener dateSetListener){
        //Date date = DateFormatHandler.stringToDate("dd/MM/yyyy", time);
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void addUserProfile(){
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        final UserProfile profile = new UserProfile();
        profile.setUserId(account.getUserId());
        profile.setDisplayName(txtDisplayName.getText().toString());
        profile.setFirstName(txtFirstName.getText().toString());
        profile.setLastName(txtLastName.getText().toString());
        profile.setPhone(txtPhone.getText().toString());
        //String DOB = bYear.getSelectedItem().toString() + "-" + bMonth.getSelectedItem().toString() + "-" + bday.getSelectedItem().toString();
        String DOB = txtDateOfBirth.getText().toString();
        Date date = DateFormatHandler.stringToDate("yyyy-MM-dd", DOB);
        Timestamp timestamp = new Timestamp(date.getTime());
        Log.d("ADDPROFILE", "onClick: " + timestamp.toString());
        profile.setDOB(timestamp);
        if(gender.getSelectedItem().toString().equalsIgnoreCase("female")){
            profile.setGender(false);
        }else{
            profile.setGender(true);
        }
        profile.setAddress(txtAddress.getText().toString());
        UserProfileServices.addUserProfile(profile, context, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                if(gson.fromJson(response.toString(), UserProfile.class).getUserId() != 0){
                    profilePrefs.saveUserProfile(gson.toJson(profile));
                    dismiss();
                }
            }
        });
    }

    private void setupAction() {
        txtFirstName.setOnEditorActionListener(this);
        txtLastName.setOnEditorActionListener(this);
        txtPhone.setOnEditorActionListener(this);
        txtAddress.setOnEditorActionListener(this);

        txtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        submitFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDisplayName.getText().toString().equalsIgnoreCase("")){
                    txtDisplayName.setError("Thông tin bắt buộc");
                }else if(txtFirstName.getText().toString().equalsIgnoreCase("")){
                    txtFirstName.setError("Thông tin bắt buộc");
                }else if(txtLastName.getText().toString().equalsIgnoreCase("")){
                    txtLastName.setError("Thông tin bắt buộc");
                }else if(txtPhone.getText().toString().equalsIgnoreCase("")){
                    txtPhone.setError("Thông tin bắt buộc");
                }else if(txtAddress.getText().toString().equalsIgnoreCase("")){
                    txtAddress.setError("Thông tin bắt buộc");
                }else{
                    addUserProfile();
                }
            }
        });
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
}
