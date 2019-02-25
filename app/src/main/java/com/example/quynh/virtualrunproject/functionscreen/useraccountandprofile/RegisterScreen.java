package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;

import com.example.quynh.virtualrunproject.LoginScreen;
import com.example.quynh.virtualrunproject.MainActivity;
import com.example.quynh.virtualrunproject.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.services.OnReceiveResponse;
import com.example.quynh.virtualrunproject.services.UserAccountServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.example.quynh.virtualrunproject.userlogintracker.UserProfilePrefs;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.json.JSONObject;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {

    private Button normSignUpBtn;
    private EditText signUpEmail, signUpPassword, signUpConfirmPassword;
    private TextView txtSignIn;
    private ImageView backBtn;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        setupView();
        setupAction();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Register");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        normSignUpBtn = (Button) findViewById(R.id.normSignUpBtn);
        signUpEmail = (EditText) findViewById(R.id.signUpEmail);
        signUpPassword = (EditText) findViewById(R.id.signUpPassword);
        signUpConfirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
    }

    private void setupAction() {
        accountPrefs = new UserAccountPrefs(this);
        backBtn.setOnClickListener(this);
        normSignUpBtn.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.normSignUpBtn:
                String email = signUpEmail.getText().toString();
                String password = signUpPassword.getText().toString();
                String confirmPassword = signUpConfirmPassword.getText().toString();
                if(email.equalsIgnoreCase("")){
                    signUpEmail.setError("This does not filled yet");
                }else if(password.equalsIgnoreCase("")){
                    signUpPassword.setError("This does not filled yet");
                }else if(confirmPassword.equalsIgnoreCase("") || !confirmPassword.equals(password)){
                    signUpConfirmPassword.setError("This need to be the same as your password");
                }else{
                    UserAccountServices.getUserAccountWithEmail(email, this, new OnReceiveResponse() {
                        @Override
                        public void onReceive(JSONObject response) {
                            final Gson gson = new Gson();
                            UserAccount account = gson.fromJson(response.toString(), UserAccount.class);
                            if(account.getUserId() != 0){
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(RegisterScreen.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(RegisterScreen.this);
                                }
                                builder.setTitle("Register")
                                        .setMessage("The email already used")
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }else{
                                UserAccountServices.addUserAccount(account, RegisterScreen.this, new OnReceiveResponse() {
                                    @Override
                                    public void onReceive(JSONObject response) {
                                        accountPrefs.saveUserLogin(response.toString());
                                        profilePrefs.saveUserProfile(gson.toJson(new UserProfile()));
                                        Intent intent = new Intent(RegisterScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        }
                    });
                }
                break;
            case R.id.txtSignIn:
                Intent intent = new Intent(this, LoginScreen.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
