package com.qinxin.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.qinxin.common.ad.GMAdManagerHolder
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

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
        initRefreshLayout()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    companion object {
        var instance: App? = null
    }

    private fun initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context).setFinishDuration(0)
        }
    }
}