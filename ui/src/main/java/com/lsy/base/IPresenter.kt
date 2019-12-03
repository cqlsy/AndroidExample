package com.lsy.base

interface IPresenter {

    fun attachView(view: FMView)

    /**
     * 解除View
     * 释放View引用
     */
    fun detachView()

    fun start()
}