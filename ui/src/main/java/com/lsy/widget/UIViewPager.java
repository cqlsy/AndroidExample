package com.lsy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.lsy.framework.R;


/**
 * 可控制滑动支持的ViewPager
 */
public class UIViewPager extends ViewPager {

    private boolean mScrollEnabled;

    public UIViewPager(Context context) {
        super(context);
    }

    public UIViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.UIViewPager);
        mScrollEnabled = typedArray.getBoolean(R.styleable.UIViewPager_scroll_enabled, true);
        typedArray.recycle();
    }

    public void setScrollEnabled(boolean enabled) {
        mScrollEnabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollEnabled) {
            try {
                //ViewPager和PhotoView滑动冲突
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mScrollEnabled) {
            try {
                //ViewPager和PhotoView滑动冲突
                return super.onTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        } else {
            return false;
        }
    }
}
