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
import com.onemeter.fragment.TotalHuoDongFragment;
import com.onemeter.fragment.ZhiDingHuoDongFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:指定场景数据统计数据
 * 时间：2016/1/18 18:21
 * 备注：
 */
public class ZhiDingQianKeTongJiActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ImageView activity_qian_ke_zhi_ding_img_return;
    private RadioGroup qianke_radioGroup;
    private RadioButton zhiding_qianke_rb_xianshang, zhiding_qianke_rb_xianxia;
    private ViewPager viewpager_zhiding_qianke;
    List<Fragment> list = null;
    View zd_view_cursor_1;
    View zd_view_cursor_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zhi_ding_qian_ke_tong_ji_main);
      initView();
    }
    /**
    *
    * 描述：初始化组件
    * 作者：$angelyin
    * 时间：2016/1/18 18:23
    *
    */
    private void initView() {
        zd_view_cursor_1=findViewById(R.id.zd_view_cursor_1);
        zd_view_cursor_1.setBackgroundColor(getResources().getColor(R.color.green));
        zd_view_cursor_2=findViewById(R.id.zd_view_cursor_2);
        zd_view_cursor_2.setVisibility(View.INVISIBLE);
        qianke_radioGroup = (RadioGroup)findViewById(R.id.qianke_radioGroup);
        zhiding_qianke_rb_xianshang = (RadioButton)findViewById(R.id.zhiding_qianke_rb_xianshang);
        zhiding_qianke_rb_xianxia = (RadioButton)findViewById(R.id.zhiding_qianke_rb_xianxia);
        viewpager_zhiding_qianke=(ViewPager)findViewById(R.id.viewpager_zhiding_qianke);
        activity_qian_ke_zhi_ding_img_return=(ImageView)findViewById(R.id.activity_qian_ke_zhi_ding_img_return);
        activity_qian_ke_zhi_ding_img_return.setOnClickListener(this);
        list = new ArrayList<Fragment>();
        ZhiDingHuoDongFragment zdxs=new ZhiDingHuoDongFragment();
        TotalHuoDongFragment zdxx=new TotalHuoDongFragment();
        list.add(zdxs);
        list.add(zdxx);

        ZdhdqkAdapter zxzc = new ZdhdqkAdapter(getSupportFragmentManager(), list);
        viewpager_zhiding_qianke.setAdapter(zxzc);
        zxzc.notifyDataSetChanged();

        qianke_radioGroup.setOnCheckedChangeListener(this);
        zhiding_qianke_rb_xianshang.setChecked(true);
//        zhiding_qianke_rb_xianxia.setChecked(true);

//滑动切换
        viewpager_zhiding_qianke.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        zhiding_qianke_rb_xianshang.setChecked(true);
                        break;
                    case 1:
                        zhiding_qianke_rb_xianxia.setChecked(true);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_qian_ke_zhi_ding_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int cheakedId) {
        if (cheakedId == zhiding_qianke_rb_xianshang.getId()) {
            viewpager_zhiding_qianke.setCurrentItem(0);
            zd_view_cursor_1.setVisibility(View.VISIBLE);
            zd_view_cursor_1.setBackgroundColor(getResources().getColor(R.color.green));
            zd_view_cursor_2.setVisibility(View.INVISIBLE);

        } else if (cheakedId == zhiding_qianke_rb_xianxia.getId()) {
            viewpager_zhiding_qianke.setCurrentItem(1);
            zd_view_cursor_2.setVisibility(View.VISIBLE);
            zd_view_cursor_2.setBackgroundColor(getResources().getColor(R.color.green));
            zd_view_cursor_1.setVisibility(View.INVISIBLE);
        }
    }


    class ZdhdqkAdapter extends FragmentStatePagerAdapter {


        List<Fragment> list;
        public ZdhdqkAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.list=list;
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
