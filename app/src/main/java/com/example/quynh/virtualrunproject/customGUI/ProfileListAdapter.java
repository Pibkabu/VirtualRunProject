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
import com.example.quynh.virtualrunproject.entity.UserProfile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by quynh on 4/23/2019.
 */

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder>{

    private List<UserProfile> profiles;
    private Context context;

    public ProfileListAdapter(List<UserProfile> profiles, Context context) {
        this.profiles = profiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_list_item, parent, false);

        return new ProfileListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserProfile profile = profiles.get(position);
        holder.displayName.setText(profile.getDisplayName());
        if(!profile.getUserImage().equalsIgnoreCase("")){
            try{
                Glide.with(context).load(profile.getUserImage())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(holder.userIcon);
            }catch (Exception e){
                Log.e("GildeError", "setupView: ", e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView displayName;
        private CircleImageView userIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.displayName = (TextView) itemView.findViewById(R.id.display_name);
            this.userIcon = (CircleImageView) itemView.findViewById(R.id.user_icon);
        }
    }

}
