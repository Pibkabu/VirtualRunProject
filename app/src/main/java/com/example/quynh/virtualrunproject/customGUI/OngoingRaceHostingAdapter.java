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

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnSwipeButtonClickAdapter;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.functionscreen.race.RaceDetailScreen;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.example.quynh.virtualrunproject.helper.PictureResizeHandler;
import com.google.gson.Gson;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Date;
import java.util.List;

/**
 * Created by quynh on 3/31/2019.
 */

public class OngoingRaceHostingAdapter extends RecyclerView.Adapter<OngoingRaceHostingAdapter.ViewHolder> {
    private List<Race> races;
    private FragmentActivity context;
    private OnSwipeButtonClickAdapter onSwipeButtonClickAdapter;

    public OngoingRaceHostingAdapter(List<Race> races) {
        this.races = races;
    }

    public OngoingRaceHostingAdapter(List<Race> races, FragmentActivity context) {
        this.races = races;
        this.context = context;
    }

    public OngoingRaceHostingAdapter(List<Race> races, OnSwipeButtonClickAdapter onSwipeButtonClickAdapter) {
        this.races = races;
        this.onSwipeButtonClickAdapter = onSwipeButtonClickAdapter;
    }

    public void setOnSwipeButtonClickAdapter(OnSwipeButtonClickAdapter onSwipeButtonClickAdapter) {
        this.onSwipeButtonClickAdapter = onSwipeButtonClickAdapter;
    }

    @NonNull
    @Override
    public OngoingRaceHostingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.races_list_for_hosting_ongoing, parent, false);

        return new OngoingRaceHostingAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingRaceHostingAdapter.ViewHolder holder, final int position) {
        holder.raceTitle.setText(races.get(position).getName());
        holder.numberOfPlayers.setText(races.get(position).getTotalPlayer() + " Người tham gia cuộc đua");
        String password  = races.get(position).getRacePassword().trim();
        if(!password.equalsIgnoreCase("")){
            holder.raceLock.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(races.get(position).getRaceImage()).into(holder.raceImage);

        Date startDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getStartTime().toString());
        Date endDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getEndTime().toString());
        final String startTime = DateFormatHandler.dateToString("dd MM", startDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", startDate) + ") Giờ Việt Nam";
        final String endTime = DateFormatHandler.dateToString("dd MM", endDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", endDate) + ") Giờ Việt Nam";
        holder.startAndEndTime.setText(startTime + " đến " + endTime);

        //Image setting will be dealt with later
        holder.raceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RaceDetailScreen.class);
                //transfer race's information
                Gson gson = new Gson();
                intent.putExtra("raceString", gson.toJson(races.get(position)));
                context.startActivity(intent);
            }
        });

        holder.editRaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeButtonClickAdapter.onEditClick(position);
            }
        });

        holder.cancelRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeButtonClickAdapter.onCancelClick(position);
            }
        });

        PushDownAnim.setPushDownAnimTo(holder.editRaceBtn);
        PushDownAnim.setPushDownAnimTo(holder.cancelRace);

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
        private ImageView editRaceBtn;
        private ImageView cancelRace;
        private ImageView raceLock;

        public ViewHolder(View itemView) {
            super(itemView);
            this.raceTitle = (TextView) itemView.findViewById(R.id.race_title);
            this.numberOfPlayers = (TextView) itemView.findViewById(R.id.number_of_players);
            this.startAndEndTime = (TextView) itemView.findViewById(R.id.start_and_end_time);
            this.raceImage = (ImageView) itemView.findViewById(R.id.race_image);
            this.editRaceBtn = (ImageView) itemView.findViewById(R.id.edit_race_btn);
            this.cancelRace = (ImageView) itemView.findViewById(R.id.cancel_race);
            this.raceLock = (ImageView) itemView.findViewById(R.id.race_lock);
        }
    }
}
