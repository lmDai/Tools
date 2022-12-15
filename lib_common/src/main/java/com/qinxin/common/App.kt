package com.qinxin.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.qinxin.common.ad.GMAdManagerHolder

/**
 * @author dlm
 * @description Application
 */
class App : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        GMAdManagerHolder.init(this)
        ARouter.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    companion object {
        var instance: App? = null
    }
}