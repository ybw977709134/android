package com.onemeter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：绑定邮箱操作
 * 作者：angelyin
 * 时间：2016/3/28 19:05
 * 备注：
 */
public class BindEmaiActivity extends BaseActivity implements View.OnClickListener {
    private ImageView email_change_queding;
    private ImageView activity_email_img_return;
    private EditText text_setting_email;
    private String email_name;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        prodialog=new ProgressDialog(this);
        setContentView(R.layout.activity_email_layout);
        email_name = getIntent().getStringExtra("email");
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        email_change_queding = (ImageView) findViewById(R.id.email_change_queding);
        activity_email_img_return = (ImageView) findViewById(R.id.activity_email_img_return);
        text_setting_email = (EditText) findViewById(R.id.text_setting_email);
        text_setting_email.setText(email_name);

        email_change_queding.setOnClickListener(this);
        activity_email_img_return.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_email_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.email_change_queding:
                //确定
                getNetemailChang();
                break;
        }
    }

    /**
     * 绑定邮箱提交
     */
    private void getNetemailChang() {
        if(Utils.isEmail(text_setting_email.getText().toString())){
            if (!MyApplication.isNetAvailable) {// 网络不可用
                Utils.showToast(this, "网络不可用，请打开网络");
                return;
            }
            prodialog.setMessage("加载中");
            prodialog.show();
            RequestParams params = new RequestParams();
            params.addBodyParameter("id", UserLoginActivity.userInfos.get(0).getUid() + "");
            params.addBodyParameter("email", text_setting_email.getText().toString());
            params.addBodyParameter("type", "修改邮箱");
            new NetUtil(this).sendPostToServer(params, Constants.API_UPDATE_USER, BindEmaiActivity.this, "修改邮箱");
        }else {
            toast("邮箱格式不正确");
        }





    }

    /**
     *
     * 请求结束后此方法更新数据
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

            if (action.equals("修改邮箱")) {
                if (statu) {//成功
                    toast("修改成功");
                this.finish();
                    MyApplication.user_email=text_setting_email.getText().toString();
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                } else {
                    toast(message + "修改失败");
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
