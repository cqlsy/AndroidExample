/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.io.File;
import java.security.MessageDigest;


public class GlideUtil {

    private static String IMG_SERVER = "";

    public static void loadRoundImage(Context context, String url, ImageView view, int defaultResId) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            url = IMG_SERVER + url;
        }
        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .placeholder(defaultResId);
        Glide.with(context).load(url).apply(requestOptions)
                .into(view);
    }

    public static void loadRoundImage(Context context, int resId, ImageView view, int defaultResId) {
        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .placeholder(defaultResId);
        Glide.with(context).load(resId).apply(requestOptions)
                .into(view);
    }

    public static void loadRoundImage(Context context, String url, ImageView view) {
        loadRoundImage(context, url, view, R.drawable.ic_default);
    }

    public static void loadRoundImage(Context context, Drawable drawable, ImageView view) {
        loadRoundImage(context, drawable, view, R.drawable.ic_default);

    }

    public static void loadRoundImage(Context context, Drawable drawable, ImageView view, int defaultResId) {
        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .placeholder(defaultResId);
        Glide.with(context).load(drawable).apply(requestOptions)
                .into(view);
    }

    public static void loadImage(Context context, String url, ImageView view) {
        loadImage(context, url, view, R.drawable.ic_default);
    }

    public static void loadImage(Context context, String url, ImageView view, int defaultResId) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            url = IMG_SERVER + url;
        }
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(defaultResId);
        Glide.with(context).load(url).apply(requestOptions)
                .into(view);

    }

    public static void loadCornerImage(Context context, String url, ImageView view, float corner) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            url = IMG_SERVER + url;
        }
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.drawable.ic_default);
        Glide.with(context).load(url).apply(requestOptions).into(view);

    }

    public static void loadCornerImage(Context context, String url, ImageView view, float corner, int defaultResId) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http")) {
            url = IMG_SERVER + url;
        }
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(defaultResId);
        Glide.with(context).load(url).apply(requestOptions).into(view);

    }

    public static void loadCornerImage(Context context, Bitmap bitmap, ImageView view, float corner) {
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.drawable.ic_default);
        Glide.with(context).load(bitmap).apply(requestOptions).into(view);

    }

    public static void loadCornerImage(Context context, int resId, ImageView view, float corner) {
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.drawable.ic_default);
        Glide.with(context).load(resId).apply(requestOptions).into(view);

    }

    public static void loadCornerImage(Context context, Drawable bitmap, ImageView view, float corner) {
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.drawable.ic_default);
        Glide.with(context).load(bitmap).apply(requestOptions).into(view);

    }

    public static void loadCornerImage(Context context, File file, ImageView view, float corner) {
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.drawable.ic_default);
        Glide.with(context).load(file).apply(requestOptions).into(view);

    }

    public static void loadCornerImage(Context context, File file, ImageView view, float corner, SizeReadyCallback callback) {
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.drawable.ic_default);
        Glide.with(context).load(file).apply(requestOptions).into(view).getSize(callback);

    }

    public static void loadCornerImage(Context context, String file, ImageView view, float corner, SizeReadyCallback callback) {
        GlideRoundedCornersTransform roundedCorners = new GlideRoundedCornersTransform(DisplayUtil.dp2px(context, corner), CornerType.ALL);
        RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners).placeholder(R.drawable.ic_default);
        Glide.with(context).load(file).apply(requestOptions).into(view).getSize(callback);

    }

    public enum CornerType {
        ALL,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT,
        TOP_LEFT_BOTTOM_RIGHT,
        TOP_RIGHT_BOTTOM_LEFT,
        TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT,
        TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT,
    }

    /* 圆角中心剪切显示的l */
    public static class GlideRoundedCornersTransform extends CenterCrop {
        private float mRadius;
        private CornerType mCornerType;
        private final int VERSION = 1;
        private final String ID = BuildConfig.APPLICATION_ID + "GlideRoundedCornersTransform." + VERSION;
        private final byte[] ID_BYTES = ID.getBytes(CHARSET);


        public GlideRoundedCornersTransform(float radius, CornerType cornerType) {
            super();
            mRadius = radius;//dp ->px
            mCornerType = cornerType;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap transform = super.transform(pool, toTransform, outWidth, outHeight);
            return roundCrop(pool, transform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) {
                return null;
            }
            int width = source.getWidth();
            int height = source.getHeight();
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);


            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config
                        .ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader
                    .TileMode.CLAMP));
            paint.setAntiAlias(true);


            Path path = new Path();
            drawRoundRect(canvas, paint, path, width, height);

            return result;
        }

        private void drawRoundRect(Canvas canvas, Paint paint, Path path, int width, int height) {
            float[] rids;
            switch (mCornerType) {
                case ALL:
                    rids = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case TOP_LEFT:
                    rids = new float[]{mRadius, mRadius, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case TOP_RIGHT:
                    rids = new float[]{0.0f, 0.0f, mRadius, mRadius, 0.0f, 0.0f, 0.0f, 0.0f};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case BOTTOM_RIGHT:
                    rids = new float[]{0.0f, 0.0f, 0.0f, 0.0f, mRadius, mRadius, 0.0f, 0.0f};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case BOTTOM_LEFT:
                    rids = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, mRadius, mRadius};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case TOP:
                    rids = new float[]{mRadius, mRadius, mRadius, mRadius, 0.0f, 0.0f, 0.0f, 0.0f};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case BOTTOM:
                    rids = new float[]{0.0f, 0.0f, 0.0f, 0.0f, mRadius, mRadius, mRadius, mRadius};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case LEFT:
                    rids = new float[]{mRadius, mRadius, 0.0f, 0.0f, 0.0f, 0.0f, mRadius, mRadius};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case RIGHT:
                    rids = new float[]{0.0f, 0.0f, mRadius, mRadius, mRadius, mRadius, 0.0f, 0.0f};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case TOP_LEFT_BOTTOM_RIGHT:
                    rids = new float[]{mRadius, mRadius, 0.0f, 0.0f, mRadius, mRadius, 0.0f, 0.0f};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case TOP_RIGHT_BOTTOM_LEFT:
                    rids = new float[]{0.0f, 0.0f, mRadius, mRadius, 0.0f, 0.0f, mRadius, mRadius};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT:
                    rids = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, 0.0f, 0.0f};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                case TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT:
                    rids = new float[]{0.0f, 0.0f, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
                    drawPath(rids, canvas, paint, path, width, height);
                    break;
                default:
                    throw new RuntimeException("RoundedCorners type not belong to CornerType");
            }
        }


        /**
         * @param rids 圆角的半径，依次为左上角xy半径，右上角，右下角，左下角
         */
        private void drawPath(float[] rids, Canvas canvas, Paint paint, Path path, int width, int height) {
            path.addRoundRect(new RectF(0, 0, width, height), rids, Path.Direction.CW);
//        canvas.clipPath(path);
            canvas.drawPath(path, paint);
        }


        @Override
        public boolean equals(Object o) {
            return o instanceof GlideRoundedCornersTransform;
        }


        @Override
        public int hashCode() {
            return ID.hashCode();
        }


        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(ID_BYTES);
        }
    }
/*---------------------
    作者：villa_mou
    来源：CSDN
    原文：https://blog.csdn.net/villa_mou/article/details/80816857
    版权声明：本文为博主原创文章，转载请附上博文链接！*/
}
