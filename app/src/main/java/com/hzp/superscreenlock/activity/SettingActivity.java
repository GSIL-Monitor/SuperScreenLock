package com.hzp.superscreenlock.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.view.adapter.EnvironmentAdapter;

public class SettingActivity extends AppCompatActivity {

    public static final int SETTING_MODE_NORMAL = 1;
    public static final int SETTING_MODE_EDIT = 2;

    private Toolbar toolbar;

    private int settingMode = SETTING_MODE_NORMAL;

    private RecyclerView envList;
    private EnvironmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingMode = SETTING_MODE_NORMAL;
                    invalidateOptionsMenu();
                }
            });
        }

        envList = (RecyclerView) findViewById(R.id.setting_environment_list);
        envList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EnvironmentAdapter(this);
        envList.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.addItem(new EnvironmentInfo().setTitle("test1").setLockType(EnvironmentInfo.LockType.LOCK_TYPE_NONE));
        adapter.addItem(new EnvironmentInfo().setTitle("test2").setLockType(EnvironmentInfo.LockType.LOCK_TYPE_PASSWORD));
        adapter.addItem(new EnvironmentInfo().setTitle("test3").setLockType(EnvironmentInfo.LockType.LOCK_TYPE_PATTERN));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (settingMode) {
            case SETTING_MODE_NORMAL:
                setActionBarNormal(menu);
                break;
            case SETTING_MODE_EDIT:
                setActionBarEdit(menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                // TODO: 2016/8/27
                Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_edit_mode:
                settingMode = SETTING_MODE_EDIT;
                invalidateOptionsMenu();
                break;
            case R.id.action_delete_item:
                adapter.commitEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBarNormal(Menu menu) {
        MenuItem delete = menu.findItem(R.id.action_delete_item);
        if (delete != null) {
            delete.setVisible(false);
        }
        MenuItem add = menu.findItem(R.id.action_add_item);
        if (add != null) {
            add.setVisible(true);
        }
        MenuItem editMode = menu.findItem(R.id.action_edit_mode);
        if (editMode != null) {
            editMode.setVisible(true);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(false);
            toolbar.setNavigationIcon(null);
        }

        adapter.setType(EnvironmentAdapter.Type.NORMAL);
    }

    private void setActionBarEdit(Menu menu) {
        MenuItem delete = menu.findItem(R.id.action_delete_item);
        if (delete != null) {
            delete.setVisible(true);
        }
        MenuItem add = menu.findItem(R.id.action_add_item);
        if (add != null) {
            add.setVisible(false);
        }
        MenuItem editMode = menu.findItem(R.id.action_edit_mode);
        if (editMode != null) {
            editMode.setVisible(false);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        }

        adapter.setType(EnvironmentAdapter.Type.EDIT);
    }
}
