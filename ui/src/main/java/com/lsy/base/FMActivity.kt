package com.lsy.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lsy.framework.R
import com.lsy.utils.LogUtil
import com.lsy.utils.ToastUtil
import com.lsy.widget.loading.DefaultLoading
import com.lsy.widget.loading.ILoadingIndicator
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
abstract class FMActivity : AppCompatActivity(), SwipeBackActivityBase, FMView {

    /* 这个写在这里是方便调用 */
    protected lateinit var mActivity: Activity
    /* RXjava 管理 */
    private lateinit var mHelper: SwipeBackActivityHelper  //滑动返回
    private val mCompositeDisposable = CompositeDisposable()

    private var mDoubleBack = false
    private var mLoadingDialog: ILoadingIndicator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        /* 设置presenter */
        FMContract.attachView(this)
        /* 设置滑动关闭 */
        initSwipeBack()
        /*  */
        val mViewBinding: ViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initBinding(mViewBinding)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        FMContract.detachView(this)
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
    }

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

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun initLoading(): ILoadingIndicator? {
        return null
    }

    override fun <T : FMView> getPresenter(): FMPresenter<T>? {
        return null
    }

    override fun <T : FMView> getPresenters(): ArrayList<FMPresenter<T>?>? {
        return null
    }

    /**
     * 初始化 Binding, 防止忘记,这里只做一些 binding有关的操作，责任分明
     */
    abstract fun initBinding(viewDataBinding: ViewDataBinding)

    /**
     * 初始化 View
     */
    abstract fun initViews()


    open fun getDoubleBackString(): String {
        return ""
    }

    override fun handleException(throwable: Throwable) {
        LogUtil.d("Exception", throwable.message)
    }

    override fun getLoadingIndicator(): ILoadingIndicator {
        if (mLoadingDialog == null) {
            mLoadingDialog = initLoading()
            if (mLoadingDialog == null) {
                mLoadingDialog = DefaultLoading(this)
            }
        }
        return mLoadingDialog as ILoadingIndicator
    }

    override fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onBackPressed() {
        if (!mDoubleBack && !TextUtils.isEmpty(getDoubleBackString())) {
            ToastUtil.showToast(getDoubleBackString())
            mDoubleBack = true
            Handler().postDelayed({ mDoubleBack = false }, 2000)
            return
        }
        super.onBackPressed()
    }


    // ====================================  滑动退出 代码 start ====================================
    /** SwipeBackLayout.EDGE_LEFT
    SwipeBackLayout.EDGE_RIGHT*/

    private fun initSwipeBack() {
        if (canBack() != -1) {
            mHelper = SwipeBackActivityHelper(this)
            mHelper.onActivityCreate()
            mHelper.swipeBackLayout.setEdgeTrackingEnabled(canBack())
        }
    }

    open fun canBack(): Int {
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

}