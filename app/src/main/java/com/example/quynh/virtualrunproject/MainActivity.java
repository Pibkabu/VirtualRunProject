package com.example.quynh.virtualrunproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quynh.virtualrunproject.customGUI.ProfileRegisterDialog;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.ProfileChangeScreen;
import com.example.quynh.virtualrunproject.mainfragmentscreens.FragmentScreensAdapter;
import com.example.quynh.virtualrunproject.mainfragmentscreens.HostingFragment;
import com.example.quynh.virtualrunproject.mainfragmentscreens.RacesFragment;
import com.example.quynh.virtualrunproject.mainfragmentscreens.UserProfileFragment;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.example.quynh.virtualrunproject.userlogintracker.UserProfilePrefs;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout drawer;
    private ImageView racesBtn, profileBtn, hosting;
    private ImageView menu, message;
    private ImageView userProfilePic;
    private TextView userDisplayName, userId;
    private ViewPager mainContents;
    private FragmentScreensAdapter adapter;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;
    private UserAccount account;
    private UserProfile profile;
    private ProfileRegisterDialog registerDialog;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerDialog = new ProfileRegisterDialog(this);
        accountPrefs = new UserAccountPrefs(this);
        profilePrefs = new UserProfilePrefs(this);
        checkUserProfile();
        if (registerDialog.isShowing()){
            registerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setContentView(R.layout.activity_main);
                    setupView();
                    setupAction();
                }
            });
        }else{
            setContentView(R.layout.activity_main);
            setupView();
            setupAction();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Gson gson = new Gson();
            profile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);
            userDisplayName.setText(profile.getDisplayName());
            if(UserProfileFragment.displayName != null){
                UserProfileFragment.displayName.setText(profile.getDisplayName());
            }
        }
    }

    private void checkUserProfile() {
        Gson gson = new Gson();
        UserProfile userProfile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);
        if(userProfile.getUserId() == 0){
            registerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            registerDialog.show();
        }
    }

    private void setupView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Home");

        userDisplayName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_display_name);
        userId = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_id);
        userProfilePic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_pic);
        menu = (ImageView) findViewById(R.id.menu_btn);
        menu.setVisibility(View.VISIBLE);
        message = (ImageView) findViewById(R.id.message_icon);
        message.setVisibility(View.VISIBLE);
        mainContents = (ViewPager) findViewById(R.id.mainContents);
        racesBtn = (ImageView) findViewById(R.id.racesBtn);
        profileBtn = (ImageView) findViewById(R.id.profileBtn);
        hosting = (ImageView) findViewById(R.id.hosting);

        Gson gson = new Gson();
        profile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);
        userDisplayName.setText(profile.getDisplayName());
        Glide.with(this).load(getIntent().getStringExtra("userProfilePic")).into(userProfilePic);
    }


    private void setupAction() {
        menu.setOnClickListener(this);
        message.setOnClickListener(this);
        adapter = new FragmentScreensAdapter(getSupportFragmentManager());
        setupViewPager(mainContents);
        hosting.setOnClickListener(this);
        racesBtn.setOnClickListener(this);
        profileBtn.setOnClickListener(this);

        mainContents.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerTabChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            //temporary
            Intent intent = new Intent(this, ProfileChangeScreen.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.introduction) {

        } else if (id == R.id.logout) {
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
                            userProfilePic.setImageResource(0);
                            userDisplayName.setText("");
                            userId.setText("");
                            accountPrefs.saveUserLogin("");
                            profilePrefs.saveUserProfile("");
                            Intent intent = new Intent(MainActivity.this, LoginScreen.class);
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

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new RacesFragment(), RacesFragment.class.getSimpleName());
        adapter.addFragment(new HostingFragment(), HostingFragment.class.getSimpleName());
        adapter.addFragment(new UserProfileFragment(), UserProfileFragment.class.getSimpleName());
        //viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }

    private void viewPagerTabChanged(int position){
        if(position == 0){
            title.setText("Home");
            racesBtn.setImageResource(R.drawable.home_red);
            hosting.setImageResource(R.drawable.icon_hosting);
            profileBtn.setImageResource(R.drawable.user_black);

            //set enable
            racesBtn.setEnabled(false);
            hosting.setEnabled(true);
            profileBtn.setEnabled(true);
        }
        else if(position == 1){
            title.setText("Hosting");
            racesBtn.setImageResource(R.drawable.home_black);
            hosting.setImageResource(R.drawable.icon_hosting_red);
            profileBtn.setImageResource(R.drawable.user_black);

            //set enable
            racesBtn.setEnabled(true);
            hosting.setEnabled(false);
            profileBtn.setEnabled(true);
        }else if(position == 2){
            title.setText("Profile");
            racesBtn.setImageResource(R.drawable.home_black);
            hosting.setImageResource(R.drawable.icon_hosting);
            profileBtn.setImageResource(R.drawable.user_red);

            //set enable
            racesBtn.setEnabled(true);
            hosting.setEnabled(true);
            profileBtn.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_btn:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.message_icon:
                //message screen and its business
                break;
            case R.id.racesBtn:
                mainContents.setCurrentItem(0);
                viewPagerTabChanged(0);
                break;
            case R.id.hosting:
                mainContents.setCurrentItem(1);
                viewPagerTabChanged(1);
                break;
            case R.id.profileBtn:
                mainContents.setCurrentItem(2);
                viewPagerTabChanged(2);
                break;
        }
    }
}
