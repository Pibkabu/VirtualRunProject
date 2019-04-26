package com.example.quynh.virtualrunproject.functionscreen.adminscreens;

import android.content.Context;
import android.content.Intent;
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

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.customGUI.AdminRacesAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.functionscreen.hosting.RaceResultScreen;
import com.example.quynh.virtualrunproject.services.RaceServices;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class EndedRacesScreen extends AppCompatActivity implements TextView.OnEditorActionListener,
        SwipeRefreshLayout.OnRefreshListener{

    private ImageView backBtn;
    private RecyclerView recyclerView;
    private List<Race> races;
    private AdminRacesAdapter adapter;
    private TextView nameSearched;
    private ImageView imgSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ended_races_screen);

        setupView();
        setupAction();
        setupRaces();
    }

    private void search(Race name) {

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

    private void setupRaces() {
        RaceServices.getALlEndedRaces(this, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                final Gson gson = new Gson();
                RacesListDAO dao = gson.fromJson(response.toString(), RacesListDAO.class);
                races = dao.getRaces();
                adapter = new AdminRacesAdapter(races, EndedRacesScreen.this);
                adapter.setOnButtonClickRecyclerViewAdapter(new OnButtonClickRecyclerViewAdapter() {
                    @Override
                    public void OnButtonClick(int position) {
                        Intent intent = new Intent(EndedRacesScreen.this, RaceResultScreen.class);
                        intent.putExtra("raceString", gson.toJson(races.get(position)));
                        startActivity(intent);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(EndedRacesScreen.this));
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);

                if(!races.isEmpty()){
                    recyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
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
                InputMethodManager inputManager = (InputMethodManager) EndedRacesScreen.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(EndedRacesScreen.this.getCurrentFocus().getWindowToken(), 0);
                Race race = new Race();
                race.setName(nameSearched.getText().toString());
                search(race);
            }
        });
        nameSearched.setOnEditorActionListener(this);
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Các Đường Chạy Đã Kết Thúc");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        nameSearched = (TextView) findViewById(R.id.name_searched);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        noData = (LinearLayout) findViewById(R.id.no_data);
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
