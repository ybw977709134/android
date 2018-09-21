package com.onemeter.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.utils.Constants;
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


/**
 * 描述：弹出分享的对话框
 * 作者：Administrator
 * 时间：2016/1/18 18:58
 * 备注：
 */
public class showDialogShareActivity extends BaseActivity implements View.OnClickListener {
    private ImageView show_dialog_share_img_quxiao;//取消
    private LinearLayout activity_huodong_setting_main_pop_weixin_btn;//分享微信
    private LinearLayout activity_huodong_setting_main_friend_share_btn;//朋友圈
    private LinearLayout activity_huodong_setting_main_qq_btn;//QQ
    private LinearLayout activity_huodong_setting_main_link_btn;//浏览器
    private IWXAPI wxApi;//微信分享api
    private Tencent mTencent;//QQ分享api

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_main);
        //实例化微信
        wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        wxApi.registerApp(Constants.WX_APP_ID);
        //实例化QQ
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.8
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.7
        getWindow().setAttributes(p);
        initView();
    }

    private void initView() {
        show_dialog_share_img_quxiao = (ImageView) findViewById(R.id.show_dialog_share_img_quxiao);
        activity_huodong_setting_main_pop_weixin_btn = (LinearLayout) findViewById(R.id.activity_huodong_setting_main_pop_weixin_btn);
        activity_huodong_setting_main_friend_share_btn = (LinearLayout) findViewById(R.id.activity_huodong_setting_main_friend_share_btn);
        activity_huodong_setting_main_qq_btn = (LinearLayout) findViewById(R.id.activity_huodong_setting_main_qq_btn);
        activity_huodong_setting_main_link_btn = (LinearLayout) findViewById(R.id.activity_huodong_setting_main_link_btn);


        activity_huodong_setting_main_pop_weixin_btn.setOnClickListener(this);
        activity_huodong_setting_main_friend_share_btn.setOnClickListener(this);
        activity_huodong_setting_main_qq_btn.setOnClickListener(this);
        activity_huodong_setting_main_link_btn.setOnClickListener(this);
        show_dialog_share_img_quxiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_dialog_share_img_quxiao:
                //取消
                this.finish();
                break;
            case R.id.activity_huodong_setting_main_pop_weixin_btn:
                //微信
                wechatShare(0);
                break;
            case R.id.activity_huodong_setting_main_friend_share_btn:
                //朋友圈
                wechatShare(1);
                break;
            case R.id.activity_huodong_setting_main_qq_btn:
                //QQ
                onClickShare();
                break;
            case R.id.activity_huodong_setting_main_link_btn:
                //浏览器打开
                link();
                break;
        }
    }


    /**
     * QQ分享功能
     */
    private void onClickShare() {
        ShareListener myListener = new ShareListener();
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://www.baidu.com/");
        mTencent.shareToQQ(showDialogShareActivity.this, params, myListener);

    }

    /**
     * 浏览器打开url
     */
    private void link() {
        String url = "http://www.baidu.com"; // web address
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

    //微信分享
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.baidu.com/";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "这里填写标题";
        msg.description = "这里填写内容";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.banner001);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 150, 150, true);
//        thumbBmp.recycle();
//        msg.setThumbImage(thumbBmp);
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);

    }


    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            showDialogShareActivity.this.toast("分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            showDialogShareActivity.this.toast("分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            showDialogShareActivity.this.toast("分享出错");
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareListener myListener = new ShareListener();
        Tencent.onActivityResultData(requestCode, resultCode, data, myListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
