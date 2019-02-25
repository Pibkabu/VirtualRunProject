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

import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.facebooksdk.FacebookLogin;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.RegisterScreen;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.ResetPasswordScreen;
import com.example.quynh.virtualrunproject.services.OnReceiveResponse;
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

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        if(!userAccount.equalsIgnoreCase("")){
            Intent intent = new Intent(this, MainActivity.class);
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
                                            if(account.getUserId() != 0){
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
                                            }else{
                                                //if there are no account with the same email
                                                //add account to database
                                                account.setEmail(email);
                                                account.setPassword("");
                                                UserAccountServices.addUserAccount(account, LoginScreen.this, new OnReceiveResponse() {
                                                    @Override
                                                    public void onReceive(JSONObject response) {
                                                        accountPrefs.saveUserLogin(response.toString());
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }catch (Exception ex){
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
    }

    private void setupAction() {
        fbLoginBtn.setOnClickListener(this);
        normLoginBtn.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    //testing stava
//    private void stravaApiCall(){
//        String URL = "https://www.strava.com/api/v3/athlete";
//        CustomRequest customRequest = new CustomRequest(URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("LoginScreen", "onResponse: " + response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("LoginScreen", "onResponse: " + error);
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer " + getBaseContext().getResources().getString(R.string.access_token));
//                return headers;
//            }
//        };
//        customRequest.setRetryPolicy(AppController.myRetryPolicy);
//        AppController.getInstance().addToRequestQueue(customRequest);
//    }

    //testing server
//    private void restfulCall(){
//        Gson gson = new Gson();
//        String URL = "http://192.168.1.76:8080/user/id?id=User03";
//        Map<String, String> params = new HashMap<>();
//        CustomRequest customRequest = new CustomRequest(URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("LoginScreen", "onResponse: " + response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("LoginScreen", "onResponse: " + error);
//            }
//        });
//        customRequest.setRetryPolicy(AppController.myRetryPolicy);
//        AppController.getInstance().addToRequestQueue(customRequest);
//    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.fbLoginBtn:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.normLoginBtn:
                //Temporary
                String email = emailtext.getText().toString();
                String password = passwordtext.getText().toString();
                if(email.equalsIgnoreCase("")){
                    emailtext.setError("This does not filled yet");
                }else if(password.equalsIgnoreCase("")){
                    passwordtext.setError("This does not filled yet");
                }else{
                    UserAccount account = new UserAccount();
                    account.setEmail(email);
                    account.setPassword(password);
                    UserAccountServices.accountLogin(account, this, new OnReceiveResponse() {
                        @Override
                        public void onReceive(JSONObject response) {
                            final Gson gson = new Gson();
                            UserAccount responeAcc = gson.fromJson(response.toString(), UserAccount.class);
                            if(responeAcc.getUserId() != 0){
                                accountPrefs.saveUserLogin(response.toString());
                                UserProfileServices.getUserProfileWithId(responeAcc.getUserId(), LoginScreen.this, new OnReceiveResponse() {
                                    @Override
                                    public void onReceive(JSONObject response) {
                                        profilePrefs.saveUserProfile(response.toString());
                                        Log.d("CHECKRESULT", "onReceive: " + response);
//                                        UserProfile profile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);
//                                        Log.d("CHECKDOB", "onReceive: " + profile.getDOB().toString());
                                        Intent intent1 = new Intent(LoginScreen.this, MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                });
                            }else{
                                Toast.makeText(LoginScreen.this, "Your email or password is incorrect", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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
