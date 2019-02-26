package com.example.quynh.virtualrunproject.mainfragmentscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.functionscreen.useraccountandprofile.ProfileChangeScreen;
import com.example.quynh.virtualrunproject.mainfragmentscreens.userprofilecontainedfragments.ProfileAchievementFragment;
import com.example.quynh.virtualrunproject.mainfragmentscreens.userprofilecontainedfragments.UserPostFragment;
import com.example.quynh.virtualrunproject.userlogintracker.UserProfilePrefs;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by quynh on 12/26/2018.
 */

public class UserProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userprofile_fragment_layout, container, false);
        return view;
    }

    private UserProfilePrefs profilePrefs;
    public static TextView displayName;
    private CircleImageView profilePic;
    private Button editProfileBtn;
    private FragmentScreensAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager tablayoutContent;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePrefs = new UserProfilePrefs(getActivity());
        setupView(view);
        setupViewPager(tablayoutContent);
        setupAction();
    }

    private void setupView(View view) {
        Gson gson = new Gson();
        UserProfile profile = gson.fromJson(profilePrefs.getProfile(), UserProfile.class);
        displayName = view.findViewById(R.id.user_display_name);
        displayName.setText(profile.getDisplayName());
        profilePic = (CircleImageView) view.findViewById(R.id.user_profile_pic);
        editProfileBtn = (Button) view.findViewById(R.id.edit_profile_btn);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tablayoutContent = (ViewPager) view.findViewById(R.id.tablayout_content);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragmentScreensAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new ProfileAchievementFragment(), "Information");
        adapter.addFragment(new UserPostFragment(), "Post");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupAction(){
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileChangeScreen.class);
                getActivity().startActivityForResult(intent, 1);
            }
        });
    }
}
