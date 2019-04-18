package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.helper.EmailSender;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Random;

public class GetVerifyCodeScreen extends AppCompatActivity {

    private String email;
    private String code;
    private EditText verifyCode;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_verify_code_screen);

        setupView();

        setupAction();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        sendEmailThread.execute();
    }

    public String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    private void setupAction() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.equalsIgnoreCase(verifyCode.getText().toString())){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(GetVerifyCodeScreen.this, "Mã xác nhận không chính xác", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupView() {
        verifyCode = (EditText) findViewById(R.id.verify_code);
        confirmButton = (Button) findViewById(R.id.confirm_btn);
        PushDownAnim.setPushDownAnimTo(confirmButton);
    }

    private void sendEmail(){
        try {
            code = getRandomNumberString();
            EmailSender sender = new EmailSender("virtualraceaplication@gmail.com", "suncho2511");
            sender.sendMail("Verify Register",
                    "Xin Chào bạn \n Chúng mình là đội ngũ phát triển của VRA, chúng mình cần bạn điền đoạn mã xác " +
                            "nhận dưới đây vào app để xác nhận đăng ký cho tài khoản của bạn \n" +
                            "MÃ XÁC NHẬN: " + code,
                    "virtualraceaplication@gmail.com", email);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    AsyncTask<Void, Void, Void> sendEmailThread = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            sendEmail();
            return null;
        }
    };
}
