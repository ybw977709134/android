package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;



/**
 * 描述：短信找回密码页面
 * 作者：Administrator
 * 时间：2016/1/27 15:28
 * 备注：
 */
public class SmsFindPassWordActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 键盘输入管理器
     **/
    private InputMethodManager mInputMethodManager;
    private ImageView activity_sms_find_password_img;
    private EditText ed_sms_find_phone;
    private EditText ed_sms_find_code;
    private Button ed_sms_find_dynamic_code;
    private Button ed_sms_find_phone_next;
    private LinearLayout rel_forget;
    /**
     * 倒计时
     **/
    private TimeCount timeCount;
    Intent intent;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框

    //验证码解析字段
    private String phhone;
    private int code;
    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sms_find_password_main);
        // 点击屏幕的其他的地方可以收起键盘
        mInputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        prodialog = new ProgressDialog(this);
        initView();
    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/27 15:45
     */
    private void initView() {
        timeCount = new TimeCount(60000, 1000);
        rel_forget = (LinearLayout) findViewById(R.id.rel_forget);
        activity_sms_find_password_img = (ImageView) findViewById(R.id.activity_sms_find_password_img);
        ed_sms_find_phone = (EditText) findViewById(R.id.ed_sms_find_phone);
        ed_sms_find_code = (EditText) findViewById(R.id.ed_sms_find_code);

        ed_sms_find_dynamic_code = (Button) findViewById(R.id.ed_sms_find_dynamic_code);
        ed_sms_find_phone_next = (Button) findViewById(R.id.ed_sms_find_phone_next);

        activity_sms_find_password_img.setOnClickListener(this);
        ed_sms_find_phone_next.setOnClickListener(this);
        ed_sms_find_dynamic_code.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ed_sms_find_phone_next:
                //下一步
                if (isInputEmpty()) {
                    //判断输入的验证码和实际的是否一样
                    if (!ed_sms_find_code.getText().toString().equals(code + "")) {
                        toast("验证码错误");
                        return;
                    }
                    //判断手机验证码的失效性
                    Date dt = new Date();
                    Long date1 = dt.getTime();
                    Long mm = date1 - date;
                    toast(mm + "");
                    if (mm > 180000) {
                        toast("验证码已失效，请重新获取验证码");
                        return;
                    }

                    intent = new Intent(this, ByPhoneResetPasswordActivity.class);
                    intent.putExtra("phone", ed_sms_find_phone.getText().toString());
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                }
                break;
            case R.id.activity_sms_find_password_img:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.ed_sms_find_dynamic_code:
                //获取验证码
                if (isInputPhoneEmpty()) {
                    checkGetCode();
                }
                break;

        }
    }

    /**
     * 点击获取验证码判断输入框
     *
     * @return
     */
    private boolean isInputPhoneEmpty() {
        if ("".equals(ed_sms_find_phone.getText().toString())) {
            toast("手机号码不能为空");
            return false;
        }
        if (!Utils.isPhoneNum(ed_sms_find_phone.getText().toString())) {
            toast("手机号码格式不正确");
            return false;
        }
        return true;
    }

    /**
     * 下一步点击判断输入框
     *
     * @return
     */
    private boolean isInputEmpty() {
        if ("".equals(ed_sms_find_phone.getText().toString())) {
            toast("手机号码不能为空");
            return false;
        }
        if (!Utils.isPhoneNum(ed_sms_find_phone.getText().toString())) {
            toast("手机号码格式不正确");
            return false;
        }
        if ("".equals(ed_sms_find_code.getText().toString())) {
            toast("请输入动态码");
            return false;
        }
        return true;
    }


    /**
     * 触摸隐藏软键盘
     *
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
     * 倒计时
     */

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            ed_sms_find_dynamic_code.setText("重新发送");
            ed_sms_find_dynamic_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ed_sms_find_dynamic_code.setClickable(false);
            ed_sms_find_dynamic_code.setText(millisUntilFinished / 1000 + "秒后重新获取");
        }
    }


    /**
     * 描述：发送并验证验证码
     * 作者：$angelyin
     * 时间：2016/1/27 15:56
     */
    private void checkGetCode() {
        timeCount.start();
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();

        String urlvalue = "mobile/sms/sendCode?phone=" + ed_sms_find_phone.getText().toString();
        new NetUtil(this).sendGetToServer(urlvalue, SmsFindPassWordActivity.this, "忘记密码发送验证码");


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


            /**************************************************************************/
            if (action.equals("忘记密码发送验证码")) {
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


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
