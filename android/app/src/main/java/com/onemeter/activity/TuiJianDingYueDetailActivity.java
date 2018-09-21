package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.onemeter.R;
import com.onemeter.adapter.TuiJianDingYueDetailAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.hotschoolInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：推荐订阅学校
 * 项目名称：zhaosb
 * 作者：angelyin
 * 时间：2016/3/3 13:20
 * 备注：
 */
public class TuiJianDingYueDetailActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    /**
     * 返回键
     **/
    private ImageView activity_tuijian_dingyue_detail_img_return;

    /**
     * 列表组件
     **/
    private XListView tuijian_dingyue_listview;
    /**
     * 适配器
     **/
    private TuiJianDingYueDetailAdapter mTuiJianDingYueDetailAdapter;
    public List<hotschoolInfo> mhsf;
    private Handler handler;
    /**
     * 图片工具类对象
     **/
    private ImageLoader mImageLoader;
    private Context mContext;
    /**
     * 进度条弹框
     **/
    private ProgressDialog prodialog;// 加载进度条对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tuijian_dingyue_detail_layout);
        prodialog = new ProgressDialog(this);
        mhsf=new ArrayList<hotschoolInfo>();
        mContext = this;
        mImageLoader = ImageLoader.getInstance();
        handler = new Handler();
        initData();
        initView();
    }

    /**
     * 提交获取订阅学校信息
     */
    private void initData() {
        int id= Integer.valueOf(MyApplication.tuijian_dingyue_id.get(0).toString()).intValue();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_ZHI_DING_HOT_SCHOOL_DETAIL+"memberId="+id;
        new NetUtil(this).sendGetToServer(urlvalue, TuiJianDingYueDetailActivity.this, "推荐学校详情");


    }


    /**
     * 初始化组件
     */
    private void initView() {

        activity_tuijian_dingyue_detail_img_return = (ImageView) findViewById(R.id.activity_tuijian_dingyue_detail_img_return);
        tuijian_dingyue_listview = (XListView) findViewById(R.id.tuijian_dingyue_listview2);
        // 设置xlistview可以加载、刷新
        tuijian_dingyue_listview.setPullLoadEnable(true);
        tuijian_dingyue_listview.setPullRefreshEnable(true);
        tuijian_dingyue_listview.setXListViewListener(this);
        tuijian_dingyue_listview.setOnItemClickListener(this);
        activity_tuijian_dingyue_detail_img_return.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_tuijian_dingyue_detail_img_return:
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;

        }
    }


    @Override
    public void onRefresh() {
        // 模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tuijian_dingyue_listview.stopRefresh();
                Utils.showToast(getApplicationContext(), "刷新成功");

            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            // 模拟加载数据，1s之后停止加载
            @Override
            public void run() {
                tuijian_dingyue_listview.stopLoadMore();
                Utils.showToast(getApplicationContext(), "加载成功");
            }
        }, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toast(position + "");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImageLoader != null) {
            mImageLoader.clearMemoryCache();
            mImageLoader.clearDiscCache();
        }
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

        if(action.equals("推荐学校详情")){
            if(statu){//成功
                if (jsonObject1.length() == 0) {//判断为空
                    Utils.showToast(this, "没有数据");
                }else {//有数据
                    hotschoolInfo   hh=new hotschoolInfo();

                    if(jsonObject1.isNull("id")){
                    }else {
                        int id=jsonObject1.getInt("id");
                        hh.setSchoolId(id);
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
                        mhsf.add(hh);
                    }
//                  toast(mhsf.size()+"");
                }

                mTuiJianDingYueDetailAdapter = new TuiJianDingYueDetailAdapter(mContext, mhsf, mImageLoader);
                tuijian_dingyue_listview.setAdapter(mTuiJianDingYueDetailAdapter);

            }else {
                toast(message);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
