package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.locker.LockManager;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.view.CircleTextView;
import com.hzp.superscreenlock.view.adapter.AppInfoAdapter;


public class LockScreenFragment extends Fragment {

    private RecyclerView mainRecyclerView;
    private AppInfoAdapter appInfoAdapter;
    private CircleTextView hintTextView;

    public LockScreenFragment() {
    }

    public static LockScreenFragment newInstance() {
        LockScreenFragment fragment = new LockScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_screen_main, container, false);

        hintTextView = (CircleTextView) view.findViewById(R.id.circle_text_hint);
        mainRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);

        setupMainRecyclerView();;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupHintText();
    }

    private void setupMainRecyclerView() {

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        mainRecyclerView.setLayoutManager(layoutManager);
        appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance().getListDisplayOnBottom());
        mainRecyclerView.setAdapter(appInfoAdapter);

    }

    private void setupHintText(){
        EnvironmentInfo currentEnv = LockManager.getInstance().getCurrentEnvironment();
        if(currentEnv!=null){
            hintTextView.setText(currentEnv.getHint());
        }
    }

}
