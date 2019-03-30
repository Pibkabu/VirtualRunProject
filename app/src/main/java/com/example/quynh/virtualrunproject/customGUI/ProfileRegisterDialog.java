package com.example.quynh.virtualrunproject.customGUI;

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

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by quynh on 2/19/2019.
 */

public class ProfileRegisterDialog extends Dialog implements TextView.OnEditorActionListener{

    private EditText txtEmail, txtDisplayName, txtFirstName, txtLastName, txtPhone, txtAddress;
    private Spinner bday, bMonth, bYear, gender;
    private Button submitFormBtn;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;
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
        bday = (Spinner) findViewById(R.id.bday);
        bMonth = (Spinner) findViewById(R.id.bMonth);
        bYear = (Spinner) findViewById(R.id.bYear);
        gender = (Spinner) findViewById(R.id.gender);
        submitFormBtn = (Button) findViewById(R.id.submit_form_btn);

        Gson gson = new Gson();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        txtEmail.setText(account.getEmail());
        setupSpinner();
    }

    private void setupSpinner() {
        MySpinnerAdapter adapter = new MySpinnerAdapter(context, context.getResources().getStringArray(R.array.date), R.layout.spinner_items_normal);
        bday.setAdapter(adapter);
        adapter = new MySpinnerAdapter(context, context.getResources().getStringArray(R.array.month), R.layout.spinner_items_normal);
        bMonth.setAdapter(adapter);
        List<String> years = new ArrayList<>();
        for(int i = 1900; i <= 2019; i++){
            years.add(String.valueOf(i));
        }
        String[] yearsArray = new String[years.size()];
        for(int i = 0; i < years.size(); i++){
            yearsArray[i] = years.get(i);
        }
        adapter = new MySpinnerAdapter(context, yearsArray, R.layout.spinner_items_normal);
        bYear.setAdapter(adapter);

        adapter = new MySpinnerAdapter(context, context.getResources().getStringArray(R.array.gender), R.layout.spinner_items_normal);
        gender.setAdapter(adapter);
    }

    private void setupAction() {
        txtFirstName.setOnEditorActionListener(this);
        txtLastName.setOnEditorActionListener(this);
        txtPhone.setOnEditorActionListener(this);
        txtAddress.setOnEditorActionListener(this);
        submitFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDisplayName.getText().toString().equalsIgnoreCase("")){
                    txtDisplayName.setError("This does not filled yet");
                }else if(txtFirstName.getText().toString().equalsIgnoreCase("")){
                    txtFirstName.setError("This does not filled yet");
                }else if(txtLastName.getText().toString().equalsIgnoreCase("")){
                    txtLastName.setError("This does not filled yet");
                }else if(txtPhone.getText().toString().equalsIgnoreCase("")){
                    txtPhone.setError("This does not filled yet");
                }else if(txtAddress.getText().toString().equalsIgnoreCase("")){
                    txtAddress.setError("This does not filled yet");
                }else{
                    final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
                    final UserProfile profile = new UserProfile();
                    profile.setUserId(account.getUserId());
                    profile.setDisplayName(txtDisplayName.getText().toString());
                    profile.setFirstName(txtFirstName.getText().toString());
                    profile.setLastName(txtLastName.getText().toString());
                    profile.setPhone(txtPhone.getText().toString());
                    String DOB = bYear.getSelectedItem().toString() + "-" + bMonth.getSelectedItem().toString() + "-" + bday.getSelectedItem().toString();
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
