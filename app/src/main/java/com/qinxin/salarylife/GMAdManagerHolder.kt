package com.qinxin.salarylife

import android.content.Context
import android.provider.Settings
import com.bytedance.msdk.api.v2.*
import com.bytedance.msdk.api.v2.GMAdConstant.ADULT_STATE
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 可以用一个单例来保存GMAdManager实例，在需要初始化sdk的时候调用
 *
 * @author dlm
 */
object GMAdManagerHolder {
    private var sInit = false

    @JvmStatic
    fun init(context: Context) {
        doInit(context)
    }

    /**
     * step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
     *
     * @param context 上下文
     */
    private fun doInit(context: Context) {
        if (!sInit) {
            GMMediationAdSdk.initialize(context, buildV2Config(context))
            sInit = true
        }
    }

    fun buildV2Config(context: Context): GMAdConfig {
        var jsonObject: JSONObject? = null
        //读取json文件，本地缓存的配置
        try {
            jsonObject = JSONObject(getJson("androidlocalconfig.json", context))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return GMAdConfig.Builder()
            .setAppId(ThirdKeyConfig.GMAdConfig_APPID)
            .setAppName("i薪生活")
            .setPublisherDid(getAndroidId(context)!!)
            .setOpenAdnTest(false)
            .setPangleOption(
                GMPangleOption.Builder()
                    .setIsPaid(false)
                    .setTitleBarTheme(GMAdConstant.TITLE_BAR_THEME_DARK)
                    .setAllowShowNotify(true)
                    .setAllowShowPageWhenScreenLock(true)
                    .setDirectDownloadNetworkType(
                        GMAdConstant.NETWORK_STATE_WIFI,
                        GMAdConstant.NETWORK_STATE_3G
                    )
                    .setIsUseTextureView(true)
                    .setNeedClearTaskReset()
                    .setKeywords("")
                    .build()
            )
            .setGdtOption(
                GMGdtOption.Builder()
                    .setWxInstalled(false)
                    .setOpensdkVer(null)
                    .setSupportH265(false)
                    .setSupportSplashZoomout(false)
                    .build()
            )
            /**
             * 隐私协议设置，详见GMPrivacyConfig
             */
            .setPrivacyConfig(object : GMPrivacyConfig() {
                override fun isCanUseLocation(): Boolean {
                    return false
                }

                override fun isCanUseAndroidId(): Boolean {
                    return false
                }

                override fun isCanUseMacAddress(): Boolean {
                    return false
                }

                override fun isCanUseOaid(): Boolean {
                    return false
                }

                override fun isCanUsePhoneState(): Boolean {
                    return false
                }

                //当isCanUseWifiState=false时，可传入Mac地址信息，穿山甲sdk使用您传入的Mac地址信息
                override fun getMacAddress(): String {
                    return ""
                }

                // 设置青少年合规，默认值GMAdConstant.ADULT_STATE.AGE_ADULT为成年人
                override fun getAgeGroup(): ADULT_STATE {
                    return ADULT_STATE.AGE_ADULT
                }
            })
            .setCustomLocalConfig(jsonObject)
            .build()
    }

    fun getAndroidId(context: Context): String? {
        var androidId: String? = null
        try {
            androidId =
                Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return androidId
    }

    fun getJson(fileName: String?, context: Context): String {
        val stringBuilder = StringBuilder()
        try {
            val `is` = context.assets.open(fileName!!)
            val bufferedReader = BufferedReader(InputStreamReader(`is`))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}