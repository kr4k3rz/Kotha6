package com.codelite.kr4k3rz.kotha6.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codelite.kr4k3rz.kotha6.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FabFragment extends Fragment {

    public FabFragment() {
        // Required empty public constructor
    }

    public static FabFragment newInstance() {
        return new FabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fab, container, false);
    }

}
