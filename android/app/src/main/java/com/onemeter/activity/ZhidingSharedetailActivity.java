package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.changjingsettingInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：指定场景分享页面
 * 项目名称：zhaosb
 * 作者：angelyin
 * 时间：2016/3/3 11:15
 * 备注：
 */
public class ZhidingSharedetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回键
     **/
    private ImageView activity_share_changjing_img_return;
    /**
     * 场景标题
     **/
    private TextView activity_share_changjing_change_title;
    /**
     * 场景图标
     **/
    private ImageView activity_share_changjing_share_img;
    /**
     * 场景描述
     **/
    private TextView activity_share_changjing_change_text;
    /**
     * 时间
     **/
    private TextView activity_share_changjing_change_time;
    //按钮部分
    /**
     * 微信
     **/
    private LinearLayout btn_share_activity_weixin;
    /**
     * 朋友圈
     **/
    private LinearLayout btn_share_activity_firend;

    /**
     * QQ
     **/
    private LinearLayout btn_share_activity_qq;

    /**
     * 浏览器
     **/
    private LinearLayout btn_share_activity_web;
    ProgressDialog prodialog;// 加载进度条对话框
    public static List<changjingsettingInfo> cjst;
    //单个场景的ID和类型
    String type;
    int activityId;
    String  imagpath;
    private IWXAPI wxApi;//微信api
    private Tencent mTencent;//QQ分享api
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zhiding_share_detail_layout);
        //实例化
        wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        wxApi.registerApp(Constants.WX_APP_ID);
        //实例化QQ
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());

        prodialog=new ProgressDialog(this);
        cjst=new ArrayList<changjingsettingInfo>();
        type = MyApplication.changjing_type.get(0).toString();
        activityId = Integer.valueOf(MyApplication.changjing_position.get(0).toString()).intValue();
        imagpath=MyApplication.changjing_cover.get(0).toString();
        initData();
        initView();
    }

    /**
     * 初始化数据提交请求
     */
    private void initData() {
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_FIND_BY_ID_CHANG_JING_SET_STUDY + "activityId=" + activityId + "&type=" + type;
        new NetUtil(this).sendGetToServer(urlvalue, ZhidingSharedetailActivity.this, "指定活动分享");

    }


    private void initView() {
        activity_share_changjing_img_return = (ImageView) findViewById(R.id.activity_share_changjing_img_return);
        activity_share_changjing_change_title = (TextView) findViewById(R.id.activity_share_changjing_change_title);
        activity_share_changjing_share_img = (ImageView) findViewById(R.id.activity_share_changjing_share_img);
        activity_share_changjing_change_text = (TextView) findViewById(R.id.activity_share_changjing_change_text);
        activity_share_changjing_change_time = (TextView) findViewById(R.id.activity_share_changjing_change_time);
        BitmapUtils bitmapUtils=new BitmapUtils(this);
        bitmapUtils.display(activity_share_changjing_share_img,imagpath);
        //按钮
        btn_share_activity_weixin = (LinearLayout) findViewById(R.id.btn_share_activity_weixin);
        btn_share_activity_firend = (LinearLayout) findViewById(R.id.btn_share_activity_firend);
        btn_share_activity_qq = (LinearLayout) findViewById(R.id.btn_share_activity_qq);
        btn_share_activity_web = (LinearLayout) findViewById(R.id.btn_share_activity_web);

        activity_share_changjing_img_return.setOnClickListener(this);
        btn_share_activity_weixin.setOnClickListener(this);
        btn_share_activity_qq.setOnClickListener(this);
        btn_share_activity_web.setOnClickListener(this);
        btn_share_activity_firend.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_share_changjing_img_return:
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.btn_share_activity_weixin:
//                toast("微信");
                wechatShare(0);
                break;
            case R.id.btn_share_activity_firend:
//                toast("朋友圈");
                wechatShare(1);
                break;
            case R.id.btn_share_activity_qq:
//                toast("qq");
                onClickShare();
                break;
            case R.id.btn_share_activity_web:
//                toast("浏览器");
                link();
                break;


        }
    }

    /**
     * qq分享功能
     */
    private void onClickShare() {
        ShareListener myListener = new ShareListener();
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, activity_share_changjing_change_title.getText().toString());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  activity_share_changjing_change_text.getText().toString());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://www.baidu.com/");
        mTencent.shareToQQ(ZhidingSharedetailActivity.this, params, myListener);



    }

    /**
     * 微信、朋友圈分享
     * @param flag
     */
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.baidu.com/";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title =activity_share_changjing_change_title.getText().toString();
        msg.description = activity_share_changjing_change_text.getText().toString();
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

    /**
     * 在浏览器上打开指定url
     */
    private void link() {
        String url = "http://www.baidu.com"; // web address
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    /**
     * 提交请求后此方法更新数据
     * @param result
     * @param isSuccess
     * @param api
     * @param action
     */
    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        JSONObject jsonObject = null;
        if (prodialog != null && prodialog.isShowing()) {
            prodialog.dismiss();
            Log.i("Onemeter", "关闭prodialog");
        }
        if (!isSuccess) {// 请求不成功
            Utils.showToast(this, getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");

            /**************************************************************************/
        if(action.equals("指定活动分享")){
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("music");
            if(statu){//成功
                cjst.clear();
                changjingsettingInfo cfo = new changjingsettingInfo();

                if (jsonObject1.isNull("name")) {
                } else {
                    String name = jsonObject1.getString("name");
                    cfo.setName(name);
                }

                if (jsonObject1.isNull("createDate")) {
                } else {
                    Long createDate = jsonObject1.getLong("createDate");
                    cfo.setCreateDate(createDate);

                }

                if (jsonObject1.isNull("subhead")) {
                    cfo.setSubhead("未设置");
                } else {
                    String subhead = jsonObject1.getString("subhead");
                    cfo.setSubhead(subhead);
                }
                cjst.add(cfo);

                activity_share_changjing_change_title.setText(cjst.get(0).getName().toString());
                activity_share_changjing_change_text.setText(cjst.get(0).getSubhead().toString());
                String datatime = Utils.getFormatedDateTime("yyyy-MM-dd", cjst.get(0).getCreateDate());
                activity_share_changjing_change_time.setText(datatime);

            }else{
             toast(message);

            }



        }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            ZhidingSharedetailActivity.this.toast("分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            ZhidingSharedetailActivity.this.toast("分享成功");
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            ZhidingSharedetailActivity.this.toast("分享出错");
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareListener myListener = new ShareListener();
        Tencent.onActivityResultData(requestCode,resultCode,data,myListener);
    }

}
