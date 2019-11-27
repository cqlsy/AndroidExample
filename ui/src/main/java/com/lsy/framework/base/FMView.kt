@file:Suppress("UNCHECKED_CAST")

package com.lsy.framework.base

import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable

/* 连接 activity 和 presenter 的接口 */
interface FMView {

    /* 数据处理后View的展示 */
    /**
     * 需要 loading
     */
    fun showLoading()

    /**
     *
     */
    fun hideLoading()

    /**
     * loading 线程
     */
    fun <O> bindLoading(): ObservableTransformer<O, O>

    /**
     * loading 线程
     */
    fun <O> bindLoading(need: Boolean): ObservableTransformer<O, O>

    /**
     * 处理错误的情况
     */
    fun handleException(throwable: Throwable)


    fun addDisposable(disposable: Disposable)
}