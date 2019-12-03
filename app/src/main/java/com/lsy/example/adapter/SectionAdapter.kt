package com.lsy.example.adapter

import android.content.Context
import com.lsy.example.R
import com.lsy.example.databinding.ItemSectionBinding
import com.lsy.example.entity.SectionEntity
import com.lsy.widget.recycle.BaseAdapter
import com.lsy.widget.recycle.BaseViewHolder

class SectionAdapter(mContext: Context, private val mData: ArrayList<SectionEntity>) :
    BaseAdapter(mContext) {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_section
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: ItemSectionBinding = holder.mBinding as ItemSectionBinding
        binding.tv.text = mData[position].sectionName

    }

}