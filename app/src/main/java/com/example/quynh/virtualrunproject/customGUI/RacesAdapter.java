package com.example.quynh.virtualrunproject.customGUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.functionscreen.race.RaceDetailScreen;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.helper.PictureResizeHandler;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

/**
 * Created by quynh on 2/7/2019.
 */

public class RacesAdapter extends RecyclerView.Adapter<RacesAdapter.ViewHolder> {

    private List<Race> races;
    private FragmentActivity context;

    public RacesAdapter(List<Race> races) {
        this.races = races;
    }

    public RacesAdapter(List<Race> races, FragmentActivity context) {
        this.races = races;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.races_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.raceTitle.setText(races.get(position).getName());
        holder.numberOfPlayers.setText(races.get(position).getTotalPlayer() + " Runners have joined the race");

        Date startDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getStartTime().toString());
        Date endDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getEndTime().toString());
        final String startTime = DateFormatHandler.dateToString("dd MMM", startDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", startDate) + ") Vietnam time";
        final String endTime = DateFormatHandler.dateToString("dd MMM", endDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", endDate) + ") Vietnam time";
        holder.startAndEndTime.setText(startTime + " to " + endTime);

        //Image setting will be dealt with later
        holder.raceImage.setImageDrawable(PictureResizeHandler.resizeImage(R.drawable.dummy_picture, context));
        holder.raceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RaceDetailScreen.class);
                //transfer race's infomation
                intent.putExtra("race", races.get(position));
                intent.putExtra("startTime", races.get(position).getStartTime().toString());
                intent.putExtra("endTime", races.get(position).getEndTime().toString());
                Gson gson = new Gson();
                intent.putExtra("raceString", gson.toJson(races.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView raceTitle;
        private TextView numberOfPlayers;
        private TextView startAndEndTime;
        private ImageView raceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.raceTitle = (TextView) itemView.findViewById(R.id.race_title);
            this.numberOfPlayers = (TextView) itemView.findViewById(R.id.number_of_players);
            this.startAndEndTime = (TextView) itemView.findViewById(R.id.start_and_end_time);
            this.raceImage = (ImageView) itemView.findViewById(R.id.race_image);
        }
    }
}
