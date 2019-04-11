package com.example.quynh.virtualrunproject.dao;

import com.example.quynh.virtualrunproject.entity.Donation;
import com.example.quynh.virtualrunproject.entity.UserProfile;

import java.util.List;

/**
 * Created by quynh on 4/11/2019.
 */

public class DonationDAO {
    private List<DonationInfo> infos;

    public DonationDAO() {
    }

    public List<DonationInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<DonationInfo> infos) {
        this.infos = infos;
    }

    public static class DonationInfo{
        private UserProfile profile;
        private Donation donation;

        public DonationInfo(UserProfile profile, Donation donation) {
            this.profile = profile;
            this.donation = donation;
        }

        public UserProfile getProfile() {
            return profile;
        }
        public void setProfile(UserProfile profile) {
            this.profile = profile;
        }
        public Donation getDonation() {
            return donation;
        }
        public void setDonation(Donation donation) {
            this.donation = donation;
        }
    }
}
