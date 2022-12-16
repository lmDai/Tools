package com.qinxin.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.qinxin.common.bean.NewsListBean
import com.qinxin.main.R

class NewsListAdapter() :
    BaseQuickAdapter<NewsListBean?, BaseViewHolder>(R.layout.item_news) {
    override fun convert(holder: BaseViewHolder, item: NewsListBean?) {
        holder.setText(R.id.tv_month, item?.title)
    }
}