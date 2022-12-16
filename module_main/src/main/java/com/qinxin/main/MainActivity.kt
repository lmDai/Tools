package com.qinxin.main

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.UIUtils
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdListener
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gyf.immersionbar.ImmersionBar
import com.qinxin.common.ad.AdBannerManager
import com.qinxin.common.ad.ThirdKeyConfig
import com.qinxin.common.bean.GirlBean
import com.qinxin.common.bean.JokesListBean
import com.qinxin.common.bean.MenuBean
import com.qinxin.common.constants.RouterPaths
import com.qinxin.common.net.BaseRefreshActivity
import com.qinxin.main.adapter.GirlBannerAdapter
import com.qinxin.main.adapter.JokesRandomAdapter
import com.qinxin.main.databinding.ActivityMainBinding
import com.qinxin.main.view.NineMenuView
import com.qinxin.main.viewmodel.ToolsViewModel
import com.qinxin.main.viewmodel.ViewModelFactory
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = RouterPaths.MAIN_ACTIVITY)
class MainActivity : BaseRefreshActivity<ActivityMainBinding, ToolsViewModel, JokesListBean>() {
    private var mAdBannerManager: AdBannerManager? = null
    private var mAdBannerListener: GMBannerAdListener? = null
    private lateinit var mViewPager: BannerViewPager<GirlBean>
    private val nineMenuView: NineMenuView by lazy { NineMenuView(this, this@MainActivity) }
    private fun setupViewPager() {
        mViewPager = mBinding!!.banner as BannerViewPager<GirlBean>
        mViewPager.apply {
            adapter = GirlBannerAdapter()
            registerLifecycleObserver(lifecycle)
        }.create()
    }

    private val mAdapter by lazy { JokesRandomAdapter() }
    override fun initView() {
        super.initView()
        val nineMenuList = mutableListOf(
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/102632/18/20350/13255/61e131feE788947ef/c391b8590cdf427e.png!q70.jpg",
                "超市",
                "m01"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/172178/32/9487/10851/609d18e0Ed273eec1/a1e099f1601c8cc2.png!q70.jpg",
                "服饰",
                "m03"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/193038/4/3881/13295/60a4b6a7E4124dc9e/fdd2934e97eab3ac.png!q70.jpg",
                "免费水果",
                "m04"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/181856/30/3968/10274/609e3d99E07c63af4/cb0d5b21c461b07f.png!q70.jpg",
                "喜喜",
                "m05"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/91538/20/21166/8105/61e128b9E06558f00/2c9273cdee9b2e3d.png!q70.jpg",
                "生活缴费",
                "m06"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/185536/13/17333/14522/6108a9f0E62572408/8222cc8a66134776.png!q70.jpg",
                "领豆",
                "m07"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/175369/35/26446/17302/61e12945E09ef2a2f/87b391aff2da560a.png!q70.jpg",
                "领券",
                "m08"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/127505/17/20504/10647/61e112b0E4f382c96/7a042862fc7c479e.png!q70.jpg",
                "领金贴",
                "m09"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/107776/17/21311/9709/61e12b6eEa79cbefa/bd0bb902e126fafb.png!q70.jpg",
                "会员",
                "m10"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/104637/39/25735/190791/622f3682E168960d2/2dbfbaf4147134c1.gif",
                "国际",
                "m11"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/146216/7/22157/182757/622f37e8Ec2445bac/658b95154cb12771.gif",
                "电器",
                "m02"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/189533/37/5319/12852/60b05d59Eb3b3fd29/4c478e3347507e51.png!q70.jpg",
                "生鲜",
                "m13"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/129902/19/16911/11005/60b05dd8Edad29a3f/ca6b3ea0f67e1826.png!q70.jpg",
                "沃尔玛",
                "m14"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/192361/9/5316/11815/60b05d25Eec7f6b5e/5555dcc59d99428d.png!q70.jpg",
                "旅行",
                "m15"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/109159/13/17721/9654/60b05d73Eefa154db/3a4a46ef2755c622.png!q70.jpg",
                "看病购药",
                "m16"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/188405/35/3707/11116/60a26055Ef91c306d/1ba7aa3b9328e35e.png!q70.jpg",
                "拍拍二手",
                "m17"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/177688/12/4847/7352/60a39a9bEe0a7e4f8/1a8efe458d1c3ee2.png!q70.jpg",
                "种豆得豆",
                "m18"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/184718/8/4051/11977/609e5222Ea2816259/d29fec6d4d8558f1.png!q70.jpg",
                "萌宠",
                "m19"
            ),
            MenuBean(
                "https://m15.360buyimg.com/mobilecms/jfs/t1/126310/38/18675/7912/60b05e32E736cb5fe/2bb8508e955b88ee.png!q70.jpg",
                "更多频道",
                "m20"
            )
        )
        mBinding!!.nineMenuView.run {
            removeAllViews()
            addView(nineMenuView)
        }
        nineMenuView.setData(nineMenuList)


        recyclerview.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
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
                mViewPager.refreshData(response)
                mAdBannerManager!!.loadAdWithCallback(ThirdKeyConfig.GMAdConfig_UNIT_BANNER_ID_MINE)

            }
        }
    }

    override fun onBindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        mViewModel?.onViewRefresh()
    }

    override fun onBindLoadSir(): View? {
        return null
    }

    override fun onBindWrapRefresh(): WrapRefresh {
        return WrapRefresh(refreshView, mAdapter as BaseQuickAdapter<JokesListBean, BaseViewHolder>)
    }

}