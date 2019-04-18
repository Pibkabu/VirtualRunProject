package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;

import com.example.quynh.virtualrunproject.LoginScreen;
import com.example.quynh.virtualrunproject.MainActivity;
import com.example.quynh.virtualrunproject.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.helper.EmailSender;
import com.example.quynh.virtualrunproject.services.UserAccountServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.example.quynh.virtualrunproject.userlogintracker.UserProfilePrefs;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

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
        title.setText("Đăng Ký");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        normSignUpBtn = (Button) findViewById(R.id.normSignUpBtn);
        signUpEmail = (EditText) findViewById(R.id.signUpEmail);
        signUpPassword = (EditText) findViewById(R.id.signUpPassword);
        signUpConfirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
        PushDownAnim.setPushDownAnimTo(normSignUpBtn);
    }

    private void setupAction() {
        accountPrefs = new UserAccountPrefs(this);
        profilePrefs = new UserProfilePrefs(this);
        backBtn.setOnClickListener(this);
        normSignUpBtn.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
    }

    private void registerAccount(final String email, final String password) {
        UserAccountServices.getUserAccountWithEmail(email, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                final Gson gson = new Gson();
                UserAccount account = gson.fromJson(response.toString(), UserAccount.class);
                account.setEmail(email);
                account.setPassword(password);
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
        });
    }

    private void checkEmail(final String email) {
        UserAccountServices.getUserAccountWithEmail(email, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                final Gson gson = new Gson();
                UserAccount account = gson.fromJson(response.toString(), UserAccount.class);
                if (account.getUserId() != 0) {
                    Toast.makeText(RegisterScreen.this, "Email đã có người sử dụng", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent1 = new Intent(RegisterScreen.this, GetVerifyCodeScreen.class);
                    intent1.putExtra("email", email);
                    startActivityForResult(intent1, 1);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.normSignUpBtn:
                String email = signUpEmail.getText().toString();
                String password = signUpPassword.getText().toString();
                String confirmPassword = signUpConfirmPassword.getText().toString();
                if (email.equalsIgnoreCase("")) {
                    signUpEmail.setError("Thông tin bắt buộc");
                } else if (password.equalsIgnoreCase("")) {
                    signUpPassword.setError("Thông tin bắt buộc");
                } else if (confirmPassword.equalsIgnoreCase("") || !confirmPassword.equals(password)) {
                    signUpConfirmPassword.setError("Xác nhận mật khẩu phải trùng với mật khẩu của bạn");
                } else {
                    checkEmail(email);
                }
                break;
            case R.id.txtSignIn:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.d("TestVerifyCode", "onActivityResult: " + "SUCCESS");
            registerAccount(signUpEmail.getText().toString(), signUpPassword.getText().toString());
        }
    }
}
