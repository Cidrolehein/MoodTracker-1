package com.openclassrooms.moodtracker.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.openclassrooms.moodtracker.Controllers.Fragments.PageFragment;

/**
 * Created by berenger on 18/01/2018.
 */

public class PageAdapter extends FragmentPagerAdapter {

    private int [] colors;
    private int [] feelings;

    public PageAdapter(FragmentManager fm) {
        super(fm);
        this.colors = colors;
    }

    @Override
    public Fragment getItem(int position) {
        return(PageFragment.newInstance(position, this.colors[position], this.feelings[position]));
    }

    @Override
    public int getCount() {
        return 5;
    }
}
