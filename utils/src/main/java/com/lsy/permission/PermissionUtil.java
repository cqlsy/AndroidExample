package com.lsy.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lsy.storage.SPUtil;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    private Object mContext;
    private int mRequestCode = 0x11;
    private PermissionCallback mCallback;
    private String[] mPermissions;

    private boolean mNeedResume = false;


    public PermissionUtil(Object context) {
        mContext = context;
    }

    public void requestPermissions(String[] permissions, boolean fourse,
                                   PermissionCallback callback) {
        requestPermissions(permissions, 0x11, fourse, callback);
    }

    public void requestPermissions(String[] permissions,
                                   PermissionCallback callback) {
        requestPermissions(permissions, 0x11, false, callback);
    }

    /**
     * 请求权限处理
     *
     * @param requestCode 请求码
     * @param permissions 需要请求的权限
     * @param callback    结果回调
     */
    public void requestPermissions(String[] permissions, int requestCode, boolean fourse,
                                   PermissionCallback callback) {
        mCallback = callback;
        mRequestCode = requestCode;
        mPermissions = permissions;
        checkCallingObjectSuitability(mContext); /* 检查是否正确 */
        if (!isOverMarshmallow() || checkPermissions(getContext(mContext), permissions)) {
            /* 所有申请的权限通过，执行这里;小于 23 的直接通过 */
            if (mCallback != null)
                mCallback.onPermissionGranted();
            return;
        }
        StringBuilder key = new StringBuilder();
        for (String s : permissions) {
            key.append(s);
        }
        if (!fourse) {
            /* 如果不是强制的，就需要判断之前是否已经判断过一次了 */
            if (SPUtil.getIntPub(key.toString(), 0) == 1) {
                if (mCallback != null)
                    mCallback.onCancel();
                return;
            }
        }
        /* 有还没有申请的权限，在这里再申请一次 */
        List<String> deniedPermissions = getDeniedPermissions(getContext(mContext), permissions);
        if (deniedPermissions.size() > 0) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).requestPermissions(permissions, requestCode);
            } else if (mContext instanceof android.app.Fragment) {
                ((android.app.Fragment) mContext).requestPermissions(permissions, requestCode);
            } else if (mContext instanceof Fragment) {
                ((Fragment) mContext).requestPermissions(permissions, requestCode);
            }
            SPUtil.putPub(key.toString(), 1);
        }
    }

    /**
     * 请求权限结果，对应onRequestPermissionsResult()方法。
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == mRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (mCallback != null)
                    mCallback.onPermissionGranted();
            } else {
                if (mCallback != null) {
                    /* 部分权限无法使用 */
                    if (mCallback.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                        return;
                    }
                    mCallback.onCancel();
                }
            }
        }
    }

    /**
     * 显示提示对话框
     */
    public void showTipsDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，部分功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback != null) {
                            mCallback.onCancel();
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings(context);
            }
        }).show();
    }

    /**
     * 显示提示对话框
     */
    public void showTipsDialog(final Context context, String tip) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage(tip)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback != null) {
                            mCallback.onCancel();
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings(context);
            }
        }).show();
    }

    public void onResume() {
        if (mNeedResume && mCallback != null) {
            mNeedResume = false;
            if (!isOverMarshmallow() || checkPermissions(getContext(mContext), mPermissions)) {
                /* 所有申请的权限通过，执行这里;小于 23 的直接通过 */
                mCallback.onPermissionGranted();
            } else {
                mCallback.onCancel();
            }
        }
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
        mNeedResume = true;
    }

    /**
     * 验证权限是否都已经授权
     */
    private boolean verifyPermissions(int[] grantResults) {
        // 如果请求被取消，则结果数组为空
        if (grantResults.length <= 0)
            return false;

        // 循环判断每个权限是否被拒绝
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限列表中所有需要授权的权限
     *
     * @param context     上下文
     * @param permissions 权限列表
     * @return
     */
    private List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 检查所传递对象的正确性
     *
     * @param object 必须为 activity or fragment
     */
    private void checkCallingObjectSuitability(Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;

        if (!(isActivity || isSupportFragment || isAppFragment)) {
            throw new IllegalArgumentException(
                    "Caller must be an Activity or a Fragment");
        }
    }


    /**
     * 检查所有的权限是否已经被授权
     *
     * @param permissions 权限列表
     * @return
     */
    private boolean checkPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取上下文
     */
    private Context getContext(Object object) {
        Context context;
        if (object instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else {
            context = (Activity) object;
        }
        return context;
    }

    /**
     * 判断当前手机API版本是否 >= 6.0
     */
    private boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
