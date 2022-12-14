package com.qinxin.common.net

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider


/**
 * @author dlm
 */
abstract class BaseMvvmActivity<DB : ViewDataBinding?, VM : BaseViewModel<*>> :
    BaseActivity<DB>() {
    @JvmField
    protected var mViewModel: VM? = null
    override fun initParams() {
        initViewModel()
        initBaseViewObservable()
        initViewObservable()
    }

    override fun initView() {
        showInitView()
    }

    private fun initViewModel() {
        mViewModel = createViewModel()
        lifecycle.addObserver(mViewModel!!)
    }

    private fun createViewModel(): VM {
        return ViewModelProvider(this, onBindViewModelFactory()!!)[onBindViewModel()!!]
    }

    /**
     * 绑定viewModel
     *
     * @return class
     */
    protected abstract fun onBindViewModel(): Class<VM>?

    /**
     * 绑定viewModelFactory
     *
     * @return ViewModelProvider.Factory
     */
    protected abstract fun onBindViewModelFactory(): ViewModelProvider.Factory?

    /**
     * 初始化View视图观察者
     */
    protected abstract fun initViewObservable()
    protected open fun initBaseViewObservable() {
        mViewModel!!.showInitViewEvent.observe(this) { showInitView() }
        mViewModel!!.showLoadingViewEvent?.observe(
            this
        ) { tip: String? -> showLoadingView(tip) }
        mViewModel!!.showEmptyViewEvent?.observe(this) { showEmptyView() }
        mViewModel!!.showErrorViewEvent.observe(this) { showErrorView() }
        mViewModel!!.finishSelfEvent?.observe(this) { finish() }
        mViewModel!!.clearStatusEvent?.observe(this) { clearStatus() }
    }
}