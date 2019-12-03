package com.lsy.base

import android.app.Application
import androidx.multidex.MultiDex
import com.lsy.storage.SPUtil
import com.lsy.utils.LogUtil
import com.lsy.utils.Utils

/**
 * 建议做法, 继承该类的 app 中不要包含第三方初始化，
 * 引入 ConfigManager 和 ParamsManager 来做初始化，保证 application的干净整洁
 */
abstract class FMApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        LifeCircleMgr.getInstance(this).init()
        initConfig()
        init()
    }

    /* 这里是自己的库的引用的默认实现，也可以自己重新去试下 */
    protected open fun initConfig() {
        /* log 初始化 */
        Utils.getInstance().init(this)
        SPUtil.initKey(this)
        LogUtil.init(
            isDebug(), "Example",
            if (isDebug()) LogUtil.ERROR else LogUtil.DEBUG
        )
    }

    abstract fun init()

    abstract fun isDebug(): Boolean
}