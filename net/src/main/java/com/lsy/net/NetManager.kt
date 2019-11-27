package com.lsy.net

import com.lsy.net.log.ILog
import com.lsy.net.log.LogType

/**
 * 使用这个该 lib的时候，需要初始化
 */
class NetManager(private var mLog: ILog?, var mBaseUrl: String) {

    fun log(type: LogType, msg: String) {
        mLog?.log(type, msg)
    }

    companion object {
        private var mInstance: NetManager? = null

        fun getInstance(): NetManager {
            if (mInstance == null) {
                throw RuntimeException("please init NetManage first!")
            }
            return mInstance as NetManager
        }

        /**
         * 要么在第一次使用网络请求时前调用，要么在 application调用
         */
        fun init(log: ILog, baseUrl: String) {
            mInstance = NetManager(log, baseUrl)
        }
    }
}