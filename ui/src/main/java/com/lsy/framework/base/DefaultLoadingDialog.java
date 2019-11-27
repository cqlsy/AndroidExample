/*
 * Copyright (c) 2017 yeeyuntech. All rights reserved.
 */

package com.lsy.framework.base;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lsy.framework.R;

/**
 * Default loading.
 */
public class DefaultLoadingDialog extends Dialog implements ILoadingIndicator {

    private Context mContext;
    private TextView mTvTips;

    public DefaultLoadingDialog(Context context) {
        super(context, R.style.YYDialog_LoadingDialog);
        this.mContext = context;
        init();
    }

    private void init() {
        Window window = getWindow();
        if (window != null) {
            // 避免loading使软键盘收起
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fm_dialog_default_loading, null);
        mTvTips = (TextView) view.findViewById(R.id.yy_default_loading_dialog_tips);
        //设置加载的view
        setContentView(view);
        //设置点击外面不消失
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            mTvTips.setVisibility(View.GONE);
        } else {
            mTvTips.setText(message);
            mTvTips.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void showLoading() {
        if (!isShowing()) {
            show();
        }
    }

    @Override
    public void dismissLoading() {
        if (isShowing()) {
            dismiss();
        }
    }
}
