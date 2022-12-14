package com.qinxin.common.net

import com.qinxin.common.net.exception.CustException
import com.qinxin.common.net.exception.ExceptionConverter
import com.qinxin.common.net.exception.ExceptionRetry
import com.qinxin.common.net.exception.InterceptableException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @author dlm
 * @description Rx适配器
 */
object RxAdapter {
    /**
     * 线程调度器
     */
    fun <T : Any> schedulersTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 异常处理方式
     * <br></br>StreamHandler(将内部异常选择性抛出,可设置需要重试的异常)->
     * <br></br>ExceptionRetry(所有异常都会经过此处,可拦截需要重试的内部异常,如Token超时等)->
     * <br></br>ExceptionHandler(统一处理未被拦截内部异常和所有外部异常)
     */
    fun <T : Any> exceptionTransformer(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable: Observable<T> ->
            observable
                .flatMap(StreamHandler())
                .retryWhen(ExceptionRetry()) //拦截需要处理的异常
                .onErrorResumeNext(ExceptionHandler())
        }
    }

    /**
     * 将内部异常选择性抛出,可设置需要重试的异常
     */
    private class StreamHandler<T : Any> : Function<T, ObservableSource<T>> {
        override fun apply(o: T): Observable<T> {
            return handle(o)
        }

        private fun handle(o: T): Observable<T> {
            if (o is ResponseDTO<*>) {
                val respDTO = o as ResponseDTO<*>
                //选择性抛出内部异常
                if (respDTO.code != ExceptionConverter.APP_ERROR.SUCCESS) {
                    var throwable: Exception =
                        CustException(respDTO.code, respDTO.msg!!, respDTO.data)
                    //如果是token超时,则尝试重试
                    if (respDTO.code == ExceptionConverter.APP_ERROR.NO_LOGIN) {
                        throwable = InterceptableException(respDTO.code, respDTO.msg)
                    }
                    return Observable.error(throwable)
                }
            }
            return Observable.just(o)
        }
    }

    /**
     * 统一处理未被拦截内部异常和所有外部异常
     */
    private class ExceptionHandler<T : Any> : Function<Throwable, Observable<T>> {
        override fun apply(t: Throwable): Observable<T> {
            return handle(t)
        }

        private fun handle(t: Throwable): Observable<T> {
            return Observable.error(t)
        }
    }
}