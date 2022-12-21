package com.qinxin.main.viewmodel

import android.app.Application
import com.qinxin.common.SingleLiveEvent
import com.qinxin.common.bean.GirlBean
import com.qinxin.common.bean.JokesListBean
import com.qinxin.common.net.BaseRefreshViewModel
import com.qinxin.common.net.ResponseDTO
import java.util.concurrent.TimeUnit


class ToolsViewModel(
    application: Application,
    model: ToolsModel
) : BaseRefreshViewModel<ToolsModel, JokesListBean>(application, model) {
    private var mEnterMsgEvent: SingleLiveEvent<List<GirlBean>>? = null
    fun getmEnterMsgEvent(): SingleLiveEvent<List<GirlBean>> {
        return createLiveData(mEnterMsgEvent).also { mEnterMsgEvent = it }
    }

    override fun onViewRefresh() {
        mModel.girlRandom()
            .doFinally { super.onViewRefresh() }
            .doOnNext { response: ResponseDTO<List<GirlBean>> ->
                getmEnterMsgEvent().value = response.data
            }
            .delay(5, TimeUnit.SECONDS, true)
            .flatMap { mModel.jokesListRandom() }
            .subscribe({ response: ResponseDTO<List<JokesListBean>> ->
                clearStatusEvent?.call()
                finishRefreshEvent.postValue(response.data)
            }, { e: Throwable ->
                showErrorViewEvent.call()
                e.printStackTrace()
            }
            )
    }
}