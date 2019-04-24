package com.example.quynh.virtualrunproject.customGUI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnSwipeButtonClickAdapter;
import com.example.quynh.virtualrunproject.entity.Race;
import com.example.quynh.virtualrunproject.helper.DateFormatHandler;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Date;
import java.util.List;

/**
 * Created by quynh on 4/13/2019.
 */

public class AdminRaceEditAdapter extends RecyclerView.Adapter<AdminRaceEditAdapter.ViewHolder>{

    private List<Race> races;
    private OnButtonClickRecyclerViewAdapter listener;
    private OnSwipeButtonClickAdapter onSwipeButtonClickAdapter;
    private Context context;

    public AdminRaceEditAdapter(List<Race> races) {
        this.races = races;
    }

    public AdminRaceEditAdapter(List<Race> races, Context context) {
        this.races = races;
        this.context = context;
    }

    public void setOnButtonClickRecyclerViewAdapter(OnButtonClickRecyclerViewAdapter listener) {
        this.listener = listener;
    }

    public void setOnSwipeButtonClickAdapter(OnSwipeButtonClickAdapter onSwipeButtonClickAdapter) {
        this.onSwipeButtonClickAdapter = onSwipeButtonClickAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_edit_race_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(races.get(position).getRaceImage()).into(holder.raceImage);
        holder.raceName.setText(races.get(position).getName());
        holder.players.setText(races.get(position).getTotalPlayer() + " Người dùng đang tham gia");
        Date date = DateFormatHandler.stringToDate("yyyy-MM-dd HH:ss:mm", races.get(position).getCreateTime().toString());
        holder.createdTime.setText("Tạo ngày " + DateFormatHandler.dateToString("yyyy-MM-dd", date));
        PushDownAnim.setPushDownAnimTo(holder.editRaceBtn);
        PushDownAnim.setPushDownAnimTo(holder.cancelRace);
        PushDownAnim.setPushDownAnimTo(holder.layout);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnButtonClick(position);
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
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView raceName;
        private TextView players;
        private TextView createdTime;
        private ImageView editRaceBtn;
        private ImageView cancelRace;
        private ImageView raceImage;
        private LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.raceName = (TextView) itemView.findViewById(R.id.race_name);
            this.players = (TextView) itemView.findViewById(R.id.players);
            this.createdTime = (TextView) itemView.findViewById(R.id.created_time);
            this.editRaceBtn = (ImageView) itemView.findViewById(R.id.edit_race_btn);
            this.cancelRace = (ImageView) itemView.findViewById(R.id.cancel_race);
            this.raceImage = (ImageView) itemView.findViewById(R.id.race_image);
            this.layout = (LinearLayout) itemView.findViewById(R.id.race_layout);
        }
    }
}
