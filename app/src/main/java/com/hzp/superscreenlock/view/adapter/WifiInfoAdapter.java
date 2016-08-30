package com.hzp.superscreenlock.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.WifiInfo;
import com.hzp.superscreenlock.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hoholiday on 2016/8/30.
 * @email hoholiday@hotmail.com
 */

public class WifiInfoAdapter extends RecyclerView.Adapter<WifiInfoAdapter.InfoHolder> {
    private List<WifiInfo> list;

    private AdapterCallback<WifiInfo> callback;

    public WifiInfoAdapter(List<WifiInfo> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public InfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wifi_info, parent, false);

        return new InfoHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoHolder holder, int position) {

        final WifiInfo wifiInfo = list.get(position);
        if (wifiInfo != null) {
            holder.SSID.setText(wifiInfo.getSSID());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback!=null){
                        callback.onItemClick(wifiInfo);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class InfoHolder extends RecyclerView.ViewHolder {

        public TextView SSID;

        InfoHolder(View itemView) {
            super(itemView);
            SSID = (TextView) itemView.findViewById(R.id.item_wifi_info_ssid);
        }
    }

    public WifiInfoAdapter setCallback(AdapterCallback callback) {
        this.callback = callback;
        return this;
    }
}
