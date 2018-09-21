package com.onemeter.activity;

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
import com.onemeter.fragment.DiBiaoShowFragment;
import com.onemeter.fragment.ZhouBianDongTaiFragment;
import com.onemeter.view.AlwaysMarqueeTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：周边动态
 * 项目名称：zhaosb_project
 * 作者：Administrator
 * 时间：2016/2/17 13:48
 * 备注：
 */
public class DynamicPeripheryActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private ImageView activity_app_dongtai_img_return;
    private RadioButton fragment_dy_rb_dongtai;
    private RadioButton fragment_dt_rb_dibiao;
    private RadioGroup fragment_dt_radioGroup_title;
    private View dt_view_cursor_1, dt_view_cursor_2;
    private ViewPager fragment_dt_vp;
    List<Fragment> list = null;

    private AlwaysMarqueeTextView zhoubian_dingwei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dynamic_periphery_main);
        initView();
    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/2/17 13:56
     */
    private void initView() {
        zhoubian_dingwei= (AlwaysMarqueeTextView) findViewById(R.id.zhoubian_dingwei);
        zhoubian_dingwei.setText(MyApplication.dingweiLocation);
        activity_app_dongtai_img_return = (ImageView) findViewById(R.id.activity_app_dongtai_img_return);
        activity_app_dongtai_img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
            }
        });
        fragment_dy_rb_dongtai = (RadioButton) findViewById(R.id.fragment_dy_rb_dongtai);
        fragment_dt_rb_dibiao = (RadioButton) findViewById(R.id.fragment_dt_rb_dibiao);
        fragment_dt_radioGroup_title = (RadioGroup) findViewById(R.id.fragment_dt_radioGroup_title);
        dt_view_cursor_1 = findViewById(R.id.dt_view_cursor_1);
        dt_view_cursor_2 = findViewById(R.id.dt_view_cursor_2);
        fragment_dt_vp = (ViewPager) findViewById(R.id.fragment_dt_vp);

        dt_view_cursor_1.setVisibility(View.VISIBLE);
        dt_view_cursor_2.setVisibility(View.INVISIBLE);
        list = new ArrayList<Fragment>();

        ZhouBianDongTaiFragment zbdt = new ZhouBianDongTaiFragment();
        DiBiaoShowFragment dbs = new DiBiaoShowFragment();
        list.add(zbdt);
        list.add(dbs);

        ViewPagerAdapter vpdt = new ViewPagerAdapter(getSupportFragmentManager(), list);
        fragment_dt_vp.setAdapter(vpdt);
        vpdt.notifyDataSetChanged();

        fragment_dt_radioGroup_title.setOnCheckedChangeListener(this);
        fragment_dy_rb_dongtai.setChecked(true);
        //滑动切换
        fragment_dt_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        fragment_dy_rb_dongtai.setChecked(true);
                        dt_view_cursor_1.setVisibility(View.VISIBLE);
                        dt_view_cursor_2.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        fragment_dt_rb_dibiao.setChecked(true);
                        dt_view_cursor_2.setVisibility(View.VISIBLE);
                        dt_view_cursor_1.setVisibility(View.INVISIBLE);
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (fragment_dy_rb_dongtai.getId() == checkedId) {
            fragment_dt_vp.setCurrentItem(0);
            dt_view_cursor_1.setVisibility(View.VISIBLE);
            dt_view_cursor_2.setVisibility(View.INVISIBLE);

        } else if (fragment_dt_rb_dibiao.getId() == checkedId) {
            fragment_dt_vp.setCurrentItem(1);
            dt_view_cursor_2.setVisibility(View.VISIBLE);
            dt_view_cursor_1.setVisibility(View.INVISIBLE);
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
