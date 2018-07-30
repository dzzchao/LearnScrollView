package com.dzzchao.learnscrollview.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller

/**
 * 外部拦截法处理第一种滑动冲突（内外滑动方向不一致）
 */
class HorizontalScrollViewEx : ViewGroup {

    private var mChildrenSize: Int = 0
    private var mChildWidth: Int = 0
    private var mChildIndex: Int = 0

    // 分别记录上次滑动的坐标
    private var mLastX = 0
    private var mLastY = 0
    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private var mLastXIntercept = 0
    private var mLastYIntercept = 0

    private var mScroller: Scroller? = null
    private var mVelocityTracker: VelocityTracker? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mScroller = Scroller(context)
        mVelocityTracker = VelocityTracker.obtain()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var intercepted = false
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                //如果 scroller 没结束，就停止动画
                if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastXIntercept
                val deltaY = y - mLastYIntercept
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true
                } else {
                    intercepted = false
                }
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
            else -> {
            }
        }

        Log.d(TAG, "intercepted=$intercepted")
        mLastX = x
        mLastY = y
        mLastXIntercept = x
        mLastYIntercept = y

        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker!!.addMovement(event)
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                scrollBy(-deltaX, 0)
            }
            MotionEvent.ACTION_UP -> {
                val scrollX = scrollX
                val scrollToChildIndex = scrollX / mChildWidth
                mVelocityTracker!!.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker!!.xVelocity
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = if (xVelocity > 0) mChildIndex - 1 else mChildIndex + 1
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1))
                val dx = mChildIndex * mChildWidth - scrollX
                smoothScrollBy(dx, 0)
                mVelocityTracker!!.clear()
            }
            else -> {
            }
        }

        mLastX = x
        mLastY = y
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measuredWidth = 0
        var measuredHeight = 0
        val childCount = childCount
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthSpaceSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightSpaceSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (childCount == 0) {
            setMeasuredDimension(0, 0)
        } else if (heightSpecMode == View.MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measuredHeight = childView.measuredHeight
            setMeasuredDimension(widthSpaceSize, childView.measuredHeight)
        } else if (widthSpecMode == View.MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measuredWidth = childView.measuredWidth * childCount
            setMeasuredDimension(measuredWidth, heightSpaceSize)
        } else {
            val childView = getChildAt(0)
            measuredWidth = childView.measuredWidth * childCount
            measuredHeight = childView.measuredHeight
            setMeasuredDimension(measuredWidth, measuredHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        val childCount = childCount
        mChildrenSize = childCount

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {
                val childWidth = childView.measuredWidth
                mChildWidth = childWidth
                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.measuredHeight)
                childLeft += childWidth
            }
        }
    }

    private fun smoothScrollBy(dx: Int, dy: Int) {
        mScroller!!.startScroll(scrollX, 0, dx, 0, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            scrollTo(mScroller!!.currX, mScroller!!.currY)
            postInvalidate()
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker!!.recycle()
        super.onDetachedFromWindow()
    }

    companion object {
        private val TAG = "HorizontalScrollViewEx"
    }
}
