package com.lsy.example

import com.lsy.base.FMApp

class App : FMApp() {

    override fun init() {
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }


}