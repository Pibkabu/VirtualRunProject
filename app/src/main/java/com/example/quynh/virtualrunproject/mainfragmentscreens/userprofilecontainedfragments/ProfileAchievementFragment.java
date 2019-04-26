package com.example.quynh.virtualrunproject.mainfragmentscreens.userprofilecontainedfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.dao.PlayerListDAO;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.UserAccount;
import com.example.quynh.virtualrunproject.functionscreen.players.PlayerAchievementScreen;
import com.example.quynh.virtualrunproject.services.PlayerServices;
import com.example.quynh.virtualrunproject.userlogintracker.UserAccountPrefs;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quynh on 2/26/2019.
 */

public class ProfileAchievementFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_achievement_fragment, container, false);
        return view;
    }

    private TextView txtDistance, txtNumberOfRuns;
    private TextView numberOf1st, numberOf2nd, numberOf3rd;
    private TextView seeAllAchievement;
    private UserAccountPrefs accountPrefs;
    private List<Player> players;
    private UserAccount account;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountPrefs = new UserAccountPrefs(getActivity());
        setupView(view);
        setupAction();
    }

    private void setupUserInfo(List<Player> completedRecords){
        txtNumberOfRuns.setText("Số đường chạy: " + completedRecords.size());
        int numberOf1stMedals = 0;
        int numberOf2ndMedals = 0;
        int numberOf3rdMedals = 0;
        int totalDistance = 0;
        for (int i = 0; i < completedRecords.size(); i++){
            totalDistance += completedRecords.get(i).getTravelDistance();
            if(completedRecords.get(i).getRankInRace() == 1){
                numberOf1stMedals++;
            }else if(completedRecords.get(i).getRankInRace() == 2){
                numberOf2ndMedals++;
            }else if(completedRecords.get(i).getRankInRace() == 3){
                numberOf3rdMedals++;
            }
        }
        numberOf1st.setText(String.valueOf(numberOf1stMedals));
        numberOf2nd.setText(String.valueOf(numberOf2ndMedals));
        numberOf3rd.setText(String.valueOf(numberOf3rdMedals));
        txtDistance.setText("Khoảng cách: " + totalDistance + " (km)");
    }

    private void setupView(View view) {
        txtDistance = (TextView) view.findViewById(R.id.txt_distance);
        txtNumberOfRuns = (TextView) view.findViewById(R.id.txt_number_of_runs);
        numberOf1st = (TextView) view.findViewById(R.id.number_of_1st);
        numberOf2nd = (TextView) view.findViewById(R.id.number_of_2nd);
        numberOf3rd = (TextView) view.findViewById(R.id.number_of_3rd);
        seeAllAchievement = (TextView) view.findViewById(R.id.see_all_achievement);

        players = new ArrayList<>();
        final Gson gson = new Gson();
        account = gson.fromJson(accountPrefs.getUserAccount(), UserAccount.class);
        PlayerServices.getPlayerRecordWithId(account.getUserId(), getActivity(), new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                PlayerListDAO dao = gson.fromJson(response.toString(), PlayerListDAO.class);
                players = dao.getPlayers();
                if(!players.isEmpty()){
                    List<Player> completedRecords = new ArrayList<>();
                    for (Player player : players){
                        if(!(player.getRankInRace() == 0)){
                            completedRecords.add(player);
                        }
                    }
                    setupUserInfo(completedRecords);
                }
            }
        });
    }

    private void setupAction() {
        seeAllAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayerAchievementScreen.class);
                PlayerListDAO dao = new PlayerListDAO(players);
                Gson gson = new Gson();
                intent.putExtra("playerRecords", gson.toJson(dao));
                intent.putExtra("UserId", account.getUserId());
                getActivity().startActivity(intent);
            }
        });
    }
}
