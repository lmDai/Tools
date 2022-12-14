package com.qinxin.common.status

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.qinxin.common.R

class LoadingStatus : Callback() {
    override fun onCreateView(): Int {
        return R.layout.common_layout_loading
    }

    override fun onReloadEvent(context: Context, view: View): Boolean {
        //不响应reload事件
        return true
    }

    override fun getSuccessVisible(): Boolean {
        //背景可自定义
        return true
    }
}