package com.lsy.utils;

/**
 * 跳转到 第三方地图软件，用于定位位置
 * 1:   腾讯 https://lbs.qq.com/uri_v1/guide-mobile-poiMarker.html
 * 2:   百度 http://lbsyun.baidu.com/index.php?title=uri/api/android
 * 3:   高德 https://lbs.amap.com/api/amap-mobile/guide/android/marker
 */
public class MapUtil {

//    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
//    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
//    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名
//
//    /**
//     * 检查地图应用是否安装
//     *
//     * @return
//     */
//    public static boolean isGdMapInstalled() {
//        return isInstallPackage(PN_GAODE_MAP);
//    }
//
//    public static boolean isBaiduMapInstalled() {
//        return isInstallPackage(PN_BAIDU_MAP);
//    }
//
//    public static boolean isTencentMapInstalled() {
//        return isInstallPackage(PN_TENCENT_MAP);
//    }
//
//    private static boolean isInstallPackage(String packageName) {
//        return new File("/data/data/" + packageName).exists();
//    }
//
//    /**
//     * 百度转高德
//     *
//     * @param bd_lat
//     * @param bd_lon
//     * @return
//     */
//    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
//        double[] gd_lat_lon = new double[2];
//        double PI = 3.14159265358979324 * 3000.0 / 180.0;
//        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
//        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
//        gd_lat_lon[0] = z * Math.cos(theta);
//        gd_lat_lon[1] = z * Math.sin(theta);
//        return gd_lat_lon;
//    }
//
//    /**
//     * 高德、腾讯转百度
//     *
//     * @param gd_lon
//     * @param gd_lat
//     * @return
//     */
//    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
//        double[] bd_lat_lon = new double[2];
//        double PI = 3.14159265358979324 * 3000.0 / 180.0;
//        double x = gd_lon, y = gd_lat;
//        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
//        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
//        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
//        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
//        return bd_lat_lon;
//    }
//
//    /**
//     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
//     * 即 百度 转 谷歌、高德
//     *
//     * @param latLng
//     * @returns 使用此方法需要下载导入百度地图的BaiduLBS_Android.jar包
//     */
//    public static LatLng BD09ToGCJ02(LatLng latLng) {
//        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
//        double x = latLng.longitude - 0.0065;
//        double y = latLng.latitude - 0.006;
//        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
//        double gg_lat = z * Math.sin(theta);
//        double gg_lng = z * Math.cos(theta);
//        return new LatLng(gg_lat, gg_lng);
//    }
//
//    /**
//     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
//     * 即谷歌、高德 转 百度
//     *
//     * @param latLng
//     * @returns 需要百度地图的BaiduLBS_Android.jar包
//     */
//    public static LatLng GCJ02ToBD09(LatLng latLng) {
//        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
//        double z = Math.sqrt(latLng.longitude * latLng.longitude + latLng.latitude * latLng.latitude) + 0.00002 * Math.sin(latLng.latitude * x_pi);
//        double theta = Math.atan2(latLng.latitude, latLng.longitude) + 0.000003 * Math.cos(latLng.longitude * x_pi);
//        double bd_lat = z * Math.sin(theta) + 0.006;
//        double bd_lng = z * Math.cos(theta) + 0.0065;
//        return new LatLng(bd_lat, bd_lng);
//    }
//
//    /**
//     * 打开高德地图导航功能
//     *
//     * @param context
//     * @param dlat    终点纬度
//     * @param dlon    终点经度
//     * @param dname   终点名称 必填
//     */
//    public static void openGaoDeNavi(Context context, double dlat, double dlon, String dname) {
//        String uriString = null;
//        dname = dname == null ? "" : dname;
//        uriString = "androidamap://viewMap?sourceApplication=amap" + "&lat=" + dlat +
//                "&lon=" + dlon +
//                "&poiname=" + dname +
//                "&dev=0";
//        Intent intent = new Intent(Intent.ACTION_DEFAULT);
//        intent.setPackage(PN_GAODE_MAP);
//        intent.setData(Uri.parse(uriString));
//        context.startActivity(intent);
//    }
//
//    /**
//     * 打开腾讯地图
//     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
//     *
//     * @param context
//     * @param dlat    终点纬度
//     * @param dlon    终点经度
//     * @param dname   终点名称 必填
//     *                驾车：type=drive，policy有以下取值
//     *                0：较快捷
//     *                1：无高速
//     *                2：距离
//     *                policy的取值缺省为0
//     *                &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
//     */
//    public static void openTencentMap(Context context, double dlat, double dlon, String dname) {
//        String uriString = null;
//        uriString = "qqmap://map/marker?referer=zhongshuo" + "&marker=coord:" +
//                dlat +
//                "," +
//                dlon +
//                ";" +
//                "title:" +
//                dname +
//                ";" +
//                "addr:" +
//                dname;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setPackage(PN_TENCENT_MAP);
//        intent.setData(Uri.parse(uriString));
//        context.startActivity(intent);
//    }
//
//    /**
//     * @param context
//     * @param dlat    终点纬度
//     * @param dlon    终点经度
//     */
//    public static void openBaiDuNavi(Context context, double dlat, double dlon) {
//        String uriString = null;
//        //终点坐标转换
//        uriString = "baidumap://map/geocoder?" + "location=" +
//                dlat +
//                "," +
//                dlon +
//                "&src=" +
//                BuildConfig.APPLICATION_ID;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setPackage(PN_BAIDU_MAP);
//        intent.setData(Uri.parse(uriString));
//        context.startActivity(intent);
//    }
//
//    public static void showDialog(Context context, double dlat, double dlon, String dname) {
//        View bottomView = View.inflate(context, R.layout.dialog_open_map, null);
//        TextView tvBd = (TextView) bottomView.findViewById(R.id.tv_bd);
//        TextView tvGd = (TextView) bottomView.findViewById(R.id.tv_gd);
//        TextView tvTx = (TextView) bottomView.findViewById(R.id.tv_tx);
//        TextView tvCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);
//        Dialog mDialog = new Dialog(context, R.style.YYDialog_BottomDialog);
//        mDialog.setContentView(bottomView);
//        Window window = mDialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.AnimBottom);
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(params);
//        View.OnClickListener clickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                switch (view.getId()) {
//                    case R.id.tv_bd:
//                        if (isBaiduMapInstalled()) {
//                            openBaiDuNavi(context, dlat, dlon);
//                        } else {
//                            ActivityUIHelper.toast("本机未安装百度地图，请重新选择", context);
//                            return;
//                        }
//                        break;
//                    case R.id.tv_gd:
//                        if (isGdMapInstalled()) {
//                            double[] lat_lon = bdToGaoDe(dlat, dlon);
//                            openGaoDeNavi(context,
//                                    lat_lon[1], lat_lon[0], dname);
//                        } else {
//                            ActivityUIHelper.toast("本机未安装高德地图，请重新选择", context);
//                            return;
//                        }
//                        break;
//                    case R.id.tv_tx:
//                        if (isTencentMapInstalled()) {
//                            double[] lat_lon = bdToGaoDe(dlat, dlon);
//                            openTencentMap(context,
//                                    lat_lon[1], lat_lon[0], dname);
//                        } else {
//                            ActivityUIHelper.toast("本机未安装腾讯地图，请重新选择", context);
//                            return;
//                        }
//                        break;
//                }
//                mDialog.dismiss();
//            }
//        };
//        tvBd.setOnClickListener(clickListener);
//        tvGd.setOnClickListener(clickListener);
//        tvTx.setOnClickListener(clickListener);
//        tvCancel.setOnClickListener(clickListener);
//        mDialog.show();
//    }
}

/*
---------------------
作者：i996 
来源：CSDN 
原文：https://blog.csdn.net/i996573526/article/details/82117862 
版权声明：本文为博主原创文章，转载请附上博文链接！*/
