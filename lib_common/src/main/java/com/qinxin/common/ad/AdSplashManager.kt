package com.qinxin.common.ad

import android.app.Activity
import com.bytedance.msdk.adapter.util.UIUtils
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAd
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash

/**
 * 开屏管理类。
 * 只需要复制粘贴到项目中，通过回调处理相应的业务逻辑即可使用完成广告加载&展示
 *
 * @author dlm
 */
class AdSplashManager
/**
 * 管理类构造函数
 *
 * @param activity             开屏展示的Activity
 * @param splashAdLoadCallback 开屏加载广告回调
 * @param splashAdListener     开屏广告监听回调
 */(
    private var mActivity: Activity?,
    /**
     * 开屏加载广告回调
     * 请在加载广告成功后展示广告
     */
    private var mGMSplashAdLoadCallback: GMSplashAdLoadCallback?,
    /**
     * 开屏广告监听回调
     */
    private var mSplashAdListener: GMSplashAdListener?
) {
    /**
     * 获取开屏广告对象
     */
    /**
     * 开屏对应的广告对象
     * 每次加载全屏视频广告的时候需要新建一个GMSplashAd，否则可能会出现广告填充问题
     */
    var splashAd: GMSplashAd? = null
        private set

    /**
     * 加载开屏广告
     *
     * @param adUnitId 广告位ID
     */
    fun loadSplashAd(adUnitId: String?) {
        splashAd = GMSplashAd(mActivity, adUnitId)
        splashAd!!.setAdSplashListener(mSplashAdListener)
        val adSlot = GMAdSlotSplash.Builder()
            .setImageAdSize(
                UIUtils.getScreenWidth(mActivity),
                (UIUtils.getScreenHeight(mActivity) * 0.875).toInt()
            )
            .setTimeOut(AD_TIME_OUT)
            .setBidNotify(true)
            .setSplashShakeButton(true)
            .build()
        splashAd!!.loadAd(adSlot, mGMSplashAdLoadCallback)
    }

    /**
     * 在Activity onDestroy中需要调用清理资源
     */
    fun destroy() {
        if (splashAd != null) {
            splashAd!!.destroy()
        }
        mActivity = null
        mGMSplashAdLoadCallback = null
        mSplashAdListener = null
    }

    companion object {
        /**
         * 开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
         */
        private const val AD_TIME_OUT = 3000
    }
}