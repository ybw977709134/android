package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.onemeter.R;
import com.onemeter.activity.TuiJianDingYueDetailActivity;
import com.onemeter.adapter.TuiJianDingYueFragmentAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.hotdyInfo;
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
 * 描述：推荐订阅信息
 * 作者：angelyin
 * 时间：2016/2/16 16:25
 * 备注：
 */
public class TuiJianDingYueFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    private Handler handler;
    View view;
    private XListView tuijian_dingyue_listview;
    private TuiJianDingYueFragmentAdapter mtuiJianDingYueFragmentAdapter;
    /**
     * 进度条弹框
     **/
    private ProgressDialog prodialog;// 加载进度条对话框
    public static List<hotdyInfo> mhfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tui_jian_main, container, false);
        prodialog = new ProgressDialog(getActivity());// 进度条对话框
        mhfo = new ArrayList<hotdyInfo>();
        initData();
        initView();
        return view;
    }

    /**
     * 初始化数据信息
     */
    private void initData() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_DING_YUE_HOT_SCHOOL;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, TuiJianDingYueFragment.this, "订阅showr热门学校");

    }

    private void initView() {
        handler = new Handler();
        tuijian_dingyue_listview = (XListView) view.findViewById(R.id.tuijian_dingyue_listview);
        // 设置xlistview可以加载、刷新
        tuijian_dingyue_listview.setPullLoadEnable(true);
        tuijian_dingyue_listview.setPullRefreshEnable(true);
        tuijian_dingyue_listview.setXListViewListener(this);
        tuijian_dingyue_listview.setOnItemClickListener(this);

    }


    @Override
    public void onRefresh() {
        // 模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tuijian_dingyue_listview.stopRefresh();
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
                tuijian_dingyue_listview.stopLoadMore();
                Utils.showToast(getActivity(), "加载成功");
            }
        }, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyApplication.tuijian_dingyue_id.clear();
        MyApplication.tuijian_dingyue_id.add(mhfo.get(position-1).getSchooId()+"");
//        Utils.showToast(getActivity(), mtuiJianDingYueFragmentAdapter.getItemId(position) + "");
        Intent intent = new Intent(getActivity(), TuiJianDingYueDetailActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
    }

    /**
     * 请求结束后更新数据的方法
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
            Utils.showToast(getActivity(), getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");
            JSONArray jsonarray = jsonObject.getJSONArray("data");


            /**************************************************************************/
            if (action.equals("订阅showr热门学校")) {
                if (statu) {//成功
                    if (jsonarray.length() == 0) {//判断为空
                        Utils.showToast(getActivity(), "没有数据");
                    } else {//有数据
                        for (int i = 0; i < jsonarray.length(); i++) {
                            hotdyInfo hdf = new hotdyInfo();
                            if (jsonarray.getJSONObject(i).isNull("id")) {
                            } else {
                                hdf.setSchooId(jsonarray.getJSONObject(i).getInt("id"));
                            }
                            hdf.setSchool(jsonarray.getJSONObject(i).getString("school"));
                            hdf.setHeadImage(jsonarray.getJSONObject(i).getString("headImage"));
                            if(jsonarray.getJSONObject(i).isNull("banner")){
                                hdf.setBanner("assets/processedimages/main_banner.png");
                            }else {
                                String banner=jsonarray.getJSONObject(i).getString("banner");
                                String url="http://7xrula.com1.z0.glb.clouddn.com/"+banner;
                              hdf.setBanner(url);
                            }
                            mhfo.add(hdf);

                        }
                    }
                    mtuiJianDingYueFragmentAdapter = new TuiJianDingYueFragmentAdapter(getActivity(),mhfo);
                    tuijian_dingyue_listview.setAdapter(mtuiJianDingYueFragmentAdapter);


                } else {
                    Utils.showToast(getActivity(), message + "提交失败");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
