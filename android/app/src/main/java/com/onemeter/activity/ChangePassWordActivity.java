package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.ClearEditText;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 修改密码页面
 */
public class ChangePassWordActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回键
     **/
    private ImageView activity_change_img_return_icon;
    /**
     * 新密码输入框
     **/
    private ClearEditText activity_change_ed_reset_password;
    /**
     * 再次新密码输入框
     **/
    private ClearEditText activity_change_ed_confirm_password;
    /**
     * 修改提交
     **/
    private Button activity_change_password_btn;

    ProgressDialog prodialog;
    private int userId ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password_main);
        userId=UserLoginActivity.userInfos.get(0).getUid();
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        prodialog = new ProgressDialog(this);// 进度条对话框
        activity_change_img_return_icon = (ImageView) findViewById(R.id.activity_change_img_return_icon);
        activity_change_ed_reset_password = (ClearEditText) findViewById(R.id.activity_change_ed_reset_password);
        activity_change_ed_confirm_password = (ClearEditText) findViewById(R.id.activity_change_ed_confirm_password);
        activity_change_password_btn = (Button) findViewById(R.id.activity_change_password_btn);

        activity_change_img_return_icon.setOnClickListener(this);
        activity_change_password_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_change_img_return_icon:
                //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.activity_change_password_btn:
                //提交修改请求
                if (isSameEmpty()) {
                    if (!activity_change_ed_reset_password.getText().toString().equals(activity_change_ed_confirm_password.getText().toString())) {
                        toast("两次密码不一致");
                        return;
                    }

                    getNetChangePassword();
                }

                break;

        }
    }


    /**
     * 发送提交请求
     */
    private void getNetChangePassword() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", userId+"");
        params.addBodyParameter("password", activity_change_ed_reset_password.getText().toString());
        params.addBodyParameter("confirmPassword", activity_change_ed_confirm_password.getText().toString());
        new NetUtil(this).sendPostToServer(params, Constants.API_UPDATE_PASSWORD, ChangePassWordActivity.this, "修改密码");

    }

    /**
     * 判断两次输入密码是否相同
     *
     * @return
     */
    private boolean isSameEmpty() {

        if ("".equals(activity_change_ed_reset_password.getText().toString())) {
            toast("请输入新密码");
            return false;
        }
        if ("".equals(activity_change_ed_confirm_password.getText().toString())) {
            toast("请再次输入新密码");
            return false;
        }

        return true;
    }


    /**
     * 发送请求后此方法更新数据
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

        /*******************************************************************************/

        if (action.equals("修改密码")) {
            if (statu) {//成功
                toast("修改成功");
                this.finish();
                ClearUser();
             Intent   intent = new Intent(this, UserLoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

            } else {

                toast(message + "修改失败");

            }


        }

    }

    /**
     * 清除当前账号的所有信息
     */
    private void ClearUser() {
        MyApplication.ni_name="";
        MyApplication.sex_name="";
        MyApplication.user_email="";
        MyApplication.user_name="";
        MyApplication.user_miaoshu="";
        MyApplication.htuijian_changjing_id.clear();
        MyApplication.tuijian_dingyue_id.clear();
        MyApplication.changjing_position.clear();
        MyApplication.changjing_type.clear();
        MyApplication.free_position.clear();
        UserLoginActivity.userInfos.clear();
        //设置登陆为离线状态
        MyApplication.islogin.add(0, "未登录");

    }
}
