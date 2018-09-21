package com.onemeter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 描述：意见反馈页面(测试)
 * 作者：angelyind
 * 时间：2016/1/28 15:28
 * 备注：
 */
public class AppFanKuiActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回键
     **/
    private ImageView activity_fankui_img_return;
    /**
     * 文本框
     **/
    private TextView activity_fankui_text;
    /**
     * 提交按钮
     **/
    private TextView activity_fankui_tijiao;
    /**
     * 提交的内容
     **/
    String text;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框
    int userId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_fan_kui_main);
        prodialog = new ProgressDialog(this);// 进度条对话框
        userId = UserLoginActivity.userInfos.get(0).getUid();
        initView();

    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/28 16:05
     */
    private void initView() {
        activity_fankui_img_return = (ImageView) findViewById(R.id.activity_fankui_img_return);
        activity_fankui_text = (TextView) findViewById(R.id.activity_fankui_text);
        activity_fankui_tijiao = (TextView) findViewById(R.id.activity_fankui_tijiao);

        activity_fankui_img_return.setOnClickListener(this);
        activity_fankui_tijiao.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_fankui_img_return:
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.activity_fankui_tijiao:
                getTextString();
                break;
        }
    }

    /**
     * 获取输入框中意见内容并提交服务器
     */
    private void getTextString() {
        text = activity_fankui_text.getText().toString().trim();
        if (text.length() > 0) {
            if (!MyApplication.isNetAvailable) {// 网络不可用
                Utils.showToast(this, "网络不可用，请打开网络");
                return;
            }

            getNetFeedBack();


        } else {

            toast("请输入内容后再提交！");
        }


    }

    /**
     * 提交意见请求
     */
    private void getNetFeedBack() {
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = "mobile/feedback/save?userId="+userId+"&content="+text;
        new NetUtil(this).sendGetToServer(urlvalue, AppFanKuiActivity.this, "意见反馈");

    }

    /**
     * 当请求数据完成后调用此方法更新数据
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**************************************************************************/
        if (action.equals("意见反馈")) {
            if (statu) {//成功
                toast("提交成功");
                finish();
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
            } else {
                toast(message + "提交失败");

            }


        }


    }
}
