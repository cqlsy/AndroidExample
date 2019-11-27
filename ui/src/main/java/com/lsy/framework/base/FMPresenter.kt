package com.lsy.framework.base

import io.reactivex.disposables.Disposable

/* 数据处理 */
abstract class FMPresenter<T : FMView> {

    private var mView: T? = null

    /**
     * 绑定View
     * 设置View引用
     *
     * @param view
     */
    fun attachView(view: FMView) {
        mView = view as T
    }

    /**
     * 解除View
     * 释放View引用
     */
    fun detachView() {
        mView = null
    }

    /**
     * 添加成功，就交给 activity 或者 fragment 去处理
     * 否者 直接断开
     */
    fun addDisposable(disposable: Disposable) {
        try {
            mView!!.addDisposable(disposable)
        } catch (e: Exception) {
            disposable.dispose()
        }
    }
}