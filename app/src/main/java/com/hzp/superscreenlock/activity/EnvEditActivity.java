package com.hzp.superscreenlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.fragment.EnvEditFragment;

/**
 * Created by hezhipeng on 2016/8/29.
 */
public class EnvEditActivity extends AppCompatActivity
        implements EnvEditFragment.SettingChangedCallBack {

    private Toolbar toolbar;
    private EnvEditFragment envEditFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_edit);

        initViews();
    }

    private void initViews() {
        setupToolBar();
        setupSettingFragment();
    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.env_edit_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        }
    }

    private void setupSettingFragment() {
        if (envEditFragment == null) {
            envEditFragment = EnvEditFragment.newInstance();
            envEditFragment.setCallback(this);
        }
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.env_edit_frame_layout, envEditFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.env_eidt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done_item:
                // TODO: 2016/8/29
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        envEditFragment.clear();
    }

    @Override
    public void onEnvironmentChanged(String env) {
        Intent intent= new Intent(this,DetailEditActivity.class);
        switch (env){
            case EnvironmentInfo.TYPE_WIFI://打开WIFI列表进行选取
                intent.putExtra("detail_type",DetailEditActivity.DETAIL_TYPE_ENV_WIFI);
                break;
            case EnvironmentInfo.TYPE_LOCATION://打开地图定位选点设置
                intent.putExtra("detail_type",DetailEditActivity.DETAIL_TYPE_ENV_LOCATION);
                break;
            case EnvironmentInfo.TYPE_DEFAULT://默认环境暂时不需要其他设置
                return;
        }
        startActivity(intent);
    }

    @Override
    public void onLockTypeChanged(EnvironmentInfo.LockType lockType) {
        Intent intent= new Intent(this,DetailEditActivity.class);
        switch (lockType){
            case LOCK_TYPE_NONE:
                break;
            case LOCK_TYPE_PASSWORD:
                intent.putExtra("detail_type",DetailEditActivity.DETAIL_TYPE_LOCK_PASSWORD);
                break;
            case LOCK_TYPE_PATTERN:
                intent.putExtra("detail_type",DetailEditActivity.DETAIL_TYPE_LOCK_PATTERN);
                break;
        }
        startActivity(intent);
    }
}
