/*
 * Copyright (c) 2017 yeeyuntech. All rights reserved.
 */

package com.lsy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 图形验证码
 * 仅支持数字0～9
 */
public class ImageCodeView extends View {

    //验证码长度
    private int codeLen = 4;
    //最小案字体大小
    private int fontSizeMin = 24;
    //最大字体大小
    private int fontSizeMax = 50;
    //文字颜色范围
    private int[] colors = new int[]{0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFF53DA33, 0xFFAA0000, 0xFFFFBB00};
    //背景颜色
    private int bgColor = 0xFFEEEEEE;
    //干扰线条数量
    private int lines = 8;
    //干扰线条颜色范围
    private int[] lineColors = new int[]{0xFF888888, 0xFFFF7744, 0xFF888800, 0xFF008888};
    //线条最小高度
    private int lineHeightMin = 1;
    //线条最大高度
    private int lineHeightMax = 5;
    //线条最小宽度
    private int lineWidthMin = 1;
    //线条最大宽度
    private int lineWidthMax = 60;
    //验证码字符串范围
    private String[] str = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    //画笔
    private Paint paint;
    //宽高
    private int width, height;
    //验证码字符串
    private String code = "";

    public ImageCodeView(Context context) {
        super(context);
        init();
    }

    public ImageCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        code = createCode();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0) {
            width = getWidth();
            height = getHeight();
            fontSizeMin = height / 4;
            fontSizeMax = height * 3 / 4;
        }

        //背景色
        paint.setColor(bgColor);
        canvas.drawRect(0, 0, width, height, paint);

        //画数字
        int weight = width / code.length();
        for (int i = 0; i < code.length(); i++) {
            paint.setColor(colors[randInt(0, colors.length - 1)]);
            paint.setTextSize(randInt(fontSizeMin, fontSizeMax));
            int deg = randInt(-30, 30);
            canvas.rotate(deg, weight * (i + 0.5f), height / 2);
            canvas.drawText(code.substring(i, i + 1), weight * (i + 0.5f), randInt(fontSizeMax, height), paint);
            canvas.rotate(-deg, weight * (i + 0.5f), height / 2);
        }

        //画干扰线
        for (int i = 0; i < lines; i++) {
            paint.setStrokeWidth(randInt(lineHeightMin, lineHeightMax));
            paint.setColor(lineColors[randInt(0, lineColors.length - 1)]);
            canvas.drawLine(randInt(0, width), randInt(0, height), randInt(0, width), randInt(0, height), paint);
        }

    }

    private int randInt(int min, int max) {
        int c = max - min + 1;
        double num = Math.random() * c + min;
        return (int) Math.floor(num);
    }

    private String randStr() {
        return str[randInt(0, str.length - 1)];
    }

    private String createCode() {
        String codeStr = "";
        for (int i = 0; i < codeLen; i++) {
            codeStr = codeStr.concat(randStr());
        }
        return codeStr;
    }

    public void update() {
        code = createCode();
        invalidate();
    }

    public String getText() {
        return code;
    }
}