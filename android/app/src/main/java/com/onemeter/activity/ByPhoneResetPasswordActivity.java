package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
 * 描述：根据短信验证码重置密码（下一步页面）
 * 作者：Administrator
 * 时间：2016/1/27 16:05
 * 备注：
 */
public class ByPhoneResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 键盘输入管理器
     **/
    private InputMethodManager mInputMethodManager;
    private ImageView activity_sms_find_password_img;
    private ClearEditText ed_reset_password;
    private ClearEditText ed_confirm_password;
    private Button ByPhoneResetPasswordActivity_next;
    Intent intent;
    private String phone;
    ProgressDialog prodialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_by_phone_reset_main);
        // 点击屏幕的其他的地方可以收起键盘
        mInputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        phone = getIntent().getStringExtra("phone");
        prodialog = new ProgressDialog(this);
//        toast(phone);
        initView();
    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/27 16:22
     */
    private void initView() {
        activity_sms_find_password_img = (ImageView) findViewById(R.id.activity_sms_find_password_img);
        ed_reset_password = (ClearEditText) findViewById(R.id.ed_reset_password);
        ed_confirm_password = (ClearEditText) findViewById(R.id.ed_confirm_password);
        ByPhoneResetPasswordActivity_next = (Button) findViewById(R.id.ByPhoneResetPasswordActivity_next);

        activity_sms_find_password_img.setOnClickListener(this);
        ByPhoneResetPasswordActivity_next.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_sms_find_password_img:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.ByPhoneResetPasswordActivity_next:
                //下一步
                if (isInputEmpty()) {
                    if (!MyApplication.isNetAvailable) {// 网络不可用
                        Utils.showToast(this, "网络不可用，请打开网络");
                        return;
                    }
                    getNetFindChangePassword();
                }

                break;
        }
    }

    /**
     * 提交重置密码请求
     */
    private void getNetFindChangePassword() {
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("password", ed_reset_password.getText().toString());
        params.addBodyParameter("confirmPassword", ed_reset_password.getText().toString());
        new NetUtil(this).sendPostToServer(params, Constants.API_POST_FORGET_PASSWORD, ByPhoneResetPasswordActivity.this, "忘记密码重置提交");

    }

    /**
     * 描述：判断输入框
     * 作者：$angelyin
     * 时间：2016/2/1 14:46
     */
    private boolean isInputEmpty() {
        if ("".equals(ed_reset_password.getText().toString())) {
            toast("密码不能为空");
            return false;
        }
        if ("".equals(ed_confirm_password.getText().toString())) {
            toast("再次密码不能为空");
            return false;
        }
        if (!ed_reset_password.getText().toString().equals(ed_confirm_password.getText().toString())) {
            toast("两次输入的密码不相同");
            return false;
        }
        return true;
    }


    /*
     * 触摸隐藏软键盘
	 * @param event
	 * @return
	 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }


    /**
     * 提交请求后此方法更新数据
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

            /**************************************************************************/
            if (action.equals("忘记密码重置提交")) {
                if (statu) {//成功
                    toast("重置密码成功");
                    intent = new Intent(this, ByPhoneResetPasswordOverActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

                } else {//失败
                    toast(message);
                }
            }

            /*****************************************************************************/

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}

