package com.onemeter.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.activity.DingYueActivity;
import com.onemeter.activity.DynamicPeripheryActivity;
import com.onemeter.activity.HomeTJDingYuedetail;
import com.onemeter.activity.ViShowRedlistActivity;
import com.onemeter.adapter.HomeGridViewAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.hotchangjingInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 描述：主菜单页面
 * 作者：angelyin
 * 时间：2016/1/14 16:25
 * 备注：
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    View view;
    private ImageView iv_add;

    //轮播图相关
    /**
     * 图片资源
     **/
    int[] images = new int[]{
            // 图片资源ID
            R.mipmap.banner_2, R.mipmap.banner_1, R.mipmap.banner_2, R.mipmap.banner_1, R.mipmap.banner_2};
    /**
     * 标题资源
     **/
    String[] titles = new String[]{
            "教育自媒体行业第一平台", "引领教育自媒体营销四大变革", "酷炫H5引爆教育020"};
    /**
     * 图片资源
     **/
    private ArrayList<ImageView> imageSource = null;
    /**
     * 点
     **/
    private ArrayList<View> dots = null;
    /**
     * 显示图片的组件
     **/
    private ViewPager viewPager;
    /**
     * 图片适配器
     **/
    private MyPagerAdapter adapter;
    private ListViewForScrollView activity_home_model_gridview;
    private HomeGridViewAdapter homeGridViewAdapter;
    private int currPage = 0;// 当前显示的页
    private int oldPage = 0;// 上一次显示的页

    //点击按钮
    /**
     * 订阅show
     **/
    private TextView btn_dingyue_show;
    private ImageView img_dingyue_show;
    /**
     * 微秀热榜
     **/
    private TextView btn_vishow_list;
    private ImageView img_vishow_list;
    /**
     * 周边动态
     **/
    private TextView btn_dynmic_periphery;
    private ImageView img_dynmic_periphery;
    //滚动条
    private ScrollView fragment_home_scrollview;
    Intent intent;
    private RelativeLayout fragment_home_title_rel;

    //定位地址
    private TextView home_dingwei;
    //进度条信息
    ProgressDialog prodialog;

    public List<hotchangjingInfo> hcjfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        prodialog = new ProgressDialog(getActivity());
        hcjfo = new ArrayList<hotchangjingInfo>();
        initData();
        initView();
        return view;
    }

    /**
     * 初始化数据信息
     */
    private void initData() {
        hcjfo.clear();
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_DING_YUE_HOT_SCHOOL;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, HomeFragment.this, "订阅推荐热门学校");

    }

    @Override
    public void onResume() {
        super.onResume();
        fragment_home_scrollview.smoothScrollTo(0, 0);
        fragment_home_title_rel.setFocusable(true);
        fragment_home_title_rel.setFocusableInTouchMode(true);
        fragment_home_title_rel.requestFocus();
    }

    /**
     * 描述： 初始化组件
     * 作者：$angelyin
     * 时间：2016/1/15 18:12
     */
    private void initView() {
        init();
        img_dingyue_show = (ImageView) view.findViewById(R.id.img_dingyue_show);
        img_vishow_list = (ImageView) view.findViewById(R.id.img_vishow_list);
        img_dynmic_periphery = (ImageView) view.findViewById(R.id.img_dynmic_periphery);
        home_dingwei = (TextView) view.findViewById(R.id.home_dingwei);
        home_dingwei.setText(MyApplication.dingweiLocation);
        home_dingwei.setFocusable(true);
        btn_dingyue_show = (TextView) view.findViewById(R.id.btn_dingyue_show);
        btn_vishow_list = (TextView) view.findViewById(R.id.btn_vishow_list);
        btn_dynmic_periphery = (TextView) view.findViewById(R.id.btn_dynmic_periphery);
        fragment_home_scrollview = (ScrollView) view.findViewById(R.id.fragment_home_scrollview);
        fragment_home_title_rel = (RelativeLayout) view.findViewById(R.id.fragment_home_title_rel);

        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        activity_home_model_gridview = (ListViewForScrollView) view.findViewById(R.id.activity_home_model_gridview);

        activity_home_model_gridview.setOnItemClickListener(this);

        img_vishow_list.setOnClickListener(this);
        img_dingyue_show.setOnClickListener(this);
        img_dynmic_periphery.setOnClickListener(this);

        btn_dingyue_show.setOnClickListener(this);
        btn_vishow_list.setOnClickListener(this);
        btn_dynmic_periphery.setOnClickListener(this);
        iv_add.setOnClickListener(this);

    }

    /**
     * 描述：
     * 作者：$angelyin
     * 时间：2016/1/19 15:58
     */
    private void init() {
        // 将要显示的图片放到list集合中
        imageSource = new ArrayList<ImageView>();
        for (int i = 0; i < images.length; i++) {
            ImageView image = new ImageView(getActivity());
            image.setBackgroundResource(images[i]);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
//            image.setImageResource(images[i]);
            imageSource.add(image);
        }

        // 获取显示的点（即文字下方的点，表示当前是第几张）
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.dot1));
        dots.add(view.findViewById(R.id.dot2));
        dots.add(view.findViewById(R.id.dot3));
        dots.add(view.findViewById(R.id.dot4));
        dots.add(view.findViewById(R.id.dot5));

        // 显示图片的VIew
        viewPager = (ViewPager) view.findViewById(R.id.vp);
        // 为viewPager设置适配器
        adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        // 为viewPager添加监听器，该监听器用于当图片变换时，标题和点也跟着变化
        MyPageChangeListener listener = new MyPageChangeListener();
        viewPager.setOnPageChangeListener(listener);
        // 开启定时器，每隔3秒自动播放下一张（通过调用线程实现）
        ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();
        // 设置一个线程，该线程用于通知UI线程变换图片
        ViewPagerTask pagerTask = new ViewPagerTask();
        scheduled.scheduleAtFixedRate(pagerTask, 3, 3, TimeUnit.SECONDS);
    }


    // ViewPager每次仅最多加载三张图片（有利于防止发送内存溢出）
    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // 判断将要显示的图片是否和现在显示的图片是同一个
            // arg0为当前显示的图片，arg1是将要显示的图片
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 销毁该图片
            container.removeView(imageSource.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 初始化将要显示的图片，将该图片加入到container中，即viewPager中
            container.addView(imageSource.get(position));
            return imageSource.get(position);
        }
    }

    // 监听ViewPager的变化
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // 当显示的图片发生变化之后
            // 改变点的状态
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            dots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
            // 记录的页面
            oldPage = position;
            currPage = position;
        }
    }

    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            // 改变当前页面的值
            currPage = (currPage + 1) % images.length;
            // 发送消息给UI线程
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 接收到消息后，更新页面
            viewPager.setCurrentItem(currPage);
        }


    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyApplication.htuijian_changjing_id.clear();
        int  meberId=hcjfo.get(position).getId();
        MyApplication.htuijian_changjing_id.add(meberId+"");
        intent  =new Intent(getActivity(),HomeTJDingYuedetail.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_dingyue_show:
            case R.id.btn_dingyue_show:
                //订阅show
                intent = new Intent(getActivity(), DingYueActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.img_vishow_list:
            case R.id.btn_vishow_list:
                //微秀热榜
//                Utils.showToast(getActivity(), "微秀热榜");
                intent = new Intent(getActivity(), ViShowRedlistActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.img_dynmic_periphery:
            case R.id.btn_dynmic_periphery:
                //周边动态
                Utils.showToast(getActivity(), "周边动态");
                intent = new Intent(getActivity(), DynamicPeripheryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.iv_add:
//                intent = new Intent(getActivity(), ChooseSceneTypeActivity.class);
//                startActivity(intent);
                break;
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
            Utils.showToast(getActivity(), getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");

            /**************************************************************************/
            if (action.equals("订阅推荐热门学校")) {
                if (statu) {//成功
//                    Utils.showToast(getActivity(), "提交成功" + result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() == 0) {
                        Utils.showToast(getActivity(), "没有数据");
                        return;
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        hotchangjingInfo ff = new hotchangjingInfo();

                        if (jsonArray.getJSONObject(i).isNull("id")) {
                        } else {
                            int id = jsonArray.getJSONObject(i).getInt("id");
                            ff.setId(id);
                        }
                        if (jsonArray.getJSONObject(i).isNull("banner")) {
                            ff.setBanner("为空");
                        } else {
                            String banner = jsonArray.getJSONObject(i).getString("banner");
                            String url="http://7xrula.com1.z0.glb.clouddn.com/"+banner;
                            ff.setBanner(url);
                        }
                        hcjfo.add(ff);

                    }
                    homeGridViewAdapter = new HomeGridViewAdapter(getActivity(), hcjfo);
                    activity_home_model_gridview.setAdapter(homeGridViewAdapter);

                } else {
                    Utils.showToast(getActivity(), message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
