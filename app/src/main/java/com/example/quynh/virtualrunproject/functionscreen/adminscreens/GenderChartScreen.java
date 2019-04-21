package com.example.quynh.virtualrunproject.functionscreen.adminscreens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.UserProfileDAO;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.services.UserProfileServices;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GenderChartScreen extends AppCompatActivity {

    private ImageView backBtn;
    private PieChart genderChart;
    private List<UserProfile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_chart_screen);

        setupView();
        setupAction();
        getProfileData();
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Tỉ Lệ Giới Tính");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        genderChart = (PieChart) findViewById(R.id.gender_chart);
        genderChart.setRotationEnabled(true);
        genderChart.setHoleRadius(25);
        genderChart.setTransparentCircleAlpha(0);
        genderChart.setCenterText("Giới Tính");
        genderChart.setCenterTextSize(10);
        Description description = new Description();
        description.setTextSize(16);
        description.setText("Thống Kê Giới Tính");
        genderChart.setDescription(description);
    }

    public void getProfileData() {
        UserProfileServices.getAllUserProfile(this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                UserProfileDAO dao = gson.fromJson(response.toString(), UserProfileDAO.class);
                profiles = dao.getProfiles();
                if(!profiles.isEmpty()){
                    setupChart();
                }
            }
        });
    }

    private void setupChart() {
        List<UserProfile> males = new ArrayList<>();
        List<UserProfile> females = new ArrayList<>();
        for (UserProfile profile : profiles){
            if(profile.isGender()){
                males.add(profile);
            }else{
                females.add(profile);
            }
        }

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(males.size(), "Nam"));
        pieEntries.add(new PieEntry(females.size(), "Nữ"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(12);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueFormatter(new PercentFormatter(genderChart));
        PieData data = new PieData(dataSet);
        genderChart.setUsePercentValues(true);
        genderChart.setData(data);
        genderChart.animateY(1000);
        genderChart.invalidate();
    }
}
