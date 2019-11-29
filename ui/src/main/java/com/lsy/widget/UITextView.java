/*
 * Copyright (c) 2017 sh. All rights reserved.
 */

package com.lsy.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.ColorUtils;

import com.lsy.framework.R;
import com.lsy.utils.ColorUtil;


/**
 * 扩展TextView
 * 支持设置点击反馈背景色和文字色
 * 支持设置圆角
 */
public class UITextView extends AppCompatTextView {

    private int mBorderWidth;
    private int mBorderRadius;
    private int mBorderColor;
    private int mBorderColorPressed;
    private int mBorderColorDisabled;
    private int mBgColor;
    private int mBgColorPressed;
    private int mBgColorDisabled;
    private int mTextColor;
    private int mTextColorPressed;
    private int mTextColorDisabled;

    // 是否启用ripple效果, 若不启用则只使用selector效果
    private boolean mRippleEnabled;
    // 是否启用点击反馈效果
    private boolean mTouchEffectEnabled;

    public UITextView(Context context) {
        this(context, null);
    }

    public UITextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UITextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    // init params
    private void init(AttributeSet attrs, int defStyleAttr) {


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UITextView, defStyleAttr, 0);
        mRippleEnabled = a.getBoolean(R.styleable.UITextView_ui_ripple_enabled, true);
        mTouchEffectEnabled = a.getBoolean(R.styleable.UITextView_ui_touch_effect_enabled, true);
        // 默认的ripple颜色
        int defaultRippleColor = a.getColor(R.styleable.UITextView_ui_ripple_color, 0);
        // 使用默认ripple颜色的相反色作为按钮禁用时的遮罩
        int disabledMaskColor = ColorUtil.reverse(defaultRippleColor);

        // border
        mBorderWidth = a.getDimensionPixelSize(R.styleable.UITextView_ui_border_width, 0);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.UITextView_ui_border_radius, 0);
        mBorderColor = a.getColor(R.styleable.UITextView_ui_border_color, 0);
        mBorderColorPressed = a.getColor(R.styleable.UITextView_ui_border_color_pressed, mBorderColor);
        mBorderColorDisabled = a.getColor(R.styleable.UITextView_ui_border_color_disabled,
                ColorUtils.compositeColors(disabledMaskColor, mBorderColor));

        // 背景色
        mBgColor = a.getColor(R.styleable.UITextView_ui_background_color, 0);
        if (mTouchEffectEnabled) {
            int bgColorPressed;
            if (mRippleEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 当5.0及以上系统版本开启ripple效果时，默认的按下颜色设置为默认ripple颜色
                bgColorPressed = defaultRippleColor;
            } else {
                // 其他情况使用selector，给原背景色附加0.9不透明度的ripple颜色作为默认的按下效果
                bgColorPressed = ColorUtils.compositeColors(
                        ColorUtil.setRelativeAlphaComponent(defaultRippleColor, 0.36f),
                        mBgColor);
            }
            mBgColorPressed = a.getColor(R.styleable.UITextView_ui_background_color_pressed, bgColorPressed);
        } else {
            mBgColorPressed = mBgColor;
        }
        // 获取按钮禁用时的背景颜色
        mBgColorDisabled = a.getColor(R.styleable.UITextView_ui_background_color_disabled,
                ColorUtils.compositeColors(disabledMaskColor, mBgColor));

        mTextColor = a.getColor(R.styleable.UITextView_ui_text_color, 0);
        mTextColorPressed = a.getColor(R.styleable.UITextView_ui_text_color_pressed,
                ColorUtil.setRelativeAlphaComponent(mTextColor, 0.8f));
        mTextColorDisabled = a.getColor(R.styleable.UITextView_ui_text_color_disabled,
                ColorUtils.compositeColors(disabledMaskColor, mTextColor));
        a.recycle();

        update();
    }

    /**
     * 设置背景和文字颜色
     */
    public void update() {
        StateListDrawable backgroundDrawable = new StateListDrawable();
        backgroundDrawable.addState(new int[]{-android.R.attr.state_enabled},
                createGradientDrawable(mBgColorDisabled, mBorderRadius,
                        mBorderWidth, mBorderColorDisabled));
        if (mRippleEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // create rippleDrawable
            RippleDrawable rippleDrawable = new RippleDrawable(
                    ColorStateList.valueOf(mBgColorPressed),
                    createGradientDrawable(mBgColor, mBorderRadius, mBorderWidth, mBorderColor),
                    null);
            backgroundDrawable.addState(new int[]{}, rippleDrawable);
        } else {
            backgroundDrawable.addState(new int[]{android.R.attr.state_pressed},
                    createGradientDrawable(mBgColorPressed, mBorderRadius, mBorderWidth,
                            mBorderColorPressed));
            backgroundDrawable.addState(new int[]{},
                    createGradientDrawable(mBgColor, mBorderRadius, mBorderWidth, mBorderColor));
        }
        setBackgroundDrawable(backgroundDrawable);

        ColorStateList colorStateList = createColorStateList(mTextColor, mTextColorPressed,
                mTextColorDisabled);
        setTextColor(colorStateList);
    }

    public void setBgColor(int color) {
        mBgColor = color;
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }

    public void setUITextColor(int color) {
        mTextColor = color;
    }


    /**
     * 动态设置
     *
     * @param color
     */
    public void setBgColor(String color) {
        if (!TextUtils.isEmpty(color)) {
            if (!color.startsWith("#")) {
                if (color.length() == 6) {
                    color = "ff" + color;
                }
                color = "#" + color;
            }
        }
        try {
            setBgColor(Color.parseColor(color));
        } catch (Exception ignored) {

        }
    }

    /**
     * 动态设置
     *
     * @param color
     */
    public void setBorderColor(String color) {
        if (!TextUtils.isEmpty(color)) {
            if (!color.startsWith("#")) {
                if (color.length() == 6) {
                    color = "ff" + color;
                }
                color = "#" + color;
            }
        }
        try {
            setBorderColor(Color.parseColor(color));
        } catch (Exception ignored) {

        }
    }

    /**
     * 根据文字颜色创建ColorStateList
     *
     * @param normal   正常状态下颜色
     * @param pressed  按下状态颜色
     * @param disabled 不可用状态颜色
     */
    private ColorStateList createColorStateList(int normal, int pressed, int disabled) {
        int[] colors = new int[]{disabled, pressed, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    /**
     * 创建按钮背景
     *
     * @param color        背景颜色
     * @param cornerRadius 圆角半径
     * @param borderWidth  边框宽度
     * @param borderColor  边框颜色
     */
    private GradientDrawable createGradientDrawable(int color, float cornerRadius, int borderWidth, int borderColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(cornerRadius);
        drawable.setStroke(borderWidth, borderColor);
        drawable.setColor(color);
        return drawable;
    }
}