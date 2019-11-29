package com.lsy.base

class FMContract {

    companion object {
        fun <T : FMView> attachView(view: T) {
            /* 对 presenter 参数初始化 */
            val mPresenters = ArrayList<FMPresenter<T>?>()
            val presenter = view.getPresenter<T>()
            if (presenter != null) {
                mPresenters.add(presenter)
            }
            val presenters = view.getPresenters<T>()
            if (presenters != null) {
                mPresenters.addAll(presenters)
            }
            for (p in mPresenters) {
                p?.attachView(view)
            }
        }

        fun <T : FMView> detachView(view: T) {
            val mPresenters = ArrayList<FMPresenter<T>?>()
            val presenter = view.getPresenter<T>()
            if (presenter != null) {
                mPresenters.add(presenter)
            }
            val presenters = view.getPresenters<T>()
            if (presenters != null) {
                mPresenters.addAll(presenters)
            }
            for (p in mPresenters) {
                p?.detachView()
            }
        }
    }


}