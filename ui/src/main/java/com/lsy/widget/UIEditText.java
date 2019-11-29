/*
 * Copyright (c) 2017 yeeyuntech. All rights reserved.
 */

package com.lsy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatEditText;

import com.lsy.framework.R;


/**
 * 支持一键清除文字，可自定义图标
 */
public class UIEditText extends AppCompatEditText {

    private boolean mClearEnabled;
    private Drawable mClearIcon;

    public UIEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UIEditText);
        mClearEnabled = a.getBoolean(R.styleable.UIEditText_ui_clear_enabled, true);
        setClearIcon(a.getDrawable(R.styleable.UIEditText_ui_clear_icon));
        a.recycle();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mClearEnabled) {
            if (hasFocus() && text.length() > 0) {
                showClearIcon();
            } else {
                hideClearIcon();
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (mClearEnabled) {
            if (focused && getText().length() > 0) {
                showClearIcon();
            } else {
                hideClearIcon();
            }
        }
    }

    private void showClearIcon() {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mClearIcon, getCompoundDrawables()[3]);
    }

    private void hideClearIcon() {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mClearEnabled) {
            final int x = (int) event.getX();
            if (mClearIcon != null && getText().length() > 0 && x > getWidth() - getPaddingRight() - mClearIcon.getIntrinsicWidth()) {
                setText("");
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setClearEnabled(boolean enabled) {
        mClearEnabled = enabled;
        if (mClearEnabled) {
            // 如果icon为null，将使用默认icon
            setClearIcon(mClearIcon);
        }
    }

    public void setClearIcon(@DrawableRes int resId) {
        setClearIcon(AppCompatResources.getDrawable(getContext(), resId));
    }

    /**
     * 设置清空按钮icon
     *
     * @param icon 为null时，无法触发点击清空事件
     */
    public void setClearIcon(Drawable icon) {
        mClearIcon = icon;
        if (mClearEnabled && mClearIcon == null) {
            mClearIcon = AppCompatResources.getDrawable(getContext(),
                    R.drawable.yy_uiedittext_default_clear_icon);
        }
        if (mClearIcon != null) {
            mClearIcon.setBounds(0, 0, mClearIcon.getIntrinsicWidth(),
                    mClearIcon.getIntrinsicHeight());
        }
    }
}