package com.example.quynh.virtualrunproject.mainfragmentscreens.userprofilecontainedfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.connection.AppController;
import com.example.quynh.virtualrunproject.connection.CustomRequest;
import com.example.quynh.virtualrunproject.customGUI.AttendingRaceAdapter;
import com.example.quynh.virtualrunproject.customGUI.MyLoadingDialog;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.RacesListDAO;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.entity.UserAndRaceMaped;
import com.example.quynh.virtualrunproject.services.PlayerServices;
import com.example.quynh.virtualrunproject.strava.StravaDAO;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;
import com.sweetzpot.stravazpot.authenticaton.api.AccessScope;
import com.sweetzpot.stravazpot.authenticaton.api.AuthenticationAPI;
import com.sweetzpot.stravazpot.authenticaton.api.StravaLogin;
import com.sweetzpot.stravazpot.authenticaton.model.AppCredentials;
import com.sweetzpot.stravazpot.authenticaton.model.LoginResult;
import com.sweetzpot.stravazpot.authenticaton.ui.StravaLoginActivity;
import com.sweetzpot.stravazpot.common.api.AuthenticationConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sweetzpot.stravazpot.authenticaton.api.ApprovalPrompt.AUTO;

/**
 * Created by quynh on 2/26/2019.
 */

public class AttendingRaceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attending_race_fragment, container, false);
        return view;
    }

    private static final int RQ_LOGIN = 1001;
    private RecyclerView racesList;
    private AttendingRaceAdapter adapter;
    private List<Race> races;
    private Player record;
    private Race race;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noData;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        getAttending();
    }

    private void setupView(View view) {
        racesList = (RecyclerView) view.findViewById(R.id.racesList);
        races = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        noData = (LinearLayout) view.findViewById(R.id.no_data);
    }

    private void getAttending(){
        UserAccountPrefs prefs = new UserAccountPrefs(getActivity());
        final Gson gson = new Gson();
        UserAccount account = gson.fromJson(prefs.getUserAccount(), UserAccount.class);
        PlayerServices.getAttendingRaces(account.getUserId(), getActivity(), new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                RacesListDAO dao = gson.fromJson(response.toString(), RacesListDAO.class);
                if(!dao.getRaces().isEmpty()){
                    racesList.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    races = dao.getRaces();
                    adapter = new AttendingRaceAdapter(races, getActivity(), new OnButtonClickRecyclerViewAdapter() {
                        @Override
                        public void OnButtonClick(int position) {
                            race = races.get(position);
                            loginStrava();
                        }
                    });
                    racesList.setLayoutManager(new LinearLayoutManager(getContext()));
                    racesList.setAdapter(adapter);
                    racesList.setNestedScrollingEnabled(false);
                }else{
                    racesList.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void loginStrava(){
        Intent intent = StravaLogin.withContext(getActivity())
                .withClientID(31904)
                .withRedirectURI("http://localhost:8080")
                .withApprovalPrompt(AUTO)
                .withAccessScope(AccessScope.VIEW_PRIVATE)
                .makeIntent();
        startActivityForResult(intent, RQ_LOGIN);
    }

    private void sendResult(){
        PlayerServices.sendResult(record, getActivity(), new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                Player player = gson.fromJson(response.toString(), Player.class);
                if(player.getRankInRace() == 0){
                    Toast.makeText(getActivity(), "Khoảng cách đường chạy bạn đã chạy phải lớn hơn hoặc bằng khoảng cách quy định của cuộc đua", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Kết quả chạy của bạn đã được gửi thành công", Toast.LENGTH_LONG).show();
                    races.remove(race);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void getStravaInfo(Intent data){

        AsyncTask<String, String, String> getStravaInfoTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                Log.d("stravaDOINBACKGROUND", "doInBackground: " + strings[0]);
                AuthenticationConfig config = AuthenticationConfig.create()
                        .debug()
                        .build();
                AuthenticationAPI api = new AuthenticationAPI(config);
                LoginResult result = api.getTokenForApp(AppCredentials.with(31904, "e62a25113fce4ed3cad56af48401500a48778242"))
                        .withCode(strings[0])
                        .execute();
                Gson gson = new Gson();
                Log.d("CHECKRESULT", "onActivityResult: " + gson.toJson(result));
                return gson.toJson(result);
            }

            @Override
            protected void onPostExecute(String stravaResult) {
                //super.onPostExecute(s);
                Gson gson = new Gson();
                StravaDAO dao = gson.fromJson(stravaResult, StravaDAO.class);
                String accessToken = dao.getAccess_token().getValue();
                Log.d("CheckReturnResult", "onPostExecute: " + dao.getAccess_token().getValue());
                getStravaRecord(accessToken);
            }
        };

        getStravaInfoTask.execute(data.getStringExtra(StravaLoginActivity.RESULT_CODE));
    }

    private void getStravaRecord(final String accessToken){
        final MyLoadingDialog loadingDialog = new MyLoadingDialog(getActivity());
        loadingDialog.show();
        String URL = "https://www.strava.com/api/v3/athlete/activities";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                UserAccountPrefs prefs = new UserAccountPrefs(getActivity());
                UserAccount account = gson.fromJson(prefs.getUserAccount(), UserAccount.class);
                try{
                    record = new Player();
                    record.setUserAndRaceMaped(new UserAndRaceMaped(account.getUserId(), race.getRaceId()));
                    record.setTravelDistance((response.getJSONObject(0).getDouble("distance")) / 1000);
                    record.setTravelTime((response.getJSONObject(0).getDouble("moving_time")) / 60);
                    sendResult();
                }catch (Exception ex){
                    Toast.makeText(getActivity(), "Tài khoản Strava của bạn chưa có hoạt động nào", Toast.LENGTH_LONG).show();
                    Log.e("GetStravaRecordJsonException", "onResponse: ", ex);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(getActivity(), "Error with Strava", Toast.LENGTH_LONG).show();
                Log.d("GetStravaRecord", "onResponse: " + error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(arrayRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RQ_LOGIN && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("StravaCode", data.getStringExtra(StravaLoginActivity.RESULT_CODE));
            getStravaInfo(data);
        }
    }

    @Override
    public void onRefresh() {
        races.clear();
        getAttending();
    }
}
