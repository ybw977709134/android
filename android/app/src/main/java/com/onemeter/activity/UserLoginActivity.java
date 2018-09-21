package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.userInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：登入页面
 * 作者：angelyin
 * 时间：2015/12/23 12:03
 * 备注：
 */
public class UserLoginActivity extends BaseActivity implements View.OnClickListener {
    /**
     * app名字
     **/
    private String appName = "shop";

    /**
     * 用户名
     **/
    private EditText edt_username;
    /**
     * 密码
     **/
    private EditText edt_password;
    /**
     * 登入
     **/
    private Button btn_login;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框
    //声明变量
    private String schoolName;
    private String userName;
    private String passWord;
    /**
     * 注册
     */
    private TextView txt_register;
    private TextView btn_txt_register;
    /**
     * 忘记密码
     */
    private TextView txt_forget_password;
    private Intent intent;
    private ImageView activity_login_return_icon;

    /**
     * 登录用户集合
     **/
    public static List<userInfo> userInfos;
    /**
     * 配置信息
     */
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    //是否第一次
    SharedPreferences  preferences;
    int isfirst;
    String islogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        islogin=MyApplication.islogin.get(0);
        userInfos = new ArrayList<userInfo>();
        preferences = getSharedPreferences("isfirst",MODE_WORLD_READABLE);
        //记录是否是第一次启动app
        isfirst = preferences.getInt("isfirst", 0);
        initView();
    }






    /**
     * 初始化组件
     * 作者：$angelyin
     * 时间：2015/12/23 12:10
     */
    private void initView() {
        activity_login_return_icon = (ImageView) findViewById(R.id.activity_login_return_icon);
        txt_forget_password = (TextView) findViewById(R.id.txt_forget_password);
        txt_register = (TextView) findViewById(R.id.txt_register);
        prodialog = new ProgressDialog(this);// 进度条对话框
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_username = (EditText) findViewById(R.id.edt_username);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_txt_register = (TextView) findViewById(R.id.btn_txt_register);

        btn_txt_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        txt_register.setOnClickListener(this);
        activity_login_return_icon.setOnClickListener(this);
        txt_forget_password.setOnClickListener(this);

        //获取保存的用户名
        sp = getSharedPreferences("userInfo_config", Context.MODE_PRIVATE);
        editor = sp.edit();
        if (sp.getString("username", "") != null// 设置用户名
                && !sp.getString("username", "").equals("")) {
            edt_username.setText(sp.getString("username", ""));
            edt_username.setSelection(edt_username.getText().toString().length());
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_login_return_icon:
//                //返回键
//                this.finish();
//                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.btn_login:
                //登入

                getNetLogin();
                break;
            case R.id.txt_register:
            case R.id.btn_txt_register:
                //注册
                intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.txt_forget_password:
                //忘记密码
                intent = new Intent(UserLoginActivity.this, SmsFindPassWordActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
        }


    }

    /**
     * 登入的方法
     * 作者：$angelyin
     * 时间：2015/12/23 12:27
     */
    private void getNetLogin() {
        userName = edt_username.getText().toString().trim();
        passWord = edt_password.getText().toString().trim();
        if (isempty()) {
            return;
        } else {
            if (!MyApplication.isNetAvailable) {// 网络不可用
                Utils.showToast(this, "网络不可用，请打开网络");
                return;
            }
            isShopLogin();
//
        }


    }

    /**
     * 使用微秀账号登陆（测试账号admin  密码：111111）
     */
    private void isShopLogin() {
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", userName);
        params.addBodyParameter("password", passWord);
        new NetUtil(this).sendPostToServer(params, Constants.API_POST_LOGIN, UserLoginActivity.this, "登入提交");

    }


    /**
     * 输入框是否为空
     * 作者：$angelyin
     * 时间：2015/12/23 17:36
     */
    private boolean isempty() {
        if ("".equals(userName)) {
            toast("用户名不能为空");
            return true;
        }
        if ("".equals(passWord)) {
            toast("请输入密码");
            return true;
        }
        return false;
    }


    /**
     * 当请求数据完成后调用此方法更新数据
     *
     * @param @param result
     * @param @param isSuccess
     * @param @param api 设定文件
     * @return void 返回类型
     * @throws
     * @Title: onCompleted
     */

    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        boolean ss=false;
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
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("user");
            JSONObject jsonObject3 = jsonObject1.getJSONObject("role");

            /**************************************************************************/
            if (action.equals("登入提交")) {
                if (statu) {//成功
                    //解析结果
                    editor.putString("username", userName);
                    editor.commit();
                    int uid = jsonObject1.getInt("id");
                    if(jsonObject1.isNull("headImage")){
                        MyApplication.user_imgpath="";
                    }else {
                        String headImage = jsonObject1.getString("headImage");
                        MyApplication.user_imgpath=headImage;
                    }

                    if(jsonObject1.isNull("describe")){
                        MyApplication.user_miaoshu ="这家伙很懒，什么也没留下";
                    }else {
                        String describe = jsonObject1.getString("describe");
                        MyApplication.user_miaoshu=describe;
                    }

                    String phone = jsonObject2.getString("phone");
                    if(jsonObject2.isNull("email")){
                        MyApplication.user_email="未绑定";
                    }else {
                        String email = jsonObject2.getString("email");
                        MyApplication.user_email=email;
                    }
                    String username = jsonObject2.getString("username");
                    if (jsonObject1.getBoolean("sex")) {
                        ss = true;//(女)
                        MyApplication.sex_name="女";
                    } else {
                        ss = false;//（男）
                        MyApplication.sex_name="男";
                    }
                    if (jsonObject2.isNull("username")) {
                        MyApplication.ni_name=phone;
                    } else {
                        MyApplication.ni_name=username;
                    }
                    MyApplication.user_name=phone;

                    String vipname = jsonObject3.getString("name");
                    userInfo ufo = new userInfo();
                    ufo.setUid(uid);
                    ufo.setPhone(phone);
                    ufo.setUsername(username);
                    ufo.setVIPName(vipname);
                    ufo.setSex(ss);
                    userInfos.add(ufo);
//                    toast("uid:"+uid);
                    MyApplication.islogin.add(0,"已登录");
                    //判断是否第一次启动登陆
                    if(isfirst==0){
                        Intent intent = new Intent(this, ChooseMyLoveDingYueActivity.class);
                        startActivity(intent);
                        this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                        finish();
                    }else {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                        finish();
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putInt("isfirst", ++isfirst);
                    //提交修改
                    editor.commit();

                } else {//失败
                    MyApplication.islogin.add(0,"未登录");
                    toast(message);
                }
            }

            /*****************************************************************************/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.setAction(BaseActivity.SYSTEM_EXIT);
            sendBroadcast(intent);
            return  false;
        }
        return  super.onKeyDown(keyCode, event);

    }

}


