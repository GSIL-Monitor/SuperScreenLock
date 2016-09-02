package com.hzp.superscreenlock.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.AppInfo;
import com.hzp.superscreenlock.locker.LockManager;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.view.AppSelectDialog;
import com.hzp.superscreenlock.view.LinearSpaceItemDecoration;
import com.hzp.superscreenlock.view.adapter.AppInfoAdapter;

public class ScreenIconSettingActivity extends AppCompatActivity implements AppInfoAdapter.AppInfoListener, AppSelectDialog.AppSelectDialogListener {

    private static final String TAG = ScreenIconSettingActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private AppInfoAdapter appInfoAdapter;

    private  int showType = AppInfo.SCREEN_SHOW_TYPE_SELECT_LIST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();
        if(!TextUtils.isEmpty(action)){
            View view = null;
            switch (action) {
                case "com.hzp.superscreenlock.activity.ScreenIconSettingActivity.bottom":
                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_lock_screen_main, null);
                    view.findViewById(R.id.hint_icon_image).setVisibility(View.GONE);
                    showType = AppInfo.SCREEN_SHOW_TYPE_BOTTOM;
                    break;
                case "com.hzp.superscreenlock.activity.ScreenIconSettingActivity.slide":
                    view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.drawer_layout_content, null);
                    showType = AppInfo.SCREEN_SHOW_TYPE_SLIDE;
                    break;
                default:
                    return;
            }
            view.setBackgroundResource(R.drawable.bg_screenlock_blue);

            setContentView(view);
            setupRecyclerView();
        }
    }

    private void setupRecyclerView(){
        switch (showType) {
            case AppInfo.SCREEN_SHOW_TYPE_BOTTOM:
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance()
                        .getListToDisplay(AppInfo.SCREEN_SHOW_TYPE_BOTTOM));
                break;
            case AppInfo.SCREEN_SHOW_TYPE_SLIDE:
                recyclerView = (RecyclerView) findViewById(R.id.drawer_recycler_view);
                recyclerView.setBackgroundColor(Color.BLACK);
                recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
                appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance()
                        .getListToDisplay(AppInfo.SCREEN_SHOW_TYPE_SLIDE));
                recyclerView.addItemDecoration(new LinearSpaceItemDecoration(25));

                break;
            default:
                return;
        }

        //添加一个AppEditStub
        appInfoAdapter.addItem(
                AppInfoManager.getInstance().getStubAppInfo()
        );

        appInfoAdapter.setType(AppInfoAdapter.TYPE_EDIT);
        appInfoAdapter.setListener(this);
        recyclerView.setAdapter(appInfoAdapter);
    }

    @Override
    public void onItemClick(AppInfo appInfo, int position) {
        LogUtil.i(TAG,"item click position = "+ position);

        //传入参数为用户选择的位置，打开对话框
        AppSelectDialog.newInstance(position,showType)
                .setSelectDialogListener(this)
                .show(getFragmentManager(),"AppSelectDialog");
    }

    @Override
    public void onItemLongClick(AppInfo appInfo, int position) {
        //长按删除
        AppInfoManager.getInstance().removeItemsAndSort(appInfo);
        appInfoAdapter.removeItem(appInfo);
    }


    @Override
    public void onDialogDismiss() {
        appInfoAdapter.clear();
        appInfoAdapter.addItems(
                AppInfoManager.getInstance().getListToDisplay(showType)
        );
        appInfoAdapter.addItem(
                AppInfoManager.getInstance().getStubAppInfo()
        );
        appInfoAdapter.notifyDataSetChanged();
    }

}
