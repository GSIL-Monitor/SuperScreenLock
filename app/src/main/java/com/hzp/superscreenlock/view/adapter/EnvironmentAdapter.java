package com.hzp.superscreenlock.view.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.EnvironmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/23.
 */
public class EnvironmentAdapter extends RecyclerView.Adapter<EnvironmentAdapter.EnvironmentHolder> {

    List<EnvironmentInfo> list;
    List<EnvironmentInfo> deleteList;

    public enum Type {
        EDIT,
        NORMAL;
    }

    private Type type = Type.NORMAL;

    private int colorNormal, colorPassword, colorPatten;

    public EnvironmentAdapter(Context context) {
        this(context, null);
    }

    public EnvironmentAdapter(Context context, List<EnvironmentInfo> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list = new ArrayList<>();
        }
        deleteList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorNormal = context.getResources().getColor(R.color.color_env_normal, null);
            colorPassword = context.getResources().getColor(R.color.color_env_password, null);
            colorPatten = context.getResources().getColor(R.color.color_env_pattern, null);

        } else {
            colorNormal = context.getResources().getColor(R.color.color_env_normal);
            colorPassword = context.getResources().getColor(R.color.color_env_password);
            colorPatten = context.getResources().getColor(R.color.color_env_pattern);
        }
    }

    public void addItem(EnvironmentInfo item) {
        if (item == null) {
            return;
        }
        int position = getItemCount();
        list.add(item);
        notifyItemInserted(position);
    }

    public void removeItems(List<EnvironmentInfo> l) {
        this.list.removeAll(l);
        notifyDataSetChanged();
    }

    @Override
    public EnvironmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_environment_info, parent, false);
        return new EnvironmentHolder(view);
    }

    @Override
    public void onBindViewHolder(final EnvironmentHolder holder, int position) {
        final EnvironmentInfo info = list.get(position);
        if (info == null) {
            return;
        }

        holder.title.setText(info.getTitle());

        switch (info.getLockType()) {
            case LOCK_TYPE_NONE:
                holder.setBackgroundColor(colorNormal);
                break;
            case LOCK_TYPE_PASSWORD:
                holder.setBackgroundColor(colorPassword);
                break;
            case LOCK_TYPE_PATTERN:
                holder.setBackgroundColor(colorPatten);
                break;
        }

        switch (type) {
            case NORMAL:
                holder.checkBox.setVisibility(View.GONE);
                break;
            case EDIT:
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(false);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.checkBox.isChecked()) {
                            holder.checkBox.setChecked(false);
                            deleteList.remove(info);
                        } else {
                            holder.checkBox.setChecked(true);
                            deleteList.add(info);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setType(Type type) {
        this.type = type;

        notifyDataSetChanged();
    }

    public void commitEdit() {
        if (deleteList.size() > 0) {
            removeItems(deleteList);
        }
        deleteList.clear();
    }

    public static class EnvironmentHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public CheckBox checkBox;
        public CardView cardView;

        public EnvironmentHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_env_title);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_env_checkbox);
            cardView = (CardView) itemView.findViewById(R.id.item_env_card);
        }

        public void setBackgroundColor(int color) {
            cardView.setCardBackgroundColor(color);
        }
    }
}
