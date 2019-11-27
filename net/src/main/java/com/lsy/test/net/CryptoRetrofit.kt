package com.lsy.test.net

import android.annotation.SuppressLint
import com.lsy.net.Helper
import com.lsy.net.cryptoParams.ReqMap
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class CryptoRetrofit : Helper() {

    /**
     *  参数加密
     */
    private fun signParams(params: ReqMap) {
        params["appid"] = "muhxi9gpsgkowhrd"
        params["sign"] = getSign(params)
    }

    @SuppressLint("DefaultLocale")
    private fun getSign(map: ReqMap): String {
        val sb = StringBuffer()
        val list = ArrayList<Map.Entry<String, Any>>(map.entries)
        list.sortWith(Comparator { o1, o2 ->
            //升序排序
            o1.key.compareTo(o2.key)
        })
        for (i in list.indices) {
            val entry = list[i]
            val key1 = entry.key
            val value = entry.value
            if ("" != value && "0" != value && "sign" != key1 && "key" != key1) {
                sb.append("$key1=$value&")
            }
        }
        //拼接成StringA
        sb.deleteCharAt(sb.length - 1)
        sb.append("cacfcc014d8b6606")//StringA最后加上key
        return getMessageDigest(sb.toString()).toUpperCase()
    }

    private fun getMessageDigest(text: String): String {
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest: ByteArray = instance.digest(text.toByteArray())
            val sb = StringBuffer()
            for (b in digest) {
                //获取低八位有效值
                val i: Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    //如果是一位的话，补0
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    ////////////////////////////////////////////  网络请求方法  ////////////////////////////////////////////

    fun login(account: String, password: String): Observable<String> {
        val params = ReqMap()
        params["phoneNumber"] = account
        params["password"] = password
        params["port_type"] = "1"
        params["device_sn"] = "device_sn"
        signParams(params)
        return getRetrofitService()
            .post(APIPath.PATH_LOGIN, params)
            .subscribeOn(Schedulers.io())
    }


}