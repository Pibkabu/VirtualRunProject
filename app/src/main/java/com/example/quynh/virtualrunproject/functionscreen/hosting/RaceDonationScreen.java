package com.example.quynh.virtualrunproject.functionscreen.hosting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.DonationAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.DonationDAO;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.DonateAccount;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.services.DonationServices;
import com.example.quynh.virtualrunproject.services.HostingServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RaceDonationScreen extends AppCompatActivity {

    private TextView accountName;
    private TextView accountNumber;
    private ImageView backBtn;
    private RecyclerView recyclerView;
    private DonationAdapter adapter;
    private List<DonationDAO.DonationInfo> infos;
    private Button addDonation;
    private DonateAccount donateAccount;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_donation_screen);

        setupView();
        checkHost();
        setupAccountAndDonationInfo();
        setupAction();
    }

    private void checkHost() {
        UserAccountPrefs prefs = new UserAccountPrefs(this);
        final Gson gson = new Gson();
        UserAccount account = gson.fromJson(prefs.getUserAccount(), UserAccount.class);
        HostingServices.getOngoingRacesUserHosting(account.getUserId(), this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                RacesListDAO dao = gson.fromJson(response.toString(), RacesListDAO.class);
                if (!dao.getRaces().isEmpty()) {
                    for (Race ongoingRace : dao.getRaces()) {
                        if (ongoingRace.getRaceId() == donateAccount.getRaceId()) {
                            addDonation.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
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
        addDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDonation();
            }
        });
    }

    private void setupAccountAndDonationInfo() {
        Intent intent = getIntent();
        final Gson gson = new Gson();
        donateAccount = gson.fromJson(intent.getStringExtra("donateAccount"), DonateAccount.class);
        accountName.setText(donateAccount.getAccountName());
        accountNumber.setText(donateAccount.getAccountNumber());

        DonationServices.getRaceDonationRecord(donateAccount.getRaceId(), this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                DonationDAO dao = gson.fromJson(response.toString(), DonationDAO.class);
                infos = dao.getInfos();
                adapter = new DonationAdapter(infos);
                recyclerView.setLayoutManager(new LinearLayoutManager(RaceDonationScreen.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);
            }
        });
    }

    private boolean checkFillData(String inputData) {
        if (inputData.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    private void inputDonation() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.input_donation, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);

        final EditText email = (EditText) promptsView
                .findViewById(R.id.input_email);
        final EditText description = (EditText) promptsView
                .findViewById(R.id.input_description);
        final EditText money = (EditText) promptsView
                .findViewById(R.id.input_money);

        final TextView cancel = (TextView) promptsView.findViewById(R.id.cancel);
        final TextView okBtn = (TextView) promptsView.findViewById(R.id.ok_btn);

        TextView labelText = (TextView) promptsView.findViewById(R.id.label_text);
        labelText.setText("Điền thông tin quyên góp: ");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFillData(email.getText().toString())) {
                    email.setError("Thông tin bắt buộc");
                } else if (!checkFillData(description.getText().toString())) {
                    description.setError("Thông tin bắt buộc");
                } else if (!checkFillData(money.getText().toString())) {
                    money.setError("Thông tin bắt buộc");
                } else {
                    final Gson gson = new Gson();
                    DonationServices.addDonateAccount
                            (donateAccount.getRaceId(), email.getText().toString(), description.getText().toString(), Double.valueOf(money.getText().toString()),
                                    RaceDonationScreen.this, new OnReceiveResponse() {
                                        @Override
                                        public void onReceive(JSONObject response) {
                                            alertDialog.cancel();
                                            DonationDAO.DonationInfo info = gson.fromJson(response.toString(), DonationDAO.DonationInfo.class);
                                            if (info.getProfile().getUserId() != 0) {
                                                infos.add(info);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(RaceDonationScreen.this, "Tài khoản người dùng không tồn tại", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                }
            }
        });

        alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Thông Tin Quyên Góp");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        accountName = (TextView) findViewById(R.id.account_name);
        accountNumber = (TextView) findViewById(R.id.account_number);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addDonation = (Button) findViewById(R.id.add_donation);
        PushDownAnim.setPushDownAnimTo(addDonation);
    }
}
