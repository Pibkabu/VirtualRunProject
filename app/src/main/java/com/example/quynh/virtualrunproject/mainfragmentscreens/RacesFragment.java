package com.example.quynh.virtualrunproject.mainfragmentscreens;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.daimajia.swipe.SwipeLayout;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.RacesAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.services.RaceServices;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quynh on 12/26/2018.
 */

public class RacesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.races_fragment_layout, container, false);

        return view;
    }

    private EditText fromDistance, toDistance;
    private Button filterBtn;
    private RecyclerView recyclerView;
    private List<Race> races;
    private RacesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupAction();
        getOngoingRaces();
    }

    private void setupView(View view){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        fromDistance = (EditText) view.findViewById(R.id.distance_from);
        toDistance = (EditText) view.findViewById(R.id.distance_to);
        filterBtn = (Button) view.findViewById(R.id.filter_btn);

        recyclerView = (RecyclerView) view.findViewById(R.id.racesList);
        races = new ArrayList<>();
        adapter = new RacesAdapter(races, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        PushDownAnim.setPushDownAnimTo(filterBtn);
    }

    private void setupAction() {
        swipeRefreshLayout.setOnRefreshListener(this);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close android keyboard
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                if(fromDistance.getText().toString().equalsIgnoreCase("")){
                    fromDistance.setError("This does not filled yet");
                }else if(toDistance.getText().toString().equalsIgnoreCase("")){
                    toDistance.setError("This does not filled yet");
                }else{
                    double from = Double.valueOf(fromDistance.getText().toString());
                    double to = Double.valueOf(toDistance.getText().toString());
                    getRacesWithDistanceRange(from, to);
                }
            }
        });
    }

    private void getRacesWithDistanceRange(double from, double to){
        RaceServices.getRacesWithDistanceRange(from, to, getActivity(), new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                Log.d("RacesFragment", "onResponse: " + response);
                races.clear();
                RacesListDAO racesListDAO = gson.fromJson(response.toString(), RacesListDAO.class);
                if(response != null || !response.toString().equals("")){
                    for(Race race : racesListDAO.getRaces()){
                        races.add(race);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void getOngoingRaces(){
        RaceServices.getOngoingRaces(getActivity(), new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                Log.d("RacesFragment", "onResponse: " + response);
                RacesListDAO racesListDAO = gson.fromJson(response.toString(), RacesListDAO.class);
                if (!racesListDAO.getRaces().isEmpty()) {
                    for (Race race : racesListDAO.getRaces()) {
                        races.add(race);
                    }
                    adapter.notifyDataSetChanged();
                }
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        races = new ArrayList<>();
        getOngoingRaces();
    }
}
