package com.qinxin.common.net.exception

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @author dlm
 * @description 所有异常都会经过此处, 可拦截需要重试的内部异常, 如Token超时等
 */
class ExceptionRetry : Function<Observable<Throwable?>, Observable<*>> {
    @Throws(Exception::class)
    override fun apply(observable: Observable<Throwable?>): Observable<*> {
        return observable.compose { upstream: Observable<Throwable?> ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
            .flatMap { throwable: Throwable? ->
                Observable.error<Any?>(
                    throwable
                )
            }
    }
}