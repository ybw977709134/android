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
import android.widget.ProgressBar;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
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

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 描述：
 * 项目名称：vishow_project
 * 作者：angelyin
 * 时间：2016/3/22 12:52
 * 备注：
 */
public class QuHuoDongWebActivity extends BaseActivity implements View.OnClickListener {
    private MyWebView qu_quhuodong_web;
    private ProgressBar pb;
    private IWXAPI wxApi;//微信api
    private Tencent mTencent;//QQ分享api
    Handler mHandler = new Handler();

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huo_dong_web_main);
        //实例化
        wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        wxApi.registerApp(Constants.WX_APP_ID);
        //实例化QQ
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
        //进度条
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setMax(100);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        qu_quhuodong_web = (MyWebView) findViewById(R.id.qu_quhuodong_web);
        qu_quhuodong_web.setVerticalScrollbarOverlay(true);
        WebSettings wSet = qu_quhuodong_web.getSettings();
        //加上这句话才能使用javascript方法
        wSet.setJavaScriptEnabled(true);
//        wSet.setSupportZoom(true);
        wSet.setDefaultTextEncodingName("utf-8");
        wSet.setUseWideViewPort(true);
        wSet.setLoadWithOverviewMode(true);
        qu_quhuodong_web.loadUrl("http://101.200.186.44:8080/h5/views/fun-activity/category-list.html");
//        qu_luyin_web.loadUrl("file:///android_asset/edit-activity.html");
        //在js中调用本地java方法
        qu_quhuodong_web.addJavascriptInterface(new JsInterface(this), "AndroidWebView");
        //不新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        qu_quhuodong_web.setWebViewClient(new WebViewClient() {
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

        qu_quhuodong_web.setWebChromeClient(new WebChromeClient() {
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
                qu_quhuodong_web.loadUrl("javascript:setAppUserInfo('" + UserLoginActivity.userInfos.get(0).getUid()
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
                QuHuoDongWebActivity.this.finish();

            } else if (name.equals("场景页")) {
                Intent intent = new Intent(QuHuoDongWebActivity.this, MainActivity.class);
                startActivity(intent);
                QuHuoDongWebActivity.this.finish();
            }

        }

        //在js中调用 微信好友分享方法
        @JavascriptInterface
        public void showWeiXinHShare(String info) {
            try {
                JSONObject mjson = new JSONObject(info);
                String title = mjson.optString("title");
                int id = mjson.optInt("id");
                String desc = mjson.optString("desc");
                String cover = mjson.optString("cover");
                String link = mjson.optString("link");
                int flag = 0;
                wechatShare(id, title, desc, cover, link, flag);
//                toast("id:"+id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//
        }

        //在js中调用 微信朋友圈分享方法aaa
        @JavascriptInterface
        public void showWeiXinPShare(String info) {
            try {
                JSONObject mjson = new JSONObject(info);
                String title = mjson.optString("title");
                int id = mjson.optInt("id");
                String desc = mjson.optString("desc");
                String cover = mjson.optString("cover");
                String link = mjson.optString("link");
                int flag = 1;
                wechatShare(id, title, desc, cover, link, flag);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //在js中调用 QQ分享方法
        @JavascriptInterface
        public void showQQShare(String info) {
//           toast("QQ分享");
            try {
                JSONObject mjson = new JSONObject(info);
                String title = mjson.optString("title");
                int id = mjson.optInt("id");
                String desc = mjson.optString("desc");
                String cover = mjson.optString("cover");
                String link = mjson.optString("link");
                onClickShare(id, title, desc, cover, link);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        }


    }

    /**
     * qq分享功能
     */
    private void onClickShare(int id, String title, String desc, String cover, String link) {
        ShareListener myListener = new ShareListener();
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://www.baidu.com/");//http://www.qq.com/news/1.html
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "https://www.baidu.com/");
        mTencent.shareToQQ(QuHuoDongWebActivity.this, params, myListener);


    }

    /**
     * 微信、朋友圈分享
     *
     * @param flag
     */
    private void wechatShare(int id, String title, String desc, String cover, String link, int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = link;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        toast("id:" + id + "title:" + title + "desc:" + desc + "link:" + link);

        //这里替换一张自己工程里的图片资源
//        Bitmap thumb = BitmapFactory.decodeFile("http://image.baidu.com/search/redirect?tn=redirect&word=j&juid=7AE735&sign=cizciakige&url=http%3A%2F%2Fdown.chinavisual.com%2Fstuff%2F5178d9cf4b7959393e0000bc.html&objurl=http%3A%2F%2Fimg.xiaba.cvimage.cn%2F5178e0deb89bd3e3770002f0.jpg");
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.banner_1);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 150, 150, true);
        thumb.recycle();
        msg.setThumbImage(thumbBmp);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
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
        if (keyCode == KeyEvent.KEYCODE_BACK && qu_quhuodong_web.canGoBack()) {
            qu_quhuodong_web.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            QuHuoDongWebActivity.this.toast("分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            QuHuoDongWebActivity.this.toast("分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            QuHuoDongWebActivity.this.toast("分享出错");
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareListener myListener = new ShareListener();
        Tencent.onActivityResultData(requestCode, resultCode, data, myListener);
    }

}
