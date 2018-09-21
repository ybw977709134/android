package com.onemeter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
 * 设置个性签名
 * 作者：angelyi
 * 时间：2016/3/28 18:52
 * 备注：
 */
public class ChangeMiaoShuActivity extends BaseActivity implements View.OnClickListener {
    private ImageView activity_miaoshu_img_return;
    private EditText text_setting_miaoshu;
    private ImageView miaoshu_change_queding;
    private String miaoshu_name;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_miaoshu_layout);
        prodialog = new ProgressDialog(this);
        miaoshu_name=getIntent().getStringExtra("miaoshu");
    initView();

    }

    /**
     * 初始化组件
     */
    private void initView() {
        activity_miaoshu_img_return= (ImageView) findViewById(R.id.activity_miaoshu_img_return);
        text_setting_miaoshu= (EditText) findViewById(R.id.text_setting_miaoshu);
        text_setting_miaoshu.setText(MyApplication.user_miaoshu);
        miaoshu_change_queding= (ImageView) findViewById(R.id.miaoshu_change_queding);

        activity_miaoshu_img_return.setOnClickListener(this);
        miaoshu_change_queding.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_miaoshu_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.miaoshu_change_queding:
                //确定
                getNetMiaoShuChang();
                break;
        }
    }

    /**
     * 提交设置描述请求
     */
    private void getNetMiaoShuChang() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", UserLoginActivity.userInfos.get(0).getUid() + "");
        params.addBodyParameter("describe", text_setting_miaoshu.getText().toString());
        params.addBodyParameter("type", "修改描述");
        new NetUtil(this).sendPostToServer(params, Constants.API_UPDATE_USER, ChangeMiaoShuActivity.this, "修改描述");

    }

    /**
     * 提交请求后此方法更新数据
     * @param result
     * @param isSuccess
     * @param api
     * @param action
     */
    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        JSONObject jsonObject = null;
        if (prodialog != null && prodialog.isShowing()) {
            prodialog.dismiss();
            Log.i("Onemeter", "关闭prodialog");
        }
        if (!isSuccess) {// 请求不成功
            Utils.showToast(this, getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");


        /**************************************************************************/

        if (action.equals("修改描述")) {
            if (statu) {//成功
                toast("修改成功" + result);
                this.finish();
                MyApplication.user_miaoshu=text_setting_miaoshu.getText().toString();
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

            } else {
                toast(message + "修改失败");
            }
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
