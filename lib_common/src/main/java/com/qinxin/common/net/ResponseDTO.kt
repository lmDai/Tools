package com.qinxin.common.net

/**
 * @author dlm
 * @description 返回数据基类
 */
class ResponseDTO<T> {
    @JvmField
    var code = 0

    @JvmField
    var msg: String? = null

    @JvmField
    var data: T? = null
}