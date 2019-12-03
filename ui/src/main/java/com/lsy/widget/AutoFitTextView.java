/*
 * Copyright (c) 2017 yeeyuntech. All rights reserved.
 */

package com.lsy.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 根据TextView宽度自动缩小文字
 */
public class AutoFitTextView extends AppCompatTextView {

    private static final float DEFAULT_MIN_TEXT_SIZE = 2;
    private static final float DEFAULT_MAX_TEXT_SIZE = 20;
    // Attributes
    private Paint testPaint;
    private float minTextSize;
    private float maxTextSize;

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        testPaint = new Paint();
        testPaint.set(this.getPaint());
        // max size defaults to the intially specified text size unless it is
        // too small
        maxTextSize = this.getTextSize();
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }
        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() -
                    this.getPaddingRight();
            float trySize = maxTextSize;
            testPaint.setTextSize(trySize);
            while ((trySize > minTextSize) &&
                    (testPaint.measureText(text) > availableWidth)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }
}
