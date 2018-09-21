package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.Date;

/**
 * 描述：注册页面
 * 项目名称：vishow_project
 * 作者：angelyin
 * 时间：2016/3/7 9:46
 * 备注：
 */
public class UserRegisterActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 注册返回键
     **/
    private ImageView img_register_back;

    /**
     * 手机注册页面布局
     **/
    private String phone;
    private String password;
    private String confirm_password;
    /**
     * 手机号码编辑框
     **/
    private EditText ed_pr_phone;
    /**
     * 动态验证码编辑框
     **/
    private EditText ed_pr_dt_code;
    /**
     * 确认密码编辑框
     **/
    private EditText ed_pr_confirm_password;
    /**
     * 密码编辑框
     **/
    private EditText ed_pr_password;
    /**
     * 注册按钮
     **/
    private Button bt_pr_register;
    /**
     * 动态验证码按钮
     **/
    private Button bt_dynamic_code;
    /**
     * 倒计时
     **/
    private TimeCount timeCount;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框

    //验证码解析字段
    private String phhone;
    private int code;
    private long date;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_phone_main);
        prodialog = new ProgressDialog(this);// 进度条对话框
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        timeCount = new TimeCount(60000, 1000);
        ed_pr_phone = (EditText) findViewById(R.id.ed_pr_phone);
        ed_pr_dt_code = (EditText) findViewById(R.id.ed_pr_dt_code);
        ed_pr_confirm_password = (EditText) findViewById(R.id.ed_pr_confirm_password);
        ed_pr_password = (EditText) findViewById(R.id.ed_pr_password);
        bt_pr_register = (Button) findViewById(R.id.bt_pr_register);
        bt_dynamic_code = (Button) findViewById(R.id.bt_dynamic_code);
        img_register_back = (ImageView) findViewById(R.id.img_register_back);


        img_register_back.setOnClickListener(this);
        bt_dynamic_code.setOnClickListener(this);
        bt_pr_register.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_register_back:
                //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            // 注册按钮
            case R.id.bt_pr_register:
                checkPhoneRegisterInfo();
                break;
            // 获取验证码
            case R.id.bt_dynamic_code:
                checkGetCode();
                break;
            default:
                break;
        }
    }

    /**
     * 验证手机号码注册判断
     */

    private void checkPhoneRegisterInfo() {
        phone = ed_pr_phone.getText().toString().trim();
        if (phone.length() == 0) {
            Utils.showToast(this, "手机号码不能为空");
            return;
        }
        if (!Utils.isPhoneNum(phone)) {
            Utils.showToast(this, "你填写的手机号码格式不正确");
            return;
        }

        password = ed_pr_password.getText().toString().trim();
        confirm_password = ed_pr_confirm_password.getText().toString().trim();
        if (ed_pr_password.length() < 6) {
            Utils.showToast(this, "密码不能少于6位");
            return;
        }
        if (!password.equals(confirm_password)) {
            Utils.showToast(this, "两次密码输入不一致");
            return;
        }

        if (ed_pr_dt_code.getText().length() == 0) {
            Utils.showToast(this, "请输入验证码");
            return;
        }

        prodialog.setMessage("加载中");
        prodialog.show();

        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }

        //判断手机验证码的失效性
        Date dt = new Date();
        Long date1 = dt.getTime();
        Long mm=date1-date;
        toast(mm + "");
        if(mm>180000){
            toast("验证码已失效，请重新获取验证码");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", ed_pr_phone.getText().toString());
        params.addBodyParameter("password", ed_pr_password.getText().toString());
        new NetUtil(this).sendPostToServer(params, Constants.API_POST_REGISTER, UserRegisterActivity.this, "注册提交");





    }

    /**
     * 验证填写验证码是否正确
     */
    private void checkGetCode() {

        if (ed_pr_phone.getText().length() == 0) {
            Utils.showToast(this, "请输入手机号码");
            return;
        }
        if (!Utils.isPhoneNum(ed_pr_phone.getText().toString())) {
            Utils.showToast(this, "你填写的手机号码格式不正确");
            return;
        }
        timeCount.start();
        getNetCodeforRegister();


    }

    /**
     * 发送获取注册验证码请求
     */
    private void getNetCodeforRegister() {
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = "mobile/sms/sendCode?phone=" + ed_pr_phone.getText().toString();
        new NetUtil(this).sendGetToServer(urlvalue, UserRegisterActivity.this, "注册发送验证码");


    }


    /**
     * 倒计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            bt_dynamic_code.setText("重新验证");
            bt_dynamic_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bt_dynamic_code.setClickable(false);
            bt_dynamic_code.setText(millisUntilFinished / 1000 + "s后可重发");
        }
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


            /**************************************************************************/
            if (action.equals("注册发送验证码")) {
                if (statu) {//成功
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    phhone = jsonObject1.getString("phone");
                    code = jsonObject1.getInt("code");
                    date = jsonObject1.getLong("date");
                    toast("code:" + code + "==data:" + date);

                } else {//失败
                    toast(message);
                }
            }

            /*****************************************************************************/

            if (action.equals("注册提交")) {
                if (statu) {//成功
                    //成功获取返回信息后 提示
                    toast("注册成功");
                    finish();
                    intent=new Intent(UserRegisterActivity.this,UserLoginActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

                } else {//失败
                    toast(message);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
