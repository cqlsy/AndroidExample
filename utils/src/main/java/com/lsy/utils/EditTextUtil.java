/*
 * Copyright (c)
 * BlackBoy,  All rights reserved.
 * Time: 2019-11-21
 */

package com.lsy.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.lsy.utils.TextUtil.formatNum;
import static com.lsy.utils.TextUtil.getDouble;


/**
 * Created by adu on 2017/5/20.
 * EditText相关工具类
 */

public class EditTextUtil {

    /**
     * @param editTexts EditText
     * @param doubles   EditText权重
     * @param accuracy  计算精度
     */
    public static void syncEditText(final EditText[] editTexts, final double[] doubles, final int[] accuracy) {
        for (int i = 0; i < editTexts.length; i++) {
            final int finalI = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTexts[finalI].hasFocus()) {
                        //如果权重为0,不影响其他值
                        if (doubles[finalI] == 0) {
                            return;
                        }
                        double value = getDouble(s.toString());
                        //计算 值/比重
                        double weight = value / doubles[finalI];
                        for (int j = 0; j < editTexts.length; j++) {
                            if (j != finalI) {
                                //需要同步
                                editTexts[j].setText(formatNum(weight * doubles[j], accuracy[j]));
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }


    /**
     * 检测editText是否符合条件，并设置btn点击
     *
     * @param editTexts
     * @param regxs     二维数组，每个EditText至少传一个正则，可以为空字符串
     * @param mBtn
     */
    public static void checkEditText(final EditText[] editTexts, final String[] regxs, final View mBtn) {
        for (int i = 0; i < editTexts.length; i++) {
            final int finalI = i;
            // 设置变化监听
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTexts[finalI].hasFocus()) {
                        checkEditText(editTexts[finalI], regxs[finalI]);
                        boolean isFill = true;
                        for (EditText editText : editTexts) {
                            isFill = isFill && (boolean) (editText.getTag() == null ? false : editText.getTag());
                        }
                        mBtn.setEnabled(isFill);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            // 设置监听器的同时检测正则
            checkEditText(editTexts[finalI], regxs[finalI]);
        }
        // 首次检测设置
        boolean isFill = true;
        for (EditText editText : editTexts) {
            isFill = isFill && (boolean) (editText.getTag() == null ? false : editText.getTag());
        }
        mBtn.setEnabled(isFill);
    }

    /**
     * 检测editText是否符合条件，并设置btn点击
     *
     * @param editTexts
     * @param regxs     二维数组，每个EditText至少传一个正则，可以为空字符串
     * @param mBtn
     */
    public static List<TextWatcher> checkEditTextWatcher(final EditText[] editTexts, final String[] regxs, final View mBtn) {
        List<TextWatcher> watchers = new ArrayList<>();
        for (int i = 0; i < editTexts.length; i++) {
            final int finalI = i;
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editTexts[finalI].hasFocus()) {
                        checkEditText(editTexts[finalI], regxs[finalI]);
                        boolean isFill = true;
                        for (EditText editText : editTexts) {
                            isFill = isFill && (boolean) (editText.getTag() == null ? false : editText.getTag());
                        }
                        mBtn.setEnabled(isFill);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };
            watchers.add(textWatcher);
            // 设置变化监听
            editTexts[i].addTextChangedListener(textWatcher);
            // 设置监听器的同时检测正则
            checkEditText(editTexts[finalI], regxs[finalI]);
        }
        // 首次检测设置
        boolean isFill = true;
        for (EditText editText : editTexts) {
            isFill = isFill && (boolean) (editText.getTag() == null ? false : editText.getTag());
        }
        mBtn.setEnabled(isFill);
        return watchers;
    }

    /**
     * 检测单个EditText是否符合正则
     *
     * @param editText
     * @param regex
     */
    private static void checkEditText(EditText editText, String regex) {
        String text = editText.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(regex)) {
            if (!TextUtils.isEmpty(text)) {
                editText.setTag(true);
            } else {
                editText.setTag(false);
            }
        } else {
            Pattern pattern = Pattern.compile(regex);
            boolean is = pattern.matcher(text).matches();
            if (is) {
                editText.setTag(true);
            } else {
                editText.setTag(false);
            }
        }
    }


    /**
     * 设置EditText光标到末尾
     *
     * @param editText
     */
    public static void setCursorEnd(EditText editText) {
        editText.setSelection(editText.getText().toString().length());
    }

}
