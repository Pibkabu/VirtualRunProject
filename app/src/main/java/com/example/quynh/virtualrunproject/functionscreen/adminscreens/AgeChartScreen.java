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
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgeChartScreen extends AppCompatActivity {

    private ImageView backBtn;
    private PieChart ageChart;
    private List<UserProfile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_chart_screen);

        setupView();
        setupAction();
        getProfileData();
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
        title.setText("Tỉ Lệ Độ Tuổi");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        ageChart = (PieChart) findViewById(R.id.age_chart);
        ageChart.setRotationEnabled(true);
        ageChart.setHoleRadius(25);
        ageChart.setTransparentCircleAlpha(0);
        ageChart.setCenterText("Độ Tuổi");
        ageChart.setCenterTextSize(10);
        Description description = new Description();
        description.setTextSize(16);
        description.setText("Thống Kê Độ Tuổi");
        ageChart.setDescription(description);
    }

    private int getProfileAge(UserProfile profile){
        Date date = DateFormatHandler.stringToDate("yyyy-MM-dd", profile.getDOB().toString());
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        int profileYear = calendar.get(Calendar.YEAR);
        return currentYear - profileYear;
    }

    private void setupChart() {
        List<UserProfile> lessThan18 = new ArrayList<>();
        List<UserProfile> from18to25 = new ArrayList<>();
        List<UserProfile> greaterThan25 = new ArrayList<>();
        for (UserProfile profile : profiles){
            if(getProfileAge(profile) < 18){
                lessThan18.add(profile);
            }else if(getProfileAge(profile) >= 18 || getProfileAge(profile) <= 25){
                from18to25.add(profile);
            }else{
                greaterThan25.add(profile);
            }
        }

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(lessThan18.size(), "< 18 tuổi"));
        pieEntries.add(new PieEntry(from18to25.size(), "18-25 tuổi"));
        pieEntries.add(new PieEntry(greaterThan25.size(), "> 25 tuổi"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(12);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueFormatter(new PercentFormatter(ageChart));
        PieData data = new PieData(dataSet);
        ageChart.setUsePercentValues(true);
        ageChart.setData(data);
        ageChart.animateY(1000);
        ageChart.invalidate();
    }
}
