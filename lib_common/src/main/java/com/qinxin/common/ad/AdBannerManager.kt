package com.qinxin.common.ad

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import com.bytedance.msdk.api.UIUtils
import com.bytedance.msdk.api.v2.GMAdSize
import com.bytedance.msdk.api.v2.GMMediationAdSdk
import com.bytedance.msdk.api.v2.GMSettingConfigCallback
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAd
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdListener
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil
import com.bytedance.msdk.api.v2.slot.GMAdSlotBanner

/**
 * banner管理类。
 * 只需要复制粘贴到项目中，通过回调处理相应的业务逻辑即可使用完成广告加载&展示
 */
class AdBannerManager
/**
 * 管理类构造函数
 *
 * @param activity             banner展示的Activity
 * @param bannerAdLoadCallback banner加载广告回调
 * @param adBannerListener     banner广告监听回调
 */(
    private var mActivity: Activity?,
    /**
     * banner加载广告回调
     * 请在加载广告成功后展示广告
     */
    private var mBannerAdLoadCallback: GMBannerAdLoadCallback?,
    /**
     * banner广告监听回调
     */
    private var mAdBannerListener: GMBannerAdListener?, private val width: Int
) {
    /**
     * 获取banner广告对象
     */
    /**
     * banner对应的广告对象
     * 每次加载banner的时候需要新建一个GMBannerAd，一个广告对象只能load一次，banner广告对象getBannerView只能一次，第二次调用会返回空
     */
    var bannerAd: GMBannerAd? = null
        private set
    private var mAdUnitId //广告位
            : String? = null

    /**
     * 加载banner广告，如果没有config配置会等到加载完config配置后才去请求广告
     *
     * @param adUnitId 广告位id
     */
    fun loadAdWithCallback(adUnitId: String?) {
        mAdUnitId = adUnitId
        if (GMMediationAdSdk.configLoadSuccess()) {
            loadBannerAd(adUnitId)
        } else {
            GMMediationAdSdk.registerConfigCallback(mSettingConfigCallback)
        }
    }

    /**
     * 真正的开始加载banner广告
     *
     * @param adUnitId 广告位id
     */
    private fun loadBannerAd(adUnitId: String?) {
        if (bannerAd != null) {
            bannerAd!!.destroy()
        }
        bannerAd = GMBannerAd(mActivity, adUnitId)

        //设置广告事件监听
        bannerAd!!.setAdBannerListener(mAdBannerListener)
        val gdtNativeAdLogoParams = FrameLayout.LayoutParams(
            UIUtils.dip2px(mActivity!!.applicationContext, 40f),
            UIUtils.dip2px(mActivity!!.applicationContext, 13f),
            Gravity.RIGHT or Gravity.TOP
        )
        val adSlotNativeBuilder = GMAdOptionUtil.getGMAdSlotGDTOption()
            .setNativeAdLogoParams(gdtNativeAdLogoParams)
        //设置广告配置
        /**
         * 创建BANNER广告请求类型参数GMAdSlotBanner,具体参数含义参考文档
         */
        var slotBanner = GMAdSlotBanner.Builder()
            .setBannerSize(GMAdSize.BANNER_CUSTOME)
            .setImageAdSize(width, width / 20 * 3)
            .setAllowShowCloseBtn(true)
            .setBidNotify(true)
            .setGMAdSlotGDTOption(adSlotNativeBuilder.build())
            .setAdmobNativeAdOptions(GMAdOptionUtil.getAdmobNativeAdOptions())
            .setMuted(true)
            .build()
        bannerAd!!.loadAd(slotBanner, mBannerAdLoadCallback)
    }

    fun onResume() {
        if (bannerAd != null) {
            bannerAd!!.onResume()
        }
    }

    fun onPause() {
        if (bannerAd != null) {
            bannerAd!!.onPause()
        }
    }

    /**
     * 在Activity onDestroy中需要调用清理资源
     */
    fun destroy() {
        if (bannerAd != null) {
            bannerAd!!.destroy()
        }
        mActivity = null
        bannerAd = null
        mBannerAdLoadCallback = null
        mAdBannerListener = null
        GMMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback)
    }

    /**
     * config配置回调
     */
    private val mSettingConfigCallback = GMSettingConfigCallback { loadAdWithCallback(mAdUnitId) }
    fun printLoadFailAdnInfo() {
        if (bannerAd == null) {
            return
        }

        // 获取本次waterfall加载中，加载失败的adn错误信息。
        Log.d(
            "single",
            "InterstitialFull ad loadinfos: " + bannerAd?.adLoadInfoList
        )
    }
}
