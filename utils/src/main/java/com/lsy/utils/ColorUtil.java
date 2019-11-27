/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.core.graphics.ColorUtils;

/**
 *
 */
public class ColorUtil {

    @ColorInt
    public static int setRelativeAlphaComponent(
            @ColorInt int color, @FloatRange(from = 0, to = 1) float scale) {
        return ColorUtils.setAlphaComponent(color, (int) (Color.alpha(color) * scale));
    }

    @ColorInt
    public static int reverse(@ColorInt int color) {
        return ColorUtils.setAlphaComponent(~color, Color.alpha(color));
    }
}
