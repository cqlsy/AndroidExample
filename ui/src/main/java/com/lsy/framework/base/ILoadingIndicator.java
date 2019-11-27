/*
 * Copyright (c) 2017 yeeyuntech. All rights reserved.
 */

package com.lsy.framework.base;

/**
 * loading 继承该接口实现 loading
 */
public interface ILoadingIndicator {

    void showLoading();

    void dismissLoading();
}
