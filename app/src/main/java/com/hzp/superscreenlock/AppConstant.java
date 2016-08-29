package com.hzp.superscreenlock;

/**
 * Created by hezhipeng on 2016/8/22.
 */
public class AppConstant {
    public enum ENV {
        DEBUG(true),
        RELEASE(false);

        private boolean logEnable;

        ENV(boolean logEnable) {
            this.logEnable = logEnable;
        }

        public boolean isLogEnable() {
            return logEnable;
        }
    }

    public static final ENV env = ENV.DEBUG;

    public static final String DEFAULT_SHAREPREFERENCES="com.hzp.superscreenlock_sharepreferences";
}

