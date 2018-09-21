package com.onemeter.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.onemeter.R;
import com.onemeter.activity.ChooseSceneTypeActivity;
import com.onemeter.activity.HuoDongSettingActivity;
import com.onemeter.activity.HuoDongbianjiwebActivity;
import com.onemeter.activity.UserLoginActivity;
import com.onemeter.activity.ZhiDingQianKeTongJiActivity;
import com.onemeter.activity.ZhidingSharedetailActivity;
import com.onemeter.activity.changjingwebActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.VishowInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 描述：场景页面
 * 项目名称：zhaosb_project
 * 作者：angelyin
 * 时间：2016/2/16 14:31
 * 备注：
 */
public class ChangJingFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    /**
     * 图片资源
     **/
    int[] images = new int[]{
            // 图片资源ID
            R.mipmap.banner_1, R.mipmap.banner_2, R.mipmap.banner_1,};
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
    private int currPage = 0;// 当前显示的页
    private int oldPage = 0;// 上一次显示的页
    private ImageView iv_add;

    //手机场景
    private ListView mobai_fragment_listview;
    private MobaiAdapter mobaiAdapter;
    View view;
    Intent intent;
    private Dialog dialog;
    //滚动条
    private ScrollView fragment_changjing_scrollview;
    private RelativeLayout rel_huo_dong_2;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框

    public static List<VishowInfo> mvishoww;//场景列表集合
    private Context mContext;
    //单个场景的ID和类型
    String type;
    int activityId;
    int p;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_changjing, container, false);
        prodialog = new ProgressDialog(getActivity());
        mvishoww = new ArrayList<VishowInfo>();
        mContext = getActivity();
        init();
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
        int uid = UserLoginActivity.userInfos.get(0).getUid();
//        int uid = 1;
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_MY_HUO_DONG_LIST + "memberId=" + uid;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, ChangJingFragment.this, "场景列表");

    }

    @Override
    public void onResume() {
        super.onResume();
        mvishoww.clear();
        initData();
        fragment_changjing_scrollview.smoothScrollTo(0, 0);
        rel_huo_dong_2.setFocusable(true);
        rel_huo_dong_2.setFocusableInTouchMode(true);
        rel_huo_dong_2.requestFocus();

    }

    /**
     * 描述：  初始化组件
     * 作者：$angelyin
     * 时间：2016/2/16 14:39
     */
    private void initView() {
        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        //点击右边显示
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), ChooseSceneTypeActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
            }
        });

        dialog = new Dialog(getActivity(), R.style.AppTheme);
        mobai_fragment_listview = (ListView) view.findViewById(R.id.mobai_fragment_listview);
        fragment_changjing_scrollview = (ScrollView) view.findViewById(R.id.fragment_changjing_scrollview);
        rel_huo_dong_2 = (RelativeLayout) view.findViewById(R.id.rel_huo_dong_2);
        mobai_fragment_listview.setOnItemClickListener(this);

    }

    /**
     * 描述：初始化轮播图
     * 作者：$angelyin
     * 时间：2016/1/19 19:52
     */
    private void init() {
        // 将要显示的图片放到list集合中
        imageSource = new ArrayList<ImageView>();
        for (int i = 0; i < images.length; i++) {
            ImageView image = new ImageView(getActivity());
            image.setBackgroundResource(images[i]);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            imageSource.add(image);
        }

        // 获取显示的点（即文字下方的点，表示当前是第几张）
        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.dot1));
        dots.add(view.findViewById(R.id.dot2));
        dots.add(view.findViewById(R.id.dot3));

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

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        MyApplication.changjing_position.clear();
        MyApplication.changjing_type.clear();
        MyApplication.changjing_position.add(mvishoww.get(position).getId() + "");
        MyApplication.changjing_type.add(mvishoww.get(position).getType());
        //传值到web上
        Intent intent = new Intent(getActivity(), changjingwebActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

    }


    /**
     * 描述：更多操作弹窗
     * 作者：$angelyin
     * 时间：2016/1/18 17:39
     */
    private void sharePopuwindow(int position) {
        p = position;
        MyApplication.changjing_position.clear();
        MyApplication.changjing_type.clear();
        MyApplication.changjing_cover.clear();
        MyApplication.changjing_position.add(mvishoww.get(position).getId() + "");
        MyApplication.changjing_type.add(mvishoww.get(position).getType());
        MyApplication.changjing_cover.add(mvishoww.get(position).getHdTemplate());
        type = MyApplication.changjing_type.get(0).toString();
        activityId = Integer.valueOf(MyApplication.changjing_position.get(0).toString()).intValue();
        View layoutView = getActivity().getLayoutInflater().inflate(
                R.layout.share_popuwindow_layout, null);
        dialog.setContentView(layoutView, new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        //按钮点击事件
        LinearLayout mabai_fragment_listview_item_huodong_tongji = (LinearLayout) layoutView.findViewById(R.id.mabai_fragment_listview_item_huodong_tongji);
        LinearLayout mabai_fragment_listview_item_huodong_setting = (LinearLayout) layoutView.findViewById(R.id.mabai_fragment_listview_item_huodong_setting);
        LinearLayout activity_huodong_setting_main_pop_quxiao_btn = (LinearLayout) layoutView.findViewById(R.id.activity_huodong_setting_main_pop_quxiao_btn);
        LinearLayout activity_huodong_setting_main_pop_bianxie_btn = (LinearLayout) layoutView.findViewById(R.id.activity_huodong_setting_main_pop_bianxie_btn);
        LinearLayout activity_huodong_setting_main_pop_share_btn = (LinearLayout) layoutView.findViewById(R.id.activity_huodong_setting_main_pop_share_btn);
        LinearLayout activity_huodong_setting_main_pop_copy_btn = (LinearLayout) layoutView.findViewById(R.id.activity_huodong_setting_main_pop_copy_btn);
        LinearLayout activity_huodong_setting_main_pop_delete_btn = (LinearLayout) layoutView.findViewById(R.id.activity_huodong_setting_main_pop_delete_btn);

        activity_huodong_setting_main_pop_bianxie_btn.setOnClickListener(this);
        activity_huodong_setting_main_pop_share_btn.setOnClickListener(this);
        activity_huodong_setting_main_pop_copy_btn.setOnClickListener(this);
        activity_huodong_setting_main_pop_delete_btn.setOnClickListener(this);
        activity_huodong_setting_main_pop_quxiao_btn.setOnClickListener(this);
        mabai_fragment_listview_item_huodong_tongji.setOnClickListener(this);
        mabai_fragment_listview_item_huodong_setting.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_huodong_setting_main_pop_bianxie_btn:
                //编辑
                intent = new Intent(getActivity(), HuoDongbianjiwebActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                dialog.dismiss();
                changbeijing();
                break;
            case R.id.activity_huodong_setting_main_pop_share_btn:
                //分享
                intent = new Intent(getActivity(), ZhidingSharedetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                dialog.dismiss();
                changbeijing();
                break;
            case R.id.activity_huodong_setting_main_pop_copy_btn:
                //复制
//                Utils.showToast(getActivity(), "复制");
                dialog.dismiss();
                changbeijing();
                getNetAddData();

                break;
            case R.id.activity_huodong_setting_main_pop_delete_btn:
                //删除
                getNetDelete();
                mvishoww.remove(p);
                mobaiAdapter.notifyDataSetChanged();
                dialog.dismiss();
                changbeijing();
                break;
            case R.id.activity_huodong_setting_main_pop_quxiao_btn:
                //取消
                dialog.dismiss();
                changbeijing();
                break;
            case R.id.mabai_fragment_listview_item_huodong_setting:
                //场景设置
                dialog.dismiss();
                changbeijing();
                intent = new Intent(getActivity(), HuoDongSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.mabai_fragment_listview_item_huodong_tongji:
                //统计
                dialog.dismiss();
                changbeijing();
                intent = new Intent(getActivity(), ZhiDingQianKeTongJiActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
        }
    }

    /**
     * 复制指定活动，添加到服务器
     */
    private void getNetAddData() {
        String addid = activityId + "";
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("复制中，请稍后");
        prodialog.show();
        String urlvalue = Constants.API_COPY_ACTIVITY_BY_ID_HUO_DONG + "activityId=" + addid + "&type=" + type;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, ChangJingFragment.this, "首页复制场景活动");

    }

    /**
     * 删除活动列表
     */
    private void getNetDelete() {
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("删除中");
        prodialog.show();
        String urlvalue = Constants.API_DELETE_BY_ID_HUO_DONG + "id=" + activityId + "&type=" + type;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, ChangJingFragment.this, "首页删除场景活动");
    }

    /**
     * 改变弹窗背景
     */
    private void changbeijing() {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1f;
        getActivity().getWindow().setAttributes(lp);
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
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            /**************************************************************************/
            if (action.equals("场景列表")) {
                if (statu) {//成功
//                toast("获取：" + result);
                    if (jsonArray.length() == 0) {
                        Utils.showToast(getActivity(), "数据为空");
                    } else {
                        mvishoww.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            VishowInfo vsif = new VishowInfo();
                            int id = jsonArray.getJSONObject(i).getInt("id");
                            String name = jsonArray.getJSONObject(i).getString("name");
                            int scanCount = jsonArray.getJSONObject(i).getInt("scanCount");
                            String type = jsonArray.getJSONObject(i).getString("type");
                            int memberId = jsonArray.getJSONObject(i).getInt("memberId");
                            long createDate = jsonArray.getJSONObject(i).getLong("createDate");
                            if (jsonArray.getJSONObject(i).isNull("subhead")) {
                            } else {
                                String subhead = jsonArray.getJSONObject(i).getString("subhead");
                                vsif.setSubhead(subhead);
                            }
                            if (jsonArray.getJSONObject(i).isNull("applyCount")) {
                            } else {
                                int applyCount = jsonArray.getJSONObject(i).getInt("applyCount");
                                vsif.setApplyCount(applyCount);
                            }
                            if(jsonArray.getJSONObject(i).isNull("template")){

                            }else{
                                String template = jsonArray.getJSONObject(i).getJSONObject("template").getString("cover");
                                String url = "http://7xrula.com1.z0.glb.clouddn.com/" + template;
                                vsif.setHdTemplate(url);
                            }

                            vsif.setId(id);
                            vsif.setName(name);
                            vsif.setScanCount("浏览量：" + scanCount);
                            vsif.setType(type);
                            vsif.setCreateDate(createDate);
                            vsif.setMemberId(memberId);
                            vsif.setScanCountId(scanCount);
                            mvishoww.add(vsif);
                        }
                    }

                    mobaiAdapter = new MobaiAdapter(getActivity(), mvishoww);
                    mobai_fragment_listview.setAdapter(mobaiAdapter);
                    mobaiAdapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getActivity(), message);
                }

            }

            /**********************************************************************************/
            if (action.equals("首页删除场景活动")) {
                if (statu) {
                    //成功

                } else {

                    //失败
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*************************************************************************************/
        if (action.equals("首页复制场景活动")) {
            if (statu) {
                //复制活动成功
                Utils.showToast(getActivity(), "copy成功");
                initData();
            } else {
                Utils.showToast(getActivity(), message);
            }

        }


    }


    class MobaiAdapter extends BaseAdapter {

        Context context;

        public List<VishowInfo> mhoww;//场景列表集合
        /**
         * 图片的url地址
         **/
        private String[] url = {
                "assets/processedimages/cover.jpg",
                "assets/processedimages/cover_2.jpg",
                "assets/processedimages/cover_3.jpg",
                "assets/processedimages/cover_4.jpg",
                "assets/processedimages/cover_5.jpg",
                "assets/processedimages/cover_6.jpg",
                "assets/processedimages/cover_7.jpg",
                "assets/processedimages/cover.jpg",
                "assets/processedimages/cover_2.jpg",
                "assets/processedimages/cover_3.jpg",
                "assets/processedimages/cover_4.jpg",
                "assets/processedimages/cover_5.jpg",
                "assets/processedimages/cover_6.jpg",
                "assets/processedimages/cover_7.jpg"
        };

        private BitmapUtils bitmapUtils;

        public MobaiAdapter(Context context, List<VishowInfo> mhoww) {
            this.context = context;
            this.mhoww = mvishoww;
            bitmapUtils = new BitmapUtils(context);
        }

        @Override
        public int getCount() {
            if (mhoww == null) {
                return 0;
            } else {
                return mhoww.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mhoww == null) {
                return null;
            } else {
                return mhoww.get(position);
            }
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.mobai_fragment_listview_item_layout, null);
                holder.mabai_fragment_listview_item_title = (TextView) convertView.findViewById(R.id.mabai_fragment_listview_item_title);
                holder.mabai_fragment_listview_item_time = (TextView) convertView.findViewById(R.id.mabai_fragment_listview_item_time);
                holder.mabai_fragment_listview_item_huodong_baoming = (ImageButton) convertView.findViewById(R.id.mabai_fragment_listview_item_huodong_baoming);
                holder.mabai_fragment_listview_item_huodong_baoming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInfo(position);

                    }
                });
                ViewUtils.inject(holder, convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置数据
            if (this.mhoww != null) {
                VishowInfo vfo = this.mhoww.get(position);
                bitmapUtils.display(holder.mabai_fragment_listview_item_img, vfo.getHdTemplate());
                if (vfo.getSubhead() != null) {
                    holder.mabai_fragment_listview_item_title_sm.setText(vfo.getSubhead().toString());
                }
                holder.mabai_fragment_listview_item_title.setText(vfo.getName());
                String time = Utils.getFormatedDateTime("yyyy-MM-dd", vfo.getCreateDate().longValue());
                holder.mabai_fragment_listview_item_time.setText(time);
            }
            return convertView;
        }

        /**
         * itembutton点击事件
         *
         * @param position
         */
        private void showInfo(int position) {
//            Utils.showToast(getActivity(), position + "");
            sharePopuwindow(position);

        }

        public class ViewHolder {
            @ViewInject(R.id.mabai_fragment_listview_item_img)
            private ImageView mabai_fragment_listview_item_img;//活动图标
            private TextView mabai_fragment_listview_item_title;//活动标题
            @ViewInject(R.id.mabai_fragment_listview_item_title_sm)
            private TextView mabai_fragment_listview_item_title_sm;//活动副标题
            private TextView mabai_fragment_listview_item_time;//活动发布时间
            private ImageButton mabai_fragment_listview_item_huodong_baoming;//更多操作

        }
    }
}
