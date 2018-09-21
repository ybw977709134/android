package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.fragment.FourFragment;
import com.onemeter.fragment.OneFragment;
import com.onemeter.fragment.ThreeFragment;
import com.onemeter.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：引导页面
 * 作者：angelyin
 * 时间：2016/3/11 11:04
 * 备注：
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    /**
     * 滑动组件
     **/
    private ViewPager viewPager_guide;
    /**
     * 登陆微秀
     **/
    private Button button_login_vishow;
    /**
     * 登陆看看
     **/
    private Button button_first_look;
    /**
     * 小圆点组件
     **/
    private LinearLayout hot_ponit;
    /**
     * 立即体验
     **/
    private Button button_Immediate_login;
    /**添加的view页面**/
    /**
     * 页面集合
     */
    List<Fragment> fragmentList;
    /**
     * 四个Fragment（页面）
     */
    OneFragment oneFragment;
    TwoFragment twoFragment;
    ThreeFragment threeFragment;
    FourFragment fourFragment;

    /**
     * 四个点的组件
     **/
    private ImageView hot_ponit_1;
    private ImageView hot_ponit_2;
    private ImageView hot_ponit_3;
    private ImageView hot_ponit_4;

    /**
     * 滑动适配器
     **/
    private GuideViewPagerAdapter mGuideViewPagerAdapter;

    Intent intent;

    /**
     * 跳转按钮
     **/

    private Button btn_login_tiaozhuan;
    /**跳转布局**/

    private LinearLayout btn_login_tiaozhuan_rel;
    String  islogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide_main);
        islogin=MyApplication.islogin.get(0);
        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
        threeFragment = new ThreeFragment();
        fourFragment = new FourFragment();
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(oneFragment);
        fragmentList.add(twoFragment);
        fragmentList.add(threeFragment);
        fragmentList.add(fourFragment);
        hot_ponit_1 = (ImageView) findViewById(R.id.hot_ponit_1);
        hot_ponit_2 = (ImageView) findViewById(R.id.hot_ponit_2);
        hot_ponit_3 = (ImageView) findViewById(R.id.hot_ponit_3);
        hot_ponit_4 = (ImageView) findViewById(R.id.hot_ponit_4);
        hot_ponit_1.setImageResource(R.mipmap.hot_yes);
        btn_login_tiaozhuan_rel= (LinearLayout) findViewById(R.id.btn_login_tiaozhuan_rel);
        btn_login_tiaozhuan = (Button) findViewById(R.id.btn_login_tiaozhuan);
        viewPager_guide = (ViewPager) findViewById(R.id.viewPager_guide);
        button_login_vishow = (Button) findViewById(R.id.button_login_vishow);
        button_first_look = (Button) findViewById(R.id.button_first_look);
        hot_ponit = (LinearLayout) findViewById(R.id.hot_ponit);
        button_Immediate_login = (Button) findViewById(R.id.button_Immediate_login);
        mGuideViewPagerAdapter = new GuideViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager_guide.setAdapter(mGuideViewPagerAdapter);
        viewPager_guide.setCurrentItem(0);
        viewPager_guide.setOnPageChangeListener(this);
        button_login_vishow.setOnClickListener(this);
        button_first_look.setOnClickListener(this);
        button_Immediate_login.setOnClickListener(this);
        btn_login_tiaozhuan.setOnClickListener(this);
        btn_login_tiaozhuan_rel.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_tiaozhuan:
            case R.id.btn_login_tiaozhuan_rel:
                //跳转
                finish();
                intent = new Intent(GuideActivity.this, UserLoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.button_login_vishow:
                //登入微秀
                    finish();
                    intent = new Intent(GuideActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.button_first_look:
                //登入看看
                if(islogin.equals("未登录")){
                    finish();
                    intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

                }else {
                   MyApplication.islogin.add(0,"未登录");
                    finish();
                    intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);



                }


                break;
            case R.id.button_Immediate_login:
                //立即体验
                finish();
                intent = new Intent(GuideActivity.this, UserLoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;

        }
    }


    /**
     * 滑动监听
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {

        switch (position) {
            case 0:
                hot_ponit_1.setImageResource(R.mipmap.hot_yes);
                hot_ponit_2.setImageResource(R.mipmap.hot_no);
                hot_ponit_3.setImageResource(R.mipmap.hot_no);
                hot_ponit_4.setImageResource(R.mipmap.hot_no);

                button_Immediate_login.setVisibility(View.GONE);
                break;
            case 1:
                hot_ponit_1.setImageResource(R.mipmap.hot_no);
                hot_ponit_2.setImageResource(R.mipmap.hot_yes);
                hot_ponit_3.setImageResource(R.mipmap.hot_no);
                hot_ponit_4.setImageResource(R.mipmap.hot_no);
                button_Immediate_login.setVisibility(View.GONE);
                break;
            case 2:
                hot_ponit_1.setImageResource(R.mipmap.hot_no);
                hot_ponit_2.setImageResource(R.mipmap.hot_no);
                hot_ponit_3.setImageResource(R.mipmap.hot_yes);
                hot_ponit_4.setImageResource(R.mipmap.hot_no);
                button_Immediate_login.setVisibility(View.GONE);
                break;
            case 3:
                hot_ponit_1.setImageResource(R.mipmap.hot_no);
                hot_ponit_2.setImageResource(R.mipmap.hot_no);
                hot_ponit_3.setImageResource(R.mipmap.hot_no);
                hot_ponit_4.setImageResource(R.mipmap.hot_yes);
                button_Immediate_login.setVisibility(View.GONE);
                button_Immediate_login.setAlpha(0.8f);
                break;


        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 滑动适配器
     */
    class GuideViewPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragmentList;

        public GuideViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {

            return fragmentList.size();
        }


    }
}
