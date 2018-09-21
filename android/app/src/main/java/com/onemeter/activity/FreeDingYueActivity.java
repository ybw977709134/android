package com.onemeter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.adapter.FreeDingYueActivityAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.hotschool;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：免费订阅页面
 * 项目名称：zhaosb_project
 * 作者：angelyin
 * 时间：2016/2/18 14:31
 * 备注：
 */
public class FreeDingYueActivity extends BaseActivity implements View.OnClickListener, ExpandableListView.OnChildClickListener {
   private ImageView activity_choose_mylove_detail_img_return;
    private ExpandableListView free_dingyue_xlistview;

    /**
     * 子item数据
     **/
    public List<List<String>> child = new ArrayList<List<String>>();

    /**
     * 适配器
     **/

    private FreeDingYueActivityAdapter mFreeDingYueActivityAdapter;
    private TextView title_text;

    ProgressDialog prodialog;// 加载进度条对话框
   public List<hotschool> mhtcl;//请求数据集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_free_ding_yue_main);
        prodialog=new ProgressDialog(this);
        mhtcl=new ArrayList<hotschool>();
        getNetDingyue();
        initView();

    }

    /**
     * 提交请求，获取免费订阅信息
     */
    private void getNetDingyue() {
        int ID= Integer.valueOf(MyApplication.free_position.get(0).toString()).intValue();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_ZHI_DING_HOT_SCHOOL_DETAIL+"memberId="+ID;
        new NetUtil(this).sendGetToServer(urlvalue, FreeDingYueActivity.this, "免费订阅详情");

    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < mhtcl.size(); i++) {
            if (i == 0) {
                List<String> list1 = new ArrayList<String>();
                list1.add("assets/processedimages/main_banner.png");
                list1.add("assets/processedimages/main_banner1.png");
                list1.add("assets/processedimages/main_banner2.png");
                list1.add("assets/processedimages/main_banner3.png");
                child.add(list1);
            }
            if (i == 1) {
                List<String> list2 = new ArrayList<String>();
                list2.add("assets/processedimages/main_banner2.png");
                list2.add("assets/processedimages/main_banner1.png");
                child.add(list2);
            }

        }

    }

    /**
     * 初始化组件
     */
    private void initView() {

        title_text= (TextView) findViewById(R.id.title_text);
        activity_choose_mylove_detail_img_return= (ImageView) findViewById(R.id.activity_choose_mylove_detail_img_return);
        free_dingyue_xlistview= (ExpandableListView) findViewById(R.id.free_dingyue_xlistview);


        activity_choose_mylove_detail_img_return.setOnClickListener(this);
        free_dingyue_xlistview.setOnChildClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_choose_mylove_detail_img_return:
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;

        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Utils.showToast(this, "组：" + groupPosition + "===子：" + childPosition, 1000);
        return true;
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
        if (action.equals("免费订阅详情")) {
            if (statu) {//成功
//                toast("请求成功" + result);
                mhtcl.clear();
                hotschool  htl=new hotschool();
              int id=jsonObject1.getInt("id");
                String schoolName=jsonObject1.getString("schoolName");
                String headImage=jsonObject1.getString("headImage");
                int subCount=jsonObject1.getInt("subCount");
                int allScanCount=jsonObject1.getInt("allScanCount");
                htl.setSchoolId(id);
                htl.setSchool(schoolName);
                htl.setSubCount(subCount);
                htl.setAllScanCount(allScanCount);
                mhtcl.add(htl);
                initData();
                mFreeDingYueActivityAdapter = new FreeDingYueActivityAdapter(this, mhtcl, child);
                free_dingyue_xlistview.setAdapter(mFreeDingYueActivityAdapter);

            } else {
                toast(message + "请求失败");
            }

        }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
