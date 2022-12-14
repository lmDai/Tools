package com.qinxin.common.net

import com.qinxin.common.bean.GirlBean
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ToolService {
    //登录接口
    @GET("image/girl/list/random")
    fun girlRandom(
        @Query("app_id") appId: String,
        @Query("app_secret") appSecret: String
    ): Observable<ResponseDTO<List<GirlBean>>>
}