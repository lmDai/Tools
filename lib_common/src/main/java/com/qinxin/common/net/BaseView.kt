package com.qinxin.common.net

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.kingja.loadsir.callback.Callback
import com.qinxin.common.status.BlankStatus
import com.qinxin.common.status.EmptyStatus
import com.qinxin.common.status.ErrorStatus
import com.qinxin.common.status.LoadingStatus

/**
 * @author :xhkj
 * @description Activity和Fragment公用方法
 */
interface BaseView {
    /**
     * 绑定视图
     *
     * @return
     */
    @LayoutRes
    fun onBindLayout(): Int

    /**
     * 加载状态页
     */
    fun loadView()

    /**
     * 初始化参数
     */
    fun initParams() {}

    /**
     * 初始化视图
     */
    fun initView()

    /**
     * 初始化监听
     */
    fun initListener() {}

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 绑定状态视图
     *
     * @return
     */
    fun onBindLoadSir(): View?

    /**
     * 提供状态布局
     *
     * @return
     */
    val initStatus: Callback?
        get() = BlankStatus()

    /**
     * 加载页回调
     *
     * @return
     */
    val loadingStatus: Callback?
        get() = LoadingStatus()

    /**
     * 错误页回调
     *
     * @return
     */
    val errorStatus: Callback?
        get() = ErrorStatus()

    /**
     * 空视图回调
     *
     * @return
     */
    val emptyStatus: Callback?
        get() = EmptyStatus()

    /**
     * 提供额外状态布局
     *
     * @return
     */
    val extraStatus: List<Callback?>?
        get() = null

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput() {
        var activity: Activity? = null
        if (this is Fragment) {
            activity = (this as Fragment).activity
        } else if (this is Activity) {
            activity = this
        }
        if (activity == null) {
            return
        }
        val view = activity.window.decorView
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    fun showSoftInput(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        view.postDelayed({ imm.showSoftInput(view, InputMethodManager.SHOW_FORCED) }, SHOW_SPACE)
    }

    companion object {
        const val SHOW_SPACE = 200L
    }
}