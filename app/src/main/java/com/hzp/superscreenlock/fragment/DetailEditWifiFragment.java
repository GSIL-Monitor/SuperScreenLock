package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzp.superscreenlock.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailEditWifiFragment extends Fragment {


    public DetailEditWifiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_edit_wifi, container, false);
    }

}
