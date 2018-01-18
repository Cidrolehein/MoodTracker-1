package com.openclassrooms.moodtracker.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.moodtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    private static final String KEY_POSITION ="position";
    private static final String KEY_COLOR = "color";
    private static final String KEY_FEELING = "feeling";

    public PageFragment() {
        // Required empty public constructor
    }

    public static PageFragment newInstance(){
        PageFragment pageFragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        args.putInt(KEY_FEELING, feeling);
        pageFragment.setArguments(args);
        return(pageFragment);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

}
