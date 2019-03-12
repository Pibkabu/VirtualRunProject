package com.example.quynh.virtualrunproject.customGUI;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.functionscreen.race.RaceDetailScreen;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.services.OnReceiveResponse;
import com.example.quynh.virtualrunproject.services.RaceServices;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by quynh on 3/5/2019.
 */

public class PlayerAchievementAdapter extends RecyclerView.Adapter<PlayerAchievementAdapter.ViewHolder>{

    private List<Player> records;
    private Context context;

    public PlayerAchievementAdapter(List<Player> records, Context context) {
        this.records = records;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.achievements_item_layout, parent, false);

        return new PlayerAchievementAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Player player = records.get(position);
        holder.txtRanking.setText(String.valueOf(player.getRankInRace()));
        RaceServices.getRaceById(player.getUserAndRaceMaped().getRaceId(), context, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                final Gson gson = new Gson();
                final Race race = gson.fromJson(response.toString(), Race.class);
                holder.txtRaceName.setText(race.getName());
                Date date = DateFormatHandler.stringToDate("yyyy-MM-dd", race.getStartTime().toString());
                holder.txtTime.setText(DateFormatHandler.dateToString("dd/MM/yyyy", date));

                holder.txtRaceName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RaceDetailScreen.class);
                        intent.putExtra("raceString", gson.toJson(race));
                        context.startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtRanking;
        private TextView txtRaceName;
        private TextView txtTime;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtRanking = (TextView) itemView.findViewById(R.id.txt_ranking);
            this.txtRaceName = (TextView) itemView.findViewById(R.id.txt_race_name);
            this.txtTime = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }
}
