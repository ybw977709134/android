package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.onemeter.R;
import com.onemeter.alipay.PayDemoActivity;
import com.onemeter.app.BaseActivity;
import com.onemeter.utils.Utils;


/**
 * 支付中心
 * 作者：angelyin
 * 时间：2016/1/5 10:15
 * 备注：
 */
public class PayCenterActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_weixin_pay;
    private Button btn_ali_pay;
     Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pay_center);
     initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        btn_weixin_pay= (Button) findViewById(R.id.btn_weixin_pay);
        btn_ali_pay= (Button) findViewById(R.id.btn_ali_pay);

        btn_ali_pay.setOnClickListener(this);
        btn_weixin_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_weixin_pay:
                //微信支付
                Utils.showToast(getApplicationContext(), "微信支付暂未开启");
                break;
            case R.id.btn_ali_pay:
                //支付宝支付
                Utils.showToast(getApplicationContext(),"支付宝快捷支付");
              intent =new Intent(PayCenterActivity.this,PayDemoActivity.class);
                startActivity(intent);
                break;

        }
    }
}
