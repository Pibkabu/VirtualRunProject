package com.example.quynh.virtualrunproject.functionscreen.adminscreens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

public class AdminCreatedRacesScreen extends AppCompatActivity implements TextView.OnEditorActionListener{

    private ImageView backBtn;
    private RecyclerView recyclerView;
    private List<Race> races;
    private AdminRaceEditAdapter adapter;
    private TextView nameSearched;
    private ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_created_races_screen);

        setupView();
        setupAction();
        setupRaces();
    }

    private void search(String name){
        Iterator<Race> iter = races.iterator();

        while (iter.hasNext()) {
            Race race = iter.next();

            if (!race.getName().toLowerCase().contains(name.toLowerCase())){
                iter.remove();
                adapter.notifyDataSetChanged();
            }
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

                recyclerView.setLayoutManager(new LinearLayoutManager(AdminCreatedRacesScreen.this));
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
        title.setText("Các đường chạy đang diễn ra");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        nameSearched = (TextView) findViewById(R.id.name_searched);
        imgSearch = (ImageView) findViewById(R.id.img_search);
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
            search(nameSearched.getText().toString());
            return true;
        }
        return false;
    }
}
