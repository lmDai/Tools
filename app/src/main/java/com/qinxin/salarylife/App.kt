package com.qinxin.salarylife

import android.app.Application
import android.content.Context

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
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    companion object {
        var instance: App? = null
    }
}