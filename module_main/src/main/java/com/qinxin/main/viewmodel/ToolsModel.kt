package com.qinxin.main.viewmodel

import android.app.Application
import com.qinxin.common.bean.GirlBean
import com.qinxin.common.bean.JokesListBean
import com.qinxin.common.bean.NewsListBean
import com.qinxin.common.bean.NewsTypeBean
import com.qinxin.common.net.*
import io.reactivex.rxjava3.core.Observable

/**
 * @author dlm
 * @description 接口Model
 */
class ToolsModel(application: Application?) : BaseModel(application!!) {
    fun girlRandom(): Observable<ResponseDTO<List<GirlBean>>> {
        return RetrofitFactory.instance.create(ToolService::class.java)
            .girlRandom("ugbvsoeoqippqgrh", "NE4vY0IxZFc4Tk9xdUdFVjh6THdGZz09")
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun jokesListRandom(): Observable<ResponseDTO<List<JokesListBean>>> {
        return RetrofitFactory.instance.create(ToolService::class.java)
            .jokesListRandom("ugbvsoeoqippqgrh", "NE4vY0IxZFc4Tk9xdUdFVjh6THdGZz09")
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun newsTypes(): Observable<ResponseDTO<List<NewsTypeBean>>> {
        return RetrofitFactory.instance.create(ToolService::class.java)
            .newsTypes("ugbvsoeoqippqgrh", "NE4vY0IxZFc4Tk9xdUdFVjh6THdGZz09")
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }

    fun newsList(typeId: String?, page: Int): Observable<ResponseDTO<List<NewsListBean>>> {
        return RetrofitFactory.instance.create(ToolService::class.java)
            .newsList(typeId, page, "ugbvsoeoqippqgrh", "NE4vY0IxZFc4Tk9xdUdFVjh6THdGZz09")
            .compose(RxAdapter.exceptionTransformer())
            .compose(RxAdapter.schedulersTransformer())
    }
}