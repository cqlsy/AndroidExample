package com.lsy.example.ui.main

import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.lsy.base.IPresenter
import com.lsy.example.R
import com.lsy.example.adapter.SectionAdapter
import com.lsy.example.databinding.ActivityMainBinding
import com.lsy.example.entity.SectionEntity
import com.lsy.example.ui.BaseActivity
import com.lsy.example.ui.main.presenter.MainPresenter
import com.lsy.example.ui.main.view.MainView
import com.lsy.utils.DisplayUtil
import com.lsy.widget.recycle.ItemDecorationDivider

class MainActivity : BaseActivity(), MainView {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mPresenter: MainPresenter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initPresenter(presenters: ArrayList<IPresenter?>) {
        mPresenter = MainPresenter()
        presenters.add(mPresenter)
    }

    override fun initBinding(viewDataBinding: ViewDataBinding) {
        mBinding = viewDataBinding as ActivityMainBinding
    }

    override fun initViews() {
        mBinding.rv.addItemDecoration(
            ItemDecorationDivider(
                DisplayUtil.dp2px(this, 10f),
                ContextCompat.getColor(this, R.color.black)
            )
        )
        val data = ArrayList<SectionEntity>()
        for (i in 0..100) {
            data.add(SectionEntity("RecycleView相关", i))
        }
        mBinding.rv.layoutManager = LinearLayoutManager(this)
        mBinding.rv.adapter = SectionAdapter(this, data)
    }

    override fun getDoubleBackString(): String {
        return "再按一次，返回退出"
    }
}
