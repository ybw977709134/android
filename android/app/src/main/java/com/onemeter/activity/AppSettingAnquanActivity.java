package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;


/**
 * 描述：账号与安全设置页面
 * 作者：angelyin
 * 时间：2016/1/27 13:30
 * 备注：
 */
public class AppSettingAnquanActivity extends BaseActivity implements View.OnClickListener {
    private ImageView activity_app_setting_img_return_icon;
    private RelativeLayout layt_change_password;
    private RelativeLayout layt_app_clear;
    private Button app_bttn_logout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_setting_anquan_main);
        initView();
    }

    /**
     * 描述： 初始化组件
     * 作者：$angelyin
     * 时间：2016/1/27 13:57
     */
    private void initView() {
        activity_app_setting_img_return_icon = (ImageView) findViewById(R.id.activity_app_setting_img_return_icon);
        layt_change_password = (RelativeLayout) findViewById(R.id.layt_change_password);
        layt_app_clear = (RelativeLayout) findViewById(R.id.layt_app_clear);
        app_bttn_logout = (Button) findViewById(R.id.app_bttn_logout);

        activity_app_setting_img_return_icon.setOnClickListener(this);
        layt_app_clear.setOnClickListener(this);
        layt_change_password.setOnClickListener(this);
        app_bttn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layt_change_password:
                //密码修改
                intent = new Intent(this, ChangePassWordActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.activity_app_setting_img_return_icon:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.layt_app_clear:
                //清除缓存
                break;
            case R.id.app_bttn_logout:
                //登出账号
                this.finish();
                ClearUser();
                intent = new Intent(this, UserLoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
        }
    }

    /**
     * 清除账号数据
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
        MyApplication.changjing_cover.clear();
        MyApplication.free_position.clear();
        UserLoginActivity.userInfos.clear();
        //设置登陆为离线状态
        MyApplication.islogin.add(0, "未登录");

    }
}
