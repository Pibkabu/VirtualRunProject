package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;
import com.example.quynh.virtualrunproject.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ResetPasswordScreen extends AppCompatActivity implements View.OnClickListener{

    private Button sendResetEmailBtn;
    private EditText resetEmail;
    private ImageView backBtn;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.sendResetEmailBtn:
                break;
        }
    }
}
