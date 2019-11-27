package com.lsy.net.cryptoParams

import java.lang.reflect.InvocationHandler

interface IProxyHandler {

    fun getProxy(): InvocationHandler

}