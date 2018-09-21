package com.onemeter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.view.MyWebView;

/**
 * 描述：去相册活动页面
 * 项目名称：android
 * 作者: angelyin
 * 时间：2016/4/28 19:25
 * 备注：
 */
public class QuxiangceHuoDongWeb extends BaseActivity {
    private int activityId;
    private String type;
    private MyWebView quxiangce_huodong_webview;
    private ProgressBar pb;
    Handler mHandler = new Handler();
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        activityId = getIntent().getIntExtra("activityId", 0);
        setContentView(R.layout.activity_quxiangce_huo_dong_layout);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);
        initView();


    }

    /**
     * 初始化组合键
     */
    private void initView() {
        quxiangce_huodong_webview = (MyWebView) findViewById(R.id.quxiangce_huodong_webview);
        quxiangce_huodong_webview.setVerticalScrollbarOverlay(true);
        WebSettings wSet = quxiangce_huodong_webview.getSettings();
        //加上这句话才能使用javascript方法
        wSet.setJavaScriptEnabled(true);
        wSet.setDefaultTextEncodingName("utf-8");
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
        String url = "http://101.200.186.44:8080/h5/views/editor/index.html?id=" + activityId + "&type=" + type + "&loadType=activity";
        quxiangce_huodong_webview.loadUrl(url);
        //不新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        quxiangce_huodong_webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        quxiangce_huodong_webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                pb.setProgress(progress);
                if (progress == 100) {
                    pb.setVisibility(View.GONE);
                    InfoFromJava();
                }
                super.onProgressChanged(view, progress);
            }
        });
    }

    /**
     * android向H5传递数据
     */
    private void InfoFromJava() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                quxiangce_huodong_webview.loadUrl("javascript:setAppUserInfo('" + UserLoginActivity.userInfos.get(0).getUid()
                        + "')");
            }
        });

    }

    //实现js回调接口
    private class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.closeWebToApp(name),关闭web，跳转App
        @JavascriptInterface
        public void closeWebToApp(String name) {
            //判断返回键
            if (name.equals("场景类型")) {
                QuxiangceHuoDongWeb.this.finish();

            } else if (name.equals("场景页")) {
                Intent intent = new Intent(QuxiangceHuoDongWeb.this, MainActivity.class);
                startActivity(intent);
                QuxiangceHuoDongWeb.this.finish();
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && quxiangce_huodong_webview.canGoBack()) {
            quxiangce_huodong_webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
