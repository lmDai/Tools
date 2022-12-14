package com.qinxin.common.net.exception

/**
 * @author dlm
 * @description 可以被拦截的异常类
 */
class InterceptableException : Exception {

    var code: Int
    override var message: String? = null

    constructor(code: Int, message: String?) {
        this.code = code
        this.message = message
    }

    constructor(throwable: Throwable?, code: Int) : super(throwable) {
        this.code = code
    }

    override fun toString(): String {
        return "CustException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}'
    }

    companion object {
        const val TOKEN_OUTTIME = "50011"
    }
}