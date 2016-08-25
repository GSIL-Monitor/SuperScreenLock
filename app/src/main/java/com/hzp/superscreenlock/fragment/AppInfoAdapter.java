package com.hzp.superscreenlock.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/23.
 */
public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoHolder> {

    List<AppInfo> list;

    public AppInfoAdapter() {
        this(null);
    }

    public AppInfoAdapter(List<AppInfo> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list = new ArrayList<>();
        }
    }

    public void addItem(AppInfo item) {
        if (item == null) {
            return;
        }
        int position = getItemCount();
        list.add(item);
        notifyItemInserted(position);
    }

    @Override
    public AppInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app_info, parent, false);
        return new AppInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(AppInfoHolder holder, int position) {
        AppInfo appInfo = list.get(position);
        if (appInfo.getAppIcon() != null) {
            holder.icon.setImageDrawable(appInfo.getAppIcon());
        }
        if (appInfo.getAppLabel() != null) {
            holder.label.setText(appInfo.getAppLabel());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AppInfoHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView label;

        public AppInfoHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_app_info_icon);
            label = (TextView) itemView.findViewById(R.id.item_app_info_label);
        }
    }
}
