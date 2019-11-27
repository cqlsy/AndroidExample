package com.lsy.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lsy.net.R
import com.lsy.test.net.RetrofitFactory
import okhttp3.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val disposable = RetrofitFactory.mCryptoHelper.login("13072302927", "zaq1xsw2")
            .subscribe({
                Log.d(javaClass.simpleName, it)
            }, {
                Log.e(javaClass.simpleName, it.message as String)
            })
    }
}
