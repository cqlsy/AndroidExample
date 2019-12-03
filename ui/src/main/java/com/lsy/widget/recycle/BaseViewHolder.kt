package com.lsy.widget.recycle

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder : RecyclerView.ViewHolder {

    var mBinding: ViewDataBinding? = null

    constructor(
        mBinding: ViewDataBinding
    ) : super(mBinding.root) {
        this.mBinding = mBinding
    }

    constructor(view: View) : super(view)

    fun getDataBinding(): ViewDataBinding {
        return mBinding as ViewDataBinding
    }

}