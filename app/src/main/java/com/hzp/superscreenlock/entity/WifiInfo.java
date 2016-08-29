package com.hzp.superscreenlock.entity;

/**
 * Created by hezhipeng on 2016/8/29.
 */
public class WifiInfo {
    private String SSID;
    private boolean checked =false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }
}
