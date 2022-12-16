package com.qinxin.common.ad

import android.content.Context
import com.kwad.sdk.api.KsAdSDK
import com.kwad.sdk.api.KsInitCallback
import com.kwad.sdk.api.SdkConfig
import com.qinxin.common.R

/**
 * @author dlm
 * @description 广告sdk 初始化工具
 */
object AdSdkInitUtil {
    fun initSdk(appContext: Context) {
        KsAdSDK.init(
            appContext, SdkConfig.Builder()
                .appId(ThirdKeyConfig.KS_APPID)
                .appName(appContext.getString(R.string.app_name))
                .nightThemeStyleAssetsFileName("ks_adsdk_night_styles.xml")
                .showNotification(true)
                .customController(
                    UserDataObtainController.instance
                        .setUserAgree(true)
                )
                .setInitCallback(object : KsInitCallback {
                    override fun onSuccess() {}
                    override fun onFail(code: Int, msg: String) {}
                })
                .build()
        )
    }
}