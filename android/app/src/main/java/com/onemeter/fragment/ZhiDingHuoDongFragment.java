package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.ListViewForScrollView;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * 描述：场景本活动的数据
 * 作者：Administrator
 * 时间：2016/1/23 14:35
 * 备注：
 */
public class ZhiDingHuoDongFragment extends Fragment {
    View view;
    private ListViewForScrollView fragment_zhi_ding_xian_shang_listview;
    private TextView btn_more_about;
    Intent intent;
    //判断是调用哪个api接口
    String type;
    //提交单个活动的id
    int activityId;
    ProgressDialog prodialog;
    String urlvalue;
    //浏览数
    private TextView txt_fragment_qianke_today_flow;
    //参加数
    private TextView txt_fragment_flow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhi_ding_huo_dong_qian_ke, container, false);
//        Utils.showToast(getActivity(), MyApplication.changjing_position.get(0).toString());
        type= MyApplication.changjing_type.get(0).toString();
        activityId= Integer.valueOf(MyApplication.changjing_position.get(0).toString()).intValue() ;
        prodialog=new ProgressDialog(getActivity());
        initData();
        initView();
        return view;
    }

    /**
     * 根据活动ID提交获取数据请求
     */
    private void initData() {
        prodialog.setMessage("加载中");
        prodialog.show();
            urlvalue = Constants.API_MY_ZHI_DING_HUO_DONG_COUNT_STUDY+"activityId="+activityId+"&type="+type;
            new NetUtil(getActivity()).sendGetToServer(urlvalue, ZhiDingHuoDongFragment.this, "单个活动统计");

    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/23 15:34
     */
    private void initView() {
        txt_fragment_qianke_today_flow= (TextView) view.findViewById(R.id.txt_fragment_qianke_today_flow);
        txt_fragment_flow= (TextView) view.findViewById(R.id.txt_fragment_flow);
        btn_more_about= (TextView) view.findViewById(R.id.btn_more_about);
        btn_more_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
            JSONObject jsonObject1=jsonObject.getJSONObject("data");

        /**************************************************************************/
        if (action.equals("单个活动统计")) {
            if (statu) {//成功
//                Utils.showToast(getActivity(), "成功" + result);
                   if(jsonObject1.isNull("scanCount")){
                       int  scanCount=jsonObject1.getInt("scanCount");
                       txt_fragment_qianke_today_flow.setText(0+"");
                   }else {
                       int  scanCount=jsonObject1.getInt("scanCount");
                       txt_fragment_qianke_today_flow.setText(scanCount+"");
                   }

                   if(jsonObject1.isNull("applyCount")){
                       int  applyCount=jsonObject1.getInt("applyCount");
                       txt_fragment_flow.setText(0+"");
                   }else {
                       int  applyCount=jsonObject1.getInt("applyCount");
                       txt_fragment_flow.setText(applyCount+"");
                   }

            } else {
                Utils.showToast(getActivity(), message);
            }
        }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
