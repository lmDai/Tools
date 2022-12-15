package com.qinxin.main.viewmodel

import android.app.Application
import com.qinxin.common.SingleLiveEvent
import com.qinxin.common.bean.GirlBean
import com.qinxin.common.net.BaseViewModel

class ToolsViewModel(
    application: Application,
    model: ToolsModel
) : BaseViewModel<ToolsModel>(application, model) {
    private var mEnterMsgEvent: SingleLiveEvent<List<GirlBean>>? = null
    fun getmEnterMsgEvent(): SingleLiveEvent<List<GirlBean>> {
        return createLiveData(mEnterMsgEvent).also { mEnterMsgEvent = it }
    }


    fun girlRandom() {
        mModel.girlRandom()
            .doOnSubscribe { showLoadingViewEvent?.call() }
            .doFinally { clearStatusEvent?.call() }
            .subscribe({ response ->
                getmEnterMsgEvent().value = response.data
            }) { e: Throwable ->
                showErrorViewEvent.call()
                e.printStackTrace()
            }
    }
}