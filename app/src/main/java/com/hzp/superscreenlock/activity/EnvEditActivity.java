package com.hzp.superscreenlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.fragment.EnvEditFragment;
import com.hzp.superscreenlock.manager.EnvironmentManager;

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
                handleEnvEditDone();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleEnvEditDone(){
        EnvironmentInfo info = envEditFragment.getEditData();
        //开始检查完整性
        if(TextUtils.isEmpty(info.getTitle())){
            Toast.makeText(getApplicationContext(),"场景名称未填写！",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(info.getHint())){
            Toast.makeText(getApplicationContext(),"场景提示未填写！",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(info.getType())){
            Toast.makeText(getApplicationContext(),"还未选择场景！",Toast.LENGTH_SHORT).show();
            return;
        }else if(info.getLockType()==null || TextUtils.isEmpty(info.getLockType().toString())){
            Toast.makeText(getApplicationContext(),"还未选择解锁模式！",Toast.LENGTH_SHORT).show();
            return;
        }

        //检查选择的场景
        if(!TextUtils.isEmpty(info.getLockType().toString())){
            switch (info.getType()){
                case EnvironmentInfo.TYPE_DEFAULT://默认
                    break;
                case EnvironmentInfo.TYPE_WIFI://检查是否选择了wifiSSID
                    if(TextUtils.isEmpty(info.getWifiSSID())){
                        Toast.makeText(getApplicationContext(),"还未选择Wifi！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case EnvironmentInfo.TYPE_LOCATION://检查是否设置了坐标
                    if(info.getLatitude()<=0.001f||info.getLongitude()<=0.001f){
                        Toast.makeText(getApplicationContext(),"还未选择位置！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
            }
        }

        //检查加锁模式
        if(info.getLockType()!=null && !TextUtils.isEmpty(info.getLockType().toString())){
            switch (info.getLockType()){
                case LOCK_TYPE_NONE:
                    break;
                case LOCK_TYPE_PASSWORD:
                    if(TextUtils.isEmpty(info.getPassword())){
                        Toast.makeText(getApplicationContext(),"还未设置密码！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case LOCK_TYPE_PATTERN:
                    if(TextUtils.isEmpty(info.getPatternPassword())){
                        Toast.makeText(getApplicationContext(),"还未设置手势",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
            }
        }

        // 持久化数据
        EnvironmentManager.getInstance().saveItem(info);
        //通知MainActivity更新Env列表
        setResult(MainActivity.RESULT_SUCCESS,null);
        finish();
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
            case LOCK_TYPE_PASSWORD://启动设置密码锁界面
                intent.putExtra("detail_type",DetailEditActivity.DETAIL_TYPE_LOCK_PASSWORD);
                break;
            case LOCK_TYPE_PATTERN://启动设置手势锁界面
                intent.putExtra("detail_type",DetailEditActivity.DETAIL_TYPE_LOCK_PATTERN);
                break;
            case LOCK_TYPE_NONE://不加锁不需要其他设置
                return;
        }
        startActivity(intent);
    }
}
