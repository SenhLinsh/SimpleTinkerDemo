/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linsh.lshtinkerdemo.tinker.service;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.linsh.lshtinkerdemo.tinker.util.TinkerUtils;
import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;


/**
 * patch补丁合成进程将合成结果返回给主进程的类, 自己实现需要在AndroidManifest上添加你的Service
 * <p>
 * 默认我们在DefaultTinkerResultService会杀掉:patch进程，假设当前是补丁升级并且成功了，我们会杀掉当前进程，让补丁包更快的生效。
 * 若是修复类型的补丁包并且失败了，我们会卸载补丁包。
 * <p>
 * 在SampleResultService.java中，我们没有立刻杀掉当前进程去应用补丁，而选择在当前应用在退入后台或手机锁屏时这两个时机。
 * 你也可以在自杀前，通过发送service或者broadcast inent来尽快重启进程。
 */
public class TinkerResultService extends DefaultTinkerResultService {

    private static final String TAG = "LshLog.Tag.TinkerResultService";

    @Override
    public void onPatchResult(final PatchResult result) {
        if (result == null) {
            TinkerLog.e(TAG, "TinkerResultService received null result!!!!");
            return;
        }
        TinkerLog.i(TAG, "TinkerResultService receive result: %s", result.toString());

        // 首先, 我们得杀死补丁相关进程
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (result.isSuccess) {
                    Toast.makeText(getApplicationContext(), "补丁加载成功, 请重启应用", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "补丁加载失败, 请检查原因", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 成功后, 尽量删除补丁文件, 并尽快重新重启应用
        if (result.isSuccess) {
            deleteRawPatchFile(new File(result.rawPatchFilePath));

            // 为了更好的用户体验, 可以选择在应用处于后台时重启应用
            if (checkIfNeedKill(result)) {
                if (TinkerUtils.isBackground(this)) {
                    TinkerLog.i(TAG, "it is in background, just restart process");
                    restartProcess();
                } else {
                    // 如果不在后台, 我们可以注册屏幕关闭的广播, 在屏幕关闭时重启
                    TinkerLog.i(TAG, "tinker wait screen to restart process");
                    new TinkerUtils.ScreenState(getApplicationContext(), new TinkerUtils.ScreenState.IOnScreenOff() {
                        @Override
                        public void onScreenOff() {
                            restartProcess();
                        }
                    });
                }
            } else {
                TinkerLog.i(TAG, "I have already install the newly patch version!");
            }
        }
    }

    private void restartProcess() {
        TinkerLog.i(TAG, "app is background now, i can kill quietly");
        android.os.Process.killProcess(android.os.Process.myPid());
        // 如果有需要, 可以发送服务或广播来重启进程
    }

}
