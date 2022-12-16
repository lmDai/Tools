package com.qinxin.common.net

import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.animation.AlphaInAnimation
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * <br></br>Description:自动处理刷新Fragment基类
 *
 * @author xhkj
 */
abstract class BaseRefreshActivity<DB : ViewDataBinding?, VM : BaseRefreshViewModel<*, *>, T> :
    BaseMvvmActivity<DB, VM>(), OnRefreshLoadMoreListener {
    private var mWrapRefresh: WrapRefresh? = null

    @CallSuper
    override fun initListener() {
        super.initListener()
        mWrapRefresh = onBindWrapRefresh()
        mWrapRefresh!!.refreshLayout.setOnRefreshLoadMoreListener(this)
    }

    /**
     * 绑定刷新组件
     *
     * @return
     */
    protected abstract fun onBindWrapRefresh(): WrapRefresh
    override fun initBaseViewObservable() {
        super.initBaseViewObservable()
        mViewModel!!.finishRefreshEvent.observe(this) {
            onRefreshSucc(it)
            if (it!!.isEmpty() || it.size < 3) {
                mWrapRefresh?.refreshLayout?.finishRefreshWithNoMoreData()
            } else {
                mWrapRefresh?.refreshLayout?.finishRefresh()
            }
        }
        mViewModel!!.finishLoadmoreEvent.observe(this) label@{
            onLoadMoreSucc(it)
            if (it!!.isEmpty()) {
                mWrapRefresh?.refreshLayout?.finishLoadMoreWithNoMoreData()
                return@label
            }
            if (it!!.size < 3) {
                mWrapRefresh?.refreshLayout?.finishLoadMoreWithNoMoreData()
            } else {
                mWrapRefresh?.refreshLayout?.finishLoadMore()
            }
        }
        mViewModel!!.getmRefreshEvent().observe(
            this
        ) { mWrapRefresh?.refreshLayout?.finishRefresh() }
    }

    private fun onRefreshSucc(it: List<Any?>?) {
        mWrapRefresh?.quickAdapter?.setNewInstance(it as MutableList<T>)
    }

    private fun onLoadMoreSucc(it: List<Any?>?) {
        mWrapRefresh?.quickAdapter?.addData(it as MutableList<T>)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel!!.onViewLoadmore()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel!!.onViewRefresh()
    }

    protected inner class WrapRefresh(
        var refreshLayout: SmartRefreshLayout,
        var quickAdapter: BaseQuickAdapter<T, BaseViewHolder>?
    ) {
        init {
            if (quickAdapter != null) {
                quickAdapter!!.adapterAnimation = AlphaInAnimation()
            }
        }
    }

    override fun showErrorView() {
        if ((mWrapRefresh?.quickAdapter?.data)!!.isEmpty()) {
            super.showErrorView()
        }
    }

    override fun showEmptyView() {
        if ((mWrapRefresh?.quickAdapter?.data)!!.isNotEmpty()) {
            mWrapRefresh?.quickAdapter?.data?.clear()
            mWrapRefresh?.quickAdapter?.notifyDataSetChanged()
        }
        super.showEmptyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != mWrapRefresh) {
            mWrapRefresh!!.refreshLayout.setOnRefreshLoadMoreListener(null)
        }
    }
}