package com.lsy.widget.recycle

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException

abstract class BaseAdapter(var mContext: Context) : RecyclerView.Adapter<BaseViewHolder>() {


   final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var layoutId: Int = getLayoutId(viewType)
        if (layoutId != 0) {
            return BaseViewHolder(
                DataBindingUtil.inflate<ViewDataBinding>(
                    LayoutInflater.from(mContext)
                    , layoutId, parent, false
                )
            )
        }
        layoutId = getItemView(viewType)
        if (layoutId != 0) {

            return BaseViewHolder(
                LayoutInflater.from(mContext).inflate(
                    layoutId, parent, false
                )
            )
        }
        throw RuntimeException("Please set ViewHolder")
    }

    open fun getLayoutId(viewType: Int): Int {
        return 0
    }

    open fun getItemView(viewType: Int): Int {
        return 0
    }
}