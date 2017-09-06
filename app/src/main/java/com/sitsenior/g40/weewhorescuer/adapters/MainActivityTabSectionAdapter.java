package com.sitsenior.g40.weewhorescuer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class MainActivityTabSectionAdapter extends FragmentPagerAdapter{

    private List<Fragment> tabFragmentList;
    private List<String> tabTitleList;
    private int pageIndex;

    public MainActivityTabSectionAdapter(FragmentManager fm) {
        super(fm);
        tabFragmentList = new ArrayList<>();
        tabTitleList = new ArrayList<>();
    }

    public void addFragment(Fragment fm, String title){
        this.tabFragmentList.add(fm);
        this.tabTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return tabFragmentList.size();
    }


}
