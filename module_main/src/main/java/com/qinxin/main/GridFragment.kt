package com.qinxin.main

import android.util.Log
import android.view.View
import com.qinxin.common.bean.MenuBean
import com.qinxin.common.constants.RouterPaths
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

    override fun initListener() {
        gridView.setOnItemClickListener { p0, p1, p2, p3 ->
            Log.i("single", nineGridAdapter.data[p2].menuName)
            when (p2) {
                0 -> mRouter.build(RouterPaths.KS_ACTIVITY).navigation()

                1 -> mRouter.build(RouterPaths.NEWS_ACTIVITY).navigation()
            }
        }
    }
    override fun initData() {
    }

    override fun onBindLoadSir(): View? {
        return null
    }

}