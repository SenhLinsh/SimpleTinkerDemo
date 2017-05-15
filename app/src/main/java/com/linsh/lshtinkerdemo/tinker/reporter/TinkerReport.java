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

package com.linsh.lshtinkerdemo.tinker.reporter;

import com.linsh.lshtinkerdemo.tinker.util.TinkerUtils;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;


/**
 * a simple tinker data reporter
 * Created by zhangshaowen on 16/9/17.
 */
public class TinkerReport {
    private static final String TAG = "Tinker.TinkerReport";

    // KEY - PV
    public static final int KEY_REQUEST = 0;
    public static final int KEY_DOWNLOAD = 1;
    public static final int KEY_TRY_APPLY = 2;
    public static final int KEY_TRY_APPLY_SUCCESS = 3;
    public static final int KEY_APPLIED_START = 4;
    public static final int KEY_APPLIED = 5;
    public static final int KEY_LOADED = 6;
    public static final int KEY_CRASH_FAST_PROTECT = 7;
    public static final int KEY_CRASH_CAUSE_XPOSED_DALVIK = 8;
    public static final int KEY_CRASH_CAUSE_XPOSED_ART = 9;
    public static final int KEY_APPLY_WITH_RETRY = 10;

    //Key -- try apply detail
    public static final int KEY_TRY_APPLY_UPGRADE = 70;
    public static final int KEY_TRY_APPLY_DISABLE = 71;
    public static final int KEY_TRY_APPLY_RUNNING = 72;
    public static final int KEY_TRY_APPLY_INSERVICE = 73;
    public static final int KEY_TRY_APPLY_NOT_EXIST = 74;
    public static final int KEY_TRY_APPLY_GOOGLEPLAY = 75;
    public static final int KEY_TRY_APPLY_ROM_SPACE = 76;
    public static final int KEY_TRY_APPLY_ALREADY_APPLY = 77;
    public static final int KEY_TRY_APPLY_MEMORY_LIMIT = 78;
    public static final int KEY_TRY_APPLY_CRASH_LIMIT = 79;
    public static final int KEY_TRY_APPLY_CONDITION_NOT_SATISFIED = 80;
    public static final int KEY_TRY_APPLY_JIT = 81;

    //Key -- apply detail
    public static final int KEY_APPLIED_UPGRADE = 100;
    public static final int KEY_APPLIED_UPGRADE_FAIL = 101;

    public static final int KEY_APPLIED_EXCEPTION = 120;
    public static final int KEY_APPLIED_DEXOPT_OTHER = 121;
    public static final int KEY_APPLIED_DEXOPT_EXIST = 122;
    public static final int KEY_APPLIED_DEXOPT_FORMAT = 123;
    public static final int KEY_APPLIED_INFO_CORRUPTED = 124;
    //package check
    public static final int KEY_APPLIED_PACKAGE_CHECK_SIGNATURE = 150;
    public static final int KEY_APPLIED_PACKAGE_CHECK_DEX_META = 151;
    public static final int KEY_APPLIED_PACKAGE_CHECK_LIB_META = 152;
    public static final int KEY_APPLIED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND = 153;
    public static final int KEY_APPLIED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND = 154;
    public static final int KEY_APPLIED_PACKAGE_CHECK_META_NOT_FOUND = 155;
    public static final int KEY_APPLIED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL = 156;
    public static final int KEY_APPLIED_PACKAGE_CHECK_RES_META = 157;
    public static final int KEY_APPLIED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT = 158;

    //version check
    public static final int KEY_APPLIED_VERSION_CHECK = 180;
    //extract error
    public static final int KEY_APPLIED_PATCH_FILE_EXTRACT = 181;
    public static final int KEY_APPLIED_DEX_EXTRACT = 182;
    public static final int KEY_APPLIED_LIB_EXTRACT = 183;
    public static final int KEY_APPLIED_RESOURCE_EXTRACT = 184;

    // KEY -- load detail
    public static final int KEY_LOADED_UNKNOWN_EXCEPTION = 250;
    public static final int KEY_LOADED_UNCAUGHT_EXCEPTION = 251;
    public static final int KEY_LOADED_EXCEPTION_DEX = 252;
    public static final int KEY_LOADED_EXCEPTION_DEX_CHECK = 253;
    public static final int KEY_LOADED_EXCEPTION_RESOURCE = 254;
    public static final int KEY_LOADED_EXCEPTION_RESOURCE_CHECK = 255;


    public static final int KEY_LOADED_MISMATCH_DEX = 300;
    public static final int KEY_LOADED_MISMATCH_LIB = 301;
    public static final int KEY_LOADED_MISMATCH_RESOURCE = 302;
    public static final int KEY_LOADED_MISSING_DEX = 303;
    public static final int KEY_LOADED_MISSING_LIB = 304;
    public static final int KEY_LOADED_MISSING_PATCH_FILE = 305;
    public static final int KEY_LOADED_MISSING_PATCH_INFO = 306;
    public static final int KEY_LOADED_MISSING_DEX_OPT = 307;
    public static final int KEY_LOADED_MISSING_RES = 308;
    public static final int KEY_LOADED_INFO_CORRUPTED = 309;

    //load package check
    public static final int KEY_LOADED_PACKAGE_CHECK_SIGNATURE = 350;
    public static final int KEY_LOADED_PACKAGE_CHECK_DEX_META = 351;
    public static final int KEY_LOADED_PACKAGE_CHECK_LIB_META = 352;
    public static final int KEY_LOADED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND = 353;
    public static final int KEY_LOADED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND = 354;
    public static final int KEY_LOADED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL = 355;
    public static final int KEY_LOADED_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND = 356;
    public static final int KEY_LOADED_PACKAGE_CHECK_RES_META = 357;
    public static final int KEY_LOADED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT = 358;

    public static final int KEY_LOADED_INTERPRET_GET_INSTRUCTION_SET_ERROR = 450;
    public static final int KEY_LOADED_INTERPRET_INTERPRET_COMMAND_ERROR = 451;
    public static final int KEY_LOADED_INTERPRET_TYPE_INTERPRET_OK = 452;


    public interface Reporter {

        void onReport(int key, String keyName, String detail);

        void onReport(String message);
    }

    private static Reporter reporter = null;

    public static void setReporter(Reporter reporter) {
        TinkerReport.reporter = reporter;
    }

    public static void onTryApply(boolean success) {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_TRY_APPLY, "KEY_TRY_APPLY", "补丁检查");

        reporter.onReport(KEY_TRY_APPLY_UPGRADE, "KEY_TRY_APPLY_UPGRADE", "补丁检查--升级请求");

        if (success) {
            reporter.onReport(KEY_TRY_APPLY_SUCCESS, "KEY_TRY_APPLY_SUCCESS", "补丁检查--成功");
        }
    }

    public static void onTryApplyFail(int errorCode) {
        if (reporter == null) {
            return;
        }
        switch (errorCode) {
            case ShareConstants.ERROR_PATCH_NOTEXIST:
                reporter.onReport(KEY_TRY_APPLY_NOT_EXIST, "KEY_TRY_APPLY_NOT_EXIST", "补丁检查失败: 临时补丁包文件不存在");
                break;
            case ShareConstants.ERROR_PATCH_DISABLE:
                reporter.onReport(KEY_TRY_APPLY_DISABLE, "KEY_TRY_APPLY_DISABLE", "补丁检查失败: 当前tinkerFlag为不可用状态");
                break;
            case ShareConstants.ERROR_PATCH_INSERVICE:
                reporter.onReport(KEY_TRY_APPLY_INSERVICE, "KEY_TRY_APPLY_INSERVICE", "补丁检查失败: 不能在patch补丁合成进程发起补丁的合成请求");
                break;
            case ShareConstants.ERROR_PATCH_RUNNING:
                reporter.onReport(KEY_TRY_APPLY_RUNNING, "KEY_TRY_APPLY_RUNNING", "补丁检查失败: 正在运行");
                break;
            case ShareConstants.ERROR_PATCH_JIT:
                reporter.onReport(KEY_TRY_APPLY_JIT, "KEY_TRY_APPLY_JIT", "补丁检查失败: 补丁不支持 N 之前的 JIT 模式");
                break;
            case TinkerUtils.ERROR_PATCH_ROM_SPACE:
                reporter.onReport(KEY_TRY_APPLY_ROM_SPACE, "KEY_TRY_APPLY_ROM_SPACE", "补丁检查失败: rom空间不足");
                break;
            case TinkerUtils.ERROR_PATCH_GOOGLEPLAY_CHANNEL:
                reporter.onReport(KEY_TRY_APPLY_GOOGLEPLAY, "KEY_TRY_APPLY_GOOGLEPLAY", "补丁检查失败: 补丁不支持GooglePlay渠道");
                break;
            case TinkerUtils.ERROR_PATCH_ALREADY_APPLY:
                reporter.onReport(KEY_TRY_APPLY_ALREADY_APPLY, "KEY_TRY_APPLY_ALREADY_APPLY", "补丁检查失败: 已经安装过该补丁");
                break;
            case TinkerUtils.ERROR_PATCH_CRASH_LIMIT:
                reporter.onReport(KEY_TRY_APPLY_CRASH_LIMIT, "KEY_TRY_APPLY_CRASH_LIMIT", "补丁检查失败: 崩溃次数限制");
                break;
            case TinkerUtils.ERROR_PATCH_MEMORY_LIMIT:
                reporter.onReport(KEY_TRY_APPLY_MEMORY_LIMIT, "KEY_TRY_APPLY_MEMORY_LIMIT", "补丁检查失败: 内存空间不足");
                break;
            case TinkerUtils.ERROR_PATCH_CONDITION_NOT_SATISFIED:
                reporter.onReport(KEY_TRY_APPLY_CONDITION_NOT_SATISFIED, "KEY_TRY_APPLY_CONDITION_NOT_SATISFIED", "补丁检查失败: 条件不足");
                break;
            case TinkerUtils.ERROR_PATCH_RETRY_COUNT_LIMIT:
                reporter.onReport(-1, "KEY_TRY_APPLY_RETRY_COUNT_LIMIT", "补丁检查失败: 重试次数超过限制");
                break;

        }
    }

    public static void onLoadPackageCheckFail(int errorCode) {
        if (reporter == null) {
            return;
        }
        switch (errorCode) {
            case ShareConstants.ERROR_PACKAGE_CHECK_SIGNATURE_FAIL:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_SIGNATURE, "KEY_LOADED_PACKAGE_CHECK_SIGNATURE", "补丁包检查失败: 签名校验失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_DEX_META_CORRUPTED:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_DEX_META, "KEY_LOADED_PACKAGE_CHECK_DEX_META", "补丁包检查失败: \"assets/dex_meta.txt\"信息损坏");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_LIB_META_CORRUPTED:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_LIB_META, "KEY_LOADED_PACKAGE_CHECK_LIB_META", "补丁包检查失败: \"assets/so_meta.txt\"信息损坏");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND, "KEY_LOADED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND", "补丁包检查失败: 找不到补丁中\"assets/package_meta.txt\"中的TINKER_ID");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND, "KEY_LOADED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND", "补丁包检查失败: 找不到基准apk AndroidManifest中的TINKER_ID");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL, "KEY_LOADED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL", "补丁包检查失败: 基准版本与补丁定义的TINKER_ID不相等");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND, "KEY_LOADED_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND", "补丁包检查失败: 找不到\"assets/package_meta.txt\"文件");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_RESOURCE_META_CORRUPTED:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_RES_META, "KEY_LOADED_PACKAGE_CHECK_RES_META", "补丁包检查失败: \"assets/res_meta.txt\"信息损坏");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT:
                reporter.onReport(KEY_LOADED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT, "KEY_LOADED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT", "补丁包检查失败: tinkerFlag不支持补丁中的某些类型的更改，例如补丁中存在资源更新，但是使用者指定不支持资源类型更新");
                break;
        }
    }

    public static void onLoaded(long cost) {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_LOADED, "KEY_LOADED", "补丁加载成功: 所花时间: " + cost + "ms");

        if (cost < 0L) {
            reporter.onReport("hp_report report load cost failed, invalid cost");
            TinkerLog.e(TAG, "hp_report report load cost failed, invalid cost");
            return;
        }
    }

    public static void onLoadInfoCorrupted() {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_LOADED_INFO_CORRUPTED, "KEY_LOADED_INFO_CORRUPTED", "patch.info文件损坏");
    }

    public static void onLoadFileNotFound(int fileType) {
        if (reporter == null) {
            return;
        }
        switch (fileType) {
            case ShareConstants.TYPE_DEX_OPT:
                reporter.onReport(KEY_LOADED_MISSING_DEX_OPT, "KEY_LOADED_MISSING_DEX_OPT", "加载时文件丢失: DEX_OPT文件");
                break;
            case ShareConstants.TYPE_DEX:
                reporter.onReport(KEY_LOADED_MISSING_DEX, "KEY_LOADED_MISSING_DEX", "加载时文件丢失: dex文件");
                break;
            case ShareConstants.TYPE_LIBRARY:
                reporter.onReport(KEY_LOADED_MISSING_LIB, "KEY_LOADED_MISSING_LIB", "加载时文件丢失: lib文件");
                break;
            case ShareConstants.TYPE_PATCH_FILE:
                reporter.onReport(KEY_LOADED_MISSING_PATCH_FILE, "KEY_LOADED_MISSING_PATCH_FILE", "加载时文件丢失: patch文件");
                break;
            case ShareConstants.TYPE_PATCH_INFO:
                reporter.onReport(KEY_LOADED_MISSING_PATCH_INFO, "KEY_LOADED_MISSING_PATCH_INFO", "加载时文件丢失: patch.info文件");
                break;
            case ShareConstants.TYPE_RESOURCE:
                reporter.onReport(KEY_LOADED_MISSING_RES, "KEY_LOADED_MISSING_RES", "加载时文件丢失: res文件");
                break;
        }
    }

    public static void onLoadInterpretReport(int type, Throwable e) {
        if (reporter == null) {
            return;
        }
        switch (type) {
            case ShareConstants.TYPE_INTERPRET_GET_INSTRUCTION_SET_ERROR:
                reporter.onReport(KEY_LOADED_INTERPRET_GET_INSTRUCTION_SET_ERROR, "KEY_LOADED_INTERPRET_GET_INSTRUCTION_SET_ERROR", "系统OTA后，为了加快补丁的执行，我们会采用解释模式来执行补丁");
                reporter.onReport("Tinker Exception:interpret occur exception " + TinkerUtils.getExceptionCauseString(e));
                break;
            case ShareConstants.TYPE_INTERPRET_COMMAND_ERROR:
                reporter.onReport(KEY_LOADED_INTERPRET_INTERPRET_COMMAND_ERROR, "KEY_LOADED_INTERPRET_INTERPRET_COMMAND_ERROR", "系统OTA后，为了加快补丁的执行，我们会采用解释模式来执行补丁");
                reporter.onReport("Tinker Exception:interpret occur exception " + TinkerUtils.getExceptionCauseString(e));
                break;
            case ShareConstants.TYPE_INTERPRET_OK:
                reporter.onReport(KEY_LOADED_INTERPRET_TYPE_INTERPRET_OK, "KEY_LOADED_INTERPRET_TYPE_INTERPRET_OK", "系统OTA后，为了加快补丁的执行，我们会采用解释模式来执行补丁");
                break;
        }
    }

    public static void onLoadFileMisMatch(int fileType) {
        if (reporter == null) {
            return;
        }
        switch (fileType) {
            case ShareConstants.TYPE_DEX:
                reporter.onReport(KEY_LOADED_MISMATCH_DEX, "KEY_LOADED_MISMATCH_DEX", "dex的md5与meta中定义的不一致");
                break;
            case ShareConstants.TYPE_LIBRARY:
                reporter.onReport(KEY_LOADED_MISMATCH_LIB, "KEY_LOADED_MISMATCH_LIB", "lib的md5与meta中定义的不一致");
                break;
            case ShareConstants.TYPE_RESOURCE:
                reporter.onReport(KEY_LOADED_MISMATCH_RESOURCE, "KEY_LOADED_MISMATCH_RESOURCE", "res的md5与meta中定义的不一致");
                break;
        }
    }

    public static void onLoadException(Throwable throwable, int errorCode) {
        if (reporter == null) {
            return;
        }
        boolean isCheckFail = false;
        switch (errorCode) {
            case ShareConstants.ERROR_LOAD_EXCEPTION_DEX:
                if (throwable.getMessage().contains(ShareConstants.CHECK_DEX_INSTALL_FAIL)) {
                    reporter.onReport(KEY_LOADED_EXCEPTION_DEX_CHECK, "KEY_LOADED_EXCEPTION_DEX_CHECK", "加载出现异常: 在检查dex过程中捕获到的crash");
                    isCheckFail = true;
                    TinkerLog.e(TAG, "tinker dex check fail:" + throwable.getMessage());
                } else {
                    reporter.onReport(KEY_LOADED_EXCEPTION_DEX, "KEY_LOADED_EXCEPTION_DEX", "加载出现异常: 在加载dex过程中捕获到的crash");
                    TinkerLog.e(TAG, "tinker dex reflect fail:" + throwable.getMessage());
                }
                break;
            case ShareConstants.ERROR_LOAD_EXCEPTION_RESOURCE:
                if (throwable.getMessage().contains(ShareConstants.CHECK_RES_INSTALL_FAIL)) {
                    reporter.onReport(KEY_LOADED_EXCEPTION_RESOURCE_CHECK, "KEY_LOADED_EXCEPTION_RESOURCE_CHECK", "加载出现异常: 在检查res过程中捕获到的crash");
                    isCheckFail = true;
                    TinkerLog.e(TAG, "tinker res check fail:" + throwable.getMessage());
                } else {
                    reporter.onReport(KEY_LOADED_EXCEPTION_RESOURCE, "KEY_LOADED_EXCEPTION_RESOURCE", "加载出现异常: 在加载res过程中捕获到的crash");
                    TinkerLog.e(TAG, "tinker res reflect fail:" + throwable.getMessage());
                }
                break;
            case ShareConstants.ERROR_LOAD_EXCEPTION_UNCAUGHT:
                reporter.onReport(KEY_LOADED_UNCAUGHT_EXCEPTION, "KEY_LOADED_UNCAUGHT_EXCEPTION", "加载出现异常: 没有捕获到的非java crash,这个是补丁机制的安全模式");
                break;
            case ShareConstants.ERROR_LOAD_EXCEPTION_UNKNOWN:
                reporter.onReport(KEY_LOADED_UNKNOWN_EXCEPTION, "KEY_LOADED_UNKNOWN_EXCEPTION", "加载出现异常: 没有捕获到的java crash");
                break;
        }
        //reporter exception, for dex check fail, we don't need to report stacktrace
        if (!isCheckFail) {
            reporter.onReport("Tinker Exception:load tinker occur exception " + TinkerUtils.getExceptionCauseString(throwable));
        }
    }

    public static void onApplyPatchServiceStart() {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_APPLIED_START, "KEY_APPLIED_START", "启动Patch进程");
    }

    public static void onApplyDexOptFail(Throwable throwable) {
        if (reporter == null) {
            return;
        }
        if (throwable.getMessage().contains(ShareConstants.CHECK_DEX_OAT_EXIST_FAIL)) {
            reporter.onReport(KEY_APPLIED_DEXOPT_EXIST, "KEY_APPLIED_DEXOPT_EXIST", "对合成的dex文件提前进行dexopt时出现异常");
        } else if (throwable.getMessage().contains(ShareConstants.CHECK_DEX_OAT_FORMAT_FAIL)) {
            reporter.onReport(KEY_APPLIED_DEXOPT_FORMAT, "KEY_APPLIED_DEXOPT_FORMAT", "对合成的dex文件提前进行dexopt时出现异常");

        } else {
            reporter.onReport(KEY_APPLIED_DEXOPT_OTHER, "KEY_APPLIED_DEXOPT_OTHER", "对合成的dex文件提前进行dexopt时出现异常");
            reporter.onReport("Tinker Exception:apply tinker occur exception " + TinkerUtils.getExceptionCauseString(throwable));
        }
    }

    public static void onApplyInfoCorrupted() {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_APPLIED_INFO_CORRUPTED, "KEY_APPLIED_INFO_CORRUPTED", "更新patch.info文件时发生损坏");
    }

    public static void onApplyVersionCheckFail() {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_APPLIED_VERSION_CHECK, "KEY_APPLIED_VERSION_CHECK", "对patch.info的校验版本合法性校验");
    }

    public static void onApplyExtractFail(int fileType) {
        if (reporter == null) {
            return;
        }
        switch (fileType) {
            case ShareConstants.TYPE_DEX:
                reporter.onReport(KEY_APPLIED_DEX_EXTRACT, "KEY_APPLIED_DEX_EXTRACT", "从补丁包与原始安装包中合成某种类型的文件出现错误");
                break;
            case ShareConstants.TYPE_LIBRARY:
                reporter.onReport(KEY_APPLIED_LIB_EXTRACT, "KEY_APPLIED_LIB_EXTRACT", "从补丁包与原始安装包中合成某种类型的文件出现错误");
                break;
            case ShareConstants.TYPE_PATCH_FILE:
                reporter.onReport(KEY_APPLIED_PATCH_FILE_EXTRACT, "KEY_APPLIED_PATCH_FILE_EXTRACT", "从补丁包与原始安装包中合成某种类型的文件出现错误");
                break;
            case ShareConstants.TYPE_RESOURCE:
                reporter.onReport(KEY_APPLIED_RESOURCE_EXTRACT, "KEY_APPLIED_RESOURCE_EXTRACT", "从补丁包与原始安装包中合成某种类型的文件出现错误");
                break;
        }
    }

    public static void onApplied(long cost, boolean success) {
        if (reporter == null) {
            return;
        }

        if (success) {
            reporter.onReport(KEY_APPLIED_UPGRADE, "KEY_APPLIED_UPGRADE", "补丁升级成功: 用时 " + cost + "ms");
        } else {
            reporter.onReport(KEY_APPLIED_UPGRADE_FAIL, "KEY_APPLIED_UPGRADE_FAIL", "补丁升级失败: 用时 " + cost + "ms");
        }

        TinkerLog.i(TAG, "hp_report report apply cost = %d", cost);

        if (cost < 0L) {
            TinkerLog.e(TAG, "hp_report report apply cost failed, invalid cost");
            return;
        }
    }

    public static void onApplyPackageCheckFail(int errorCode) {
        if (reporter == null) {
            return;
        }
        TinkerLog.i(TAG, "hp_report package check failed, error = %d", errorCode);

        switch (errorCode) {
            case ShareConstants.ERROR_PACKAGE_CHECK_SIGNATURE_FAIL:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_SIGNATURE, "KEY_APPLIED_PACKAGE_CHECK_SIGNATURE", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_DEX_META_CORRUPTED:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_DEX_META, "KEY_APPLIED_PACKAGE_CHECK_DEX_META", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_LIB_META_CORRUPTED:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_LIB_META, "KEY_APPLIED_PACKAGE_CHECK_LIB_META", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND, "KEY_APPLIED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND, "KEY_APPLIED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL, "KEY_APPLIED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_META_NOT_FOUND, "KEY_APPLIED_PACKAGE_CHECK_META_NOT_FOUND", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_RESOURCE_META_CORRUPTED:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_RES_META, "KEY_APPLIED_PACKAGE_CHECK_RES_META", "补丁合成过程对输入补丁包的检查失败");
                break;
            case ShareConstants.ERROR_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT:
                reporter.onReport(KEY_APPLIED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT, "KEY_APPLIED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT", "补丁合成过程对输入补丁包的检查失败");
                break;
        }
    }

    public static void onApplyCrash(Throwable throwable) {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_APPLIED_EXCEPTION, "KEY_APPLIED_EXCEPTION", "在补丁合成过程捕捉到异常");
        reporter.onReport("Tinker Exception:apply tinker occur exception " + TinkerUtils.getExceptionCauseString(throwable));
    }

    public static void onFastCrashProtect() {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_CRASH_FAST_PROTECT, "KEY_CRASH_FAST_PROTECT", "开启快速崩溃保护");
    }

    public static void onReportRetryPatch() {
        if (reporter == null) {
            return;
        }
        reporter.onReport(KEY_APPLY_WITH_RETRY, "KEY_APPLY_WITH_RETRY", "尝试重新加载补丁");
    }

}
