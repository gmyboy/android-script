package com.gmyboy.codelab.accesibility.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PointerView extends View {
    private Paint circlePaint;
    private Paint plusPaint;
    private int circleRadius = 42; // Circle radius
    private float centerX; // Circle center X coordinate
    private float centerY; // Circle center Y coordinate
    private boolean isTouchInside;
    private boolean isAnimating = false; // Animation state
    private Handler handler = new Handler();
    private Runnable clickRunnable; // Click effect Runnable
    private int clickInterval = 100; // Click interval (milliseconds)
    private int originalColor = Color.parseColor("#AAffffff"); // Original circle color
    private int touchableAreaRadius = 100; // Touchable area radius

    public PointerView(Context context) {
        super(context);
        init();
    }

    public PointerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(originalColor); // Set original circle color
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4);

        plusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        plusPaint.setColor(originalColor); // Plus sign color
        plusPaint.setStrokeWidth(6); // Plus sign line width
        plusPaint.setStyle(Paint.Style.STROKE); // Plus sign stroke style

//        startClick();

        setFocusable(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f; // Initial circle center X
        centerY = h / 2f; // Initial circle center Y
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the circle
        canvas.drawCircle(centerX, centerY, circleRadius, circlePaint);

        // Calculate plus sign coordinates
        float plusLength = circleRadius / 3f;
        canvas.drawLine(centerX, centerY - plusLength, centerX, centerY + plusLength, plusPaint); // Vertical line
        canvas.drawLine(centerX - plusLength, centerY, centerX + plusLength, centerY, plusPaint); // Horizontal line
    }

    public void startClick() {
        if (isAnimating) return; // Do not execute if already animating
        isAnimating = true; // Set to animating state

        // Start color change effect
        clickRunnable = new Runnable() {
            @Override
            public void run() {
                // Create color change animation
                ValueAnimator colorAnim = ValueAnimator.ofInt(4, 8);
                colorAnim.setDuration(100); // Animation duration
                colorAnim.addUpdateListener(animation -> {
                    plusPaint.setStrokeWidth((int) animation.getAnimatedValue());
                    circlePaint.setStrokeWidth((int) animation.getAnimatedValue());
                    invalidate(); // Refresh view
                });
                colorAnim.setRepeatCount(1); // Repeat 1 time
                colorAnim.setRepeatMode(ValueAnimator.REVERSE); // Reverse execution
                colorAnim.start(); // Start animation

                // Continue calling itself after each animation
                handler.postDelayed(this, clickInterval);
            }
        };

        // Start the first click
        clickRunnable.run();
    }

    private void stopClicking() {

        isAnimating = false; // Stop animation
        handler.removeCallbacks(clickRunnable); // Remove all click callbacks
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        isTouchInside = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (isInTouchableArea(x, y)) {
                    centerX = x; // Update center X to finger position
                    centerY = y; // Update center Y to finger position
                    stopClicking();
                    invalidate(); // Refresh view
                    return true;
                }
        }
        return false;
    }*/

    // Check if the touch event is within the touchable area
    private boolean isInTouchableArea(float x, float y) {
        float dx = x - centerX;
        float dy = y - centerY;
        return (dx * dx + dy * dy <= touchableAreaRadius * touchableAreaRadius);
    }
}
