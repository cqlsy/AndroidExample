package com.lsy.widget.loading

import io.reactivex.ObservableTransformer

interface ILoadingIndicator {

    fun showLoading()

    fun dismissLoading()

    fun <O> bindLoading(): ObservableTransformer<O, O>
}