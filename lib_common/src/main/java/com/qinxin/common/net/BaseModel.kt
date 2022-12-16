package com.qinxin.common.net

import android.app.Application
import com.qinxin.common.bean.GirlBean
import com.qinxin.common.bean.JokesListBean
import com.qinxin.common.bean.NewsListBean
import com.qinxin.common.bean.NewsTypeBean
import com.qinxin.common.net.RxAdapter.exceptionTransformer
import com.qinxin.common.net.RxAdapter.schedulersTransformer
import io.reactivex.rxjava3.core.Observable

/**
 * @author dlm
 * @description 通用Model
 */
open class BaseModel(protected var mApplication: Application) {

}