package com.lsy.storage;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lsy.crypto.Digest;
import com.lsy.utils.LogUtil;
import com.lsy.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 存储管理类;
 * 本工具类只是做存储等，是否有权限不考虑
 */
public class FileUtils {

    private static class Instance {
        private static FileUtils mInstance = new FileUtils();
    }

    public static FileUtils getInstance() {
        return Instance.mInstance;
    }

    private Application mApp;
    /* 内部数据暂时分为公共和私有 */
    private String mPubRootPath = "/pub/"; /* 公共存储;图片等文件，可删除的放在这里 */
    private String mPriRootPath = "/pri/"; /* 私有存储：在清除的时候，不能清除的部分，一些数据的保存，保存在这里 */
    /* 外部存储，全是公有的 */
    private String mExternalPath = "";

    /**
     * 要使用的话，必须要初始化
     */
    public void init(Application application) {
        init(application, "", "", "");
    }

    /**
     * 要使用的话，必须要初始化
     */
    public void init(Application application, String pub, String pri, String externalRoot) {
        mApp = application;
        if (!TextUtils.isEmpty(pub)) {
            mPubRootPath = pub;
        }
        if (!TextUtils.isEmpty(pri)) {
            mPriRootPath = pri;
        }
        if (!TextUtils.isEmpty(externalRoot)) {
            mExternalPath = externalRoot;
        } else {
            mExternalPath = application.getPackageName() + "/";
        }
    }

    public File getFileOrCreate(String fileName) {
        return getFileOrCreate("", fileName, true);
    }

    public File getFileOrCreate(String fileName, boolean isPub) {
        return getFileOrCreate("", fileName, isPub);
    }

    /**
     * 根据路径获取（如果文件不存在会创建）
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param isPub    是否是 公有的
     * @return 如果找到（或者创建成功）返回File实例，如果失败返回 null
     */
    public File getFileOrCreate(String path, String fileName, boolean isPub) {
        String root = mPubRootPath;
        if (!isPub) {
            root = mPriRootPath;
        }
        // 目录
        File dir = new File(mApp.getFilesDir(), root + path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null;
            }
        }
        // 文件
        File file = new File(dir, fileName);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    public File getDir(String path, Boolean isPub) {
        /*String parent = mPubRootPath;
        if (!isPub) {
            parent = mPriRootPath;
        }
        path = parent + path;
        String root = mApp.getFilesDir().getAbsolutePath();
        LogUtil.d(getClass().getSimpleName(), "getDir(String path, Boolean isPub)" + "root is : "
                + root + "; path is : " + path);*/
        return getFile(path, "", isPub);
    }

    /**
     * 内部存储，不需要权限
     *
     * @param path
     * @param fileName
     * @return
     */
    public File getFile(String path, String fileName) {
        return getFile(path, fileName, true);
    }

    /**
     * 内部存储，不需要权限
     *
     * @param path
     * @param fileName
     * @return
     */
    public File getFile(String path, String fileName, Boolean isPub) {
        String parent = mPubRootPath;
        if (!isPub) {
            parent = mPriRootPath;
        }
        path = parent + path;
        String root = mApp.getFilesDir().getAbsolutePath();
        LogUtil.d(getClass().getSimpleName(), "getFile(String path, String fileName, Boolean isPub)"
                + "\nroot is : " + root + ";\npath is : " + path
                + "\nfileName is : " + fileName);
        return new File(root + path, fileName);
    }

    /**
     * 外部存储，需要权限
     *
     * @param path
     * @return
     */
    public File getExtDir(String path) {
        return getExtFile(path, "");
    }

    /**
     * 外部存储，需要权限; 使用这个方法之前需要确定权限已经到位了
     *
     * @param path
     * @return
     */
    public File getExtFile(String path, String fileName) {
        path += mExternalPath;
        String root = Objects.requireNonNull(mApp.getExternalFilesDir(null)).getAbsolutePath();
        LogUtil.d(getClass().getSimpleName(), "getExtFile(String path, String fileName)"
                + "\nroot is : " + root + ";\npath is : " + path
                + ";\nfileName is : " + fileName);
        return new File(root + path, fileName);
    }

    public File getCacheFile(String path, String fileName) {
        String root = mApp.getCacheDir().getAbsolutePath();
        LogUtil.d(getClass().getSimpleName(), "getCacheFile(String path, String fileName)"
                + "\nroot is : " + root + ";\npath is : " + path
                + ";\nfileName is : " + fileName);
        return new File(root + path, fileName);
    }

    public File getExtCacheFile(String path, String fileName) {
        if (mApp.getExternalCacheDir() != null) {
            String root = mApp.getCacheDir().getAbsolutePath();
            LogUtil.d(getClass().getSimpleName(), "getExtCacheFile(String path, String fileName)"
                    + "\nroot is : " + root + ";\npath is : " + path
                    + "\nfileName is : " + fileName);
            return new File(root + path, fileName);
        }
        return null;
    }

    /**
     * 创建 File
     *
     * @param file 需要创建的File
     * @return 是否创建成功
     */
    public boolean createFile(File file) {
        try {
            File dir = file.getParentFile();
            if (dir == null) {
                LogUtil.e(getClass().getSimpleName(), "create File err");
                return false;
            }
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    LogUtil.e(getClass().getSimpleName(), "create File err");
                    return false;
                }
            }
            if (!file.createNewFile()) {
                LogUtil.e(getClass().getSimpleName(), "create File err");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(getClass().getSimpleName(), "create File err");
            return false;
        }
        LogUtil.d(getClass().getSimpleName(), "create File Success : \n" + file +
                ";\nthe path is : " + file.getAbsolutePath());
        return true;
    }

    /**
     * 获取缓存在磁盘上的数据的大小
     *
     * @param context
     * @return
     */
    public String getTotalCacheSize(Context context) {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            /* todo 这里添加相关 */
            cacheSize += getFolderSize(getExtCacheFile("", ""));
            cacheSize += getFolderSize(getCacheFile("", ""));
            cacheSize += getFolderSize(getDir("", true));
            cacheSize += getFolderSize(getExtDir(""));
            /* glide 混出删除 */
            cacheSize += CacheGlideUtil.getInstance().getGlideCacheSize(context);
        }
        return getFormatSize(cacheSize);
    }


    /**
     * 清除缓存在磁盘上的数据
     *
     * @param context
     */
    public void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(getExtCacheFile("", ""));
            deleteDir(getCacheFile("", ""));
            deleteDir(getDir("", true));
            deleteDir(getExtDir(""));
            /* glide 混出删除 */
            CacheGlideUtil.getInstance().clearImageAllCache(context);
        }

    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

// 获取文件

// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/

// 目录，一般放一些长时间保存的数据

// Context.getExternalCacheDir() -->

// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据

    public long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File value : fileList) {
                // 如果下面还有文件
                if (value.isDirectory()) {
                    size = size + getFolderSize(value);
                } else {
                    size = size + value.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;

    }

    /**
     * 格式化单位
     *
     * @param size
     */

    public String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public String getFileName(String str, String houzui) {
        return Digest.md5(str) + houzui;
    }


    // ===============================   图片存储处理 ==============================

    public void loadImageAndStorage(String url, String filePath, String fileName) {
        loadImageAndStorage(url, filePath, fileName, true, false, true);
    }

    public void loadImageAndStorage(String url, final String filePath, final String fileName,
                                    final boolean isPub, final boolean isExt, final boolean isNeedDelete) {
        Glide.with(Utils.getInstance().getContext()).load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        FileUtils.getInstance().saveImageToGallery(drawableToBitmap(resource),
                                filePath, fileName, isPub, isExt, isNeedDelete);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void saveImageToGallery(Drawable drawable, String fileName) {
        saveImageToGallery(drawableToBitmap(drawable), "", fileName,
                true, false, true);
    }

    /**
     * 主要调用这个方法
     *
     * @param drawable
     * @param path
     * @param fileName
     */
    public void saveImageToGallery(Drawable drawable, String path, String fileName) {
        saveImageToGallery(drawableToBitmap(drawable), path, fileName,
                true, false, true);
    }

    public void saveImageToGallery(Bitmap bmp, String filePath, String fileName,
                                   boolean isPub, boolean isExt, boolean isNeedDelete) {
        try {
            // 目录
            File file;
            if (isExt) {
                file = getExtFile(filePath, fileName);
            } else {
                file = getFile(filePath, fileName, isPub);
            }
            if (file.exists()) {
                if (isNeedDelete) {
                    if (!file.delete()) {
                        return;
                    }
                } else {
                    /* 如果文件存在，不处理 */
                    return;
                }
            }
            if (!createFile(file)) {
                /* 如果没有创建成功则不进行后续了 */
                return;
            }
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bmp.recycle();
        }
        if (!bmp.isRecycled()) {
            /* 这里回收一下 */
            bmp.recycle();
        }
    }

    /**
     * 作者：StoneWay3
     * 链接：https://www.jianshu.com/p/1238e8acff81
     * 来源：简书
     * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
     * <p>
     * drawable 转为 bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}
