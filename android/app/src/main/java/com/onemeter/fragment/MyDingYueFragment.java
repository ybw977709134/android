package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.onemeter.R;
import com.onemeter.activity.UserLoginActivity;
import com.onemeter.adapter.MyDingYueFragmentAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.hotschool;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * 描述：我的订阅页面()
 * 作者：Administrator
 * 时间：2016/2/16 16:26
 * 备注：
 */
public class MyDingYueFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
     View view;
    /**
     * 列表项
     **/
    private XListView my_dingyue_listview;
    private Handler handler;
    private MyDingYueFragmentAdapter myDingYueFragmentAdapter;

    ProgressDialog prodialog;// 加载进度条对话框
     private List<hotschool> hotl;
    int userId ;
    String  islogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_ding_yue_main, container, false);
        prodialog=new ProgressDialog(getActivity());
        islogin=MyApplication.islogin.get(0);
        if(islogin.equals("已登录")){
            userId = UserLoginActivity.userInfos.get(0).getUid();
            hotl=new ArrayList<hotschool>();
        }

        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(islogin.equals("已登录")){
            getNetMyhuoDong();
        }

    }

    /**
     * 发送请求获取我的活动信息
     */
    private void getNetMyhuoDong() {
        hotl.clear();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_MY_DING_YUE+"subscriber="+userId;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, MyDingYueFragment.this, "我的订阅");


    }

    /**
     * 初始化组件
     */
    private void initView() {
        handler = new Handler();
        my_dingyue_listview = (XListView) view.findViewById(R.id.my_dingyue_listview);
        //设置可以刷新和加载
        my_dingyue_listview.setPullLoadEnable(true);
        my_dingyue_listview.setPullRefreshEnable(true);
        my_dingyue_listview.setXListViewListener(this);
        my_dingyue_listview.setOnItemClickListener(this);

    }


    @Override
    public void onRefresh() {
        // 模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                my_dingyue_listview.stopRefresh();
                Utils.showToast(getActivity(), "刷新成功");

            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            // 模拟加载数据，1s之后停止加载
            @Override
            public void run() {
                my_dingyue_listview.stopLoadMore();
                Utils.showToast(getActivity(), "加载成功");
            }
        }, 1000);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Utils.showToast(getActivity(), myDingYueFragmentAdapter.getItemId(position) + "");

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
            Utils.showToast(getActivity(), getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");
            JSONArray jsonArray=jsonObject.getJSONArray("data");
        /**************************************************************************/
        if (action.equals("我的订阅")) {
            if (statu) {//成功
//                Utils.showToast(getActivity(),"提交成功" + result);
                if(jsonArray.length()==0){
                    Utils.showToast(getActivity(),"数据为空");
                   return;
                }
                for (int i=0;i<jsonArray.length();i++){
                    hotschool   hl=new hotschool();
                    int id=jsonArray.getJSONObject(i).getInt("id");
                    String schoolName=jsonArray.getJSONObject(i).getString("schoolName");
                    String headImage=jsonArray.getJSONObject(i).getString("headImage");
                    int subCount=jsonArray.getJSONObject(i).getInt("subCount");
                    int allScanCount=jsonArray.getJSONObject(i).getInt("allScanCount");
                    hl.setSchoolId(id);
                    hl.setSchool(schoolName);
                    hl.setSubCount(subCount);
                    hl.setAllScanCount(allScanCount);
                    hl.setHeadImage(headImage);
                    hotl.add(hl);
                }
                myDingYueFragmentAdapter = new MyDingYueFragmentAdapter(getActivity(),hotl);
                my_dingyue_listview.setAdapter(myDingYueFragmentAdapter);

            } else {
                Utils.showToast(getActivity(),message + "提交失败");
            }
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
