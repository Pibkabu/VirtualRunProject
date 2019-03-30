package com.example.quynh.virtualrunproject.customGUI;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by quynh on 3/28/2019.
 */

public class AttendingRaceAdapter extends RecyclerView.Adapter<AttendingRaceAdapter.ViewHolder>  {
    private List<Race> races;
    private FragmentActivity context;
    private OnButtonClickRecyclerViewAdapter listener;

    public AttendingRaceAdapter(List<Race> races) {
        this.races = races;
    }

    public AttendingRaceAdapter(List<Race> races, FragmentActivity context) {
        this.races = races;
        this.context = context;
    }

    public AttendingRaceAdapter(List<Race> races, OnButtonClickRecyclerViewAdapter listener) {
        this.races = races;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attending_race_item, parent, false);

        return new AttendingRaceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendingRaceAdapter.ViewHolder holder, final int position) {
        holder.raceTitle.setText(races.get(position).getName());
        Date startDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getStartTime().toString());
        Date endDate = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getEndTime().toString());
        String startTime = DateFormatHandler.dateToString("dd MMM", startDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", startDate) + ") Vietnam time";
        String endTime = DateFormatHandler.dateToString("dd MMM", endDate) + " ("
                + DateFormatHandler.dateToString("HH:mm a", endDate) + ") Vietnam time";

        holder.startTime.setText(startTime + " TO");
        holder.endTime.setText(endTime);

        Calendar calendar = Calendar.getInstance();
        if(startDate.getTime() > calendar.getTimeInMillis()){
            holder.cannotSendResultYet.setVisibility(View.VISIBLE);
            holder.sendResultBtn.setVisibility(View.GONE);
        }

        holder.sendResultBtn.setOnClickListener(new View.OnClickListener() {
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
        private TextView startTime;
        private TextView endTime;
        private TextView cannotSendResultYet;
        private Button sendResultBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.raceTitle = (TextView) itemView.findViewById(R.id.race_name);
            this.startTime = (TextView) itemView.findViewById(R.id.start_time);
            this.endTime = (TextView) itemView.findViewById(R.id.end_time);
            this.cannotSendResultYet = (TextView) itemView.findViewById(R.id.cannot_send_result_yet);
            this.sendResultBtn = (Button) itemView.findViewById(R.id.send_result_btn);
        }
    }
}
