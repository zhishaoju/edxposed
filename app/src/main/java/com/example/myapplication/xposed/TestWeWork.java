package com.example.myapplication.xposed;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * @Author zhisiyi
 * @Date 2020.03.04 18:39
 * @Comment
 */
public class TestWeWork implements IXposedHookLoadPackage {

    final String TAG = TestWeWork.class.getSimpleName();

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        //判断包名
        if (lpparam.packageName.equals("com.tencent.wework")) {

            Class clazz = lpparam.classLoader
                .loadClass("com.tencent.wcdb.database.SQLiteConnection");
            //hook saySomething()方法
            XposedHelpers.findAndHookMethod(clazz, "notifyChange", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.d(TAG,
                        "param.args[0] = " + param.args[0] + ",param.args[1] = " + param.args[1]
                            + ",param.args[2] = " + param.args[2]);
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) {
//                    param.setResult("我是一个坏人！");
                }
            });
        }
    }
}
