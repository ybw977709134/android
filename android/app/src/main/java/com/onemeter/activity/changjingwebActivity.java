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
 * 描述：场景详情编辑器H5
 * 项目名称：android
 * 作者：angelyin
 * 时间：2016/4/27 10:16
 * 备注：
 */
public class changjingwebActivity extends BaseActivity {
    //场景详情web页面
    private MyWebView  changjing_web_detail_activity;
    //进度条
    private ProgressBar cj_pb;
    Handler  mHandler =new Handler();
    int activityId;
    String  type;
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type= MyApplication.changjing_type.get(0).toString();
        activityId=Integer.valueOf(MyApplication.changjing_position.get(0).toString()).intValue();
        toast("type:" + type + "activityId:" + activityId);
     setContentView(R.layout.activity_changjing_web_activity_layout);
        cj_pb = (ProgressBar) findViewById(R.id.cj_pb);
        cj_pb.setMax(100);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        changjing_web_detail_activity= (MyWebView) findViewById(R.id.changjing_web_detail_activity);
        changjing_web_detail_activity.setVerticalScrollbarOverlay(true);
        WebSettings wSet = changjing_web_detail_activity.getSettings();
        //加上这句话才能使用javascript方法
        wSet.setJavaScriptEnabled(true);
        wSet.setDefaultTextEncodingName("utf-8");
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
        String  url="http://101.200.186.44:8080/h5/views/editor/index.html?id="+activityId+"&type="+type+"&loadType=activity";
        changjing_web_detail_activity.loadUrl(url);
        //在js中调用本地java方法
        changjing_web_detail_activity.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
        //不新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        changjing_web_detail_activity.setWebViewClient(new WebViewClient() {
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

        changjing_web_detail_activity.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                cj_pb.setProgress(progress);
                if (progress == 100) {
                    cj_pb.setVisibility(View.GONE);
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
                changjing_web_detail_activity.loadUrl("javascript:setAppUserInfo('" +UserLoginActivity.userInfos.get(0).getUid()
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
                changjingwebActivity.this.finish();

            }else if(name.equals("场景页")){
                Intent intent=new Intent(changjingwebActivity.this,MainActivity.class);
                startActivity(intent);
                changjingwebActivity.this.finish();
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && changjing_web_detail_activity.canGoBack()) {
            changjing_web_detail_activity.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
