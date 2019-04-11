package com.example.quynh.virtualrunproject.functionscreen.hosting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.DonationAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.DonationDAO;
import com.example.quynh.virtualrunproject.entity.DonateAccount;
import com.example.quynh.virtualrunproject.services.DonationServices;
import com.google.gson.Gson;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_donation_screen);

        setupView();
        setupAccountAndDonationInfo();
        setupAction();
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupAccountAndDonationInfo() {
        Intent intent = getIntent();
        final Gson gson = new Gson();
        DonateAccount account = gson.fromJson(intent.getStringExtra("donateAccount"), DonateAccount.class);
        accountName.setText(account.getAccountName());
        accountNumber.setText(account.getAccountNumber());

        DonationServices.getRaceDonationRecord(account.getRaceId(), this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Log.d("TestInfo", "onReceive: " + response.toString());
                DonationDAO dao = gson.fromJson(response.toString(), DonationDAO.class);
                infos = dao.getInfos();
                if(infos != null && !infos.isEmpty()){
                    adapter = new DonationAdapter(infos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RaceDonationScreen.this));
                    recyclerView.setAdapter(adapter);
                    recyclerView.setNestedScrollingEnabled(false);
                }
            }
        });
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Thông Tin Quyên Góp");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        accountName = (TextView) findViewById(R.id.account_name);
        accountNumber = (TextView) findViewById(R.id.account_number);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


    }
}
