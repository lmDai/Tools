package com.qinxin.main

import android.content.Intent
import android.view.View
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.qinxin.common.ad.AdSplashManager
import com.qinxin.common.ad.ThirdKeyConfig
import com.qinxin.common.constants.RouterPaths
import com.qinxin.common.net.BaseActivity
import com.qinxin.main.databinding.ActivitySplashBinding


/**
 * @author dlm
 * @description 闪屏界面
 */
@Route(path = RouterPaths.SPLASH_ACTIVITY)
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    /**
     * 是否可以跳转
     */
    private var canJump = false
    private var mAdSplashManager: AdSplashManager? = null
    override fun onBindLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        requestSplashAd()
    }

    /**
     * 请求广告
     */
    private fun requestSplashAd() {
        mAdSplashManager = AdSplashManager(this, object : GMSplashAdLoadCallback {
            override fun onSplashAdLoadFail(p0: AdError) {
                enterApp()
            }

            override fun onSplashAdLoadSuccess() {
                mAdSplashManager!!.splashAd?.showAd(mBinding!!.flAdContainer)
            }

            override fun onAdLoadTimeout() {
            }

        }, object : GMSplashAdListener {
            override fun onAdClicked() {

            }

            override fun onAdShow() {
            }

            override fun onAdShowFail(p0: AdError) {
                if (mAdSplashManager != null) {
                    mAdSplashManager!!.loadSplashAd(ThirdKeyConfig.GMAdConfig_UNIT_SPLASH_ID)
                }
            }

            override fun onAdSkip() {
                enterApp()
            }

            override fun onAdDismiss() {
                enterApp()
            }
        })
        if (mAdSplashManager != null) {
            mAdSplashManager!!.loadSplashAd(ThirdKeyConfig.GMAdConfig_UNIT_SPLASH_ID)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mAdSplashManager != null) {
            mAdSplashManager!!.destroy()
        }
    }

    /**
     * 进入应用
     */
    private fun enterApp() {
        if (canJump) {
            Thread {
                Thread.sleep(500)
                ARouter.getInstance().build(RouterPaths.MAIN_ACTIVITY)
                    .withTransition(0, com.qinxin.common.R.anim.fade_out)
                    .navigation(this, object : NavigationCallback {
                        override fun onFound(postcard: Postcard?) {}
                        override fun onLost(postcard: Postcard?) {}
                        override fun onArrival(postcard: Postcard?) {
                            finish()
                        }

                        override fun onInterrupt(postcard: Postcard?) {}
                    })
            }.start()
        } else {
            canJump = true
        }
    }

    override fun onPause() {
        super.onPause()
        canJump = false
    }

    override fun onResume() {
        super.onResume()
        if (canJump) {
            enterApp()
        }
        canJump = true
    }

    override fun onBackPressed() {}
    override fun initData() {
        if (!isTaskRoot) {
            val intent = intent
            // 如果当前 Activity 是通过桌面图标启动进入的
            if (intent != null && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && Intent.ACTION_MAIN == intent.action
            ) {
                finish()
            }
        }
    }

    override fun onBindLoadSir(): View? {
        return null
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig()
            .hideBar(BarHide.FLAG_HIDE_BAR)
    }
}