package com.example.quynh.virtualrunproject.functionscreen.adminscreens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.AdminRaceEditAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.custominterface.OnSwipeButtonClickAdapter;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserHost;
import com.example.quynh.virtualrunproject.functionscreen.hosting.EditRaceInfoScreen;
import com.example.quynh.virtualrunproject.functionscreen.hosting.RaceResultScreen;
import com.example.quynh.virtualrunproject.functionscreen.race.RaceDetailScreen;
import com.example.quynh.virtualrunproject.services.HostingServices;
import com.example.quynh.virtualrunproject.services.RaceServices;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class AdminCreatedRacesScreen extends AppCompatActivity implements TextView.OnEditorActionListener,
        SwipeRefreshLayout.OnRefreshListener{

    private ImageView backBtn;
    private RecyclerView recyclerView;
    private List<Race> races;
    private AdminRaceEditAdapter adapter;
    private TextView nameSearched;
    private ImageView imgSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_created_races_screen);

        setupView();
        setupAction();
        setupRaces();
    }

    private void search(Race name){
//        Iterator<Race> iter = races.iterator();
//
//        while (iter.hasNext()) {
//            Race race = iter.next();
//
//            if (!race.getName().toLowerCase().contains(name.toLowerCase())){
//                iter.remove();
//                adapter.notifyDataSetChanged();
//            }
//        }
//
//        if(!races.isEmpty()){
//            recyclerView.setVisibility(View.VISIBLE);
//            noData.setVisibility(View.GONE);
//        }else{
//            recyclerView.setVisibility(View.GONE);
//            noData.setVisibility(View.VISIBLE);
//        }

        if (!name.getName().equalsIgnoreCase("")) {
            RaceServices.searchRacesWithName(name, this, new OnReceiveResponse() {
                @Override
                public void onReceive(JSONObject response) {
                    Gson gson = new Gson();
                    races.clear();
                    RacesListDAO racesListDAO = gson.fromJson(response.toString(), RacesListDAO.class);
                    if (!racesListDAO.getRaces().isEmpty()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        for (Race race : racesListDAO.getRaces()) {
                            races.add(race);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    private void cancelRace(final Race race){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Hủy đường chạy")
                .setMessage("Bạn có chắc chắn muốn hủy đường chạy này không ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HostingServices.cancelHosting(race.getRaceId(), AdminCreatedRacesScreen.this, new OnReceiveResponse() {
                            @Override
                            public void onReceive(JSONObject response) {
                                Gson gson = new Gson();
                                UserHost host = gson.fromJson(response.toString(), UserHost.class);
                                if(host.getUserAndRaceMaped().getRaceId() == 0){
                                    races.remove(race);
                                    adapter.notifyDataSetChanged();
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

    private void setupRaces() {
        RaceServices.getALlEndedRaces(this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                final Gson gson = new Gson();
                RacesListDAO dao = gson.fromJson(response.toString(), RacesListDAO.class);
                races = dao.getRaces();
                adapter = new AdminRaceEditAdapter(races, AdminCreatedRacesScreen.this);

                adapter.setOnSwipeButtonClickAdapter(new OnSwipeButtonClickAdapter() {
                    @Override
                    public void onEditClick(int position) {
                        Intent intent = new Intent(AdminCreatedRacesScreen.this, EditRaceInfoScreen.class);
                        intent.putExtra("race", gson.toJson(races.get(position)));
                        startActivityForResult(intent, 1);
                    }

                    @Override
                    public void onCancelClick(int position) {
                        cancelRace(races.get(position));
                    }
                });

                adapter.setOnButtonClickRecyclerViewAdapter(new OnButtonClickRecyclerViewAdapter() {
                    @Override
                    public void OnButtonClick(int position) {
                        Intent intent1 = new Intent(AdminCreatedRacesScreen.this, RaceDetailScreen.class);
                        intent1.putExtra("raceString", gson.toJson(races.get(position)));
                        startActivity(intent1);
                    }
                });

                if(!races.isEmpty()){
                    recyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(AdminCreatedRacesScreen.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setupAction() {
        swipeRefreshLayout.setOnRefreshListener(this);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) AdminCreatedRacesScreen.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(AdminCreatedRacesScreen.this.getCurrentFocus().getWindowToken(), 0);
                Race race = new Race();
                race.setName(nameSearched.getText().toString());
                search(race);
            }
        });
        nameSearched.setOnEditorActionListener(this);
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Các đường chạy đang diễn ra");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        nameSearched = (TextView) findViewById(R.id.name_searched);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        noData = (LinearLayout) findViewById(R.id.no_data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            setupRaces();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            Race race = new Race();
            race.setName(nameSearched.getText().toString());
            search(race);
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        setupRaces();
    }
}
