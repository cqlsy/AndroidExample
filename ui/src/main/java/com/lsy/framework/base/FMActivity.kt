package com.lsy.framework.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lsy.framework.R
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.Utils
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper

/**
 * 当没有 presenter的时候，T 设置为 FMContract.View
 */
@Suppress("UNCHECKED_CAST")
abstract class FMActivity<T : FMView> : AppCompatActivity(), SwipeBackActivityBase, FMView {

    /* 这个写在这里是方便调用 */
    protected lateinit var mActivity: Activity
    private var mPresenters = ArrayList<FMPresenter<T>>()
    // loading count
    private var mLoadingCount: Int = 0
    // loading
    private var mLoadingDialog: ILoadingIndicator? = null
    /* RXjava 管理 */
    private val mCompositeDisposable = CompositeDisposable()

    private lateinit var mHelper: SwipeBackActivityHelper  //滑动返回

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mViewBinding: ViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        /* 设置滑动关闭 */
        if (canBack() != -1) {
            mHelper = SwipeBackActivityHelper(this)
            mHelper.onActivityCreate()
            mHelper.swipeBackLayout.setEdgeTrackingEnabled(canBack())
        }
        mActivity = this
        initBinding(mViewBinding)
        initViews()
        initPresenter()
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

    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化 Binding, 防止忘记,这里只做一些 binding有关的操作，责任分明
     */
    abstract fun initBinding(viewDataBinding: ViewDataBinding)

    /**
     * 初始化 View
     */
    abstract fun initViews()

    open fun getLoadingDialog(): ILoadingIndicator? {
        return null
    }

    open fun getPresenter(): FMPresenter<T>? {
        return null
    }

    open fun getPresenters(): ArrayList<FMPresenter<T>>? {
        return null
    }

    open fun setImmersive(): Boolean {
        return true
    }

    // ====================================  滑动退出 代码 start ====================================
    open fun canBack(): Int {
        /* SwipeBackLayout.EDGE_LEFT
         SwipeBackLayout.EDGE_RIGHT*/
        return -1
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (canBack() != -1) {
            mHelper.onPostCreate()
        }
    }

    override fun <T : View?> findViewById(id: Int): T {
        val v = super.findViewById<View>(id)
        return (if (v == null && canBack() != -1)
            mHelper.findViewById(id)
        else
            v) as T
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }
    // ====================================  滑动退出 代码 end  ====================================

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        /* todo 这里可以写启动的activity的动画 */
        overridePendingTransition(R.anim.app_slide_right_in, R.anim.app_slide_hold)
    }

    override fun finish() {
        super.finish()
        /* todo 这里可以写关闭的activity的动画 */
        overridePendingTransition(R.anim.app_slide_hold, R.anim.app_slide_right_out)
    }


    override fun handleException(throwable: Throwable) {

    }

    override fun onDestroy() {
        super.onDestroy()
        for (p in mPresenters) {
            p.detachView()
        }
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
    }

    override fun <O> bindLoading(): ObservableTransformer<O, O> {
        return bindLoading(true)
    }

    override fun <O> bindLoading(need: Boolean): ObservableTransformer<O, O> {
        return ObservableTransformer {
            if (!need) {
                it
            } else {
                it.doOnSubscribe {
                    showLoading()
                }.doOnComplete {
                    hideLoading()
                }.doOnError {
                    hideLoading()
                }.doOnDispose {
                    hideLoading()
                }
            }
        }
    }

    /**
     * 可重写实现
     */
    override fun showLoading() {
        runOnUiThread {
            mLoadingCount++
            if (mLoadingDialog == null) {
                mLoadingDialog = getLoadingDialog()
            }
            if (mLoadingDialog == null) {
                mLoadingDialog =
                    DefaultLoadingDialog(mActivity)
            }
            mLoadingDialog?.showLoading()
        }
    }

    /**
     * 可重写实现
     */
    override fun hideLoading() {
        runOnUiThread {
            mLoadingCount--
            if (mLoadingCount <= 0) {

                mLoadingDialog?.dismissLoading()
                mLoadingCount = 0
            }
        }
    }

    override fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

/*  private var mDoubleBack = false
    open fun getDoubleBackString(): String {
        return ""
    }

    override fun onBackPressed() {
        if (!mDoubleBack && !TextUtils.isEmpty(getDoubleBackString())) {
            ToastUtil.showToast(getDoubleBackString())
            mDoubleBack = true
            Handler().postDelayed({ mDoubleBack = false }, 2000)
            return
        }
        super.onBackPressed()
    }*/

}