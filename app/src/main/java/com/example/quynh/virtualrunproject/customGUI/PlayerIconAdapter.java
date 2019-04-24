package com.example.quynh.virtualrunproject.customGUI;

import android.content.Context;
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
import com.example.quynh.virtualrunproject.custominterface.OnButtonClickRecyclerViewAdapter;
import com.example.quynh.virtualrunproject.custominterface.OnReceiveResponse;
import com.example.quynh.virtualrunproject.entity.Player;
import com.example.quynh.virtualrunproject.entity.UserProfile;
import com.example.quynh.virtualrunproject.mainfragmentscreens.UserProfileFragment;
import com.example.quynh.virtualrunproject.services.UserProfileServices;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by quynh on 4/19/2019.
 */

public class PlayerIconAdapter extends RecyclerView.Adapter<PlayerIconAdapter.ViewHolder>{

    private List<Player> players;
    private Context context;
    private OnButtonClickRecyclerViewAdapter listener;

    public PlayerIconAdapter(List<Player> players) {
        this.players = players;
    }

    public PlayerIconAdapter(List<Player> players, Context context) {
        this.players = players;
        this.context = context;
    }

    public void setOnButtonClickRecyclerViewAdapter(OnButtonClickRecyclerViewAdapter listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_icon_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.userProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnButtonClick(position);
            }
        });

        UserProfileServices.getUserProfileWithId(players.get(position).getUserAndRaceMaped().getUserId(), context, new OnReceiveResponse() {
            @Override
            public void onReceive(JSONObject response) {
                Gson gson = new Gson();
                UserProfile profile = gson.fromJson(response.toString(), UserProfile.class);
                if(!profile.getUserImage().equalsIgnoreCase("")){
                    try{
                        Glide.with(context).load(profile.getUserImage())
                                .apply(RequestOptions.skipMemoryCacheOf(true))
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                .into(holder.userProfileIcon);
                    }catch (Exception e){
                        Log.e("GildeError", "setupView: ", e);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userProfileIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.userProfileIcon = (ImageView) itemView.findViewById(R.id.user_profile_pic);
        }
    }

}
