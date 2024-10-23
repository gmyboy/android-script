package com.gmyboy.codelab.accesibility.util;

import android.content.res.Resources;

/**
 * ----------------------------
 * Created by zx on 2016/12/26.
 * ----------------------------
 */
public class DensityUtil {
    private static final float dp_1_px;

    static {
        dp_1_px = Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 将 dp 换算成 px(像素)
     *
     * @param dp
     * @return
     */
    public static int dp2px(float dp) {
        if (dp_1_px <= 0) {
            try {
                throw new Exception("DensityUtil is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (int) (dp_1_px * dp);
    }
}
