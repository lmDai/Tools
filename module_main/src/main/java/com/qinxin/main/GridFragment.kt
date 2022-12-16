package com.qinxin.main

import android.view.View
import com.qinxin.common.bean.MenuBean
import com.qinxin.common.net.BaseFragment
import com.qinxin.main.adapter.NineGridAdapter
import com.qinxin.main.databinding.NineMemuGridviewBinding
import kotlinx.android.synthetic.main.nine_memu_gridview.*

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int) :
    BaseFragment<NineMemuGridviewBinding>() {
    private val nineGridAdapter: NineGridAdapter by lazy {
        NineGridAdapter(
            this.requireContext(),
            data,
            index,
            pageSize
        )
    }

    override fun onBindLayout(): Int {
        return R.layout.nine_memu_gridview
    }

    override fun initView() {
        gridView.run {
            adapter = nineGridAdapter
        }
    }

    override fun initData() {
    }

    override fun onBindLoadSir(): View? {
        return null
    }

}