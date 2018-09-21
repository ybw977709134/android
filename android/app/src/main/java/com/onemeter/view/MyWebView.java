package com.onemeter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 描述：自定义webview事件
 * 项目名称：vishow_project
 * 作者：angelyin
 * 时间：2016/3/22 17:19
 * 备注：
 */
public class MyWebView extends WebView {
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }


}
