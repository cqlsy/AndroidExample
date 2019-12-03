package com.lsy.example.ui

import android.os.Bundle
import com.lsy.base.FMActivity
import com.lsy.bus.AppBus
import com.lsy.utils.LogUtil
import com.lsy.widget.loading.ILoadingIndicator

abstract class BaseActivity : FMActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppBus.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppBus.unregister(this)
    }

    override fun handleException(throwable: Throwable) {
        LogUtil.d("Exception", throwable.message)
    }

    override fun initLoading(): ILoadingIndicator? {
        return super.initLoading()
    }
}