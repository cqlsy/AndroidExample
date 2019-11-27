package com.lsy.framework.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

abstract class FMFragment<T : FMView> : Fragment(), FMView {

    private lateinit var mViewBinding: ViewDataBinding
    var mPresenters = ArrayList<FMPresenter<T>>()
    // loading count
    private var mLoadingCount: Int = 0
    /* RXjava 管理 */
    private val mCompositeDisposable = CompositeDisposable()
    var mIsInit = false
    // loading
    private var mLoadingDialog: ILoadingIndicator? = null
    var mVisibleInVp = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initPresenter()
        mViewBinding = DataBindingUtil.inflate(
            inflater, getLayoutId(), container, false
        )
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(mViewBinding)
        initViews()
        initPresenter()
        mIsInit = true
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun getLoadingDialog(): ILoadingIndicator? {
        return null
    }

    abstract fun initBinding(viewDataBinding: ViewDataBinding)

    abstract fun initViews()

    open fun getPresenter(): FMPresenter<T>? {
        return null
    }

    open fun getPresenters(): ArrayList<FMPresenter<T>>? {
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        for (p in mPresenters) {
            p.detachView()
        }
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
    }

    private fun initPresenter() {
        val presenter = getPresenter()
        if (presenter != null) {
            mPresenters.add(presenter)
        }
        val presenters = getPresenters()
        if (presenters != null) {
            mPresenters.addAll(presenters)
        }
        for (p in mPresenters) {
            p.attachView(this)
        }
    }

    override fun showLoading() {
        if (context is Activity) {
            (context as Activity).runOnUiThread {
                mLoadingCount++
                if (mLoadingDialog == null) {
                    mLoadingDialog = getLoadingDialog()
                }
                if (mLoadingDialog == null) {
                    mLoadingDialog =
                        DefaultLoadingDialog(context)
                }
                mLoadingDialog?.showLoading()
            }
        } else {
            context.run {
                mLoadingCount++
                if (mLoadingDialog == null) {
                    mLoadingDialog = getLoadingDialog()
                }
                if (mLoadingDialog == null) {
                    mLoadingDialog =
                        DefaultLoadingDialog(context)
                }
                mLoadingDialog?.showLoading()
            }
        }
    }

    override fun hideLoading() {
        if (context is Activity) {
            (context as Activity).runOnUiThread {
                mLoadingCount--
                if (mLoadingCount <= 0) {
                    mLoadingDialog?.dismissLoading()
                    mLoadingCount = 0
                }
            }
        } else {
            context.run {
                mLoadingCount--
                if (mLoadingCount <= 0) {

                    mLoadingDialog?.dismissLoading()
                    mLoadingCount = 0
                }
            }
        }
    }

    override fun <O> bindLoading(): ObservableTransformer<O, O> {
        return bindLoading(true)
    }

    override fun <O> bindLoading(need: Boolean): ObservableTransformer<O, O> {
        return ObservableTransformer {
            if (!need) {
                it
            } else {
                it.doOnSubscribe(Consumer {
                    showLoading()
                }).doOnComplete(Action {
                    hideLoading()
                }).doOnError(Consumer {
                    hideLoading()
                }).doOnDispose {
                    hideLoading()
                }
            }
        }
    }

    override fun handleException(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onResume() {
        super.onResume()
        if (mVisibleInVp && mIsInit) {
            /* 表示可见 */
            onFragmentVisibleOnVp()
        }
    }

    /* 这个方法暂不处理，这个只在调用 hideFragment/showFragment 才会回调 */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

    }

    /**
     * 当fragment可见的时候 会调用这个方法; 当fragment 还没有生成的时候，不会回调这个方法
     */
    open fun onFragmentVisibleOnVp() {
        /* do nothing ; 表明当前的 fragment可见了*/
    }


    /**
     * Fragment可见性总结：

    1，  onHiddenChanged(boolean hidden)

    （1）只在调用hideFragment/showFragment后才会调用，PagerAdapter方式中不会调用。

    （2）对应的isHidden()方法，只对show/hide方式有用。

    （3）show/hide触发时只针对当前fragment有用，对其子fragment没有作用，即子fragment不会回调onHiddenChanged方法。


    2，  setUserVisibleHint(booleanisVisibleToUser)

    （1）只在PagerAdapter方式中回调调用。

    （2） Fragment的PagerAdapter包括FragmentStatePagerAdapter和FragmentPagerAdapter两个子抽象类。


    3，  Fragment的isVisible()判断方法

    （1）在PagerAdapter方式中不准确，即fragment不是PagerAdapter当前显示的fragment时也会是true。

    ---------------------
    作者：BangKey
    来源：CSDN
    原文：https://blog.csdn.net/dbpggg/article/details/80818488
    版权声明：本文为博主原创文章，转载请附上博文链接！
     */
}