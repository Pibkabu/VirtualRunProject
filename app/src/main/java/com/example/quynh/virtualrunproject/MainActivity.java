package com.example.quynh.virtualrunproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.quynh.virtualrunproject.customGUI.ProfileRegisterDialog;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.functionscreen.hosting.CreateRaceScreen;
import com.example.quynh.virtualrunproject.functionscreen.supports.ContactScreen;
import com.example.quynh.virtualrunproject.functionscreen.supports.IntroductionScreen;
import com.example.quynh.virtualrunproject.functionscreen.supports.TutorialScreen;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.ChangePasswordScreen;
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
    private ImageView menu;
    private ImageView userProfilePic;
    private TextView userDisplayName;
    private ViewPager mainContents;
    private FragmentScreensAdapter adapter;
    private UserAccountPrefs accountPrefs;
    private UserProfilePrefs profilePrefs;
    private UserProfile profile;
    private ProfileRegisterDialog registerDialog;
    private TextView title, txtProfile, txtHosting, txtHome;
    private LinearLayout racesBtnLayout, hostingLayout, profileBtnLayout;

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
            registerDialog.setCancelable(false);
            registerDialog.show();
            registerDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    private void setupView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Trang Chủ");

        userDisplayName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_display_name);
        userProfilePic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_pic);
        menu = (ImageView) findViewById(R.id.menu_btn);
        menu.setVisibility(View.VISIBLE);
        mainContents = (ViewPager) findViewById(R.id.mainContents);
        racesBtn = (ImageView) findViewById(R.id.racesBtn);
        profileBtn = (ImageView) findViewById(R.id.profileBtn);
        hosting = (ImageView) findViewById(R.id.hosting);
        txtProfile = (TextView) findViewById(R.id.txtProfile);
        txtHosting = (TextView) findViewById(R.id.txtHosting);
        txtHome = (TextView) findViewById(R.id.txtHome);
        racesBtnLayout = (LinearLayout) findViewById(R.id.racesBtnLayout);
        hostingLayout = (LinearLayout) findViewById(R.id.hostingLayout);
        profileBtnLayout = (LinearLayout) findViewById(R.id.profileBtnLayout);

        Gson gson = new Gson();
        profile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);
        userDisplayName.setText(profile.getDisplayName());
        Log.d("TestImage", "setupView: " + profile.getUserImage());
        if(!profile.getUserImage().equalsIgnoreCase("")){
            Log.d("TestImageProfileScreen", "setupInfo: " + profile.getUserImage());
            try{
                Glide.with(this).load(profile.getUserImage())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(userProfilePic);
            }catch (Exception e){
                Log.e("GildeError", "setupView: ", e);
            }
        }
    }


    private void setupAction() {
        menu.setOnClickListener(this);
        adapter = new FragmentScreensAdapter(getSupportFragmentManager());
        setupViewPager(mainContents);
        racesBtnLayout.setOnClickListener(this);
        hostingLayout.setOnClickListener(this);
        profileBtnLayout.setOnClickListener(this);

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
        Intent intent;
        if (id == R.id.edit_profile) {
            //temporary
            intent = new Intent(this, ProfileChangeScreen.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.edit_password) {
            intent = new Intent(this, ChangePasswordScreen.class);
            startActivity(intent);
        } else if (id == R.id.host_create) {
            intent = new Intent(this, CreateRaceScreen.class);
            startActivityForResult(intent, 2);
        } else if (id == R.id.logout) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất không ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        logout();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else if (id == R.id.introduction) {
            intent = new Intent(this, IntroductionScreen.class);
            startActivity(intent);
        }else if (id == R.id.tutorial) {
            intent = new Intent(this, TutorialScreen.class);
            startActivity(intent);
        }else if (id == R.id.contact_us) {
            intent = new Intent(this, ContactScreen.class);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        if(LoginManager.getInstance() != null){
            LoginManager.getInstance().logOut();
        }
        userDisplayName.setText("");
        accountPrefs.saveUserLogin("");
        profilePrefs.saveUserProfile("");
        Intent intent = new Intent(MainActivity.this, LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
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
            title.setText("Trang Chủ");
            racesBtn.setImageResource(R.drawable.home_red);
            hosting.setImageResource(R.drawable.icon_hosting);
            profileBtn.setImageResource(R.drawable.user_black);

            txtHome.setTextColor(getResources().getColor(R.color.red));
            txtHosting.setTextColor(getResources().getColor(R.color.gray));
            txtProfile.setTextColor(getResources().getColor(R.color.gray));

            //set enable
            racesBtnLayout.setEnabled(false);
            hostingLayout.setEnabled(true);
            profileBtnLayout.setEnabled(true);
        }
        else if(position == 1){
            title.setText("Quản Trị");
            racesBtn.setImageResource(R.drawable.home_black);
            hosting.setImageResource(R.drawable.icon_hosting_red);
            profileBtn.setImageResource(R.drawable.user_black);

            txtHome.setTextColor(getResources().getColor(R.color.gray));
            txtHosting.setTextColor(getResources().getColor(R.color.red));
            txtProfile.setTextColor(getResources().getColor(R.color.gray));

            //set enable
            racesBtnLayout.setEnabled(true);
            hostingLayout.setEnabled(false);
            profileBtnLayout.setEnabled(true);
        }else if(position == 2){
            title.setText("Thông Tin Cá Nhân");
            racesBtn.setImageResource(R.drawable.home_black);
            hosting.setImageResource(R.drawable.icon_hosting);
            profileBtn.setImageResource(R.drawable.user_red);

            txtHome.setTextColor(getResources().getColor(R.color.gray));
            txtHosting.setTextColor(getResources().getColor(R.color.gray));
            txtProfile.setTextColor(getResources().getColor(R.color.red));

            //set enable
            racesBtnLayout.setEnabled(true);
            hostingLayout.setEnabled(true);
            profileBtnLayout.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_btn:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.racesBtnLayout:
                mainContents.setCurrentItem(0);
                viewPagerTabChanged(0);
                break;
            case R.id.hostingLayout:
                mainContents.setCurrentItem(1);
                viewPagerTabChanged(1);
                break;
            case R.id.profileBtnLayout:
                mainContents.setCurrentItem(2);
                viewPagerTabChanged(2);
                break;
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
            Log.d("TestImageShit", "onActivityResult: " + profile.getUserImage());
            if(!profile.getUserImage().equalsIgnoreCase("")){
                try{
                    Glide.with(this).load(profile.getUserImage())
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(userProfilePic);
                    Glide.with(this).load(profile.getUserImage())
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(UserProfileFragment.profilePic);
                }catch (Exception e){
                    Log.e("GildeError", "setupView: ", e);
                }
            }
            mainContents.setCurrentItem(2);
            viewPagerTabChanged(2);
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            adapter.notifyDataSetChanged();
        }
    }
}
