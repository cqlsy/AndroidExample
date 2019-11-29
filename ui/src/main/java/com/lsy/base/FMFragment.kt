package com.lsy.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.lsy.utils.LogUtil
import com.lsy.widget.loading.DefaultLoading
import com.lsy.widget.loading.ILoadingIndicator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class FMFragment : Fragment(), FMView {

    private lateinit var mViewBinding: ViewDataBinding
    /* RXjava 管理 */
    private val mCompositeDisposable = CompositeDisposable()
    // loading
    private var mLoadingDialog: ILoadingIndicator? = null

    private var mIsInit = false
    var mVisibleInVp = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FMContract.attachView(this)
        mViewBinding = DataBindingUtil.inflate(
            inflater, getLayoutId(), container, false
        )
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(mViewBinding)
        initViews()
        mIsInit = true
    }

    override fun onResume() {
        super.onResume()
        if (mVisibleInVp && mIsInit) {
            /* 表示可见 */
            onFragmentVisibleOnVp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        FMContract.detachView(this)
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initBinding(viewDataBinding: ViewDataBinding)

    open fun initLoading(): ILoadingIndicator? {
        return null
    }

    abstract fun initViews()

    override fun <T : FMView> getPresenter(): FMPresenter<T>? {
        return null
    }

    override fun <T : FMView> getPresenters(): ArrayList<FMPresenter<T>?>? {
        return null
    }

    override fun getLoadingIndicator(): ILoadingIndicator {
        if (mLoadingDialog == null) {
            mLoadingDialog = initLoading()
            if (mLoadingDialog == null) {
                mLoadingDialog = DefaultLoading(activity)
            }
        }
        return mLoadingDialog as ILoadingIndicator
    }

    override fun handleException(throwable: Throwable) {
        LogUtil.d("Exception", throwable.message)
    }

    override fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
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