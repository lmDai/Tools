package com.qinxin.main.viewmodel

import android.app.Application
import com.qinxin.common.SingleLiveEvent
import com.qinxin.common.bean.NewsTypeBean
import com.qinxin.common.net.BaseViewModel
import com.qinxin.common.net.ResponseDTO


class NewsViewModel(
    application: Application,
    model: ToolsModel
) : BaseViewModel<ToolsModel>(application, model) {
    private var mEnterMsgEvent: SingleLiveEvent<List<NewsTypeBean>>? = null
    fun getmEnterMsgEvent(): SingleLiveEvent<List<NewsTypeBean>> {
        return createLiveData(mEnterMsgEvent).also { mEnterMsgEvent = it }
    }

    fun newsTypes() {
        mModel.newsTypes()
            .subscribe({ response: ResponseDTO<List<NewsTypeBean>> ->
                clearStatusEvent?.call()
                getmEnterMsgEvent().value = response.data
            }, { e: Throwable ->
                showErrorViewEvent.call()
                e.printStackTrace()
            }
            )
    }
}