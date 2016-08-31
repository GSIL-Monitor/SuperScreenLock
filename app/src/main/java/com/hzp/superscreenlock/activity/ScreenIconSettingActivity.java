package com.hzp.superscreenlock.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.AppInfo;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.view.AppSelectDialog;
import com.hzp.superscreenlock.view.adapter.AppInfoAdapter;

public class ScreenIconSettingActivity extends AppCompatActivity implements AppInfoAdapter.AppInfoListener {

    private static final String TAG = ScreenIconSettingActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private AppInfoAdapter appInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_lock_screen_main, null);
        view.setBackgroundResource(R.drawable.bg_screenlock_blue);
        setContentView(view);

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance()
                .getStubsDisplay(AppInfo.SCREEN_SHOW_TYPE_BOTTOM));
        appInfoAdapter.setListener(this);
        recyclerView.setAdapter(appInfoAdapter);
    }

    @Override
    public void onItemClick(AppInfo appInfo, int position) {
        LogUtil.i(TAG,"item click position = "+ position);
//        Toast.makeText(getApplicationContext(),"正在加载列表..",Toast.LENGTH_SHORT).show();
        //传入用户选择的位置
        AppSelectDialog.newInstance(position).show(getFragmentManager(),"AppSelectDialog");
    }
}
