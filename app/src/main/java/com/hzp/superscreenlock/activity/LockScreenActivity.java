package com.hzp.superscreenlock.activity;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.hzp.superscreenlock.AppConstant;
import com.hzp.superscreenlock.R;


public class LockScreenActivity extends AppCompatActivity {
    public static final String TAG = "LockScreenActivity";
    private Button buttonTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        initViews();
        setupViews();
    }

    private void initViews() {
        buttonTest = (Button) findViewById(R.id.button_test);
    }

    private void setupViews() {
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int key = event.getKeyCode();
        switch (key) {
            case KeyEvent.KEYCODE_BACK: {
                if (AppConstant.env.isLogEnable()) {
                    Log.i(TAG,"key KEYCODE_BACK disabled");
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
