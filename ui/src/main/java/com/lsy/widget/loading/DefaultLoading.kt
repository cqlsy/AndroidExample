package com.lsy.widget.loading

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.WindowManager
import com.lsy.framework.R
import io.reactivex.ObservableTransformer

class DefaultLoading(private val mActivity: Activity?) : ILoadingIndicator {

    private var mDialog: Dialog? = null
    private var mCount: Int = 0

    init {
        if (mActivity != null) {
            mDialog = Dialog(mActivity, R.style.YYDialog_LoadingDialog)
            val window = mDialog?.window
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
            )
            val view = LayoutInflater.from(mActivity).inflate(
                R.layout.fm_dialog_default_loading,
                null
            )
            //设置加载的view
            mDialog?.setContentView(view)
            //设置点击外面不消失
            mDialog?.setCanceledOnTouchOutside(false)
        }
    }

    override fun showLoading() {
        mCount++
        if (mCount > 0) {
            mActivity?.runOnUiThread {
                mDialog?.show()
            }
        }
    }

    override fun dismissLoading() {
        mCount--
        if (mCount <= 0) {
            mActivity?.runOnUiThread {
                mDialog?.dismiss()
            }
        }
    }

    override fun <O> bindLoading(): ObservableTransformer<O, O> {
        return ObservableTransformer {
            it.doOnSubscribe {
                showLoading()
            }.doOnComplete {
                dismissLoading()
            }.doOnError {
                dismissLoading()
            }.doOnDispose {
                dismissLoading()
            }
        }
    }
}