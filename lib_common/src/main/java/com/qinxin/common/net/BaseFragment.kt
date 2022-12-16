package com.qinxin.common.net

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.qinxin.common.App.Companion.instance
import com.qinxin.common.BaseEvent
import com.qinxin.common.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * <br></br>Description:Fragment基类,主要处理标题栏和状态页逻辑
 */
abstract class BaseFragment<DB : ViewDataBinding?> : Fragment(), BaseView, Consumer<Disposable> {
    protected var mApplication: Application? = null

    /**
     * 公用Handler
     */
    protected var mHandler = Handler(Looper.getMainLooper())

    /**
     * Disposable容器
     */
    protected var mDisposables = CompositeDisposable()

    /**
     * 用于延时显示loading状态,避免一闪而过
     */
    private val mLoadingHandler = Handler()

    /**
     * 根部局
     */
    protected var mView: View? = null

    /**
     * 状态页管理
     */
    protected var mBaseLoadService: LoadService<*>? = null
    protected var mBinding: DB? = null
    protected var mActivity: Activity? = null
    protected var mRouter = ARouter.getInstance()

    /**
     * 记录是否第一次进入
     */
    private var isFirst = true
    protected var hasInit = false

    /**
     * 是否为对象复用
     */
    protected var isReuse = false

    /**
     * 状态栏沉浸
     */
    private var mImmersionBar: ImmersionBar? = null
    protected var savedInstanceState: Bundle? = null
    protected fun onRevisible() {}
    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        mActivity = activity as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApplication = instance
        mRouter.inject(this)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isReuse = mView != null
        if (isReuse) {
            val parent = mView!!.parent as ViewGroup
            parent?.removeView(mView)
        } else {
            mView = inflater.inflate(R.layout.common_layout_root, container, false)
        }
        // 初始化沉浸式状态栏
        if (isStatusBarEnabled) {
            statusBarConfig.init()
        }
        this.savedInstanceState = savedInstanceState
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isReuse) {
            initParams()
            if (!enableLazy()) {
                loadView()
                initView()
                initListener()
                initData()
                hasInit = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (isStatusBarEnabled) {
            // 重新初始化状态栏
            statusBarConfig.init()
        }
        if (!isReuse && enableLazy() && isFirst) {
            loadView()
            initView()
            initListener()
            initData()
            hasInit = true
        }
        if (!isFirst) {
            onRevisible()
        }
        isFirst = false
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
     * 是否在 Fragment 使用沉浸式
     */
    val isStatusBarEnabled: Boolean
        get() = false

    /**
     * 获取状态栏沉浸的配置对象
     */
    protected val statusBarConfig: ImmersionBar
        protected get() {
            if (mImmersionBar == null) {
                mImmersionBar = createStatusBarConfig()
            }
            return mImmersionBar!!
        }

    /**
     * 初始化沉浸式
     */
    protected fun createStatusBarConfig(): ImmersionBar {
        return ImmersionBar.with(this)
            .statusBarDarkFont(isStatusBarDarkFont)
            .navigationBarColor(android.R.color.white)
            .autoDarkModeEnable(true, 0.2f)
    }

    /**
     * 获取状态栏字体颜色
     */
    protected val isStatusBarDarkFont: Boolean
        protected get() = true

    override fun loadView() {
        val mViewStubContent = mView!!.findViewById<ViewStub>(R.id.vs_content)
        mViewStubContent.layoutResource = onBindLayout()
        val contentView = mViewStubContent.inflate()
        mBinding = DataBindingUtil.bind(contentView)
        val builder = LoadSir.Builder()
            .addCallback(initStatus!!)
            .addCallback(emptyStatus!!)
            .addCallback(errorStatus!!)
            .addCallback(loadingStatus!!)
            .setDefaultCallback(SuccessCallback::class.java)
        if (onBindLoadSir() != null) {
            mBaseLoadService = builder.build()
                .register(onBindLoadSir(), OnReloadListener { v: View? -> onReload(v) })
        }
    }

    /**
     * 是否开启懒加载,默认true
     *
     * @return
     */
    protected fun enableLazy(): Boolean {
        return true
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        mDisposables.add(disposable)
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
            mLoadingHandler.postDelayed({
                mBaseLoadService!!.showCallback(
                    initStatus!!.javaClass
                )
            }, 300)
        }
    }

    /**
     * 显示出错状态页
     */
    open fun showErrorView() {
        clearStatus()
        if (mBaseLoadService != null) {
            mBaseLoadService!!.showCallback(errorStatus!!.javaClass)
        }
    }

    /**
     * 显示空状态页
     */
    open fun showEmptyView() {
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
        mBaseLoadService!!.setCallBack(loadingStatus!!.javaClass) { context: Context?, view1: View ->
            val tvTip = view1.findViewById<TextView>(R.id.tv_tip)
                ?: throw IllegalStateException(loadingStatus!!.javaClass.toString() + "必须带有显示提示文本的TextView,且id为R.id.tv_tip")
            if (tip == null) {
                tvTip.visibility = View.GONE
            } else {
                tvTip.visibility = View.VISIBLE
                tvTip.text = tip
            }
        }
        postDelayed(mLoadStatusRun, 300)
        if (null != mBaseLoadService) {
            mBaseLoadService!!.showCallback(loadingStatus!!.javaClass)
        }
    }

    protected fun post(runnable: Runnable?) {
        mHandler.post(runnable!!)
    }

    protected fun postDelayed(runnable: Runnable?, delayMillis: Long) {
        mHandler.postDelayed(runnable!!, delayMillis)
    }

    private val mLoadStatusRun =
        Runnable { mBaseLoadService!!.showCallback(loadingStatus!!.javaClass) }

    /**
     * 清除所有状态页
     */
    fun clearStatus() {
        mHandler.removeCallbacks(mLoadStatusRun)
        mLoadingHandler.removeCallbacksAndMessages(null)
        if (null != mBaseLoadService) {
            mBaseLoadService!!.showSuccess()
        }
    }

    /**
     * 点击状态页默认执行事件
     */
    protected fun onReload(v: View?) {
        showLoadingView("请稍后...")
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoadingHandler.removeCallbacksAndMessages(null)
        mHandler.removeCallbacksAndMessages(null)
        EventBus.getDefault().unregister(this)
        mDisposables.clear()
    }
}