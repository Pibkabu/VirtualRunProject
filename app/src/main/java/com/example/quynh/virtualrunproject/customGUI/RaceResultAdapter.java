package com.example.quynh.virtualrunproject.customGUI;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.services.UserProfileServices;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by quynh on 4/13/2019.
 */

public class RaceResultAdapter extends RecyclerView.Adapter<RaceResultAdapter.ViewHolder>{

    private List<Player> records;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public RaceResultAdapter(List<Player> records) {
        this.records = records;
    }

    public RaceResultAdapter(List<Player> records, Context context) {
        this.records = records;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.race_result_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(position == 0){
            holder.medal1st.setVisibility(View.VISIBLE);
        }else if(position == 1){
            holder.medal2nd.setVisibility(View.VISIBLE);
        }else if(position == 2){
            holder.medal3rd.setVisibility(View.VISIBLE);
        }else{
            holder.txtRank.setVisibility(View.VISIBLE);
            holder.txtRank.setText(String.valueOf(records.get(position).getRankInRace()));
        }
        holder.txtTravelDistance.setText(String.valueOf(df2.format(records.get(position).getTravelDistance())));
        holder.txtTravelTime.setText(String.valueOf(df2.format(records.get(position).getTravelTime())));

        final Gson gson = new Gson();
        UserProfileServices.getUserProfileWithId(records.get(position).getUserAndRaceMaped().getUserId(), context, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                UserProfile profile = gson.fromJson(response.toString(), UserProfile.class);
                holder.displayName.setText(profile.getDisplayName());
                if(!profile.getUserImage().equalsIgnoreCase("")){
                    try{
                        Glide.with(context).load(profile.getUserImage())
                                .apply(RequestOptions.skipMemoryCacheOf(true))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                .into(holder.userImage);
                    }catch (Exception e){
                        Log.e("GildeError", "setupView: ", e);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtRank;
        private TextView displayName;
        private TextView txtTravelDistance;
        private TextView txtTravelTime;
        private ImageView medal1st;
        private ImageView medal2nd;
        private ImageView medal3rd;
        private ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            txtRank = (TextView) itemView.findViewById(R.id.txt_rank);
            displayName = (TextView) itemView.findViewById(R.id.display_name);
            txtTravelDistance = (TextView) itemView.findViewById(R.id.txt_travel_distance);
            txtTravelTime = (TextView) itemView.findViewById(R.id.txt_travel_time);
            medal1st = (ImageView) itemView.findViewById(R.id.medal_1st);
            medal2nd = (ImageView) itemView.findViewById(R.id.medal_2nd);
            medal3rd = (ImageView) itemView.findViewById(R.id.medal_3rd);
            userImage = (ImageView) itemView.findViewById(R.id.user_image);
        }
    }
}
