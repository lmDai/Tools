package com.qinxin.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.qinxin.common.bean.MenuBean
import com.qinxin.main.GridFragment
import kotlin.math.ceil

class NineViewPagerAdapter(
    fragment: FragmentActivity,
    var data: List<MenuBean>,
    var pageSize: Int
) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return ceil(data.size * 1.0 / pageSize).toInt()
    }

    override fun createFragment(position: Int): Fragment {
        return GridFragment(data.toMutableList(), position, pageSize)
    }
}