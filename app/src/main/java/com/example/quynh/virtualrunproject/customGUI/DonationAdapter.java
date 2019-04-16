package com.example.quynh.virtualrunproject.customGUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.dao.DonationDAO;
import com.example.quynh.virtualrunproject.entity.Donation;

import java.util.List;

/**
 * Created by quynh on 4/11/2019.
 */

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder>{

    private List<DonationDAO.DonationInfo> infos;

    public DonationAdapter(List<DonationDAO.DonationInfo> infos) {
        this.infos = infos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.donatorName.setText(infos.get(position).getProfile().getDisplayName());
        holder.donateDescription.setText(infos.get(position).getDonation().getDescription());
        holder.money.setText("" + infos.get(position).getDonation().getMoney() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView donatorName;
        private TextView donateDescription;
        private TextView money;

        public ViewHolder(View itemView) {
            super(itemView);
            this.donatorName = (TextView) itemView.findViewById(R.id.donator_name);
            this.donateDescription = (TextView) itemView.findViewById(R.id.donate_description);
            this.money = (TextView) itemView.findViewById(R.id.money);
        }
    }
}
