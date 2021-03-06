@file:Suppress("UNCHECKED_CAST")

package com.lsy.base

import com.lsy.widget.loading.ILoadingIndicator
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable

/* 连接 activity 和 presenter 的接口 */
interface FMView {

    fun  getPresenters(): ArrayList<IPresenter?>

    fun getLoadingIndicator(): ILoadingIndicator

    /**
     * 处理错误的情况
     */
    fun handleException(throwable: Throwable)


    fun addDisposable(disposable: Disposable)
}