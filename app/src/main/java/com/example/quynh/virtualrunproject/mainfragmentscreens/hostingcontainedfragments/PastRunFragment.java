package com.example.quynh.virtualrunproject.mainfragmentscreens.hostingcontainedfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.RacesAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.functionscreen.hosting.RaceResultScreen;
import com.example.quynh.virtualrunproject.services.HostingServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quynh on 3/24/2019.
 */

public class PastRunFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hosting_past_run_layout, container, false);
        return view;
    }

    private RecyclerView recyclerView;
    private List<Race> races;
    private RacesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noData;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupRaceInfo();
    }

    private void setupView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        races = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        noData = (LinearLayout) view.findViewById(R.id.no_data);
        adapter = new RacesAdapter(races, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        adapter.setOnButtonClickRecyclerViewAdapter(new OnButtonClickRecyclerViewAdapter() {
            @Override
            public void OnButtonClick(int position) {
                Intent intent = new Intent(getActivity(), RaceResultScreen.class);
                Gson gson = new Gson();
                intent.putExtra("raceString", gson.toJson(races.get(position)));
                getActivity().startActivity(intent);
            }
        });
    }

    private void setupRaceInfo(){
        UserAccountPrefs accountPrefs = new UserAccountPrefs(getActivity());
        final Gson gson = new Gson();
        UserAccount account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        HostingServices.getPastRacesUserHosting(account.getUserId(), getActivity(), new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                RacesListDAO dao = gson.fromJson(response.toString(), RacesListDAO.class);
                if(!dao.getRaces().isEmpty()){
                    recyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    for (Race race : dao.getRaces()){
                        races.add(race);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    recyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        races.clear();
        setupRaceInfo();
    }
}
