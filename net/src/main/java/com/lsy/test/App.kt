package com.lsy.test

import android.app.Application
import com.lsy.test.net.RetrofitFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        RetrofitFactory.init()
    }


}