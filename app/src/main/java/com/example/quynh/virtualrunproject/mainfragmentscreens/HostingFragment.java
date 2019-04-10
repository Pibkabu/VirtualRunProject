package com.example.quynh.virtualrunproject.mainfragmentscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.functionscreen.hosting.CreateRaceScreen;
import com.example.quynh.virtualrunproject.mainfragmentscreens.hostingcontainedfragments.OngoingRunFragment;
import com.example.quynh.virtualrunproject.mainfragmentscreens.hostingcontainedfragments.PastRunFragment;
import com.thekhaeng.pushdownanim.PushDownAnim;

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
    private ImageView createRaceBtn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupAction();
        setupViewPager(tablayoutContent);
    }

    private void setupView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tablayoutContent = (ViewPager) view.findViewById(R.id.tablayout_content);
        createRaceBtn = (ImageView) view.findViewById(R.id.create_race_btn);

        PushDownAnim.setPushDownAnimTo(createRaceBtn);
    }

    private void setupAction() {
        createRaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateRaceScreen.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragmentScreensAdapter(getChildFragmentManager());
        adapter.addFragment(new OngoingRunFragment(), "Đang diễn ra");
        adapter.addFragment(new PastRunFragment(), "Đã kết thúc");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == getActivity().RESULT_OK){
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.detach(this);
            fragmentTransaction.attach(this);
            fragmentTransaction.commit();
        }
    }
}
