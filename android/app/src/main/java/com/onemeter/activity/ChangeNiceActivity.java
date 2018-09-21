package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
 * 描述：修改昵称
 * 作者：Administrator
 * 时间：2016/1/27 18:24
 * 备注：
 */
public class ChangeNiceActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框
    /**
     * 昵称输入框
     **/
    private EditText text_setting_nice;
    /**
     * 确定
     **/
    private ImageView nice_change_queding;
    /**
     * 返回键
     **/
    private ImageView activity_chose_sex_img_return;
    Intent intent;
    String resultCod;
    String ni_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nice_main);
        prodialog = new ProgressDialog(this);
//        resultCod = getIntent().getStringExtra("requestCode");
        ni_name=getIntent().getStringExtra("ni_name");
        initView();
    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/27 18:37
     */
    private void initView() {
        activity_chose_sex_img_return = (ImageView) findViewById(R.id.activity_chose_sex_img_return);
        nice_change_queding = (ImageView) findViewById(R.id.nice_change_queding);
        text_setting_nice = (EditText) findViewById(R.id.text_setting_nice);
        text_setting_nice.setText(ni_name);
        activity_chose_sex_img_return.setOnClickListener(this);
        nice_change_queding.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_chose_sex_img_return:
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.nice_change_queding:
                //确定
                getNetNiceChang();
                break;
        }
    }

    /**
     * 提交修改昵称
     */
    private void getNetNiceChang() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", UserLoginActivity.userInfos.get(0).getUid() + "");
        params.addBodyParameter("username", text_setting_nice.getText().toString());
        params.addBodyParameter("type", "修改姓名");
        new NetUtil(this).sendPostToServer(params, Constants.API_UPDATE_USER, ChangeNiceActivity.this, "修改姓名");

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
        if (action.equals("修改姓名")) {
            if (statu) {//成功
                toast("修改成功" + result);
                this.finish();
                MyApplication.ni_name=text_setting_nice.getText().toString();
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

            } else {
                toast(message + "修改失败");
            }

        }

    }

}
