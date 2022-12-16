package com.qinxin.salarylife.esign.viewmodel

import android.app.Application
import com.qinxin.common.bean.NewsListBean
import com.qinxin.common.net.BaseRefreshViewModel
import com.qinxin.common.net.ResponseDTO
import com.qinxin.main.viewmodel.ToolsModel

class NewsListViewModel(
    application: Application,
    model: ToolsModel
) : BaseRefreshViewModel<ToolsModel, NewsListBean>(application, model) {
    var page: Int = 1
    var typeId: String? = null
    override fun onViewLoadmore() {
        mModel.newsList(typeId, page)
            .subscribe({ response: ResponseDTO<List<NewsListBean>> ->
                page++
                finishLoadmoreEvent.value = response.data
            }, { e: Throwable ->
                finishLoadmoreEvent.call()
                e.printStackTrace()
            }
            )
    }

    override fun onViewRefresh() {
        page = 1
        mModel.newsList(typeId, page)
            .doFinally { super.onViewRefresh() }
            .subscribe({ response: ResponseDTO<List<NewsListBean>> ->
                clearStatusEvent?.call()
                page++
                finishRefreshEvent.postValue(response.data)
            }, { e: Throwable ->
                showErrorViewEvent.call()
                e.printStackTrace()
            }
            )
    }
}