package com.saarimproj.myapplication.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.saarimproj.myapplication.Fragments.DailyFragment;
import com.saarimproj.myapplication.Fragments.MonthlyFragment;
import com.saarimproj.myapplication.Fragments.NoteFragment;
import com.saarimproj.myapplication.Fragments.YearlyFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new NoteFragment();
        }
        else if (position == 1)
        {
            fragment = new DailyFragment();
        }
        else if (position == 2)
        {
            fragment = new MonthlyFragment();
        }
        else if (position == 3)
        {
            fragment = new YearlyFragment();
        }
        return fragment;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 1)
        {
            title = "DAILY";
        }
        else if (position == 2)
        {
            title = "MONTHLY";
        }
        else if (position == 3)
        {
            title = "YEARLY";
        }
        return title;
    }
    @Override
    public int getCount() {
        return 4;
    }
}