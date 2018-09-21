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
import com.onemeter.app.MyApplication;
import com.onemeter.view.MyWebView;

/**
 * 描述：场景编辑H5页面
 * 作者：angelyin
 * 时间：2016/4/27 13:28
 * 备注：
 */
public class HuoDongbianjiwebActivity  extends BaseActivity {
    private MyWebView huodong_bianji_web;
    private ProgressBar  huodong_pb;
    Handler  mHandler  =new Handler();
    private  int activityId;
    private  String type;
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type= MyApplication.changjing_type.get(0).toString();
        activityId=Integer.valueOf(MyApplication.changjing_position.get(0).toString()).intValue();
        toast("type:"+type+"activityId:"+activityId);
   setContentView(R.layout.activity_huodong_bianji_web_layout);
        //进度条
        huodong_pb = (ProgressBar) findViewById(R.id.huodong_pb);
        huodong_pb.setMax(100);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        huodong_bianji_web= (MyWebView) findViewById(R.id.huodong_bianji_web);
        WebSettings wSet = huodong_bianji_web.getSettings();
        //加上这句话才能使用javascript方法
        wSet.setJavaScriptEnabled(true);
//        wSet.setSupportZoom(true);
        wSet.setDefaultTextEncodingName("utf-8");
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
        String  url="http://101.200.186.44:8080/h5/views/editor/index.html?id="+activityId+"&type="+type+"&loadType=activity";
        huodong_bianji_web.loadUrl(url);
        //在js中调用本地java方法
        huodong_bianji_web.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
        //不新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        huodong_bianji_web.setWebViewClient(new WebViewClient() {
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

        huodong_bianji_web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                huodong_pb.setProgress(progress);
                if (progress == 100) {
                    huodong_pb.setVisibility(View.GONE);
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
                huodong_bianji_web.loadUrl("javascript:setAppUserInfo('" +UserLoginActivity.userInfos.get(0).getUid()
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
            if(name.equals("场景类型")){
                HuoDongbianjiwebActivity.this.finish();
            }else if(name.equals("场景页")){
                Intent intent=new Intent(HuoDongbianjiwebActivity.this,MainActivity.class);
                startActivity(intent);
                HuoDongbianjiwebActivity.this.finish();
            }

        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && huodong_bianji_web.canGoBack()) {
            huodong_bianji_web.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    }
