package com.onemeter.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.activity.UserLoginActivity;
import com.onemeter.adapter.ZhiDingXianShangFragmentAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.VishowInfo;
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
 * 描述：场景总活动数据
 * 作者：angelyin
 * 时间：2016/1/23 14:38
 * 备注：
 */
public class TotalHuoDongFragment extends Fragment implements AdapterView.OnItemClickListener {
    View view;
    private XListView fragment_zhi_ding_xian_xia_listview;
    private ZhiDingXianShangFragmentAdapter mZhiDingXianShangFragmentAdapter;
    private TextView btn_more_about;
    Intent intent;
    int uid;
    ProgressDialog prodialog;
    public static List<VishowInfo> vfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhi_ding_huo_dong_qian_ke_xia, container, false);
        prodialog = new ProgressDialog(getActivity());
        vfo = new ArrayList<VishowInfo>();
        intData();
        initView();
        return view;
    }

    /**
     * 初始化提交请求信息
     */
    private void intData() {
        if(!MyApplication.isNetAvailable){
            Utils.showToast(getActivity(), "网络未连接，请打开网络");
            return;
        }
       uid= UserLoginActivity.userInfos.get(0).getUid();
//        uid = 1;
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_MY_ALL_HUO_DONG_COUNT + "memberId=" + uid;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, TotalHuoDongFragment.this, "总活动统计");

    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/2/16 11:00
     */
    private void initView() {

        fragment_zhi_ding_xian_xia_listview = (XListView) view.findViewById(R.id.fragment_zhi_ding_xian_xia_listview);
        // 设置xlistview可以加载、刷新
        fragment_zhi_ding_xian_xia_listview.setPullLoadEnable(true);
        fragment_zhi_ding_xian_xia_listview.setPullRefreshEnable(true);
        fragment_zhi_ding_xian_xia_listview.setOnItemClickListener(this);
        btn_more_about = (TextView) view.findViewById(R.id.btn_more_about);
        btn_more_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              intent=new Intent(getActivity(), TestZheXianActivity.class);
//                startActivity(intent);
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Utils.showToast(getActivity(), mZhiDingXianShangFragmentAdapter.getItem(position) + "");
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
            Utils.showToast(getActivity(), getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            /**************************************************************************/
        if(action.equals("总活动统计")) {
            if (statu) {//成功
//           Utils.showToast(getActivity(),result);
                if (jsonArray.length() == 0) {
                    Utils.showToast(getActivity(), "没有数据");

                }else{
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int scanCount;
                        int applyCount;

                        VishowInfo vv = new VishowInfo();
                        int id = jsonArray.getJSONObject(i).getInt("id");
                        String name = jsonArray.getJSONObject(i).getString("name");
                        Long createDate = jsonArray.getJSONObject(i).getLong("createDate");
                        if (jsonArray.getJSONObject(i).isNull("scanCount")) {
                            scanCount=0;
                        } else {
                            scanCount = jsonArray.getJSONObject(i).getInt("scanCount");
                        }
                        if (jsonArray.getJSONObject(i).isNull("applyCount")) {
                            applyCount=0;
                        } else {
                            applyCount = jsonArray.getJSONObject(i).getInt("applyCount");
                        }

                        vv.setId(id);
                        vv.setCreateDate(createDate);
                        vv.setName(name);
                        vv.setScanCountId(scanCount);
                        vv.setApplyCount(applyCount);
                        vfo.add(vv);
//                        Utils.showToast(getActivity(), vfo.size() + "");
                    }
                }

                mZhiDingXianShangFragmentAdapter = new ZhiDingXianShangFragmentAdapter(getActivity(), vfo);
                fragment_zhi_ding_xian_xia_listview.setAdapter(mZhiDingXianShangFragmentAdapter);

            } else {
                Utils.showToast(getActivity(), message);
            }
        }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
