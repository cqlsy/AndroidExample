package com.lsy.widget.recycle

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lsy.utils.LogUtil

/**
 * 这个类是测试说明类，需要的请根据这个类来自定义。
 * 请谨慎使用这个类
 */
class ItemDecorationDivider : RecyclerView.ItemDecoration {

    // 单位 px
    var dividerHeight: Int = 1
    // 默认的颜色
    var dividerColor: Int = 0xEDEEF2
    var mPaint: Paint

    constructor() : this(1)

    constructor(dividerHeight: Int) : this(dividerHeight, 0x000000)

    constructor(dividerHeight: Int, dividerColor: Int) {
        this.dividerHeight = dividerHeight
        this.dividerColor = dividerColor
        mPaint = Paint()
        mPaint.isAntiAlias = true

    }

    /**
     * 最上层的绘制，覆盖在该 Item 的上层
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    }

    /**
     * 画间隔的
     * 说明： getItemOffsets() 方法不计算在item的任何属性中。
     * 在画子View中，有margin的数据，是因为画的时候，不要占用我们正常Item的空间
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        mPaint.color = dividerColor
        mPaint.strokeWidth = dividerHeight.toFloat()
        for (i in 0..parent.childCount) {
            val view: View = parent.getChildAt(i) ?: return
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams

            val left = view.left - layoutParams.leftMargin
            val top = view.top - layoutParams.topMargin
            val right = view.right + layoutParams.rightMargin
            val bottom = view.bottom + layoutParams.bottomMargin
            c.drawLine(
                left.toFloat(),
                (bottom + dividerHeight / 2).toFloat(),
                right.toFloat(),
                (bottom + dividerHeight / 2).toFloat(),
                mPaint
            )
            LogUtil.e(
                "ItemDecoration",
                "view ==> left: ${view.left}; right: ${view.right}; top: ${view.top}; bottom: ${view.bottom}"
            )
            LogUtil.e(
                "ItemDecoration",
                "layoutParams ==> left: ${layoutParams.leftMargin}; right: ${layoutParams.rightMargin}; top: ${layoutParams.topMargin}; bottom: ${layoutParams.bottomMargin}"
            )
        }

    }

    /**
     * 把间隔设置出来
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(dividerHeight, dividerHeight, dividerHeight, dividerHeight)
    }
}