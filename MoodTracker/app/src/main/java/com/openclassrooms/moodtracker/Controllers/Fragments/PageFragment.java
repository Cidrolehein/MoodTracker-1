package com.openclassrooms.moodtracker.Controllers.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ImageViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    public static PageFragment newInstance(int position, int color, int feeling){
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
        View result = inflater.inflate(R.layout.fragment_page, container, false);
        LinearLayout rootView = (LinearLayout) result.findViewById(R.id.fragment_page_rootview);
        ImageView imageView = (ImageView) result.findViewById(R.id.fragment_page_imageview);

        //Get position, color and feeling
        int position = getArguments().getInt(KEY_POSITION, -1);
        int color = getArguments().getInt(KEY_COLOR, -1);
        int feeling = getArguments().getInt(KEY_FEELING, -1);

        //Set Background color and smiley showed
        rootView.setBackgroundColor(color);
        imageView.setImageResource(feeling);

        return result;
    }

}
