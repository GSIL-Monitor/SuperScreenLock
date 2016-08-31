package com.hzp.superscreenlock.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.fragment.AppSettingFragment;

public class AppSettingActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        initViews();
        setupSettingFragment();
    }

    private void setupSettingFragment() {
        AppSettingFragment fragment = AppSettingFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.app_setting_container,fragment)
                .commit();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                finish();
                }
            });
        }
    }
}
