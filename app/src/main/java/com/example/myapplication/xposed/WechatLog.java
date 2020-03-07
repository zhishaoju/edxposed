package com.example.myapplication.xposed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * @Author zhisiyi
 * @Date 2020.03.03 17:00
 * @Comment
 */
public class WechatLog implements IXposedHookLoadPackage {

    //微信日志级别
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_FATAL = 5;
    public static final int LEVEL_NONE = 6;

    //微信日志过滤
    public static final String LOG_VERBOSE = "LOG_VERBOSE";
    public static final String LOG_DEBUG = "LOG_DEBUG";
    public static final String LOG_INFO = "LOG_INFO";
    public static final String LOG_WARNING = "LOG_WARNING";
    public static final String LOG_ERROR = "LOG_ERROR";
    public static final String LOG_FATAL = "LOG_FATAL";
    public static final String LOG_NONE = "LOG_NONE ";

    final String TAG = WechatLog.class.getSimpleName();


    private void weChatLog(final ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.tencent.mars.xlog.Xlog", classLoader, "logWrite2",
            int.class, String.class, String.class, String.class, int.class, int.class, long.class,
            long.class, String.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    int logType = (int) param.args[0];
                    String str2 = (String) param.args[1];
                    String str3 = (String) param.args[2];
                    String str4 = (String) param.args[3];
                    int num4 = (int) param.args[4];
                    int num5 = (int) param.args[5];
                    long long6 = (long) param.args[6];
                    long long7 = (long) param.args[7];
                    String str8 = (String) param.args[8];
                    //根据值来过过滤日志级别
                    String wechatLogType = getWechatLogType(logType);

                    Log.d(wechatLogType, "LogType===" + logType);
                    Log.d(wechatLogType, str2);
                    Log.d(wechatLogType, str3);
                    Log.d(wechatLogType, str4);
                    Log.d(wechatLogType, "" + num4);
                    Log.d(wechatLogType, "" + num5);
                    Log.d(wechatLogType, "" + long6);
                    Log.d(wechatLogType, "" + long7);
                    Log.d(wechatLogType, str8);
                    super.beforeHookedMethod(param);
                }
            });
    }

    //根据logWrite2方法里面传过来的参数来过滤字段，默认值
    private String getWechatLogType(int logType) {
        String TAG = null;
        if (logType == LEVEL_VERBOSE) {
            TAG = LOG_VERBOSE;
        } else if (logType == LEVEL_DEBUG) {
            TAG = LOG_DEBUG;
        } else if (logType == LEVEL_INFO) {
            TAG = LOG_INFO;
        } else if (logType == LEVEL_WARNING) {
            TAG = LOG_WARNING;
        } else if (logType == LEVEL_ERROR) {
            TAG = LOG_ERROR;
        } else if (logType == LEVEL_FATAL) {
            TAG = LOG_FATAL;
        } else if (logType == LEVEL_NONE) {
            TAG = LOG_NONE;
        }
        return TAG;
    }


    private void weChatLogOpen(final ClassLoader classLoader) {
        //isLogcatOpen
        XposedHelpers
            .findAndHookMethod("com.tencent.mm.xlog.app.XLogSetup", classLoader, "keep_setupXLog",
                boolean.class, String.class, String.class, Integer.class, Boolean.class,
                Boolean.class, String.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                        super.afterHookedMethod(param);
                        Log.i(TAG, "keep_setupXLog参数isLogcatOpen: " + param.args[5]);
                    }
                });
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        //判断包名
        if (lpparam.packageName.equals("com.tencent.mm")) {
            // openWechatLog(lpparam)
            hookActivityIntent(lpparam);
        }
    }

    private void hookActivityIntent(LoadPackageParam lpparam) throws Throwable {
        Class clazz = lpparam.classLoader.loadClass("android.app.Activity");
        XposedHelpers.findAndHookMethod(clazz, "startActivity",
            Intent.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
                    Intent intent = (Intent) param.args[0];
                    Bundle b = intent.getExtras();
                    String dataString = intent.getDataString();
                    Log.i(TAG, "beforeHookedMethod b : " + b.toString());
                    Log.i(TAG, "beforeHookedMethod dataString : " + dataString);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.i(TAG, "afterHookedMethod");
                }
            });
    }

    private void openWechatLog(LoadPackageParam lpparam) throws Throwable {
        Class clazz = lpparam.classLoader.loadClass("com.tencent.mm.xlog.app.XLogSetup");
        //hook saySomething()方法
        XposedHelpers.findAndHookMethod(clazz, "keep_setupXLog",
            boolean.class, String.class, String.class, Integer.class, Boolean.class,
            Boolean.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
                    param.args[5] = true;
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.args[5] = true;
                    super.afterHookedMethod(param);
                    Log.i(TAG, "keep_setupXLog参数isLogcatOpen: " + param.args[5]);
                }
            });
    }
}
