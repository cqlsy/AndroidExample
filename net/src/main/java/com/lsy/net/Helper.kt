package com.lsy.net

import com.lsy.net.converter.StringConverterFactory
import com.lsy.net.filedownupload.ProgressDownLoadResponseBody
import com.lsy.net.filedownupload.ProgressUpLoadRequestBody
import com.lsy.net.filedownupload.UpAndDownListener
import com.lsy.net.log.LogType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * 这个类不能直接使用，这是基类，
 * 网络请求需要自己去实现
 */
open class Helper {

    private var mOkHttpClientDefault: OkHttpClient? = null
    private var mRetrofitService: Service? = null

    /**
     * 外部调用的方法
     * 需要上传、下载需要进程通知需要调用这个;每次获取会有一个新的对象
     */
    protected fun getUpAndDownRetrofitService(upAndDownListener: UpAndDownListener?): Service {
        return getRetrofitService(getLoadClient(upAndDownListener))
    }

    /**
     * 外部调用的方法
     *  一般我们使用这个
     */
    protected fun getRetrofitService(): Service {
        if (mRetrofitService == null) {
            mRetrofitService = getRetrofitService(getOkHttpClient())
        }
        return mRetrofitService as Service
    }

    /**
     * 如果有自己的 client，在这里实现
     */
    protected open fun getOkHttpClient(): OkHttpClient? {
        return null
    }

    /**
     * 获取 默认的, 这个最好不能再其他的地方修改
     */
    protected fun getDefaultClient(): OkHttpClient? {
        if (mOkHttpClientDefault == null) {
            mOkHttpClientDefault = getClient(Interceptor { chain ->
                /* 参数打印 */
                val request = chain.request()
                val requestBody = request.body()/* 这个没有动 */
                /* 打印请求相关的信息 */
                NetManager.getInstance()
                    .log(
                        LogType.DeBug,
                        "usl is : ${request.url()}; method is ${request.method()}; body is ${request.body().toString()}"
                    )
                /* 对 body的处理 */
                val signedRequest = request.newBuilder()
                    .header("Content-Length", requestBody?.contentLength().toString())
                    .addHeader("User-Agent", "android")/* header的添加 */
                    .method(request.method(), requestBody)
                    .build()
                chain.proceed(signedRequest)
            })
        }
        return mOkHttpClientDefault
    }

    private fun getLoadClient(upAndDownListener: UpAndDownListener?): OkHttpClient? {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        //设置超时
        builder.connectTimeout(12, TimeUnit.SECONDS)
        if (upAndDownListener != null) {
            builder.addInterceptor {
                val originRequest = it.request()
                val originResponse = it.proceed(
                    originRequest.newBuilder()
                        .method(
                            originRequest.method(),
                            ProgressUpLoadRequestBody(originRequest.body(), upAndDownListener)
                        )
                        .build()
                )
                originResponse.newBuilder()
                    .body(ProgressDownLoadResponseBody(originResponse.body(), upAndDownListener))
                    .build()
            }
        }
        //错误重连
        builder.retryOnConnectionFailure(true)
        return builder.build()
    }


    /**
     * 获取 默认的, 这个最好不能再其他的地方修改
     */
    private fun getClient(intIterator: Interceptor?): OkHttpClient? {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        //设置超时
        builder.connectTimeout(12, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        builder.addInterceptor(intIterator as Interceptor)
        //错误重连
        builder.retryOnConnectionFailure(true)
        return builder.build()
    }


    /**
     * @params client 当传入的为空的时候：将会使用默认的client
     */
    private fun getRetrofitService(client: OkHttpClient?): Service {
        return Retrofit.Builder()
            .baseUrl(NetManager.getInstance().mBaseUrl)
            .client(
                client ?: getDefaultClient() as OkHttpClient
            )
            .addConverterFactory(StringConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            /* 这里指向我们 的请求接口定义类 */
            .create(Service::class.java)
    }


}