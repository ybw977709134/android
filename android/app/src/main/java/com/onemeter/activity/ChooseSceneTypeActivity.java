package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;



/**
 * 描述：选择场景类型
 * 项目名称：zhaosb_project
 * 作者：angelyin
 * 时间：2016/2/17 15:43
 * 备注：
 */
public class ChooseSceneTypeActivity extends BaseActivity implements View.OnClickListener {
    private Button choose_changjing_btn_1;
    private Button choose_changjing_btn_2;
    private Button choose_changjing_btn_3;
    private Button choose_changjing_btn_4;
    private ImageView activity_changjing_img_return;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_scence_type_main);
        initView();
    }

    private void initView() {
        activity_changjing_img_return = (ImageView) findViewById(R.id.activity_changjing_img_return);
        choose_changjing_btn_1 = (Button) findViewById(R.id.choose_changjing_btn_1);
        choose_changjing_btn_2 = (Button) findViewById(R.id.choose_changjing_btn_2);
        choose_changjing_btn_3 = (Button) findViewById(R.id.choose_changjing_btn_3);
        choose_changjing_btn_4 = (Button) findViewById(R.id.choose_changjing_btn_4);
        activity_changjing_img_return.setOnClickListener(this);
        choose_changjing_btn_1.setOnClickListener(this);
        choose_changjing_btn_2.setOnClickListener(this);
        choose_changjing_btn_3.setOnClickListener(this);
        choose_changjing_btn_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_changjing_img_return:
                //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.choose_changjing_btn_1:
                //趣学习
                intent=new Intent(ChooseSceneTypeActivity.this, QuStudyWebActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
//                toast("趣学习");
                break;
            case R.id.choose_changjing_btn_2:
                //趣活动
//                toast("趣活动");
                intent=new Intent(ChooseSceneTypeActivity.this, QuHuoDongWebActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.choose_changjing_btn_3:
                //趣相册
//                toast("趣相册");
                intent=new Intent(ChooseSceneTypeActivity.this, QupicWebActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;

            case R.id.choose_changjing_btn_4:
                //趣录音
//                toast("趣录音");
                intent=new Intent(ChooseSceneTypeActivity.this, QuluyinWebActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;

        }
    }


}
