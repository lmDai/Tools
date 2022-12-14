package com.qinxin.salarylife.adapter

import android.widget.ImageView
import coil.load
import com.qinxin.common.bean.GirlBean
import com.qinxin.salarylife.R
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

class GirlBannerAdapter : BaseBannerAdapter<GirlBean>() {

    override fun bindData(
        holder: BaseViewHolder<GirlBean>,
        data: GirlBean?,
        position: Int,
        pageSize: Int
    ) {
        val imageStart: ImageView = holder.findViewById(R.id.banner_image)
//        holder.setImageResource(R.id.banner_image, data!!.imageRes)
        imageStart.load(data?.imageUrl)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_banner;
    }
}
