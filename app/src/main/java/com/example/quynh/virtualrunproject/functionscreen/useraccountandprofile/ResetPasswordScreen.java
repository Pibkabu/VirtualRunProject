package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.helper.EmailSender;
import com.example.quynh.virtualrunproject.services.UserAccountServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class ResetPasswordScreen extends AppCompatActivity implements View.OnClickListener{

    private Button sendResetEmailBtn;
    private EditText resetEmail;
    private ImageView backBtn;
    private UserAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_screen);

        setupView();
        setupAction();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Quên Mật Khẩu");
        backBtn = (ImageView)findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        sendResetEmailBtn = (Button) findViewById(R.id.sendResetEmailBtn);
        resetEmail = (EditText) findViewById(R.id.resetEmail);
    }

    private void setupAction() {
        sendResetEmailBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void sendEmail(){
        AsyncTask<Void, Void, Void> sendEmailThread = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    EmailSender sender = new EmailSender("virtualraceaplication@gmail.com", "suncho2511");
                    sender.sendMail("Password Reset",
                            "Xin Chào bạn \n Chúng mình là đội ngũ phát triển của VRA, mật khẩu tài khoản của bạn " +
                                    "đã được resest thành công \n" +
                                    "MẬT KHẨU MỚI: 123a123",
                            "virtualraceaplication@gmail.com", resetEmail.getText().toString());
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                finish();
            }
        };

        sendEmailThread.execute();
    }

    private void changePassword(){
        final Gson gson = new Gson();
        String email = resetEmail.getText().toString();
        UserAccountServices.getUserAccountWithEmail(email, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                account = gson.fromJson(response.toString(), UserAccount.class);
                if(account.getUserId() == 0){
                    Toast.makeText(ResetPasswordScreen.this, "Tài khoản không tồn tại, " +
                            "vui lòng kiểm tra lại email của bạn", Toast.LENGTH_LONG).show();
                }else{
                    account.setPassword("123a123");
                    UserAccountServices.changePassword(account, ResetPasswordScreen.this, new OnReceiveResponse() {
                        @Override
                        public void onReceive(JSONObject response) {
                            account = gson.fromJson(response.toString(), UserAccount.class);
                            if(account.getUserId() != 0){
                                Toast.makeText(ResetPasswordScreen.this, "Mật khẩu đã được đổi thành công", Toast.LENGTH_LONG).show();
                                sendEmail();
                            }else{
                                Toast.makeText(ResetPasswordScreen.this, "Có trục trặc khi tiến hành thay đổi mật khẩu", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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
            case R.id.sendResetEmailBtn:
                if(resetEmail.getText().toString().equalsIgnoreCase("")){
                    resetEmail.setError("Thông tin bắt buộc");
                }else{
                    changePassword();
                }
                break;
        }
    }
}
