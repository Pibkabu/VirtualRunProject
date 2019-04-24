package com.example.quynh.virtualrunproject.functionscreen.adminscreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
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

public class EndedRacesScreen extends AppCompatActivity implements TextView.OnEditorActionListener {

    private ImageView backBtn;
    private RecyclerView recyclerView;
    private List<Race> races;
    private AdminRacesAdapter adapter;
    private TextView nameSearched;
    private ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ended_races_screen);

        setupView();
        setupAction();
        setupRaces();
    }

    private void search(String name) {

        Iterator<Race> iter = races.iterator();

        while (iter.hasNext()) {
            Race race = iter.next();

            if (!race.getName().toLowerCase().contains(name.toLowerCase())){
                iter.remove();
                adapter.notifyDataSetChanged();
            }
        }
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
            }
        });
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(nameSearched.getText().toString());
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
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search(nameSearched.getText().toString());
            return true;
        }
        return false;
    }
}
