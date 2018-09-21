package com.onemeter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 描述：logo启动页面
 * 作者：angelyin
 * 时间：2016/3/11 10:22
 * 备注：
 */
public class LogoActivity  extends BaseActivity implements View.OnClickListener {
    /**按钮**/
    private Button logo_first_button;
    Intent intent;
    SharedPreferences  preferences;
    int count;
    /**
     * 定位客户端
     **/
    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_logo_main);
        preferences = getSharedPreferences("count",MODE_WORLD_READABLE);
        //记录是否是第一次启动app
        count = preferences.getInt("count", 0);
        mLocationClient = new LocationClient(this.getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        initView();

    }

    /**
     * 初始化定位
     */
    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //优先gps定位
        option.setPriority(LocationClientOption.GpsFirst);
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        InitLocation();
        mLocationClient.start();
        logo_first_button= (Button) findViewById(R.id.logo_first_button);
        logo_first_button.setOnClickListener(this);

        final  Intent  intent=new Intent();
        Timer  timer=new Timer();
        TimerTask  task=new TimerTask() {
            @Override
            public void run() {
              //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
                if (count == 0) {
                    Intent intent1 = new Intent();
                    intent1.setClass(getApplicationContext(),GuideActivity.class);
                    startActivity(intent1);
                    finish();
                }else {
                    Intent intent2 = new Intent();
                    intent2.setClass(getApplicationContext(),UserLoginActivity.class);
                    startActivity(intent2);
                    finish();
                }
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putInt("count", ++count);
                //提交修改
                editor.commit();

            }
        };
        timer.schedule(task,1000*1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logo_first_button:
//                //跳转按钮
//                finish();
//               intent=new Intent(LogoActivity.this,GuideActivity.class);
//                startActivity(intent);
                break;

        }
    }

    /**
     * 定位监听器
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            if(location==null){
                return;
            }

            StringBuffer sb = new StringBuffer(256);
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append(location.getAddrStr());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append(location.getAddrStr());
            }
            //获取定位地址和经纬度
            MyApplication.dingweiLocation =sb.toString();
//            toast(location.getAddrStr().toString());

        }


    }
}
