package com.qinxin.main

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayoutMediator
import com.gyf.immersionbar.ImmersionBar
import com.qinxin.common.bean.NewsTypeBean
import com.qinxin.common.constants.RouterPaths
import com.qinxin.common.net.BaseMvvmActivity
import com.qinxin.main.databinding.ActivityNewsBinding
import com.qinxin.main.viewmodel.NewsViewModel
import com.qinxin.main.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_news.*

@Route(path = RouterPaths.NEWS_ACTIVITY)
class NewsActivity : BaseMvvmActivity<ActivityNewsBinding, NewsViewModel>(), View.OnClickListener {
    private var tabList: ArrayList<NewsTypeBean> = arrayListOf()
    private lateinit var tabViewPagerAdapter: FragmentStateAdapter
    override fun onBindLayout(): Int {
        return R.layout.activity_news
    }

    override fun initView() {
        initTabViewPagerAdapter()

    }

    override fun initListener() {
        super.initListener()
        toolbar_back.setOnClickListener(this@NewsActivity)
    }

    private fun initTabViewPagerAdapter() {
        tabViewPagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = tabList.size
            override fun createFragment(position: Int): Fragment {
                return NewListFragment(tabList[position].typeId)
            }
        }
        viewPager.adapter = tabViewPagerAdapter
    }

    private fun showTabLayout(list: List<NewsTypeBean>) {
        tabList.clear()
        tabList.addAll(list)
        tabViewPagerAdapter.notifyDataSetChanged()

        viewPager.offscreenPageLimit = list.size
        viewPager.getViewPager2?.let {
            TabLayoutMediator(tabLayout, it) { tab, position ->
                tab.text = list[position].typeName
            }.attach()
        }
    }

    override fun initData() {
        mViewModel?.newsTypes()
    }

    override fun onBindLoadSir(): View? {
        return null
    }

    override fun onBindViewModel(): Class<NewsViewModel>? {
        return NewsViewModel::class.java
    }

    override fun onBindViewModelFactory(): ViewModelProvider.Factory? {
        return ViewModelFactory.getInstance(application)
    }

    override fun initViewObservable() {
        mViewModel?.getmEnterMsgEvent()?.observe(this@NewsActivity, observer = {
            showTabLayout(it!!)
        })
    }

    override fun createStatusBarConfig(): ImmersionBar {
        return super.createStatusBarConfig().titleBar(toolBar)
    }

    override fun onClick(view: View?) {
        when (view) {
            toolbar_back -> finish()
        }
    }

}