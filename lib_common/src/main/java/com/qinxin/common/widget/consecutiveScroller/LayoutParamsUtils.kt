package com.qinxin.common.widget.consecutiveScroller

import android.view.ViewGroup.MarginLayoutParams

object LayoutParamsUtils {
    /**
     * 使子view的topMargin和bottomMargin属性无效
     *
     * @param params
     */
    fun invalidTopAndBottomMargin(params: MarginLayoutParams?) {
        if (params != null) {
            params.topMargin = 0
            params.bottomMargin = 0
        }
    }
}