package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.WifiInfo;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.PreferencesUtil;
import com.hzp.superscreenlock.utils.SystemUtil;
import com.hzp.superscreenlock.view.adapter.AdapterCallback;
import com.hzp.superscreenlock.view.adapter.WifiInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailEditWifiFragment extends Fragment  implements AdapterCallback<WifiInfo>{

    private static final String TAG = "DetailEditWifiFragment";

    private RecyclerView recyclerView;
    private WifiInfoAdapter wifiInfoAdapter;

    public DetailEditWifiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail_edit_wifi, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.wifi_list);
        setupList(recyclerView);
        return view;
    }

    private void setupList(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<WifiInfo> wifiList  = new ArrayList<>();
        for(String ssid :SystemUtil.getConfiguredWifiSSID(getContext())){
            WifiInfo t = new WifiInfo();
            t.setSSID(ssid);
            wifiList.add(t);
        }
        wifiInfoAdapter = new WifiInfoAdapter(wifiList);
        wifiInfoAdapter.setCallback(this);
        recyclerView.setAdapter(wifiInfoAdapter);
    }

    @Override
    public void onItemClick(WifiInfo item) {
        PreferencesUtil.putString(
                getContext(),
                PreferencesUtil.KEY_ENV_WIFI_SSID,
                item.getSSID());
        LogUtil.i(TAG,"Put temp wifi item{ssid=" + item.getSSID()+"}");
        getActivity().finish();
    }
}
