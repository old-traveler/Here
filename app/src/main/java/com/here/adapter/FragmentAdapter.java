package com.here.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyc on 2017/6/21 14:39
 */

public class FragmentAdapter extends FragmentPagerAdapter {


    List<Fragment> fragments=new ArrayList<>();


    public FragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragments=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
