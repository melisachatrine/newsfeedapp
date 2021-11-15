package com.kanchanpal.newsfeed.scrolling

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.kanchanpal.newsfeed.R
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import pl.droidsonroids.gif.GifImageView

class LoadingView(context: Context, attrs: AttributeSet? = null): LinearLayout(context, attrs), RefreshHeader  {
    private val gifImageView = GifImageView(context)

    init {
        gifImageView.setImageResource(R.drawable.loading)
        gifImageView.visibility = View.GONE
        addView(gifImageView)
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        gifImageView.visibility = View.GONE
        return 500
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        return
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun getView(): View {
        return this;
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.Refreshing -> {
                gifImageView.visibility = View.VISIBLE
            }
        }
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {

    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

}