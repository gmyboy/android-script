package com.gmyboy.codelab.accesibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Size;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.gmyboy.codelab.accesibility.util.DisplayMetricsUtil;
import com.gmyboy.codelab.accesibility.widget.FloatWindowManager;

public class AutoClickAccessibilityService extends AccessibilityService {
    private float clickX = -1;
    private float clickY = -1;
    private boolean isAutoClicking = false;
    private long clickInterval = 80; // 默认点击间隔时间，单位为毫秒
    private Handler handler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
/*        PointF location = new PointF();
        Size screenSize = DisplayMetricsUtil.getScreenSize(getApplicationContext());
        location.x = 0;
        location.y = screenSize.getHeight() / 3f;
        FloatWindowManager windowManager = new FloatWindowManager(getApplicationContext(), WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, location, (x, y) -> {

        });
        windowManager.showFloatView(null);*/
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
        stopAutoClick();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Size screenSize = DisplayMetricsUtil.getScreenSize(getApplicationContext());
        if (clickX < 0 || clickX > screenSize.getWidth()) {
            clickX = screenSize.getWidth() / 2f;
        }
        if (clickY < 0 || clickY > screenSize.getHeight()) {
            clickY = screenSize.getHeight() / 4f;
        }
        startAutoClick();
    }

    public void startAutoClick() {
        isAutoClicking = true;
        handler.post(autoClickRunnable);
    }

    public void stopAutoClick() {
        isAutoClicking = false;
        handler.removeCallbacks(null);
    }

    public void setClickPosition(float x, float y) {
        this.clickX = x;
        this.clickY = y;
    }

    public void setClickInterval(long interval) {
        this.clickInterval = interval;
    }

    private final Runnable autoClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAutoClicking && clickX >= 0 && clickY >= 0) {
                performClick(clickX, clickY);
                handler.postDelayed(this, clickInterval);
            }
        }
    };

    private void performClick(float x, float y) {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x, y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 50));
        dispatchGesture(builder.build(), null, null);
    }
}