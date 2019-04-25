package com.example.quynh.virtualrunproject.mainfragmentscreens.hostingcontainedfragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

public class OngoingRunFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hosting_ongoing_run_layout, container, false);
        return view;
    }

    private RecyclerView recyclerView;
    private List<Race> races;
    private OngoingRaceHostingAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noData;

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

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        noData = (LinearLayout) view.findViewById(R.id.no_data);
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
                cancelRace(races.get(position));
            }
        });
    }

    private void cancelRace(final Race race){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Hủy đường chạy")
                .setMessage("Bạn có chắc chắn muốn hủy đường chạy này không ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HostingServices.cancelHosting(race.getRaceId(), getActivity(), new OnReceiveResponse() {
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
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


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

    @Override
    public void onRefresh() {
        races.clear();
        setupRaceInfo();
    }
}
