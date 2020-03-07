package com.example.myapplication.xposed;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * @Author zhisiyi
 * @Date 2020.03.03 16:36
 * @Comment
 */
public class Test implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        //判断包名
        if (lpparam.packageName.equals("com.example.myapplication")) {

            Class clazz = lpparam.classLoader.loadClass("com.example.myapplication.MainActivity");
            //hook saySomething()方法
            XposedHelpers.findAndHookMethod(clazz, "saySomething", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    param.setResult("我是一个坏人！");
                }
            });
        }
    }
}
