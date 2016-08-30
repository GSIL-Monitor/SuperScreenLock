package com.hzp.superscreenlock.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.fragment.DetailEditLocationFragment;
import com.hzp.superscreenlock.fragment.DetailEditPasswordFragment;
import com.hzp.superscreenlock.fragment.DetailEditPatternFragment;
import com.hzp.superscreenlock.fragment.DetailEditWifiFragment;

public class DetailEditActivity extends AppCompatActivity {
    private static final String TAG = "DetailEditActivity";

    public static final int DETAIL_TYPE_ERROR = -1;
    public static final int DETAIL_TYPE_ENV_WIFI = 1;
    public static final int DETAIL_TYPE_ENV_LOCATION = 2;

    public static final int DETAIL_TYPE_LOCK_PASSWORD = 999;
    public static final int DETAIL_TYPE_LOCK_PATTERN = 998;

    private int detailType = DETAIL_TYPE_ERROR;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting);

        setupToolBar();
        setupDetail();
    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.etail_edit_toolbar);
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

    private void setupDetail() {
        detailType = getIntent().getIntExtra("detail_type", DETAIL_TYPE_ERROR);
        if (detailType == DETAIL_TYPE_ERROR) {
            Log.e(TAG, "wrong detail type!");
            finish();
        }

        Fragment fragment;

        switch (detailType) {
            case DETAIL_TYPE_ENV_WIFI:
                fragment = new DetailEditWifiFragment();
                break;
            case DETAIL_TYPE_ENV_LOCATION:
                fragment = new DetailEditLocationFragment();
                break;
            case DETAIL_TYPE_LOCK_PASSWORD:
                fragment = new DetailEditPasswordFragment();
                break;
            case DETAIL_TYPE_LOCK_PATTERN:
                fragment = new DetailEditPatternFragment();
                break;
            default:
                return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_edit_container, fragment)
                .commit();
    }

}
