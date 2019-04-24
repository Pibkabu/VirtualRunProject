package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.helper.DataValidationHelper;
import com.example.quynh.virtualrunproject.services.UserAccountServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

public class ChangePasswordScreen extends AppCompatActivity {

    private EditText oldPassword, newPassword, confirmNewPassword;
    private Button updateBtn;
    private ImageView backBtn;
    private UserAccount account;
    private UserAccountPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);

        setupView();
        setupAction();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Đổi Mật Khẩu");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);

        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password);
        updateBtn = (Button) findViewById(R.id.update_btn);

        PushDownAnim.setPushDownAnimTo(updateBtn);
    }

    private boolean checkOldPassword(String password) {
        Gson gson = new Gson();
        prefs = new UserAccountPrefs(this);
        account = gson.fromJson(prefs.getUserAccount(), UserAccount.class);
        if(!password.equals(account.getPassword())){
            return false;
        }
        return true;
    }

    private void updatePassword() {
        account.setPassword(newPassword.getText().toString());
        UserAccountServices.changePassword(account, this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                account = gson.fromJson(response.toString(), UserAccount.class);
                if(account.getUserId() != 0){
                    prefs.saveUserLogin(response.toString());
                    Toast.makeText(ChangePasswordScreen.this, "Mật khẩu đã cập nhật", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = oldPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                String confirm = confirmNewPassword.getText().toString();
                if (oldPass.equalsIgnoreCase("")) {
                    oldPassword.setError("Thông tin bắt buộc");
                } else if (newPass.equalsIgnoreCase("")) {
                    newPassword.setError("Thông tin bắt buộc");
                } else if (confirm.equalsIgnoreCase("") || !confirm.equals(newPass)) {
                    confirmNewPassword.setError("Xác nhận mật khẩu phải trùng với mật khẩu của bạn");
                } else if (!checkOldPassword(oldPass)) {
                    oldPassword.setError("Mật khẩu cũ không đúng");
                } else if (!DataValidationHelper.validatePassword(newPass)) {
                    newPassword.setError("Mật khẩu cần ít nhất 6 ký tự, 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt");
                } else {
                    updatePassword();
                }
            }
        });
    }
}
