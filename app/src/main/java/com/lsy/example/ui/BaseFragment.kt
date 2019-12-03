package com.lsy.example.ui

import android.os.Bundle
import com.lsy.base.FMFragment
import com.lsy.bus.AppBus
import com.lsy.utils.LogUtil

abstract class BaseFragment : FMFragment() {

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
}