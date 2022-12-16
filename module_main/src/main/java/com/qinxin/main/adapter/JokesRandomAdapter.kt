package com.qinxin.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.qinxin.common.bean.JokesListBean
import com.qinxin.main.R

class JokesRandomAdapter() :
    BaseQuickAdapter<JokesListBean?, BaseViewHolder>(R.layout.item_jokes) {
    override fun convert(holder: BaseViewHolder, item: JokesListBean?) {
        holder.setText(R.id.tv_month, item?.content)
            .setText(R.id.tv_time, item?.updateTime)
    }
}