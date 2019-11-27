package com.lsy.bus;


/**
 * Created by adu on 2017/5/20.
 * RxBus单例
 */

public class AppBus {

    private static class InstanceHolder {
        private static final Bus instance = RxBus.get();
    }

    public static Bus getInstance() {
        return InstanceHolder.instance;
    }

    public static void post(Object e) {
        getInstance().post(e);
    }

    public static void register(Object e) {
        getInstance().register(e);
    }

    public static void unregister(Object e) {
        getInstance().unregister(e);
    }
}
