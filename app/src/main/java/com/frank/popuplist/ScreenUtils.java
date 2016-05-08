package com.frank.popuplist;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtils {

    public static Resources getResources(Context context) {
        if (context == null) {
            return Resources.getSystem();
        } else {
            return context.getResources();
        }
    }

    public static int dp2px(float dpValue) {
        return dp2px(null, dpValue);
    }

    public static int dp2px(Context context, float dpValue) {
        Resources r = getResources(context);
        float scale = r.getDisplayMetrics().densityDpi;
        return (int) (dpValue * (scale / 160) + 0.5f);
    }

    public static float px2dp(float pxValue) {
        return px2dp(null, pxValue);
    }

    public static float px2dp(Context context, float pxValue) {
        Resources r = getResources(context);
        float scale = r.getDisplayMetrics().densityDpi;
        return (pxValue * 160) / scale;
    }

    public static int sp2px(float spValue) {
        return sp2px(null, spValue);
    }

    public static int sp2px(Context context, float spValue) {
        Resources r = getResources(context);
        float scale = r.getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static float px2sp(float pxValue) {
        return px2sp(null, pxValue);
    }

    public static float px2sp(Context context, float pxValue) {
        Resources r = getResources(context);
        float scale = r.getDisplayMetrics().scaledDensity;
        return pxValue / scale;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}