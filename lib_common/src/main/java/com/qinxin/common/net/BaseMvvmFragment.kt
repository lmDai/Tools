package com.qinxin.common.net

import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

/**
 * @author dlm
 * @description MvvmFragment基类
 */
abstract class BaseMvvmFragment<DB : ViewDataBinding?, VM : BaseViewModel<*>> :
    BaseFragment<DB>() {
    @JvmField
    protected var mViewModel: VM? = null
    override fun initParams() {
        initViewModel()
        initBaseViewObservable()
        initViewObservable()
    }

    @CallSuper
    override fun loadView() {
        super.loadView()
        //默认显示初始化视图
        showInitView()
    }

    protected fun initViewModel() {
        mViewModel = createViewModel()
        lifecycle.addObserver(mViewModel!!)
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this, onBindViewModelFactory()!!)[onBindViewModel()!!]
    }


    /**
     * 绑定ViewModel
     *
     * @return class
     */
    protected abstract fun onBindViewModel(): Class<VM>?

    /**
     * ViewModelFactory
     *
     * @return ViewModelProvider.Factory
     */
    protected abstract fun onBindViewModelFactory(): ViewModelProvider.Factory?

    /**
     * 初始化观察者
     */
    protected abstract fun initViewObservable()
    protected open fun initBaseViewObservable() {
        mViewModel!!.showInitViewEvent.observe(this, Observer { show: Void? -> showInitView() })
        mViewModel!!.showLoadingViewEvent!!.observe(
            this,
            Observer { tip: String? -> showLoadingView(tip) })
        mViewModel!!.showEmptyViewEvent!!.observe(this, Observer { show: Void? -> showEmptyView() })
        mViewModel!!.showErrorViewEvent.observe(this, Observer { show: Void? -> showErrorView() })
        mViewModel!!.finishSelfEvent!!.observe(this, Observer { v: Void? -> })
        mViewModel!!.clearStatusEvent!!.observe(this, Observer { v: Void? -> clearStatus() })
    }
}