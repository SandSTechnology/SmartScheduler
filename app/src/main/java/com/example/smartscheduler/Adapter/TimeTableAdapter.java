package com.example.smartscheduler.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.smartscheduler.Fragments.FridayFragment;
import com.example.smartscheduler.Fragments.MondayFragment;
import com.example.smartscheduler.Fragments.ThursdayFragment;
import com.example.smartscheduler.Fragments.TuesdayFragment;
import com.example.smartscheduler.Fragments.WednesdayFragment;

public class TimeTableAdapter extends FragmentPagerAdapter {
    int NumOfTabs;

    public TimeTableAdapter(@NonNull FragmentManager fm, int behavior, int numOfTabs) {
        super(fm, behavior);
        NumOfTabs = numOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MondayFragment();
            case 1:
                return new TuesdayFragment();
            case 2:
                return new WednesdayFragment();
            case 3:
                return new ThursdayFragment();
            case 4:
                return new FridayFragment();
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
    }

    @Override
    public int getCount() {
        return NumOfTabs;
    }
}
