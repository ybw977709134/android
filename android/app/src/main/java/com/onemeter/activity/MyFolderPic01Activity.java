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
import com.onemeter.fragment.MyPicFragment;
import com.onemeter.fragment.PicLocationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：本地、我的
 * 项目名称：android
 * 作者：angelyin
 * 时间：2016/4/11 17:04
 * 备注：
 */
public class MyFolderPic01Activity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private ImageView  activity_app_pic_img_return;
    private RadioButton  fragment_pic_rb_location;
    private RadioButton  fragment_pic_rb_my;
    private RadioGroup   fragment_pic_radioGroup_title;
    private ViewPager  fragment_pic_vp;
    private List<Fragment> list = null;
    PicLocationFragment pltn;
    MyPicFragment mft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_myfolder_pic_layout);
      initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        activity_app_pic_img_return= (ImageView) findViewById(R.id.activity_app_pic_img_return);
        activity_app_pic_img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
            }
        });
        fragment_pic_rb_location= (RadioButton) findViewById(R.id.fragment_pic_rb_location);
        fragment_pic_rb_my= (RadioButton) findViewById(R.id.fragment_pic_rb_my);
        fragment_pic_radioGroup_title= (RadioGroup) findViewById(R.id.fragment_pic_radioGroup_title);
        fragment_pic_vp= (ViewPager) findViewById(R.id.fragment_pic_vp);
        list = new ArrayList<Fragment>();
        pltn = new PicLocationFragment();
         mft = new MyPicFragment();
        list.add(pltn);
        list.add(mft);
        PicViewPagerAdapter vpdt = new PicViewPagerAdapter(getSupportFragmentManager(), list);
        fragment_pic_vp.setAdapter(vpdt);
        vpdt.notifyDataSetChanged();
        fragment_pic_radioGroup_title.setOnCheckedChangeListener(this);
        fragment_pic_rb_location.setChecked(true);
        //滑动切换
        fragment_pic_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        //本地
                        fragment_pic_rb_location.setChecked(true);
                        fragment_pic_rb_location.setTextColor(getResources().getColor(R.color.white));
                        fragment_pic_rb_location.setTextSize(18);
                        fragment_pic_rb_my.setTextColor(getResources().getColor(R.color.white_02));
                        fragment_pic_rb_my.setTextSize(16);
                        break;
                    case 1:
                        //我的
                        fragment_pic_rb_my.setChecked(true);
                        fragment_pic_rb_my.setTextColor(getResources().getColor(R.color.white));
                        fragment_pic_rb_my.setTextSize(18);
                        fragment_pic_rb_location.setTextColor(getResources().getColor(R.color.white_02));
                        fragment_pic_rb_location.setTextSize(16);
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
        if (fragment_pic_rb_location.getId() == checkedId) {
            fragment_pic_vp.setCurrentItem(0);
            pltn.onResume();

        } else if (fragment_pic_rb_my.getId() == checkedId) {
            fragment_pic_vp.setCurrentItem(1);
           mft.onResume();
        }
    }

    class PicViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> list;

        public PicViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
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
