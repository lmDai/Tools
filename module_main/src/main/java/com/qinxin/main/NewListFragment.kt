package com.qinxin.main

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.qinxin.common.bean.NewsListBean
import com.qinxin.common.net.BaseRefreshFragment
import com.qinxin.main.adapter.NewsListAdapter
import com.qinxin.main.databinding.FragmentNewsBinding
import com.qinxin.main.viewmodel.ViewModelFactory
import com.qinxin.main.viewmodel.NewsListViewModel
import kotlinx.android.synthetic.main.fragment_news.*

class NewListFragment(var typeId: String?) :
    BaseRefreshFragment<FragmentNewsBinding, NewsListViewModel, NewsListBean>() {
    private val mAdapter by lazy { NewsListAdapter() }

    override fun onBindLayout(): Int {
        return R.layout.fragment_news
    }

    override fun initView() {
        rv_content.run {
            layoutManager = LinearLayoutManager(mActivity)
            adapter = mAdapter
        }
    }

    override fun initData() {
        mViewModel?.typeId = typeId
        mViewModel?.onViewRefresh()
    }

    override fun onBindLoadSir(): View? {
        return null
    }

    override fun onBindViewModel(): Class<NewsListViewModel>? {
        return NewsListViewModel::class.java
    }

    override fun onBindViewModelFactory(): ViewModelProvider.Factory? {
        return ViewModelFactory.getInstance(mApplication!!)
    }

    override fun initViewObservable() {
    }

    override fun onBindWrapRefresh(): WrapRefresh {
        return WrapRefresh(refresh, mAdapter as BaseQuickAdapter<NewsListBean, BaseViewHolder>)
    }
}