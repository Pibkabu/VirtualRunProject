package com.example.quynh.virtualrunproject.customGUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.entity.Race;

import java.util.List;

/**
 * Created by quynh on 4/13/2019.
 */

public class AdminRaceListAdapter extends RecyclerView.Adapter<AdminRaceListAdapter.ViewHolder>{

    private List<Race> races;
    private OnButtonClickRecyclerViewAdapter listener;

    public AdminRaceListAdapter(List<Race> races) {
        this.races = races;
    }

    public void setOnButtonClickRecyclerViewAdapter(OnButtonClickRecyclerViewAdapter listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_race_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.raceName.setText(races.get(position).getName());
        holder.players.setText(races.get(position).getTotalPlayer() + " Người dùng đang tham gia");
        holder.createdTime.setText(races.get(position).getCreateTime().toString());
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView raceName;
        private TextView players;
        private TextView createdTime;
        private LinearLayout swipeButtons;
        private ImageView editRaceBtn;
        private ImageView cancelRace;

        public ViewHolder(View itemView) {
            super(itemView);
            this.raceName = (TextView) itemView.findViewById(R.id.race_name);
            this.players = (TextView) itemView.findViewById(R.id.players);
            this.createdTime = (TextView) itemView.findViewById(R.id.created_time);
            this.swipeButtons = (LinearLayout) itemView.findViewById(R.id.swipe_buttons);
            this.editRaceBtn = (ImageView) itemView.findViewById(R.id.edit_race_btn);
            this.cancelRace = (ImageView) itemView.findViewById(R.id.cancel_race);
        }
    }
}
