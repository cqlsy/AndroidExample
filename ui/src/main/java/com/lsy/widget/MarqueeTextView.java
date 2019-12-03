package com.lsy.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 自动开启跑马灯效果的TextView
 */
public class MarqueeTextView extends AppCompatTextView {

    public MarqueeTextView(Context context) {
        this(context, null);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSingleLine(true);
        setHorizontallyScrolling(true);//横向滚动
        setMarqueeRepeatLimit(-1);// 滚动一次-1是无线滚动
        setSelected(true);//开始滚
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
