package com.lsy.base

import android.app.Application
import com.lsy.storage.SPUtil
import com.lsy.utils.LogUtil
import com.lsy.utils.Utils

/**
 * 建议做法, 继承该类的 app 中不要包含第三方初始化，
 * 引入 ConfigManager 和 ParamsManager 来做初始化，保证 application的干净整洁
 */
open class FMApp : Application() {

    override fun onCreate() {
        super.onCreate()
        LifeCircleMgr.getInstance(this).init()
        initConfig()
    }

    protected open fun initConfig() {
        /* log 初始化 */
        Utils.getInstance().init(this)
        SPUtil.initKey(this)
        LogUtil.init(true, "Example", LogUtil.DEBUG)
    }
}