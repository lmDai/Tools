package com.qinxin.common.net

import android.app.Application
import com.qinxin.common.SingleLiveEvent

/**
 * <br></br>Description:配合BaseRefreshMvvmFragment使用
 *
 * @author xhkj
 */
open class BaseRefreshViewModel<M : BaseModel?, T>(application: Application, model: M) :
    BaseViewModel<M>(application, model) {
    private var mFinishRefreshEvent: SingleLiveEvent<List<T>>? = null
    private var mFinishLoadmoreEvent: SingleLiveEvent<List<T>>? = null
    private var mRefreshEvent: SingleLiveEvent<List<T>>? = null
    fun getmRefreshEvent(): SingleLiveEvent<List<T>> {
        return createLiveData(mRefreshEvent).also { mRefreshEvent = it }
    }

    /**
     * null:失败,size==0:成功,size!=0:执行onRefeshSucc
     */
    val finishRefreshEvent: SingleLiveEvent<List<T>>
        get() = createLiveData(mFinishRefreshEvent).also { mFinishRefreshEvent = it }

    /**
     * null:失败,size==0:成功,size!=0:执行onLoadmoreSucc
     */
    val finishLoadmoreEvent: SingleLiveEvent<List<T>>
        get() = createLiveData(mFinishLoadmoreEvent).also { mFinishLoadmoreEvent = it }

    /**
     * 当界面下拉刷新时,默认直接结束刷新
     */
    open fun onViewRefresh() {
        getmRefreshEvent().postValue(emptyList())
    }

    /**
     * 当界面下拉更多时,默认没有更多数据
     */
    open fun onViewLoadmore() {
        finishLoadmoreEvent.postValue(emptyList())
    }
}