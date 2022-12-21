package com.qinxin.main.adapter

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.qinxin.common.bean.NewsListBean
import com.qinxin.common.util.CoilUtil
import com.qinxin.main.R
import okio.`-DeprecatedOkio`.source

class NewsListAdapter() :
    BaseQuickAdapter<NewsListBean?, BaseViewHolder>(R.layout.item_news) {
    override fun convert(holder: BaseViewHolder, item: NewsListBean?) {
        holder.setText(R.id.tv_title, item?.title)
            .setText(R.id.postTime, item?.postTime)
            .setText(R.id.source, item?.source)
            .setText(R.id.digest, item?.digest)
        holder.getView<ImageView>(R.id.img)
            .load(item?.imgList?.get(0), CoilUtil.getImageLoader()) {
                crossfade(true)
                placeholder(com.qinxin.common.R.drawable.default_img)
                error(com.qinxin.common.R.drawable.default_img)
            }
    }
}