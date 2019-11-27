package com.lsy.net.converter

import com.lsy.net.NetManager
import com.lsy.net.log.LogType
import okhttp3.ResponseBody
import retrofit2.Converter

class StringConverter : Converter<ResponseBody, String> {
    override fun convert(value: ResponseBody): String {
        val responseString = value.string()
        NetManager.getInstance().log(LogType.DeBug, "http resp : $responseString")
        value.use {
            return responseString
        }
    }
}