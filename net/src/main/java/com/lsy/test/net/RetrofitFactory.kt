package com.lsy.test.net

import android.util.Log
import com.lsy.net.Helper
import com.lsy.net.NetManager
import com.lsy.net.log.ILog
import com.lsy.net.log.LogType

/**
 * 加密的设置
 */
class RetrofitFactory {

    companion object {
        fun init() {
            /* base url 在这里调用 */
            NetManager.init(object : ILog {
                override fun log(type: LogType, msg: String) {
                    when (type) {
                        LogType.DeBug -> {
                            Log.d("RetrofitFactory", msg)
                        }
                        LogType.Error -> {
                            Log.e("RetrofitFactory", msg)
                        }
                        LogType.Info -> {
                            Log.i("RetrofitFactory", msg)
                        }
                    }
                }
            }, "https://www.fyzcq.com")
        }

        // 需要加密的使用这个区请求
        val mCryptoHelper: CryptoRetrofit = CryptoRetrofit()
        // 不需要加密处理的请求 helper
        val mCommonHelper: Helper = Helper()
    }

}