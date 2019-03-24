package com.example.quynh.virtualrunproject.mainfragmentscreens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.mainfragmentscreens.hostingcontainedfragments.OngoingRunFragment;
import com.example.quynh.virtualrunproject.mainfragmentscreens.hostingcontainedfragments.PastRunFragment;

/**
 * Created by quynh on 3/21/2019.
 */

public class HostingFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hosting_fragment_layout, container, false);
        return view;
    }

    private FragmentScreensAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager tablayoutContent;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupViewPager(tablayoutContent);
    }

    private void setupView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tablayoutContent = (ViewPager) view.findViewById(R.id.tablayout_content);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragmentScreensAdapter(getChildFragmentManager());
        adapter.addFragment(new OngoingRunFragment(), "Đang diễn ra");
        adapter.addFragment(new PastRunFragment(), "Đã kết thúc");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
