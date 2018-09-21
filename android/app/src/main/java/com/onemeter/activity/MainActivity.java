package com.onemeter.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.fragment.ChangJingFragment;
import com.onemeter.fragment.HomeFragment;
import com.onemeter.fragment.MyFragment;


/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private long mExitTime;
    /**
     * 三个切换按钮
     **/
    private View rb1, rb3;
    private ImageView rb2;
    /**
     * 三个fragment对象
     **/
    private ChangJingFragment fragment1;
    private MyFragment fragment3;
    private HomeFragment fragment2;
    // 定义FragmentManager
    FragmentManager fragmentManager;

    /**
     * 三种切换的图标和文字
     **/
    private ImageView rb1_img, rb3_img;
    private TextView rb1_text, rb3_text;
    String islogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        islogin = MyApplication.islogin.get(0);
        initView();
        fragmentManager = getSupportFragmentManager();
        // 默认打开第一个选项
        setTabSelection(R.id.rb2);

    }

    /**
     * 初始化组件
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView() {
        LinearLayout rb2_lyout = (LinearLayout) findViewById(R.id.rb2_lyout);
        rb1_img = (ImageView) findViewById(R.id.rb1_img);
        rb3_img = (ImageView) findViewById(R.id.rb3_img);
        rb1_text = (TextView) findViewById(R.id.rb1_text);
        rb3_text = (TextView) findViewById(R.id.rb3_text);
        rb1 = findViewById(R.id.rb1);
        rb2 = (ImageView) findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);


        rb3.setOnClickListener(this);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);


    }

    // 当点击了消息tab时，选中第1个tab
    public void setTabSelection(int i) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 创建FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (i) {
            case R.id.rb1:
                if (islogin.equals("已登录")) {
                    rb1_text.setTextColor(getResources().getColor(R.color.green));
                    rb1_img.setImageResource(R.mipmap.changjing_1);
                    if (fragment1 == null) {
                        // 如果ChangJingFragment为空，则创建一个并添加到界面上
                        fragment1 = new ChangJingFragment();
                        fragmentTransaction.add(R.id.main_fragment, fragment1);
                    } else {
                        // 如果ChangJingFragment不为空，则直接将它显示出来
                        fragmentTransaction.show(fragment1);
                        fragment1.onResume();
                    }
                } else {
                    //弹出需要登录的对话框
                    isLoginDialog();
                }

                break;
            case R.id.rb2:
                rb2.setImageResource(R.mipmap.home_1);
                if (fragment2 == null) {
                    // 如果HomeFragment为空，则创建一个并添加到界面上
                    fragment2 = new HomeFragment();
                    fragmentTransaction.add(R.id.main_fragment, fragment2);
                } else {
                    // 如果HomeFragment不为空，则直接将它显示出来
                    fragmentTransaction.show(fragment2);
                }
                break;
            case R.id.rb3:
                if (islogin.equals("已登录")) {
                    rb3_text.setTextColor(getResources().getColor(R.color.green));
                    rb3_img.setImageResource(R.mipmap.my_1);
                    if (fragment3 == null) {
                        // 如果MyFragment为空，则创建一个并添加到界面上
                        fragment3 = new MyFragment();
                        fragmentTransaction.add(R.id.main_fragment, fragment3);
                    } else {
                        // 如果MyFragment不为空，则直接将它显示出来
                        fragmentTransaction.show(fragment3);
                        fragment3.onResume();
                    }
                } else {
                    //未登录时弹出需要登录的对话框
                    isLoginDialog();
                }
                break;
        }
        fragmentTransaction.commit();
    }

    /**
     * 跳转登陆的对话框
     *
     * @param
     */
    private void isLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您还没有登录，是否现在去登录？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setTabSelection(R.id.rb2);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    // 清除掉所有的选中状态。
    private void clearSelection() {
        rb1_text.setTextColor(getResources().getColor(R.color.black));
        rb1_img.setImageResource(R.mipmap.changjing_2);
        rb3_text.setTextColor(getResources().getColor(R.color.black));
        rb3_img.setImageResource(R.mipmap.my_2);
        rb2.setImageResource(R.mipmap.home_2);
    }


    // 将所有的Fragment都置为隐藏状态。
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fragment1 != null) {
            fragmentTransaction.hide(fragment1);
        }
        if (fragment2 != null) {
            fragmentTransaction.hide(fragment2);
        }
        if (fragment3 != null) {
            fragmentTransaction.hide(fragment3);
        }

    }

    // 监听手机上的BACK键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 判断两次点击的时间间隔（默认设置为2秒）
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
//                finish();
//                System.exit(0);
                Intent intent = new Intent();
                intent.setAction(BaseActivity.SYSTEM_EXIT);
                sendBroadcast(intent);
                super.onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb1:
                // 当点击了消息tab时，选中场景
                setTabSelection(R.id.rb1);
                break;
            case R.id.rb2:
                //选中主页面
                setTabSelection(R.id.rb2);
                break;
            case R.id.rb3:
                //选中我的
                setTabSelection(R.id.rb3);
                break;
        }
    }
}
