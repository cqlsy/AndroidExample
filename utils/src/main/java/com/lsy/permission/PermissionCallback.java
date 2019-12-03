package com.lsy.permission;

public interface PermissionCallback {

    /**
     * 权限认证或者申请通过的时候执行回调
     */
    void onPermissionGranted();

    /**
     * 如果在这里处理了返回true；返回false的时候将会继续执行 onCancel()
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return
     */
    boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    /**
     * 点击取消按钮;
     * <p>
     * 返回页面，或者权限没有给予的时候的执行回调
     */
    void onCancel();

}
