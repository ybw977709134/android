package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.onemeter.R;
import com.onemeter.adapter.VishowRedlistAdapter;
import com.onemeter.app.BaseActivity;
import com.onemeter.entity.VishowInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 描述：微秀热榜页面
 * 项目名称：zhaosb_project
 * 作者：Administrator
 * 时间：2016/2/16 18:18
 * 备注：
 */
public class ViShowRedlistActivity extends BaseActivity implements XListView.IXListViewListener {
    private XListView activity_vishow_redlist_listview;
    private ImageView activity_vishow_redlist_img_return;
    private Handler handler;
    private VishowRedlistAdapter mVishowRedlistAdapter;


    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框

    public static List<VishowInfo> mvishow;//微秀榜单集合
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vishow_redlist_main);
        prodialog = new ProgressDialog(this);
        mvishow = new ArrayList<VishowInfo>();
        mContext = this;
        handler = new Handler();
        intData();
        initView();

    }

    /**
     * 初始化数据
     */
    private void intData() {
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_HOT_LIST;
        new NetUtil(this).sendGetToServer(urlvalue, ViShowRedlistActivity.this, "微秀热榜");

    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/2/17 10:15
     */
    private void initView() {
        activity_vishow_redlist_img_return = (ImageView) findViewById(R.id.activity_vishow_redlist_img_return);
        activity_vishow_redlist_listview = (XListView) findViewById(R.id.activity_vishow_redlist_listview);

        // 设置xlistview可以加载、刷新
        activity_vishow_redlist_listview.setPullLoadEnable(true);
        activity_vishow_redlist_listview.setPullRefreshEnable(true);
        activity_vishow_redlist_listview.setXListViewListener(this);
        activity_vishow_redlist_img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);            }
        });


        activity_vishow_redlist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                toast(mvishow.get(position-1).getScanCountId() + "");

            }
        });

    }

    /**
     * 下拉刷新的方法
     */
    @Override
    public void onRefresh() {
        // 模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity_vishow_redlist_listview.stopRefresh();
                toast("刷新成功");

            }
        }, 1000);
    }

    /**
     * 加载更多的方法
     */
    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            // 模拟加载数据，1s之后停止加载
            @Override
            public void run() {
                activity_vishow_redlist_listview.stopLoadMore();
                toast("加载成功");
            }
        }, 1000);

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
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            /**************************************************************************/
            if (action.equals("微秀热榜")) {
                if (statu) {//成功
//                toast("获取：" + result);
                    if (jsonArray.length() == 0) {
                        toast("数据为空");
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            VishowInfo vsif = new VishowInfo();
                            int id = jsonArray.getJSONObject(i).getInt("id");
                            String name = jsonArray.getJSONObject(i).getString("name");
                            int scanCount = jsonArray.getJSONObject(i).getInt("scanCount");
                            String type = jsonArray.getJSONObject(i).getString("type");
                            int memberId = jsonArray.getJSONObject(i).getInt("memberId");
                            if (jsonArray.getJSONObject(i).isNull("template")) {
                                vsif.setHdTemplate("assets/processedimages/cover.jpg");
                            } else {
                                String template = jsonArray.getJSONObject(i).getJSONObject("template").getString("cover");
                                String url = "http://7xrula.com1.z0.glb.clouddn.com/" + template;
                                vsif.setHdTemplate(url);
                            }

                            vsif.setId(id);
                            vsif.setName(name);
                            vsif.setScanCount("浏览量：" + scanCount);
                            vsif.setType(type);
                            vsif.setMemberId(memberId);
                            vsif.setScanCountId(scanCount);

                            mvishow.add(vsif);
                        }
                    }
                    //降序排列集合
                    sortInfo();

                } else {
                    toast(message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    private void sortInfo() {
        Collections.sort(mvishow, new Comparator<VishowInfo>() {

            @Override
            public int compare(VishowInfo lhs, VishowInfo rhs) {
                int lhs1 = lhs.getScanCountId();
                int rhs2 = rhs.getScanCountId();
                //降序
                if (lhs1 < rhs2) {
                    return 1;
                }
                return -1;
            }
        });
        activity_vishow_redlist_listview.setAdapter(new VishowRedlistAdapter(mContext, mvishow));
    }

}
