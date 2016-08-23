package com.hzp.superscreenlock.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hzp.superscreenlock.AppConstant;
import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.fragment.LockScreenMainFragment;
import com.hzp.superscreenlock.fragment.LockScreenSecondFragment;
import com.hzp.superscreenlock.service.AppInfoManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LockScreenActivity extends AppCompatActivity {
    public static final String TAG = "LockScreenActivity";


    private ViewPager viewPager;
    private LockScreenMainFragment mainFragment;
    private LockScreenSecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        initViews();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.lock_screen_view_pager);
        setupViewPager(viewPager);
        setupSystemViews();
    }

    private void setupSystemViews() {
        setupImmersiveMode();
        setupTranslucentStatusBar();
    }

    /**
     * 设置沉浸模式
     */
    private void setupImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    }

    /**
     * 设置透明栏
     */
    private void setupTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return;
        }
    }

    private void setupViewPager(ViewPager viewPager){
        if(mainFragment==null){
            mainFragment = LockScreenMainFragment.newInstance();
        }
        if(secondFragment==null){
            secondFragment = LockScreenSecondFragment.newInstance();
        }
        List<Fragment> list = new ArrayList<>();
        list.add(mainFragment);
        list.add(secondFragment);
        LockScreenPagerAdapter pagerAdapter = new LockScreenPagerAdapter(
                getSupportFragmentManager(),
                list
        );
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int key = event.getKeyCode();
        switch (key) {
            case KeyEvent.KEYCODE_BACK: {
                if (AppConstant.env.isLogEnable()) {
                    Log.i(TAG, "key KEYCODE_BACK disabled");
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
