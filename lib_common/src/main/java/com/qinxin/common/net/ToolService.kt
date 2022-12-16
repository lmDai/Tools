package com.qinxin.common.net

import com.qinxin.common.bean.GirlBean
import com.qinxin.common.bean.JokesListBean
import com.qinxin.common.bean.NewsListBean
import com.qinxin.common.bean.NewsTypeBean
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

    @GET("news/types")
    fun newsTypes(
        @Query("app_id") appId: String,
        @Query("app_secret") appSecret: String
    ): Observable<ResponseDTO<List<NewsTypeBean>>>

    @GET("news/list")
    fun newsList(
        @Query("typeId") typeId: String?,
        @Query("page") page: Int,
        @Query("app_id") appId: String,
        @Query("app_secret") appSecret: String
    ): Observable<ResponseDTO<List<NewsListBean>>>

    @GET("jokes/list/random")
    fun jokesListRandom(
        @Query("app_id") appId: String,
        @Query("app_secret") appSecret: String
    ): Observable<ResponseDTO<List<JokesListBean>>>
}