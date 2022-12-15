package com.qinxin.common.net

import android.R
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.qinxin.common.BaseEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author dlm
 * @description Activity基类
 */
abstract class BaseActivity<DB : ViewDataBinding?> : AppCompatActivity(), BaseView,
    Consumer<Disposable> {
    /**
     * 公用Handler
     */
    protected var mHandler = Handler(Looper.getMainLooper())

    /**
     * Disposable容器
     */
    protected var mDisposables = CompositeDisposable()

    /**
     * 页面状态管理
     */
    protected var mBaseLoadService: LoadService<*>? = null

    @JvmField
    protected var mBinding: DB? = null
    protected var hasInit = false

    @JvmField
    protected var mRouter = ARouter.getInstance()

    /**
     * 状态栏沉浸
     */
    private var mImmersionBar: ImmersionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, onBindLayout())
        EventBus.getDefault().register(this)
        mRouter.inject(this)
        /*初始化沉浸式状态栏*/if (isStatusBarEnabled) {
            statusBarConfig.init()
        }
        loadView()
        initParams()
        initView()
        initListener()
        initData()
        hasInit = true
    }

    override fun accept(disposable: Disposable?) {
        mDisposables.add(disposable)
    }

    override fun loadView() {
        val builder = LoadSir.Builder()
            .addCallback(initStatus!!)
            .addCallback(emptyStatus!!)
            .addCallback(errorStatus!!)
            .addCallback(loadingStatus!!)
            .setDefaultCallback(initStatus!!.javaClass)
        if (onBindLoadSir() != null) {
            mBaseLoadService = builder.build()
                .register(onBindLoadSir(), OnReloadListener { v: View? -> onReload(v) })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BaseEvent?) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEventSticky(event: BaseEvent?) {
    }

    /**
     * 显示初始化状态页
     */
    fun showInitView() {
        clearStatus()
        if (mBaseLoadService != null) {
            mBaseLoadService!!.showCallback(initStatus!!.javaClass)
        }
    }

    /**
     * 显示出错状态页
     */
    fun showErrorView() {
        clearStatus()
        if (mBaseLoadService != null) {
            mBaseLoadService!!.showCallback(errorStatus!!.javaClass)
        }
    }

    /**
     * 显示空状态页
     */
    fun showEmptyView() {
        clearStatus()
        if (mBaseLoadService != null) {
            mBaseLoadService!!.showCallback(emptyStatus!!.javaClass)
        }
    }

    /**
     * 显示loading状态页
     *
     * @param tip 为null时不带提示文本
     */
    fun showLoadingView(tip: String?) {
        clearStatus()
        if (mBaseLoadService != null) {
            mBaseLoadService!!.setCallBack(loadingStatus!!.javaClass) { context: Context?, view: View? -> }
        }
        postDelayed(mLoadStatusRun, 300)
    }

    private val mLoadStatusRun = Runnable {
        if (mBaseLoadService != null) {
            mBaseLoadService!!.showCallback(loadingStatus!!.javaClass)
        }
    }

    override fun finish() {
        super.finish()
    }

    /**
     * 清除所有状态页
     */
    fun clearStatus() {
        mHandler.removeCallbacks(mLoadStatusRun)
        if (mBaseLoadService != null) {
            mBaseLoadService!!.showSuccess()
        }
    }

    /**
     * 点击状态页默认执行事件
     */
    protected fun onReload(v: View?) {
        showInitView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        //        MobclickAgent.onResume(this);
    }

    protected fun post(runnable: Runnable?) {
        mHandler.post(runnable!!)
    }

    protected fun postDelayed(runnable: Runnable?, delayMillis: Long) {
        mHandler.postDelayed(runnable!!, delayMillis)
    }

    override fun onPause() {
        super.onPause()
        //        MobclickAgent.onPause(this);
    }

    public override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        EventBus.getDefault().unregister(this)
        mDisposables.clear()
    }

    /**
     * 状态栏字体深色模式
     */
    protected val isStatusBarDarkFont: Boolean
        protected get() = true

    /**
     * 获取状态栏沉浸的配置对象
     */
    val statusBarConfig: ImmersionBar
        get() {
            if (mImmersionBar == null) {
                mImmersionBar = createStatusBarConfig()
            }
            return mImmersionBar!!
        }

    /**
     * 是否使用沉浸式状态栏
     */
    protected val isStatusBarEnabled: Boolean
        protected get() = true

    /**
     * 初始化沉浸式状态栏
     */
    protected open fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .statusBarDarkFont(isStatusBarDarkFont) // 指定导航栏背景颜色
            .navigationBarColor(R.color.white) // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
            .autoDarkModeEnable(true, 0.2f)
    }
}