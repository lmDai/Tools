package com.qinxin.common.status

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.qinxin.common.R

class BlankStatus : Callback() {
    override fun onCreateView(): Int {
        return R.layout.common_layout_init
    }

    override fun onViewCreate(context: Context, view: View) {
        super.onViewCreate(context, view)
    }

    override fun onReloadEvent(context: Context, view: View): Boolean {
        //不响应reload事件
        return true
    }
}