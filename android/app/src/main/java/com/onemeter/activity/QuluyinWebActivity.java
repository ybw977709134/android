package com.onemeter.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.entity.userInfo;
import com.onemeter.utils.Constants;
import com.onemeter.view.MyWebView;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.List;


/**
 * 描述：
 * 项目名称：vishow_project
 * 作者：Administrator
 * 时间：2016/3/22 13:28
 * 备注：
 */
public class QuluyinWebActivity extends BaseActivity implements View.OnClickListener {
    private ImageView activity_quluyin_img_return;
    private MyWebView qu_luyin_web;
    private ProgressBar pb;
    private RelativeLayout activity_quluyin_title_rel;
    private IWXAPI wxApi;//微信api
    private Tencent mTencent;//QQ分享api
    int msg=12;
    // 定义数据列表
    private List<userInfo> programList = null;
    Handler mHandler=new Handler();

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quluyin_web_main);
        //实例化
        wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        wxApi.registerApp(Constants.WX_APP_ID);
        //实例化QQ
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);
        programList = UserLoginActivity.userInfos;
//         toast(programList.size()+"");
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        activity_quluyin_title_rel = (RelativeLayout) findViewById(R.id.activity_quluyin_title_rel);
        activity_quluyin_img_return = (ImageView) findViewById(R.id.activity_quluyin_img_return);
        activity_quluyin_img_return.setOnClickListener(this);
        qu_luyin_web = (MyWebView) findViewById(R.id.qu_luyin_web);
        qu_luyin_web.setVerticalScrollbarOverlay(true);
        WebSettings wSet = qu_luyin_web.getSettings();
        //加上这句话才能使用javascript方法
        wSet.setJavaScriptEnabled(true);
//        wSet.setSupportZoom(true);
        wSet.setDefaultTextEncodingName("utf-8");
        wSet.setAllowFileAccess(true);
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
//        qu_luyin_web.loadUrl("http://101.200.186.44:8080/h5/views/editor/index.html");
        qu_luyin_web.loadUrl("file:///android_asset/edit-activity.html");
        //在js中调用本地java方法
        // 把programList添加到js的全局对象window中，这样就可以使用window.programList来获取数据
        qu_luyin_web.addJavascriptInterface(programList,"programList");
        qu_luyin_web.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
        //不新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        qu_luyin_web.setWebViewClient(new WebViewClient() {
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

        qu_luyin_web.setWebChromeClient(new WebChromeClient() {
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


    public void InfoFromJava(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                toast(msg+"");
                qu_luyin_web.loadUrl("javascript:showInfoFromJava('" + msg + "')");

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
            QuluyinWebActivity.this.finish();
            wechatShare(0);
//            toast("传过来的参数：" + name);
        }

        //在js中调用 微信好友分享方法
        @JavascriptInterface
        public void  showWeiXinHShare(){
            wechatShare(0);
        }
        //在js中调用 微信朋友圈分享方法
        @JavascriptInterface
        public  void showWeiXinPShare(){
            wechatShare(1);
        }
        //在js中调用 QQ分享方法
        @JavascriptInterface
        public  void showQQShare(){
//           toast("QQ分享");
            onClickShare();
        }



    }



    /**
     * qq分享功能
     */
    private void onClickShare() {
        ShareListener myListener = new ShareListener();
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "测试标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "测试文本");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://www.baidu.com/");
        mTencent.shareToQQ(QuluyinWebActivity.this, params, myListener);



    }

    /**
     * 微信、朋友圈分享
     * @param flag
     */
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.baidu.com/";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title ="测试标题";
        msg.description = "描述内容";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.banner_1);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 150, 150, true);
//        thumbBmp.recycle();
//        msg.setThumbImage(thumbBmp);
        msg.thumbData= Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_quluyin_img_return:
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && qu_luyin_web.canGoBack()) {
            qu_luyin_web.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            QuluyinWebActivity.this.toast("分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            QuluyinWebActivity.this.toast("分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            QuluyinWebActivity.this.toast("分享出错");
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareListener myListener = new ShareListener();
        Tencent.onActivityResultData(requestCode,resultCode,data,myListener);
    }

}
