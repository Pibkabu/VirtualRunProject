package com.example.quynh.virtualrunproject.mainfragmentscreens.hostingcontainedfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.OngoingRaceHostingAdapter;
import com.example.quynh.virtualrunproject.customGUI.RacesAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.custominterface.OnSwipeButtonClickAdapter;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserHost;
import com.example.quynh.virtualrunproject.functionscreen.hosting.EditRaceInfoScreen;
import com.example.quynh.virtualrunproject.services.HostingServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quynh on 3/24/2019.
 */

public class OngoingRunFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hosting_ongoing_run_layout, container, false);
        return view;
    }

    private RecyclerView recyclerView;
    private List<Race> races;
    private OngoingRaceHostingAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupAction();
        setupRaceInfo();
    }

    private void setupView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        races = new ArrayList<>();
        adapter = new OngoingRaceHostingAdapter(races, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setupAction(){
        adapter.setOnSwipeButtonClickAdapter(new OnSwipeButtonClickAdapter() {
            @Override
            public void onEditClick(int position) {
                Gson gson = new Gson();
                Intent intent = new Intent(getActivity(), EditRaceInfoScreen.class);
                intent.putExtra("race", gson.toJson(races.get(position)));
                startActivityForResult(intent, 1);
            }

            @Override
            public void onCancelClick(int position) {
                HostingServices.cancelHosting(races.get(position).getRaceId(), getActivity(), new OnReceiveResponse() {
                    @Override
                    public void onReceive(JSONObject response) {
                        Gson gson = new Gson();
                        UserHost host = gson.fromJson(response.toString(), UserHost.class);
                        if(host.getUserAndRaceMaped().getRaceId() == 0){
                            resetFragment();
                        }
                    }
                });
            }
        });
    }

    private void setupRaceInfo(){
        UserAccountPrefs accountPrefs = new UserAccountPrefs(getActivity());
        final Gson gson = new Gson();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        HostingServices.getOngoingRacesUserHosting(account.getUserId(), getActivity(), new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                RacesListDAO dao = gson.fromJson(response.toString(), RacesListDAO.class);
                if(!dao.getRaces().isEmpty()){
                    for (Race race : dao.getRaces()){
                        races.add(race);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void resetFragment(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == getActivity().RESULT_OK){
            resetFragment();
        }
    }
}
