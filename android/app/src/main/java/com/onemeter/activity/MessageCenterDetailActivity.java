package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 描述：消息中心详情页
 * 作者：Administrator
 * 时间：2016/3/2 16:27
 * 备注：
 */
public class MessageCenterDetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 传递过来的消息ID
     **/
    private String meassgeId;
    /**
     * 返回键
     **/
    private ImageView activity_message_center_detail_img_return;
    /**
     * 消息标题
     **/
    private TextView activity_message_center_detail_title_text;
    /**
     * 消息内容
     **/
    private TextView activity_message_center_detail_text;
    Intent intent;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message_center_detail_layout);
        progressDialog = new ProgressDialog(this);
        intent = getIntent();
        meassgeId = getIntent().getStringExtra("meassgeId");
        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int mid = Integer.valueOf(meassgeId).intValue();
        int Uid = UserLoginActivity.userInfos.get(0).getUid();
        if (!MyApplication.isNetAvailable) {//没有网络
            toast("网络不可用，请打开网络");
            return;
        }
        progressDialog.setMessage("加载中");
        progressDialog.show();
        String urlvalue = Constants.API_MY_MESSAGE_DETAIL + "memberId=" + Uid + "&messageId=" + mid;
        new NetUtil(this).sendGetToServer(urlvalue, MessageCenterDetailActivity.this, "消息详情");

    }

    /**
     * 初始化组件
     */
    private void initView() {
        activity_message_center_detail_img_return = (ImageView) findViewById(R.id.activity_message_center_detail_img_return);
        activity_message_center_detail_title_text = (TextView) findViewById(R.id.activity_message_center_detail_title_text);
        activity_message_center_detail_img_return = (ImageView) findViewById(R.id.activity_message_center_detail_img_return);
        activity_message_center_detail_text = (TextView) findViewById(R.id.activity_message_center_detail_text);
        activity_message_center_detail_img_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_message_center_detail_img_return:
                //返回键
                finish();
                break;
        }
    }

    /**
     * 请求结束后此方法更新数据
     *
     * @param result
     * @param isSuccess
     * @param api
     * @param action
     */
    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        JSONObject jsonObject = null;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
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
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("message");

            /**********************************************************************************/
            if (action.equals("消息详情")) {
                if (statu) {//成功
//                    toast(result);
                    String title = jsonObject2.getString("title");
                    String content = jsonObject2.getString("content");
                    activity_message_center_detail_title_text.setText(title);
                    activity_message_center_detail_text.setText(content);
                } else {
                    toast(message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
