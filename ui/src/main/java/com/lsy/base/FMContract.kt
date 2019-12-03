package com.lsy.base

class FMContract {

    companion object {
        fun <T : FMView> attachView(view: T) {
            val presenters = view.getPresenters()
            for (p in presenters) {
                p?.attachView(view)
            }
        }

        fun <T : FMView> detachView(view: T) {
            val presenters = view.getPresenters()
            for (p in presenters) {
                p?.detachView()
            }
        }

        fun <T : FMView> start(view: T) {
            val presenters = view.getPresenters()
            for (p in presenters) {
                p?.start()
            }
        }
    }
}