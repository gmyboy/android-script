package com.gmyboy.codelab.accesibility.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Build;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gmyboy.codelab.accesibility.R;
import com.gmyboy.codelab.accesibility.util.DensityUtil;
import com.gmyboy.codelab.accesibility.util.DisplayMetricsUtil;

public class FloatWindowManager {
    private float mLastRawX, mLastRawY;
    private final Context mApplication;
    private final WindowManager mWindowManager;
    private ViewGroup parentView;
    private View vParent;
    private final WindowManager.LayoutParams mWindowLayoutParams;
    private IMoveListener moveListener;
    private boolean isMove = false;
    private int screenWidth;
    private int screenHeight;
    private int oldY;
    private int oldHeight;
    private int windowWidth, windowHeight;
    private int marginTop = 0;
    private int marginBottom = 0;
    private int marginLeft = 0;
    private int marginRight = 0;

    public FloatWindowManager(Context context, int width, int height, PointF location, IMoveListener moveListener) {
        windowWidth = width;
        windowHeight = height;
        oldHeight = height;
        Size screenSize = DisplayMetricsUtil.getScreenSize(context);
        if (location == null) {
            location = new PointF(DensityUtil.dp2px(81), DensityUtil.dp2px(1));
        }
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
        mApplication = context;
        parentView = (ViewGroup) LayoutInflater.from(mApplication).inflate(R.layout.view_floating_tool, null);
        vParent = parentView.findViewById(R.id.v_parent);
        mWindowManager = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);


        mWindowLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        mWindowLayoutParams.gravity = Gravity.END | Gravity.TOP;
//        mWindowLayoutParams.format = PixelFormat.RGBA_8888;
        mWindowLayoutParams.x = (int) location.x;
        mWindowLayoutParams.y = (int) location.y;
        oldY = mWindowLayoutParams.y;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;

        FunctionImageButton fibSet = parentView.findViewById(R.id.btn_set);
/*        fibSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoordinateDialog dialog = new CoordinateDialog(mApplication, new CoordinateDialog.OnCoordinateSetListener() {
                    @Override
                    public void onCoordinateSet(int x, int y, int interval) {

                    }
                });
                dialog.show();
            }
        });*/

        vParent.setOnTouchListener(moveTouchListener);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.moveListener = moveListener;
        if (moveListener != null) {
            moveListener.onMove(screenWidth - mWindowLayoutParams.x - windowWidth, mWindowLayoutParams.y);
        }
    }

    public FloatWindowManager(Context context, int width, int height, IMoveListener moveListener) {
        this(context, width, height, null, moveListener);
    }

    public void setMargin(int marginLeft, int marginTop, int marginRight, int marginBottom) {
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
    }

    public void showFloatView(View view) {
        try {
            if (mWindowManager != null && parentView != null && parentView.getWindowId() == null) {
                if (view != null) {
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    parentView.addView(view, 0, layoutParams);
                }
                mWindowManager.addView(parentView, mWindowLayoutParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideFloatView() {
        if (mWindowManager != null && parentView != null && parentView.getWindowId() != null) {
            parentView.removeAllViews();
            mWindowManager.removeViewImmediate(parentView);
        }
    }

    public void scaleToMini() {
        if (parentView != null) {
            mWindowLayoutParams.y = -oldHeight * 2;
            mWindowManager.updateViewLayout(parentView, mWindowLayoutParams);
        }
    }

    public void scaleToNormal() {
        if (parentView != null) {
            mWindowLayoutParams.y = oldY;
            mWindowManager.updateViewLayout(parentView, mWindowLayoutParams);
        }
    }

    public interface IMoveListener {
        void onMove(float x, float y);
    }

    private final View.OnTouchListener moveTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getPointerCount() == 2) {
                return true;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    scaleFactor = 1f;
                    mLastRawX = event.getRawX();
                    mLastRawY = event.getRawY();
                    v.postDelayed(runnable, 100);
                    break;
                case MotionEvent.ACTION_MOVE:
                    updateViewPosition(event.getRawX(), event.getRawY());
                    mLastRawX = event.getRawX();
                    mLastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    v.removeCallbacks(runnable);
                    isMove = false;
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private final Runnable runnable = () -> isMove = true;

    private void updateViewPosition(float x, float y) {
        if (!isMove) {
            return;
        }
        int dx = (int) (x - mLastRawX);
        int dy = (int) (y - mLastRawY);
        mWindowLayoutParams.x -= dx;
        mWindowLayoutParams.y += dy;
        oldY = mWindowLayoutParams.y;
        if (mWindowLayoutParams.x < marginLeft) {
            mWindowLayoutParams.x = marginLeft;
        }
        if (mWindowLayoutParams.x > (screenWidth - parentView.getMeasuredWidth() - marginRight)) {
            mWindowLayoutParams.x = (screenWidth - parentView.getMeasuredWidth() - marginRight);
        }
        if (mWindowLayoutParams.y < marginTop) {
            mWindowLayoutParams.y = marginTop;
        }
        if (mWindowLayoutParams.y > (screenHeight - parentView.getMeasuredHeight() - marginTop)) {
            mWindowLayoutParams.y = (screenHeight - parentView.getMeasuredHeight() - marginTop);
        }
        mWindowManager.updateViewLayout(parentView, mWindowLayoutParams);
        if (moveListener != null) {
            moveListener.onMove(screenWidth - mWindowLayoutParams.x - windowWidth, mWindowLayoutParams.y);
        }
    }

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private float initialWidth;
        private float initialHeight;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initialWidth = parentView.getWidth();
            initialHeight = parentView.getHeight();
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f)); // 设置缩放大小限制
            float width = initialWidth * scaleFactor;
            float height = initialHeight * scaleFactor;
            updateViewSizeAndPosition(width, height, detector.getFocusX(), detector.getFocusY());
            return true;
        }

        private void updateViewSizeAndPosition(float width, float height, float pivotX, float pivotY) {
            ViewGroup.LayoutParams layoutParams = parentView.getLayoutParams();
            layoutParams.width = (int) width;
            layoutParams.height = (int) height;
            parentView.setLayoutParams(layoutParams);
            float offsetX = (pivotX - mWindowLayoutParams.x) * (1 - scaleFactor);
            float offsetY = (pivotY - mWindowLayoutParams.y) * (1 - scaleFactor);
            updateViewPosition(mWindowLayoutParams.x + (int) offsetX, mWindowLayoutParams.y + (int) offsetY);
        }

        private void updateViewPosition(int x, int y) {
            mWindowLayoutParams.x = x;
            mWindowLayoutParams.y = y;

            // 限制视图不超出屏幕边界
            mWindowLayoutParams.x = Math.max(marginLeft, Math.min(mWindowLayoutParams.x, screenWidth - parentView.getWidth() + marginRight));
            mWindowLayoutParams.y = Math.max(marginTop, Math.min(mWindowLayoutParams.y, screenHeight - parentView.getHeight() + marginBottom));
            mWindowManager.updateViewLayout(parentView, mWindowLayoutParams);
        }
    }
}
