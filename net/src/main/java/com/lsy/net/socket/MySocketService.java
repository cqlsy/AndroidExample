/*
 * Created by lsy on 2017/12/22.
 * Copyright (c) 2017 sh. All rights reserved.
 */

package com.lsy.net.socket;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.*;

import androidx.annotation.Nullable;

/**
 * 后续再来调整
 * Description:
 */
public class MySocketService extends Service {

    public static boolean mIsLaunch = false;

    public static void launch(Activity from) {
        if (mIsLaunch) {
            return;
        }
        mIsLaunch = true;
        Intent intent = new Intent(from, MySocketService.class);
        from.startService(intent);
    }

    public static void stopService(Activity from) {
        if (!mIsLaunch) {
            return;
        }
        mIsLaunch = false;
        Intent intent = new Intent(from, MySocketService.class);
        from.stopService(intent);
    }

    private final String TAG = MySocketService.class.getSimpleName();
    private MySocket mCws;

    private Looper mLooper;
    private ServiceHandler mServiceHandler;

    private class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private void resetService() {
        mCws.release();
        init();
    }

    private void init() {
        // 开启服务
        mCws = MySocket.instance();
        /*  mCws.init(AppCfg.SERVER_SOCKET + "/api/light/v1/app/coin/ws");*/
        mCws.connect();
        mCws.sendHeart(); // 服务开始就开始发心跳
        mCws.setOnGetMsg(new MySocket.OnWsGetMsg() {
            @Override
            public void onGetMsg(String msg) {
                // 通知发生变化
                /*AppBus.post(new AppEvent(AppEventType.WS_GET_MESSAGE, msg)); // 全网的数据有变化*/
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        mLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mLooper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭 webSocket
        if (mCws != null) {
            mCws.release();
        }
        mIsLaunch = false;
        if (mLooper != null) {
            mLooper.quit();
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        init();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

  /*  @Subscribe
    public void onAppevent(AppEvent event) {
        switch (event.getType()) {
            case WS_SEND_MSG:
                sendMsg(event.getDatas());
                break;
            case WS_SEND_MSG_POLL:
                sendMsgPoll(event.getDatas());
                break;
            case NET_NODE_CHANGE:
                resetService();
                break;
        }
    }*/

    //  service 作为中间站
    private void sendMsg(final String msg) {
        if (Thread.currentThread() != mLooper.getThread()) {
            mServiceHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCws.sendMsg(msg);
                }
            });
        } else {
            mCws.sendMsg(msg);
        }
    }

    //  service 作为中间站
    private void sendMsgPoll(final String msg) {
        if (Thread.currentThread() != mLooper.getThread()) {
            mServiceHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCws.setMsg(msg, true);
                }
            });
        } else {
            mCws.setMsg(msg, true);
        }
    }

}
