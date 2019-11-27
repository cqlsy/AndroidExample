package com.lsy.test.net

object APIPath {

    /**
     *  登录接口
     *
     *  phoneNumber	        String      手机号码
     *  password	        String      验证码
     *  port_type	        int         1:B端 2：C端 3:PC端
     *  device_sn 可选	    string      手机设备标识 b端C端必须传
     */
    const val PATH_LOGIN = "/api/login/login"


}