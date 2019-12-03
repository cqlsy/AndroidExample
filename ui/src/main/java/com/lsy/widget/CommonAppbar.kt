package com.lsy.widget

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.lsy.framework.R
import com.lsy.utils.DisplayUtil

class CommonAppbar : FrameLayout {

    private val preTAG = "CommonAppbar"
    // 当title的字数达到8个及以上的的时候，默认使用跑马灯的模式
    private val needMarqueeTextView: Int = 8
    private var textSize: Float = 0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonAppbar)
        initTypedArray(array)
        array.recycle()
    }

    private fun initTypedArray(array: TypedArray) {
        val mTitle: String? = array.getString(R.styleable.CommonAppbar_appbar_title)
        val mLeftIconId: Int = array.getResourceId(R.styleable.CommonAppbar_appbar_left_icon, 0)
        addTitleTextView(mTitle)
        addLeftIconView(mLeftIconId)
        textSize =
            array.getDimension(R.styleable.CommonAppbar_appbar_title_text_size, 0f)

    }


    fun setTitle(resId: Int) {
        val str = context.getString(resId)
        setTitle(str)
    }

    fun setTitle(titleStr: String) {
        checkView("title")
        if (!TextUtils.isEmpty(titleStr)) {
            addTitleTextView(titleStr)
        }
    }

    // 这个函数只是用来设置单个的颜色，不会涉及到其他的页面
    fun setTitleColor(colorResId: Int) {
        val view: TextView? = findViewWithTag(preTAG + tag)
        view?.setTextColor(ContextCompat.getColor(context, colorResId))
    }

    fun setLeftIcon(resId: Int) {
        if (resId == 0) {
            return
        }
        checkView("left_icon")
        addLeftIconView(resId)
    }

    // 总是删除之前的View; 新建View
    private fun checkView(tag: String) {
        val view: View? = findViewWithTag(preTAG + tag)
        if (view != null) {
            removeView(view)
        }
    }

    private fun addTitleTextView(title: String?) {
        if (TextUtils.isEmpty(title) || title == null) {
            return
        }
        val params =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER
        val tv: TextView = if (title.length < needMarqueeTextView) {
            TextView(context)
        } else {
            MarqueeTextView(context)
        }
        tv.text = title
        tv.tag = preTAG + "title"
        tv.textSize = if (textSize == 0f) 16f else textSize
        tv.gravity = Gravity.CENTER
        params.marginStart = DisplayUtil.dp2px(context, 80f)
        params.marginEnd = DisplayUtil.dp2px(context, 80f)
        tv.setTextColor(ContextCompat.getColor(context, R.color.appbar_title_color))
        addView(tv, params)
    }

    private fun addLeftIconView(resId: Int) {
        if (resId == 0) {
            return
        }
        val params =
            LayoutParams(DisplayUtil.dp2px(context, 48f), LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.START
        val iv = AppCompatImageButton(context, null, R.attr.toolbarNavigationButtonStyle)
        iv.scaleType = ImageView.ScaleType.CENTER_INSIDE
        iv.setImageResource(resId)
        iv.tag = preTAG + "left_icon"
        iv.setOnClickListener {
            backListener?.onBack()
        }
        addView(iv, params)
    }

    var backListener: OnBackListener? = null

    interface OnBackListener {
        fun onBack()
    }
}