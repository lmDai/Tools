package com.qinxin.common.net.exception

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * @author dlm
 * @description 异常信息转化
 */
object ExceptionConverter {
    fun convert(e: Throwable): Exception {
        val msg: String? = when (e) {
            is HttpException -> {
                when (e.code().toString()) {
                    NET_ERROR.UNAUTHORIZED -> "操作未授权"
                    NET_ERROR.FORBIDDEN -> "请求被拒绝"
                    NET_ERROR.NOT_FOUND -> "资源不存在"
                    NET_ERROR.REQUEST_TIMEOUT -> "服务器执行超时"
                    NET_ERROR.INTERNAL_SERVER_ERROR, NET_ERROR.SERVICE_UNAVAILABLE -> "服务器正在维护中"
                    else -> "网络错误"
                }
            }
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                "解析错误"
            }
            is ConnectException -> {
                "连接失败"
            }
            is SSLException -> {
                "证书验证失败"
            }
            is ConnectTimeoutException -> {
                "连接超时"
            }
            is SocketTimeoutException -> {
                "连接超时"
            }
            is UnknownHostException -> {
                "网络异常"
            }
            is ClassCastException -> {
                "类型转换异常"
            }
            else -> {
                e.message
            }
        }
        return Exception(msg, e)
    }

    /**
     * 网络错误码
     */
    interface NET_ERROR {
        companion object {
            const val UNAUTHORIZED = "401"
            const val FORBIDDEN = "403"
            const val NOT_FOUND = "404"
            const val REQUEST_TIMEOUT = "408"
            const val INTERNAL_SERVER_ERROR = "500"
            const val SERVICE_UNAVAILABLE = "503"
        }
    }

    /**
     * APP错误码
     */
    interface APP_ERROR {
        companion object {
            /**
             * 成功
             */
            const val SUCCESS = 1

            /**
             * 自定义提示
             */
            const val CUSTOM_INFO = 5001

            /**
             * 未登录或登录状态已过期
             */
            const val NO_LOGIN = 4001

            /**
             * 非法请求
             */
            const val ILLEGAL_REQUEST = 5011

            /**
             * 请求数据大小超出限制
             */
            const val LIMIT_DATA = 5012

            /**
             * 请求方式错误
             */
            const val BAD_WAY_REQUEST = 5013

            /**
             * 服务器异常
             */
            const val SERVER_EXCEPTION = 5020

            /**
             * 未实名认证
             */
            const val NO_AUTH_NAME = 4002

            /**
             * 实名信息已绑定其他手机号
             */
            const val AUTH_NAME_PHONE_BINDED = 4010

            /**
             * 低版本提示
             */
            const val LOW_VERSION = 5002
        }
    }
}