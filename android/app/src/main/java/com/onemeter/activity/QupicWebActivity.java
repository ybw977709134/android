package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.onemeter.R;
import com.onemeter.adapter.QupicWebActivityAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.QupicModelInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：趣相册
 * 项目名称：vishow_project
 * 作者：angelyin
 * 时间：2016/3/22 13:22
 * 备注：
 */
public class QupicWebActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView  activity_qupic_img_return;//返回键
    private  String  type="趣相册" ;//参数类型
    private GridView  quxiangce_moban_gridView;
    private QupicWebActivityAdapter mQupicWebActivityAdpater;
    ProgressDialog prodialog;// 加载进度条对话框
    public List<QupicModelInfo>  mqpmf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_qupic_web_main);
        prodialog=new ProgressDialog(this);
        mqpmf=new ArrayList<QupicModelInfo>();
        initData();
        initView();
    }

    /**
     * 发起获取相册模板的请求
     */
    private void initData() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_GET_QU_PIC_MODEL_LIST + "type=" +type;
        new NetUtil(this).sendGetToServer(urlvalue, QupicWebActivity.this, "趣相册分类模板列表");

    }

    /**
     * 初始化组件
     */
    private void initView() {
        activity_qupic_img_return = (ImageView) findViewById(R.id.activity_qupic_img_return);
        quxiangce_moban_gridView= (GridView) findViewById(R.id.quxiangce_moban_gridView);
        activity_qupic_img_return.setOnClickListener(this);
        quxiangce_moban_gridView.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.activity_qupic_img_return:
            //返回键
            finish();
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
/****************************************************************************************************/
        if(action.equals("趣相册分类模板列表")){
            if(statu){
                mqpmf.clear();
                //成功
                JSONArray  jsonArray=jsonObject.getJSONArray("data");
                if(jsonArray.length()==0){
                  return;
                }
                for(int i=0;i<jsonArray.length();i++){
                    QupicModelInfo  qqinfo=new QupicModelInfo();
                    if(jsonArray.getJSONObject(i).isNull("id")){
                    }else {
                        qqinfo.setId(jsonArray.getJSONObject(i).getInt("id"));
                    }
                    if(jsonArray.getJSONObject(i).isNull("name")){
                    }else {
                        qqinfo.setName(jsonArray.getJSONObject(i).getString("name"));
                    }
                    if(jsonArray.getJSONObject(i).isNull("cover")){
                    }else{
                        String template = jsonArray.getJSONObject(i).getString("cover");
                        String url = "http://7xrula.com1.z0.glb.clouddn.com/" + template;
                      qqinfo.setCover(url);
                    }
                    if(jsonArray.getJSONObject(i).isNull("useCount")){
                    }else {
                        qqinfo.setUsecount(jsonArray.getJSONObject(i).getInt("useCount"));
                    }
                    mqpmf.add(qqinfo);
                }
                mQupicWebActivityAdpater=new QupicWebActivityAdapter(this,mqpmf);
                quxiangce_moban_gridView.setAdapter(mQupicWebActivityAdpater);
                mQupicWebActivityAdpater.notifyDataSetChanged();
            }else {
             toast(message);
            }

        }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent =new Intent(QupicWebActivity.this,qupicDetailWebActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("groupId", mqpmf.get(position).getId());
         startActivity(intent);
//        toast("传值："+mqpmf.get(position).getId()+"");
        this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

    }
}
