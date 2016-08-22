package com.hzp.superscreenlock.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hzp.superscreenlock.AppConstant;
import com.hzp.superscreenlock.entity.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class NewRelativeLayout extends RelativeLayout {
    public static final String TAG = "NewRelativeLayout";

    private float startX, startY;
    private Paint paint;

    Drawable icon1,icon2;


    public NewRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        queryAppInfo(context);
    }

    public NewRelativeLayout(Context context) {
        this(context, null);
    }

    public NewRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                processActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                processActionMove(event);
                break;
            default:
                postInvalidate();
        }
        return true;
    }

    private void processActionDown(MotionEvent event) {
        startX = event.getX();
        startY = event.getY();
        if (AppConstant.env.isLogEnable()) {
            Log.i(TAG, "ACTION_DOWN=> X=" + startX + " Y=" + startY);
        }
        alpha = 0;
        postInvalidate();
    }

    private void processActionMove(MotionEvent event){
        float detalX = startX - event.getX();
        Log.e(TAG,"detalX="+detalX);
        if(detalX>margin){
            Toast.makeText(getContext(),"L",Toast.LENGTH_SHORT).show();
            getContext().startActivity(b2.getIntent());
        }else if(detalX<-margin){
            Toast.makeText(getContext(),"R",Toast.LENGTH_SHORT).show();
            getContext().startActivity(b1.getIntent());
        }

        alpha += 10;
        postInvalidate();
    }

    List<AppInfo> mlistAppInfo = new ArrayList<>();
    public void queryAppInfo(Context context) {
        PackageManager pm = context.getPackageManager(); //获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
        if (mlistAppInfo != null) {
            mlistAppInfo.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName,
                        activityName));
                // 创建一个AppInfo对象，并赋值
                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                appInfo.setIntent(launchIntent);
                mlistAppInfo.add(appInfo);
            }
        }
        if(mlistAppInfo.isEmpty()){
            return;
        }
        for(AppInfo i :mlistAppInfo){
            if(i.getAppIcon()!=null){
                if(icon1==null){
                    icon1 = i.getAppIcon();
                    bt1=(BitmapDrawable)icon1;
                    b1=i;
                }
                else{
                    icon2 =i.getAppIcon();
                    bt2=(BitmapDrawable)icon2;
                    b2=i;
                }
            }
        }
    }

    float r = 100;
    float margin = 50;
    int alpha = 0;
    BitmapDrawable bt1,bt2;
    AppInfo b1,b2;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (alpha <= 255) {
            paint.setAlpha(alpha);
        }

        if(bt1==null){
            return;
        }
        Bitmap b1= bt1.getBitmap();
        Bitmap b2=bt2.getBitmap();
        canvas.drawBitmap(b1,startX+margin,startY-b1.getHeight()/2,paint);
        canvas.drawBitmap(b2,startX-margin-b2.getWidth(),startY-b2.getHeight()/2,paint);
    }
}
