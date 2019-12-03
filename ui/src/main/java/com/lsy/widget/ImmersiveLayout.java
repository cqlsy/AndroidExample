package com.lsy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lsy.framework.R;
import com.lsy.utils.DisplayUtil;

public class ImmersiveLayout extends FrameLayout {

    public ImmersiveLayout(@NonNull Context context) {
        this(context, null);
    }

    public ImmersiveLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImmersiveLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.ImmersiveLayout);
        boolean need = type.getBoolean(R.styleable.ImmersiveLayout_need_fit_window, true);
        if (need) {
            int mStatusHeight = DisplayUtil.getStatusBarHeight(context);
            setPadding(0, mStatusHeight, 0, 0);
        }
        type.recycle();
    }
}
