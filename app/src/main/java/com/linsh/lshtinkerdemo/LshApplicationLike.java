package com.linsh.lshtinkerdemo;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.linsh.lshtinkerdemo.tinker.Log.MyLogImp;
import com.linsh.lshtinkerdemo.tinker.reporter.TinkerReport;
import com.linsh.lshtinkerdemo.tinker.util.TinkerManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by Senh Linsh on 17/5/10.
 */
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.linsh.lshtinkerdemo.LshApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class LshApplicationLike extends DefaultApplicationLike {

    public LshApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // 初始化MultiDex必须先于初始化Tinker
        MultiDex.install(base);

        TinkerManager.setTinkerApplicationLike(this);
        TinkerManager.setUpgradeRetryEnable(true);
        TinkerInstaller.setLogIml(new MyLogImp());

        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());

        TinkerManager.setReporter(new LshTinkerReporter());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private class LshTinkerReporter implements TinkerReport.Reporter {
        @Override
        public void onReport(int key, String keyName, String detail) {
            Log.i("LshLog", "onReport: key=" + key + "  keyName=" + keyName + "  detail=" + detail);
        }

        @Override
        public void onReport(String message) {
            Log.i("LshLog", "onReport: message=" + message);
        }
    }
}
