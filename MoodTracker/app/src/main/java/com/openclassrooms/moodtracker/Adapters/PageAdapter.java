package com.openclassrooms.moodtracker.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.openclassrooms.moodtracker.Controllers.Fragments.PageFragment;

/**
 * Created by berenger on 18/01/2018.
 */

public class PageAdapter extends FragmentPagerAdapter {

    private final int [] colors;
    private final int [] smileys;

    protected PageAdapter(FragmentManager fm, int[] colors, int [] smileys) {
        super(fm);
        this.colors = colors;
        this.smileys = smileys;
    }

    @Override
    public Fragment getItem(int position) {
        return(PageFragment.newInstance(position, this.colors[position], this.smileys[position]));
    }

    @Override
    public int getCount() {
        return 5;
    }
}
