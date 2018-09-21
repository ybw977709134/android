package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;



/**
 * 描述：
 * 项目名称：xzsb_project
 * 作者：Administrator
 * 时间：2016/2/1 14:54
 * 备注：
 */
public class ByPhoneResetPasswordOverActivity extends BaseActivity implements View.OnClickListener {
    private Button activity_sms_find_password_btn_login;
    private ImageView activity_sms_find_password_img;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_by_phone_reset_success_main);
        activity_sms_find_password_img= (ImageView) findViewById(R.id.activity_sms_find_password_img);
        activity_sms_find_password_btn_login= (Button) findViewById(R.id.activity_sms_find_password_btn_login);

        activity_sms_find_password_img.setOnClickListener(this);
        activity_sms_find_password_btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_sms_find_password_img:
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.activity_sms_find_password_btn_login:
                intent=new Intent(this,UserLoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
        }
    }
}
