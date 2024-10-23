package com.gmyboy.codelab.accesibility.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gmyboy.codelab.accesibility.R;

import java.util.Calendar;

/**
 * Created by gmy on 2021-06-02.
 * Copyright (c) 2021 FOTRIC. All rights reserved.
 */
public class FunctionImageButton extends View {
    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;
    private String mDescription;
    private String mTextString;
    private int mTextColor;
    private float mTextPaddingTopDimension;
    private float mTextSize;
    private Drawable mIconDrawable;
    private int mIconColorTint;
    private Drawable mBackground;
    private Matrix mMatrix;
    private Matrix mIconMatrix = null;
    private Drawable mBackgroundSelected;
    private int mColorSelected = -1;
    private Drawable mBackgroundDisabled;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private int mIconWidth;
    private int mIconHeight;
    private float sx;
    private float sy;
    private boolean mSelectEnable;
    private boolean mSelected;
    private boolean mEnabled;
    private boolean mRippled = false;
    private Drawable mRippledBackground;

    private OnSelectChangedListener onSelectChangedListener;

    public void setOnSelectChangedListener(OnSelectChangedListener listener) {
        this.onSelectChangedListener = listener;
    }

    public FunctionImageButton(Context context) {
        super(context);
        init(null, 0);
    }

    public FunctionImageButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FunctionImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public FunctionImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FunctionImageButton, defStyleAttr, 0);
        mDescription = a.getString(R.styleable.FunctionImageButton_description);
        mTextString = a.getString(R.styleable.FunctionImageButton_title);
        mTextColor = a.getColor(R.styleable.FunctionImageButton_titleColor, ContextCompat.getColor(getContext(), R.color.xframe_color_text));
        mTextSize = a.getDimension(R.styleable.FunctionImageButton_titleSize, getResources().getDimension(R.dimen.xframe_function_image_button_title_size));
        mSelectEnable = a.getBoolean(R.styleable.FunctionImageButton_selectEnable, mSelectEnable);
        mSelected = a.getBoolean(R.styleable.FunctionImageButton_selected, mSelected);
        mEnabled = a.getBoolean(R.styleable.FunctionImageButton_enabled, true);
        mRippled = a.getBoolean(R.styleable.FunctionImageButton_rippled, mRippled);
        if (a.hasValue(R.styleable.FunctionImageButton_icon)) {
            mIconDrawable = a.getDrawable(R.styleable.FunctionImageButton_icon);
            if (mIconDrawable != null) {
                mIconWidth = a.getDimensionPixelOffset(R.styleable.FunctionImageButton_iconWidth, mIconDrawable.getIntrinsicWidth());
                mIconHeight = a.getDimensionPixelOffset(R.styleable.FunctionImageButton_iconHeight, mIconDrawable.getIntrinsicHeight());
                mIconDrawable.setCallback(this);
                mIconColorTint = a.getColor(R.styleable.FunctionImageButton_iconColorTint, -1);
                if (mIconColorTint != -1) {
                    mIconDrawable = mIconDrawable.mutate();
                    mIconDrawable.setTint(mIconColorTint);
                }
            }
        }
        float defaultPadding = 0;
        if (!TextUtils.isEmpty(mTextString) && mIconDrawable != null) {
            defaultPadding = getResources().getDimension(R.dimen.xframe_function_image_button_title_padding_top);
        }
        mTextPaddingTopDimension = a.getDimension(R.styleable.FunctionImageButton_titlePaddingTop, defaultPadding);
        mBackground = getBackground();
        if (a.hasValue(R.styleable.FunctionImageButton_colorSelected)) {
            mColorSelected = a.getColor(R.styleable.FunctionImageButton_colorSelected, -1);
        }
        if (a.hasValue(R.styleable.FunctionImageButton_backgroundSelected)) {
            mBackgroundSelected = a.getDrawable(R.styleable.FunctionImageButton_backgroundSelected);
            if (mBackgroundSelected != null) {
                mBackgroundSelected.setCallback(this);
            }
        } else {
            mBackgroundSelected = ContextCompat.getDrawable(getContext(), R.drawable.xframe_shape_selected_bg);
        }
        if (a.hasValue(R.styleable.FunctionImageButton_backgroundDisabled)) {
            mBackgroundDisabled = a.getDrawable(R.styleable.FunctionImageButton_backgroundDisabled);
            if (mBackgroundDisabled != null) {
                mBackgroundDisabled.setCallback(this);
            }
        } else {
            mBackgroundDisabled = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.xframe_color_disabled));
        }
        mRippledBackground = ContextCompat.getDrawable(getContext(), R.drawable.xframe_function_image_button_ripple_bg);
        a.recycle();

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(mTextColor);
        mMatrix = new Matrix();

        invalidBackground();
        invalidateTextPaintAndMeasurements();
    }

    private void invalidBackground() {
        if (mEnabled) {
            if (mRippled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setForeground(mRippledBackground);
                }
            }
        } else {
            if (mRippled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setForeground(null);
                }
            }
        }
        invalidSelectedState();
        setAlpha(mEnabled ? 1.0f : 0.5f);
    }

    private void invalidSelectedState() {
        if (mSelected) {
            if (mColorSelected != -1) {
                setIconColor(mColorSelected);
            } else {
                setBackground(mBackgroundSelected);
            }
        } else {
            if (mColorSelected != -1) {
                setIconColor(-1);
            } else {
                setBackground(mBackground);
            }
        }
    }

    private void invalidateTextPaintAndMeasurements() {
        if (TextUtils.isEmpty(mTextString)) return;
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextWidth = mTextPaint.measureText(mTextString);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = 0 - fontMetrics.bottom - fontMetrics.top;
    }

    private void configureBounds() {
        final int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (mIconDrawable == null || mIconWidth <= 0 || mIconHeight <= 0) {
            mIconMatrix = null;
            if (!TextUtils.isEmpty(mTextString)) {
                sx = vWidth * 0.5f;
                sy = (vHeight + mTextHeight) * 0.5f + mTextPaddingTopDimension;
            }
        } else {
            final int dWidth = mIconDrawable.getIntrinsicWidth();
            final int dHeight = mIconDrawable.getIntrinsicHeight();
            mIconDrawable.setBounds(0, 0, dWidth, dHeight);
            mIconMatrix = mMatrix;
            float scale;
            float dx;
            float dy;
            float iconW;
            float iconH;
            scale = Math.min(mIconWidth / (dWidth * 1.0f), mIconHeight / (dHeight * 1.0f));
            iconW = dWidth * scale;
            iconH = dHeight * scale;
            dx = (vWidth - iconW) * 0.5f;
            dy = (vHeight - iconH) * 0.5f;
            if (!TextUtils.isEmpty(mTextString)) {
                sx = vWidth * 0.5f;
                dy = (vHeight - mTextHeight - iconH - mTextPaddingTopDimension) * 0.5f;
                sy = dy + iconH + mTextPaddingTopDimension + mTextHeight;
            }
            mIconMatrix.setScale(scale, scale);
            mIconMatrix.postTranslate(dx, dy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (!mEnabled) {
                    return true;
                }
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    if (mEnabled && mSelectEnable) {
                        mSelected = !mSelected;
                        invalidSelectedState();
                        if (onSelectChangedListener != null) {
                            onSelectChangedListener.onSelectChanged(this, mSelected);
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int pLeft = getPaddingLeft();
        final int pRight = getPaddingRight();
        final int pTop = getPaddingTop();
        final int pBottom = getPaddingBottom();
        int w;
        int h;
        if (mIconDrawable == null) {
            mIconWidth = -1;
            mIconHeight = -1;
            w = h = 0;
        } else {
            w = Math.round(mIconWidth);
            h = Math.round(mIconHeight);
            if (w <= 0) w = 1;
            if (h <= 0) h = 1;
        }

        if (!TextUtils.isEmpty(mTextString) && mTextWidth > 0 && mTextHeight > 0) {
            w = Math.max(w, Math.round(mTextWidth + 1));
            h += Math.round(mTextHeight + 1);
        }
        int widthSize;
        int heightSize;
        w += pLeft + pRight;
        h += pTop + pBottom;
        w = Math.max(w, getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());

        widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        configureBounds();
        if (mIconDrawable != null) {
            if (mIconMatrix == null) {
                mIconDrawable.draw(canvas);
            } else {
                final int saveCount = canvas.getSaveCount();
                canvas.save();
                canvas.translate(paddingLeft, paddingTop);
                canvas.concat(mIconMatrix);
                mIconDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }
        if (!TextUtils.isEmpty(mTextString)) {
            canvas.drawText(mTextString, sx + paddingLeft, sy + paddingTop, mTextPaint);
        }
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getTitle() {
        return mTextString;
    }

    public void setTitle(String title) {
        mTextString = title;
        invalidateTextPaintAndMeasurements();
    }

    public void setTitleColor(@ColorInt int resId) {
        mTextColor = resId;
        invalidateTextPaintAndMeasurements();
    }

    public void setIconDrawable(Drawable drawable) {
        if (mIconDrawable != drawable) {
            mIconDrawable = drawable.mutate();
            invalidate();
        }
    }

    public void setIconBitmap(Bitmap bm) {
        mIconDrawable = null;
        BitmapDrawable mRecycleBitmapDrawable = new BitmapDrawable(getResources(), bm);
        setIconDrawable(mRecycleBitmapDrawable);
    }

    public void setIconResource(@DrawableRes int resId) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resId);
        if (drawable != null) {
            mIconDrawable = drawable.mutate();
        }
        invalidate();
    }

    public void setIconColorTint(@ColorRes int resId) {
        mIconColorTint = ContextCompat.getColor(getContext(), resId);
        mIconDrawable = mIconDrawable.mutate();
        mIconDrawable.setTint(mIconColorTint);
        invalidate();
    }

    public void setIconColor(@ColorInt int color) {
        mIconColorTint = color;
        mIconDrawable = mIconDrawable.mutate();
        mIconDrawable.setTint(mIconColorTint);
        invalidate();
    }

    /**
     * Set the width of icon in px
     *
     * @param width
     */
    public void setIconWidth(int width) {
        this.mIconWidth = width;
    }

    public void setIconHeight(int height) {
        this.mIconHeight = height;
    }

    public void setTitlePaddingTop(int padding) {
        this.mTextPaddingTopDimension = padding;
        invalidate();
    }

    public void setSelectEnable(boolean mSelectEnable) {
        this.mSelectEnable = mSelectEnable;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
        invalidBackground();
    }

    public void setBackgroundSelected(Drawable mBackgroundSelected) {
        this.mBackgroundSelected = mBackgroundSelected;
    }

    public void setColorSelected(int mColorSelected) {
        this.mColorSelected = mColorSelected;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
        invalidBackground();
    }

    public void setRippleEnable(boolean enable) {
        this.mRippled = enable;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public boolean isRippled() {
        return mRippled;
    }

    public interface OnSelectChangedListener {
        void onSelectChanged(View view, boolean selected);
    }
}
