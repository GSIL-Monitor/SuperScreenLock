package com.hzp.superscreenlock.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.AppInfo;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.view.adapter.AppInfoAdapter;

import java.util.List;

/**
 * @author hoholiday on 2016/8/31.
 * @email hoholiday@hotmail.com
 */

public class AppSelectDialog extends DialogFragment implements AppInfoManager.AppInfoListener, AppInfoAdapter.AppInfoListener {

    private static final String TAG = AppSelectDialog.class.getSimpleName();
    private RecyclerView recyclerView;
    private AppInfoAdapter appInfoAdapter;

    private static final String  KEY_SHOW_POSITION = "KEY_SHOW_POSITION";

    private int showPosition = -1;

    public AppSelectDialog(){}

    public static AppSelectDialog newInstance(int showPosition){

        AppSelectDialog dialog = new AppSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SHOW_POSITION,showPosition);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_app_select,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.app_select_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance()
                .getStubsDisplay(AppInfo.SCREEN_SHOW_TYPE_SELECT_LIST));
        appInfoAdapter.setType(AppInfoAdapter.TYPE_EDIT);
        appInfoAdapter.setListener(this);
        recyclerView.setAdapter(appInfoAdapter);


        AppInfoManager.getInstance().addListener(this);
        AppInfoManager.getInstance().requestUpdateAppList();

        if(showPosition!=1 && getArguments()!=null){
            showPosition = getArguments().getInt(KEY_SHOW_POSITION);
        }else{
            throw new IllegalArgumentException("args showPosition incorrect or not found!");
        }

        return view;
    }

    @Override
    public void onAppListUpdated(List<AppInfo> list) {
        appInfoAdapter.clear();
        appInfoAdapter.addItems(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppInfoManager.getInstance().removeListener(this);
    }

    @Override
    public void onItemClick(AppInfo appInfo, int position) {
        //持久化处理设置选项
        appInfo.setScreenShowType(AppInfo.SCREEN_SHOW_TYPE_BOTTOM)
                .setShowPosition(showPosition);
        //删除掉位置重复的
        // TODO: 2016/8/31 不太妥
        AppInfoManager.getInstance().removeItemByTypeAndPosition(AppInfo.SCREEN_SHOW_TYPE_BOTTOM,showPosition);
        AppInfoManager.getInstance().saveItem(appInfo);

        dismiss();
    }
}
