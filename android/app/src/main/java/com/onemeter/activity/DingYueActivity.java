package com.onemeter.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.fragment.MyDingYueFragment;
import com.onemeter.fragment.TuiJianDingYueFragment;
import com.onemeter.view.AlwaysMarqueeTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：订阅show页面
 * 项目名称：zhaosb_project
 * 作者：angelyin
 * 时间：2016/2/16 16:06
 * 备注：
 */
public class DingYueActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    List<Fragment> list = null;
    private RadioGroup fragment_dy_radioGroup_title;
    private RadioButton fragment_dy_rb_tuijian;
    private RadioButton fragment_dy_rb_my;
    private View dy_view_cursor_1, dy_view_cursor_2;
    private ViewPager fragment_dy_vp;
    private ImageView activity_app_dingyue_img_return;
    private AlwaysMarqueeTextView dingyue_dingwei;
    String islogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ding_yue_main);
        islogin = MyApplication.islogin.get(0);
        initView();

    }

    /**
     * 描述： 初始化组件
     * 作者：$angelyin
     * 时间：2016/2/16 16:20
     */
    private void initView() {
        dingyue_dingwei = (AlwaysMarqueeTextView) findViewById(R.id.dingyue_dingwei);
        dingyue_dingwei.setText(MyApplication.dingweiLocation);
        activity_app_dingyue_img_return = (ImageView) findViewById(R.id.activity_app_dingyue_img_return);
        activity_app_dingyue_img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
            }
        });
        fragment_dy_radioGroup_title = (RadioGroup) findViewById(R.id.fragment_dy_radioGroup_title);
        fragment_dy_rb_tuijian = (RadioButton) findViewById(R.id.fragment_dy_rb_tuijian);
        fragment_dy_rb_my = (RadioButton) findViewById(R.id.fragment_dy_rb_my);
        dy_view_cursor_1 = findViewById(R.id.dy_view_cursor_1);
        dy_view_cursor_2 = findViewById(R.id.dy_view_cursor_2);
        dy_view_cursor_1.setVisibility(View.VISIBLE);
        dy_view_cursor_2.setVisibility(View.INVISIBLE);
        dy_view_cursor_2 = findViewById(R.id.dy_view_cursor_2);
        fragment_dy_vp = (ViewPager) findViewById(R.id.fragment_dy_vp);

        list = new ArrayList<Fragment>();
        TuiJianDingYueFragment tjdy = new TuiJianDingYueFragment();
        list.add(tjdy);
        if(islogin.equals("已登录")){
            MyDingYueFragment mdy = new MyDingYueFragment();
            list.add(mdy);
        }

        ViewPagerAdapter vpdt = new ViewPagerAdapter(getSupportFragmentManager(), list);
        fragment_dy_vp.setAdapter(vpdt);
        vpdt.notifyDataSetChanged();
        fragment_dy_radioGroup_title.setOnCheckedChangeListener(this);
        fragment_dy_rb_tuijian.setChecked(true);
        //滑动切换
        fragment_dy_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        fragment_dy_rb_tuijian.setChecked(true);
                        dy_view_cursor_1.setVisibility(View.VISIBLE);
                        dy_view_cursor_2.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        if (islogin.equals("已登录")) {
                            fragment_dy_rb_my.setChecked(true);
                            dy_view_cursor_2.setVisibility(View.VISIBLE);
                            dy_view_cursor_1.setVisibility(View.INVISIBLE);
                        } else {
                            //未登录
                            isLoginDialog();
                        }

                        break;

                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    /**
     * 未登录的对话框
     */
    private void isLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您还没有登录，是否现在去登录？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(DingYueActivity.this, UserLoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragment_dy_rb_tuijian.setChecked(true);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (fragment_dy_rb_tuijian.getId() == checkedId) {
            fragment_dy_vp.setCurrentItem(0);
            dy_view_cursor_1.setVisibility(View.VISIBLE);
            dy_view_cursor_2.setVisibility(View.INVISIBLE);

        } else if (fragment_dy_rb_my.getId() == checkedId) {
            if (islogin.equals("已登录")) {
                fragment_dy_vp.setCurrentItem(1);
                dy_view_cursor_2.setVisibility(View.VISIBLE);
                dy_view_cursor_1.setVisibility(View.INVISIBLE);
            } else {
                isLoginDialog();
            }

        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }
}
