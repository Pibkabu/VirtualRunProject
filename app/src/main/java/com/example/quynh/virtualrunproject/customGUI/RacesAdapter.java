package com.example.quynh.virtualrunproject.customGUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
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
    private OnButtonClickRecyclerViewAdapter listener;

    public void setOnButtonClickRecyclerViewAdapter(OnButtonClickRecyclerViewAdapter listener) {
        this.listener = listener;
    }

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
        String password  = races.get(position).getRacePassword();
        if(!password.equalsIgnoreCase("")){
            holder.raceLock.setVisibility(View.VISIBLE);
        }else{
            holder.raceLock.setVisibility(View.GONE);
        }

        holder.numberOfPlayers.setText(races.get(position).getTotalPlayer() + " Người tham gia cuộc đua");

        Log.d("TestImageAdapter", "setupRaceInfo: " + races.get(position).getRaceImage());
        Glide.with(context).load(races.get(position).getRaceImage()).into(holder.raceImage);

        Date startDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getStartTime().toString());
        Date endDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getEndTime().toString());
        final String startTime = DateFormatHandler.dateToString("dd MM", startDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", startDate) + ") Giờ Việt Nam";
        final String endTime = DateFormatHandler.dateToString("dd MM", endDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", endDate) + ") Giờ Việt Nam";
        holder.startAndEndTime.setText(startTime + " đến " + endTime);

        holder.raceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnButtonClick(position);
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
        private ImageView raceLock;

        public ViewHolder(View itemView) {
            super(itemView);
            this.raceTitle = (TextView) itemView.findViewById(R.id.race_title);
            this.numberOfPlayers = (TextView) itemView.findViewById(R.id.number_of_players);
            this.startAndEndTime = (TextView) itemView.findViewById(R.id.start_and_end_time);
            this.raceImage = (ImageView) itemView.findViewById(R.id.race_image);
            this.raceLock = (ImageView) itemView.findViewById(R.id.race_lock);
        }
    }
}
