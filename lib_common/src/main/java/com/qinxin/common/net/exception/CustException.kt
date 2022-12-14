package com.qinxin.common.net.exception

/**
 * @author dlm
 * @description 自定义异常类
 */
class CustException : Exception {
    var code: Int
    override var message: String
    var data: Any? = null

    constructor(code: Int, message: String) : super(message) {
        this.code = code
        this.message = message
    }

    constructor(code: Int, message: String, data: Any?) : super(message) {
        this.code = code
        this.message = message
        this.data = data
    }

    override fun toString(): String {
        return "CustException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}'
    }
}