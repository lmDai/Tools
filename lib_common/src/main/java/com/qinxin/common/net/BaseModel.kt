package com.qinxin.common.net

import android.app.Application
import com.qinxin.common.bean.GirlBean
import com.qinxin.common.net.RxAdapter.exceptionTransformer
import com.qinxin.common.net.RxAdapter.schedulersTransformer
import io.reactivex.rxjava3.core.Observable

/**
 * @author dlm
 * @description 通用Model
 */
open class BaseModel(protected var mApplication: Application) {
    open fun girlRandom(): Observable<ResponseDTO<List<GirlBean>>> {
        return RetrofitFactory.instance.create(ToolService::class.java)
            .girlRandom("ugbvsoeoqippqgrh", "NE4vY0IxZFc4Tk9xdUdFVjh6THdGZz09")
            .compose(exceptionTransformer())
            .compose(schedulersTransformer())
    }

}