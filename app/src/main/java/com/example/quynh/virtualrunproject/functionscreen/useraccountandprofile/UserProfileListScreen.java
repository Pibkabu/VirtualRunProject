package com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.ProfileListAdapter;
import com.example.quynh.virtualrunproject.dao.UserProfileDAO;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.google.gson.Gson;

import java.util.List;

public class UserProfileListScreen extends AppCompatActivity {

    private ImageView backBtn;
    private List<UserProfile> profiles;
    private RecyclerView recyclerView;
    private TextView numberOfRunners;
    private ProfileListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_list_screen);

        setupView();
        setupAction();
        setupProfileList();
    }

    private void setupProfileList() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        UserProfileDAO dao = gson.fromJson(intent.getStringExtra("profiles"), UserProfileDAO.class);
        profiles = dao.getProfiles();
        numberOfRunners.setText(profiles.size() + " người tham gia");
        adapter = new ProfileListAdapter(profiles, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserProfileListScreen.this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
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
        title.setText("Danh sách người dùng");
        numberOfRunners = (TextView) findViewById(R.id.number_of_runners);
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }
}
