package com.qinxin.common.ad

import com.kwad.sdk.api.KsCustomController

/**
 * @author dlm
 * @description 信息获取控制工具
 */
class UserDataObtainController private constructor() : KsCustomController() {
    private var userAgree = false

    companion object {
        //单例
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UserDataObtainController()
        }
    }

    fun setUserAgree(userAgree: Boolean): UserDataObtainController {
        this.userAgree = userAgree
        return this
    }

    override fun canReadLocation(): Boolean {
        // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK获取地理位置信息。
        return if (!userAgree) {
            false
        } else super.canReadLocation()
    }

    override fun canUsePhoneState(): Boolean {
        // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用手机硬件信息。
        return if (!userAgree) {
            false
        } else super.canUsePhoneState()
    }

    override fun canUseOaid(): Boolean {
        // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用设备oaid。
        return if (!userAgree) {
            false
        } else super.canUseOaid()
    }

    override fun canUseMacAddress(): Boolean {
        // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用设备Mac地址。
        return if (!userAgree) {
            false
        } else super.canUseMacAddress()
    }

    override fun canReadInstalledPackages(): Boolean {
        // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK读取app安装列表。
        return if (!userAgree) {
            false
        } else super.canReadInstalledPackages()
    }

    override fun canUseStoragePermission(): Boolean {
        // 为提升SDK的接入体验，广告展示更流畅，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用存储权限。
        return if (!userAgree) {
            false
        } else super.canUseStoragePermission()
    }

    override fun canUseNetworkState(): Boolean {
        // 为提升SDK的接入体验，广告展示更流畅，建议媒体在用户同意隐私政策及权限信息后，允许SDK读取网络状态信息。
        return if (!userAgree) {
            false
        } else super.canUseNetworkState()
    }
}