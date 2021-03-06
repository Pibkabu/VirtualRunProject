package com.example.quynh.virtualrunproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.facebooksdk.FacebookLogin;
import com.example.quynh.virtualrunproject.functionscreen.adminscreens.AdminMainScreen;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.RegisterScreen;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.ResetPasswordScreen;
import com.example.quynh.virtualrunproject.services.UserAccountServices;
import com.example.quynh.virtualrunproject.services.UserProfileServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.example.quynh.virtualrunproject.userlogintracker.UserProfilePrefs;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private Button fbLoginBtn, normLoginBtn;
    private EditText emailtext, passwordtext;
    private TextView txtSignUp, forgotPassword;
    private CallbackManager callbackManager;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;
    private UserAccount account = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        accountPrefs = new UserAccountPrefs(this);
        profilePrefs = new UserProfilePrefs(this);
        checkUserLogin();
        setupView();
        setupFbSdk();
        setupAction();
    }

    private void checkUserLogin() {
        UserAccountPrefs prefs = new UserAccountPrefs(this);
        String userAccount = prefs.getUserAccount();
        if (!userAccount.equalsIgnoreCase("")) {

            Gson gson = new Gson();
            UserAccount account = gson.fromJson(userAccount, UserAccount.class);
            Intent intent;
            if (account.isAccountRole()) {
                intent = new Intent(this, AdminMainScreen.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

    private void setupFbSdk() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        FacebookLogin.loadUserProfile(loginResult.getAccessToken(), new FacebookLogin.ResultHandler() {
                            @Override
                            public void responeResult(JSONObject object) {
                                try {
                                    final Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                                    final Gson gson = new Gson();
                                    final String email = object.getString("email");
                                    intent.putExtra("name", object.getString("first_name") + " " + object.getString("last_name"));
                                    intent.putExtra("email", object.getString("email"));
                                    intent.putExtra("userProfilePic", "https://graph.facebook.com/" + object.getString("id") + "/picture?type=normal");

                                    UserAccountServices.getUserAccountWithEmail(email, LoginScreen.this, new OnReceiveResponse() {
                                        @Override
                                        public void onReceive(JSONObject response) {
                                            account = gson.fromJson(response.toString(), UserAccount.class);
                                            if (account.getUserId() != 0) {
                                                //if there are account with the same email
                                                accountPrefs.saveUserLogin(gson.toJson(account));
                                                UserProfileServices.getUserProfileWithId(account.getUserId(), LoginScreen.this, new OnReceiveResponse() {
                                                    @Override
                                                    public void onReceive(JSONObject response) {
                                                        profilePrefs.saveUserProfile(response.toString());
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            } else {
                                                //if there are no account with the same email
                                                //add account to database
                                                account.setEmail(email);
                                                account.setPassword("");
                                                UserAccountServices.addUserAccount(account, LoginScreen.this, new OnReceiveResponse() {
                                                    @Override
                                                    public void onReceive(JSONObject response) {
                                                        accountPrefs.saveUserLogin(response.toString());
                                                        UserProfile profile = new UserProfile();
                                                        profilePrefs.saveUserProfile(gson.toJson(profile));
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } catch (Exception ex) {
                                    Log.e("LoginScreen", "responeResult: ", ex);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }

    private void setupView() {
        fbLoginBtn = (Button) findViewById(R.id.fbLoginBtn);
        normLoginBtn = (Button) findViewById(R.id.normLoginBtn);
        emailtext = (EditText) findViewById(R.id.login_email);
        passwordtext = (EditText) findViewById(R.id.password);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);

        PushDownAnim.setPushDownAnimTo(fbLoginBtn);
        PushDownAnim.setPushDownAnimTo(normLoginBtn);
    }

    private void setupAction() {
        fbLoginBtn.setOnClickListener(this);
        normLoginBtn.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    private void login(String email, String password){
        UserAccount account = new UserAccount();
        account.setEmail(email);
        account.setPassword(password);
        UserAccountServices.accountLogin(account, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                final Gson gson = new Gson();
                UserAccount responeAcc = gson.fromJson(response.toString(), UserAccount.class);
                if (responeAcc.getUserId() != 0) {
                    if (responeAcc.isAccountRole()) {
                        Intent intent1 = new Intent(LoginScreen.this, AdminMainScreen.class);
                        startActivity(intent1);
                        finish();
                    } else {
                        UserProfileServices.getUserProfileWithId(responeAcc.getUserId(), LoginScreen.this, new OnReceiveResponse() {
                            @Override
                            public void onReceive(JSONObject response) {
                                profilePrefs.saveUserProfile(response.toString());
                                Log.d("CHECKRESULT", "onReceive: " + response);
                                Intent intent1 = new Intent(LoginScreen.this, MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        });
                    }
                    accountPrefs.saveUserLogin(response.toString());

                } else {
                    Toast.makeText(LoginScreen.this, "Email hoặc mật khẩu của bạn không đúng", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fbLoginBtn:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.normLoginBtn:
                String email = emailtext.getText().toString();
                String password = passwordtext.getText().toString();
                if (email.equalsIgnoreCase("")) {
                    emailtext.setError("Thông tin bắt buộc");
                } else if (password.equalsIgnoreCase("")) {
                    passwordtext.setError("Thông tin bắt buộc");
                } else {
                    login(email, password);
                }
                break;
            case R.id.txtSignUp:
                intent = new Intent(this, RegisterScreen.class);
                startActivity(intent);
                break;
            case R.id.forgotPassword:
                intent = new Intent(this, ResetPasswordScreen.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
