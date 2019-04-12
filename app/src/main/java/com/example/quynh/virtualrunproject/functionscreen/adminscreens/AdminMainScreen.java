package com.example.quynh.virtualrunproject.functionscreen.adminscreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.LoginScreen;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.dao.UserAccountDAO;
import com.example.quynh.virtualrunproject.functionscreen.hosting.CreateRaceScreen;
import com.example.quynh.virtualrunproject.services.RaceServices;
import com.example.quynh.virtualrunproject.services.UserAccountServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.example.quynh.virtualrunproject.userlogintracker.UserProfilePrefs;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AdminMainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawer;
    private ImageView menu;
    private TextView displayName;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;
    private TextView numbersOfUsers;
    private TextView numbersOfRaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);
        accountPrefs = new UserAccountPrefs(this);
        profilePrefs = new UserProfilePrefs(this);
        setupView();
        setupAction();
        setupData();
    }

    private void setupData() {
        final Gson gson = new Gson();
        UserAccountServices.getAllUserAccount(this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                UserAccountDAO dao = gson.fromJson(response.toString(), UserAccountDAO.class);
                numbersOfUsers.setText(String.valueOf(dao.getAccounts().size()));
            }
        });

        RaceServices.getAllRaces(this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                RacesListDAO dao = gson.fromJson(response.toString(), RacesListDAO.class);
                numbersOfRaces.setText(String.valueOf(dao.getRaces().size()));
            }
        });
    }

    private void setupAction() {
        menu.setOnClickListener(this);
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Administrator");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = (ImageView) findViewById(R.id.menu_btn);
        menu.setVisibility(View.VISIBLE);
        displayName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_display_name);
        displayName.setText("Administrator");

        numbersOfUsers = (TextView) findViewById(R.id.numbers_of_users);
        numbersOfRaces = (TextView) findViewById(R.id.numbers_of_races);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.create_race) {
            intent = new Intent(AdminMainScreen.this, CreateRaceScreen.class);
            startActivityForResult(intent, 1);
        }else if(id == R.id.view_races_result){
            intent = new Intent(AdminMainScreen.this, EndedRacesScreen.class);
            startActivity(intent);
        }else if(id == R.id.edit_race){
            intent = new Intent(AdminMainScreen.this, AdminCreatedRacesScreen.class);
            startActivity(intent);
        }else if(id == R.id.logout){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Log out")
                    .setMessage("Are you sure you want to log out ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(LoginManager.getInstance() != null){
                                LoginManager.getInstance().logOut();
                            }
                            accountPrefs.saveUserLogin("");
                            profilePrefs.saveUserProfile("");
                            Intent intent = new Intent(AdminMainScreen.this, LoginScreen.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            setupData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_btn:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
