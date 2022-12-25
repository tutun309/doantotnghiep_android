package com.nmt.minhtu.doan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nmt.minhtu.doan.fragment.FragmentFavorite;
import com.nmt.minhtu.doan.fragment.FragmentHistory;
import com.nmt.minhtu.doan.fragment.FragmentHome;
import com.nmt.minhtu.doan.fragment.FragmentProfile;

public class MainViewpagerAdapter extends FragmentStateAdapter {
    public MainViewpagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new FragmentHistory();
            case 2:
                return new FragmentFavorite();
            case 3:
                return new FragmentProfile();
            default:
                return new FragmentHome();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
