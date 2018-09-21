package com.onemeter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.htjchangjingdetail;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：推荐场景详情页
 * 作者：angelyin
 * 时间：2016/3/31 18:02
 * 备注：
 */
public class HomeTJDingYuedetail extends BaseActivity implements View.OnClickListener {
    private ImageView activity_htuijian_img_return;//返回键
    private ImageView img_htuijian_dingyue_title;//学校图标
    private TextView text_htuijian_dingyue_title;//标题
    private TextView text_htuijian_number_jiulanliang;//浏览量
    private TextView htuijian_dingyue_number;//订阅数
    private ProgressDialog prodialog;//进度条对话框
    public List<htjchangjingdetail> hcjdl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_home_tuijian_detail_layout);
        prodialog=new ProgressDialog(this);
        hcjdl=new ArrayList<htjchangjingdetail>();
        initData();

    }

    /**
     * 初始化组件
     */
    private void initView() {
        activity_htuijian_img_return= (ImageView) findViewById(R.id.activity_htuijian_img_return);
        img_htuijian_dingyue_title= (ImageView) findViewById(R.id.img_htuijian_dingyue_title);
        text_htuijian_dingyue_title= (TextView) findViewById(R.id.text_htuijian_dingyue_title);
        text_htuijian_number_jiulanliang= (TextView) findViewById(R.id.text_htuijian_number_jiulanliang);
        htuijian_dingyue_number= (TextView) findViewById(R.id.htuijian_dingyue_number);

        activity_htuijian_img_return.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        int  meberId= Integer.valueOf(MyApplication.htuijian_changjing_id.get(0));
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_ZHI_DING_HOT_SCHOOL_DETAIL+"memberId="+meberId;
        new NetUtil(this).sendGetToServer(urlvalue, HomeTJDingYuedetail.this, "主页推荐学校详情");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_htuijian_img_return:
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
        }
    }

    /**
     * 请求结束后此方法更新数据
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
         JSONObject jsonObject1=jsonObject.getJSONObject("data");
        /**************************************************************************/
        if (action.equals("主页推荐学校详情")) {
            if (statu) {//成功
                htjchangjingdetail  hh=new htjchangjingdetail();
//                toast("提交成功" + result);
               if(jsonObject1.isNull("id")){
               }else {
                   int id=jsonObject1.getInt("id");
                   hh.setId(id);
               }
                if(jsonObject1.isNull("schoolName")){
                    hh.setSchoolName("");
                }else {
                    String schoolName=jsonObject1.getString("schoolName");
                    hh.setSchoolName(schoolName);
                }
                if(jsonObject1.isNull("headImage")){
                    hh.setHeadImage("");
                }else {
                    String headImage=jsonObject1.getString("headImage");
                    hh.setHeadImage(headImage);
                }

                if(jsonObject1.isNull("subCount")){
                    hh.setSubCount(0);
                }else {
                    int  subCount=jsonObject1.getInt("subCount");
                    hh.setSubCount(subCount);
                }
                if(jsonObject1.isNull("allScanCount")){
                    hh.setAllScanCount(0);
                }else {
                  int  allScanCount=jsonObject1.getInt("allScanCount");
                    hh.setAllScanCount(allScanCount);
                }
                hcjdl.add(hh);
                initView();
                text_htuijian_dingyue_title.setText(hcjdl.get(0).getSchoolName().toString());
                text_htuijian_number_jiulanliang.setText(hcjdl.get(0).getSubCount()+"");
                htuijian_dingyue_number.setText(hcjdl.get(0).getAllScanCount()+"");

            } else {
                toast(message + "提交失败");

            }


        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
