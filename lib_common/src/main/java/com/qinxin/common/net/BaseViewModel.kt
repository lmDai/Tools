package com.qinxin.common.net

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.qinxin.common.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer


/**
 * <br></br>Description:ViewModel基类
 *
 * @author dlm
 */
open class BaseViewModel<M : BaseModel?>(application: Application, val mModel: M) :
    AndroidViewModel(application), LifecycleObserver, Consumer<Disposable> {
    /**
     * Disposable容器
     */
    private val mDisposables = CompositeDisposable()
    private var showInitLoadViewEvent: SingleLiveEvent<Void>? = null

    /**
     * 常规loading,null:隐藏,"":不带提示,"提示":带提示文本
     *
     * @return
     */
    var showLoadingViewEvent: SingleLiveEvent<String>? = null
        get() = createLiveData(field).also { field = it }
        private set

    /**
     * 数据为空
     *
     * @return
     */
    var showEmptyViewEvent: SingleLiveEvent<Void>? = null
        get() = createLiveData(field).also { field = it }
        private set
    private var showNetErrViewEvent: SingleLiveEvent<Void>? = null

    /**
     * 结束宿主视图
     *
     * @return
     */
    var finishSelfEvent: SingleLiveEvent<Void>? = null
        get() = createLiveData(field).also { field = it }
        private set

    /**
     * 清空所有状态
     *
     * @return
     */
    var clearStatusEvent: SingleLiveEvent<Void>? = null
        get() = createLiveData(field).also { field = it }
        private set

    /**
     * 初始化时loading视图
     *
     * @return
     */
    val showInitViewEvent: SingleLiveEvent<Void>
        get() = createLiveData(showInitLoadViewEvent).also { showInitLoadViewEvent = it }

    /**
     * 网络异常
     *
     * @return
     */
    val showErrorViewEvent: SingleLiveEvent<Void>
        get() = createLiveData(showNetErrViewEvent).also { showNetErrViewEvent = it }

    protected fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
        var liveData = liveData
        if (liveData == null) {
            liveData = SingleLiveEvent()
        }
        return liveData
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable) {
        mDisposables.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onCleared() {
        super.onCleared()
        mDisposables.clear()
    }
}