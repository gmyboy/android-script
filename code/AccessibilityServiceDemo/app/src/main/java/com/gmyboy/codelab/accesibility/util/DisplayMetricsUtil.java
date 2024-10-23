package com.gmyboy.codelab.accesibility.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.WindowManager;

public class DisplayMetricsUtil {
    public static Size getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return new Size(dm.widthPixels, dm.heightPixels);
    }

    public static int getRotation(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getRotation();
    }
}
