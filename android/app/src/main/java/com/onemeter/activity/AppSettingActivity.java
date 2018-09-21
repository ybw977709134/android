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
 * 描述：微秀移动端设置页面
 * 作者：angelyin
 * 时间：2016/1/27 12:01
 * 备注：
 */
public class AppSettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView activity_app_setting_img_return;
    private RelativeLayout layt_app_anquan;
    private RelativeLayout layt_app_help;
    private RelativeLayout layt_app_about;
    private Button app_btn_logout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_setting_main);
        initView();
    }

    /**
     * 描述：  初始化组件
     * 作者：$angelyin
     * 时间：2016/1/27 12:24
     */
    private void initView() {
        activity_app_setting_img_return = (ImageView) findViewById(R.id.activity_app_setting_img_return);
        layt_app_anquan = (RelativeLayout) findViewById(R.id.layt_app_anquan);
        layt_app_help = (RelativeLayout) findViewById(R.id.layt_app_help);
        layt_app_about = (RelativeLayout) findViewById(R.id.layt_app_about);
        app_btn_logout = (Button) findViewById(R.id.app_btn_logout);


        app_btn_logout.setOnClickListener(this);
        layt_app_anquan.setOnClickListener(this);
        layt_app_help.setOnClickListener(this);
        layt_app_about.setOnClickListener(this);
        activity_app_setting_img_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_btn_logout:
                //退出登录
                this.finish();
                 clearUser();
                intent = new Intent(this, UserLoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.layt_app_anquan:
                //账号与安全
                intent = new Intent(this, AppSettingAnquanActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.layt_app_help:
                break;
            case R.id.layt_app_about:
                break;
            case R.id.activity_app_setting_img_return:
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;

        }
    }

    /**
     * 退出账号，清除数据
     */
    private void clearUser() {
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
        MyApplication.islogin.add(0,"未登录");
    }
}
