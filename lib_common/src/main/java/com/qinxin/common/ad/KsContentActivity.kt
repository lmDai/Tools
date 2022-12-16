package com.qinxin.common.ad

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.kwad.sdk.api.KsAdSDK
import com.kwad.sdk.api.KsContentPage
import com.kwad.sdk.api.KsScene
import com.qinxin.common.R
import com.qinxin.common.constants.RouterPaths
import com.qinxin.common.databinding.ActivityKsBinding
import com.qinxin.common.net.BaseActivity

/**
 * @author Administrator
 */
@Route(path = RouterPaths.KS_ACTIVITY)
class KsContentActivity : BaseActivity<ActivityKsBinding?>(), View.OnClickListener {
    private var mKsContentPage: KsContentPage? = null

    /**
     * 获取KsContentPage
     */
    private fun initContentPage() {
        val adScene = KsScene.Builder(ThirdKeyConfig.POSID_CONTENT_PAGE).build()
        mKsContentPage = KsAdSDK.getLoadManager()!!.loadContentPage(adScene)
    }

    /**
     * 展示KsContentPage
     */
    private fun showContentPage() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mKsContentPage!!.fragment)
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mKsContentPage!!.onBackPressed()
    }

    override fun onBindLayout(): Int {
        return R.layout.activity_ks
    }

    override fun initView() {
        initContentPage()
        showContentPage()
    }

    override fun initListener() {
        super.initListener()
        mBinding!!.toolbarBack.setOnClickListener(this)
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig().titleBar(mBinding!!.toolBar)
    }

    override fun initData() {}
    override fun onBindLoadSir(): View? {
        return null
    }

    override fun onClick(v: View) {
        if (v === mBinding!!.toolbarBack) {
            finish()
        }
    }
}