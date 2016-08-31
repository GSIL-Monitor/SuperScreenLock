package com.hzp.superscreenlock.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.AppInfo;
import com.hzp.superscreenlock.fragment.LockScreenFragment;
import com.hzp.superscreenlock.locker.LockManager;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.WindowUtil;
import com.hzp.superscreenlock.view.adapter.AppInfoAdapter;


public class LockScreenActivity extends AppCompatActivity implements LockManager.LockManagerControl, View.OnTouchListener {
    public static final String TAG = "LockScreenActivity";

    private LockScreenFragment mainFragment;
    private DrawerLayout drawerLayout;

    private RecyclerView drawerRecyclerView;
    private AppInfoAdapter appInfoAdapter;

    private int displayHeightPx = 0;
    private float touchThreshold;
    private float touchStartX=0f,touchStartY = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        initViews();

        LockManager.getInstance().registerLockActivity(this);
    }

    private void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.lock_screen_drawer_layout);
        drawerLayout.setOnTouchListener(this);
        displayHeightPx = WindowUtil.getDisplayDimensionWithPx(this)[0];
        touchThreshold = (float) (displayHeightPx * 0.3);

        setupSystemViews();
        setupFragments();
        setupDrawerRecyclerView();
    }

    private void setupDrawerRecyclerView(){
        drawerRecyclerView  = (RecyclerView) findViewById(R.id.drawer_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        drawerRecyclerView.setLayoutManager(linearLayoutManager);
        appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance()
                .getStubsDisplay(AppInfo.SCREEN_SHOW_TYPE_SLIDE));
        drawerRecyclerView.setAdapter(appInfoAdapter);

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

    private void setupFragments() {
        if (mainFragment == null) {
            mainFragment = LockScreenFragment.newInstance();
        }

        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.lock_screen_frameLayout, mainFragment);
            transaction.commit();
        }

        showDrawer();
    }

    @Override
    public int getFragmentContainerResId(){
        return R.id.lock_screen_frameLayout;
    }

    @Override
    public void showDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void hideDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
                LogUtil.i(TAG, "key KEYCODE_BACK disabled");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.i(TAG,"new intent arrived = "+intent.toString());
        setupFragments();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStartY = event.getY();
                touchStartX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(event.getY()-touchStartY>touchThreshold
                        && Math.abs(event.getX()-touchStartX)<touchThreshold){
                    //向下滑动
                    LockManager.getInstance().startUnlockView
                            (this, getSupportFragmentManager());
                    return true;
                }else if(event.getY()-touchStartY<-touchThreshold
                        && Math.abs(event.getX()-touchStartX)<touchThreshold ){
                   //向上滑动
                }
                break;
        }
        return false;
    }
}
