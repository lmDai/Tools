package com.qinxin.main

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.UIUtils
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdListener
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback
import com.gyf.immersionbar.ImmersionBar
import com.qinxin.common.ad.AdBannerManager
import com.qinxin.common.ad.ThirdKeyConfig
import com.qinxin.common.bean.GirlBean
import com.qinxin.common.constants.RouterPaths
import com.qinxin.common.net.BaseMvvmActivity
import com.qinxin.main.adapter.GirlBannerAdapter
import com.qinxin.main.databinding.ActivityMainBinding
import com.qinxin.main.viewmodel.ToolsViewModel
import com.qinxin.main.viewmodel.ViewModelFactory
import com.zhpan.bannerview.BannerViewPager

@Route(path = RouterPaths.MAIN_ACTIVITY)
class MainActivity : BaseMvvmActivity<ActivityMainBinding, ToolsViewModel>() {
    private var mAdBannerManager: AdBannerManager? = null
    private var mAdBannerListener: GMBannerAdListener? = null
    private lateinit var mViewPager: BannerViewPager<GirlBean>
    private fun setupViewPager() {
        mViewPager = mBinding!!.banner as BannerViewPager<GirlBean>
        mViewPager.apply {
            adapter = GirlBannerAdapter()
            setLifecycleRegistry(lifecycle)
        }.create()
    }

    override fun initView() {
        super.initView()
        setupViewPager()
        mAdBannerListener = object : GMBannerAdListener {
            override fun onAdOpened() {}
            override fun onAdLeftApplication() {}
            override fun onAdClosed() {
                mBinding!!.fragmentContainer.removeAllViews()
            }

            override fun onAdClicked() {}
            override fun onAdShow() {}

            /**
             * show失败回调。如果show时发现无可用广告（比如广告过期），会触发该回调。
             * 开发者应该结合自己的广告加载、展示流程，在该回调里进行重新加载。
             * @param adError showFail的具体原因
             */
            override fun onAdShowFail(adError: AdError) {}
        }
        initBannerAd()
    }

    /**
     * 展示广告
     */
    private fun initBannerAd() {
        mBinding!!.fragmentContainer.removeAllViews()
        mAdBannerManager = AdBannerManager(this, object : GMBannerAdLoadCallback {
            override fun onAdFailedToLoad(adError: AdError) {
                mAdBannerManager?.printLoadFailAdnInfo()
                mBinding!!.fragmentContainer.removeAllViews()
            }

            override fun onAdLoaded() {
                loadBannerAd()
            }
        }, mAdBannerListener, (UIUtils.getScreenWidthDp(this)).toInt())
    }

    private fun loadBannerAd() {
        if (mAdBannerManager != null) {
            mBinding!!.fragmentContainer.removeAllViews()
            if (mAdBannerManager?.bannerAd != null) {
                if (!mAdBannerManager?.bannerAd!!.isReady) {
                    return
                }
                val view: View = mAdBannerManager!!.bannerAd?.bannerView!!
                mBinding!!.fragmentContainer.addView(view)
            }
        }
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig().titleBar(mBinding!!.searchLinearLayout)
    }

    override fun onBindViewModel(): Class<ToolsViewModel>? {
        return ToolsViewModel::class.java
    }

    override fun onBindViewModelFactory(): ViewModelProvider.Factory? {
        return ViewModelFactory.getInstance(application)
    }

    override fun initViewObservable() {
        mViewModel?.getmEnterMsgEvent()?.observe(this@MainActivity) { response ->
            kotlin.run {
                mBinding!!.tabMode.text = response?.get(0)?.imageUrl
                mViewPager.refreshData(response)
                mAdBannerManager!!.loadAdWithCallback(ThirdKeyConfig.GMAdConfig_UNIT_BANNER_ID_INDEX)

            }
        }
    }

    override fun onBindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        mViewModel?.girlRandom()
    }

    override fun onBindLoadSir(): View? {
        return null
    }

}