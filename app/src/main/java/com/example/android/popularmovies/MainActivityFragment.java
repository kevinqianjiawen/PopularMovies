package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.Arrays;

/**
 * Created by kevin on 5/15/2016.
 */
public class MainActivityFragment extends Fragment {

    private AndroidFlavorAdapter flavorAdapter;

    AndroidFlavor[] androidFlavors = {
            new AndroidFlavor("Cupcake", "1.5", R.drawable.sample_0),
            new AndroidFlavor("Donut", "1.6", R.drawable.sample_1),
            new AndroidFlavor("Eclair", "2.0-2.1", R.drawable.sample_2),
            new AndroidFlavor("Froyo", "2.2-2.2.3", R.drawable.sample_3),
            new AndroidFlavor("GingerBread", "2.3-2.3.7", R.drawable.sample_4),
            new AndroidFlavor("Honeycomb", "3.0-3.2.6", R.drawable.sample_5),
            new AndroidFlavor("Ice Cream Sandwich", "4.0-4.0.4", R.drawable.sample_6),
            new AndroidFlavor("Jelly Bean", "4.1-4.3.1", R.drawable.sample_7)
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        flavorAdapter = new AndroidFlavorAdapter(getActivity(), Arrays.asList(androidFlavors));

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);
        gridView.setAdapter(flavorAdapter);

        return rootView;
    }
}
