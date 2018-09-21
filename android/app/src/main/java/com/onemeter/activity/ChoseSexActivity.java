package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 描述：性别选择页面
 * 作者：Administrator
 * 时间：2016/1/27 17:50
 * 备注：
 */
public class ChoseSexActivity extends BaseActivity implements View.OnClickListener {
    private ImageView activity_chose_sex_img_return;
    private RelativeLayout user_setting_nan;
    private RelativeLayout user_setting_nv;
    private TextView user_setting_nan_text;
    private TextView user_setting_nv_text;
    String resultCod;
    //选择的状态
    private ImageView btn_nan;
    private ImageView btn_nv;
    private String sextext;
    private int s;
    private String sex_name;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chose_sex_main);
        sex_name = getIntent().getStringExtra("sex_name");
        initView();
    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/27 17:58
     */
    private void initView() {
        btn_nan = (ImageView) findViewById(R.id.btn_nan);
        btn_nv = (ImageView) findViewById(R.id.btn_nv);
        user_setting_nan_text = (TextView) findViewById(R.id.user_setting_nan_text);
        user_setting_nv_text = (TextView) findViewById(R.id.user_setting_nv_text);
        activity_chose_sex_img_return = (ImageView) findViewById(R.id.activity_chose_sex_img_return);
        user_setting_nan = (RelativeLayout) findViewById(R.id.user_setting_nan);
        user_setting_nv = (RelativeLayout) findViewById(R.id.user_setting_nv);

        if (sex_name.equals("女")) {
            btn_nan.setImageResource(R.mipmap.btn_sign2);
            btn_nv.setImageResource(R.mipmap.btn_sign1);

        } else {
            btn_nan.setImageResource(R.mipmap.btn_sign1);
            btn_nv.setImageResource(R.mipmap.btn_sign2);
        }

        activity_chose_sex_img_return.setOnClickListener(this);
        user_setting_nan.setOnClickListener(this);
        user_setting_nv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_chose_sex_img_return:
                //返回
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.user_setting_nan:
                //点击男
                s = 0;
                getNetChoseSex(s);
                btn_nan.setImageResource(R.mipmap.btn_sign1);
                btn_nv.setImageResource(R.mipmap.btn_sign2);
                break;
            case R.id.user_setting_nv:
                //点击女
                s = 1;
                getNetChoseSex(s);
                btn_nan.setImageResource(R.mipmap.btn_sign2);
                btn_nv.setImageResource(R.mipmap.btn_sign1);
                break;


        }
    }

    /**
     * 修改性别提交事件
     */
    private void getNetChoseSex(int s) {
        int userId = UserLoginActivity.userInfos.get(0).getUid();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", UserLoginActivity.userInfos.get(0).getUid() + "");
        params.addBodyParameter("sex", s + "");
        params.addBodyParameter("type", "修改性别");
        new NetUtil(this).sendPostToServer(params, Constants.API_UPDATE_USER, ChoseSexActivity.this, "修改性别");
    }

    /**
     * 请求结束后此方法更新数据
     *
     * @param result
     * @param isSuccess
     * @param api
     * @param action
     */

    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        JSONObject jsonObject = null;

        if (!isSuccess) {// 请求不成功
            Utils.showToast(this, getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**************************************************************************/
        if (action.equals("修改性别")) {
            if (statu) {//成功
                toast("修改成功" + result);
                if (s == 0) {
                    MyApplication.sex_name = "男";
                } else {
                    MyApplication.sex_name = "女";
                }
                finish();
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
            } else {
                toast(message);

            }

        }
/*******************************************************************************************/

    }
}
