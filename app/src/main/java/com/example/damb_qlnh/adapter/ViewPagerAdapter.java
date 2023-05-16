package com.example.damb_qlnh.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.damb_qlnh.Fragment.Fragment_QLban_ConTrong;
import com.example.damb_qlnh.Fragment.Fragment_QLban_DangDung;
import com.example.damb_qlnh.Fragment.Fragment_QLban_TatCa;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new Fragment_QLban_TatCa();
            case 1: return new Fragment_QLban_DangDung();
            case 2: return new Fragment_QLban_ConTrong();
            default: return new Fragment_QLban_TatCa();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
