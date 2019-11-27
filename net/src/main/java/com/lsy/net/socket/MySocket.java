/*
 * Created by lsy on 2017/12/22.
 * Copyright (c) 2017 sh. All rights reserved.
 */

package com.lsy.net.socket;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.lsy.net.NetManager;
import com.lsy.net.log.LogType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;
import okio.ByteString;

/**
 * Description:
 */
public class MySocket {

    public static final int WS_OPEN = 0;
    public static final int WS_CLOSE = 1;

    private Request mRequest;
    private RealWebSocket mWs;
    private OkHttpClient mOkHttpClient;
    private static MySocket mInstance;
    private Disposable mDisposable;
    private int mStatus;
    private String mUrl;

    private boolean isFirst = true;

    // 保证单列模式
    public static MySocket instance() {
        if (mInstance == null) {
            synchronized (MySocket.class) {
                mInstance = new MySocket();
            }
        }
        return mInstance;
    }

    private MySocket() {
    }

    public void init(String url) {
        mUrl = url;
        initClient();
        mRequest = new Request.Builder()
                .url(url)
                .build();
        mWs = new RealWebSocket(mRequest, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                if (isFirst) {
                    isFirst = false;
                }
                mStatus = WS_OPEN;
                NetManager.Companion.getInstance().log(LogType.DeBug, "SOCKET    连接成功了");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                mStatus = WS_OPEN;
                if (!TextUtils.isEmpty(text)) {
                    if (mOnWsGetMsg != null) {
                        mOnWsGetMsg.onGetMsg(text);
                    }
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
                mStatus = WS_OPEN;
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                mStatus = WS_CLOSE;
//                AppBus.post(new AppEvent(AppEventType.WS_CLOSE));
                NetManager.Companion.getInstance().log(LogType.DeBug, "SOCKET    关了");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                mStatus = WS_CLOSE;
//                AppBus.post(new AppEvent(AppEventType.WS_CLOSE));
                NetManager.Companion.getInstance().log(LogType.DeBug, "SOCKET    连接错误了");

            }

        }, new SecureRandom(), 0);

//        pollingSendMsg();
    }

    public void connect() {
        // 这里直接再次新建连接
        init(mUrl);
        mWs.connect(mOkHttpClient);
    }

    // 心跳发送
    public void sendHeart() {
        mDisposable = Observable.interval(10 * 1000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (mStatus == WS_CLOSE) {
                            connect();
                            return;
                        }
                        sendMsg("{\"event\":\"heart\"}");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时
        builder.connectTimeout(12, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        mOkHttpClient = builder.build();
    }

    // 发送消息
    // 轮询发消息
    public void sendMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (mWs == null) {
            // 重新连接 并且去发送消息
            NetManager.Companion.getInstance().log(LogType.Error, "SOCKET   ==== 连接错误,请检查网络连接状况");
            return;
        }
        if (!mWs.send(msg)) {
            NetManager.Companion.getInstance().log(LogType.Error, "SOCKET   ==== send msg : failed");
            return;
        }
        NetManager.Companion.getInstance().log(LogType.Error, "SOCKET   ==== send msg :" + msg);
    }

    // 释放支援
    public void release() {
        mWs.close(1001, "关闭服务");
        mDisposable.dispose();
    }


    private List<String> mMsg = new ArrayList<>();

    /**
     * 轮询发消息
     */
    private void pollingSendMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mStatus == WS_OPEN) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (String s : mMsg) {
                            sendMsg(s);
                        }
                        mMsg.clear();
                    }
                }
            }
        }).start();
    }

    private int mMsgCount = 1;

    /**
     * 设置缓存的消息有几条
     *
     * @param count
     */
    public void setMsgCount(int count) {
        if (count < 1) {
            return;
        }
        mMsgCount = count;
    }

    /**
     * @param msg
     * @param isFirst 是否最先发这条消息
     */
    public void setMsg(String msg, boolean isFirst) {
        if (mMsg.size() < mMsgCount) {
            if (isFirst) {
                mMsg.add(0, msg);
            } else {
                mMsg.add(msg);
            }
        } else {
            if (isFirst) {
                mMsg.remove(0);
                mMsg.add(0, msg);
            }
        }
    }

    private OnWsGetMsg mOnWsGetMsg;

    public void setOnGetMsg(OnWsGetMsg onGetMsg) {
        mOnWsGetMsg = onGetMsg;
    }

    public interface OnWsGetMsg {
        void onGetMsg(String msg);
    }

}
