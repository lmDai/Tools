package com.qinxin.common.status

import com.kingja.loadsir.callback.Callback
import com.qinxin.common.R

class ErrorStatus : Callback() {
    override fun onCreateView(): Int {
        return R.layout.common_layout_error
    }
}