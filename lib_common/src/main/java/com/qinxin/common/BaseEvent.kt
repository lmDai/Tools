package com.qinxin.common

/**
 * @author dlm
 * @description EventBus基础类
 */
class BaseEvent {
    var code: Int
    var data: Any? = null

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: Any?) {
        this.code = code
        this.data = data
    }
}