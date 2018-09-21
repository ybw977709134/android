package com.onemeter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.onemeter.R;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：周边动态页面
 * 项目名称：zhaosb_project
 * 作者：Administrator
 * 时间：2016/2/17 14:01
 * 备注：
 */
public class ZhouBianDongTaiFragment extends Fragment implements XListView.IXListViewListener {
    View view;
    private ZhouBianDongTaiFragmentAdapter zhouBianDongTaiFragmentAdapter;
    private XListView activity_zhou_bian_dong_tai_listview;
    private Handler handler;
    /**
     * 集合
     **/
    public static List<String> mList;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    LayoutInflater inflater;

    /**
     * 图片资源
     **/
    int[] text_img = new int[]{
            // 图片资源ID
            R.mipmap.main_no1, R.mipmap.main_no2, R.mipmap.main_no3,};

    String[] number = new String[]{"浏览量:1234", "浏览量:123", "浏览量:100", "浏览量:80", "浏览量:20"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhou_bian_dong_tai_main, container, false);
        initView();
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        mList = new ArrayList<String>();
        mList.add("这是一波席卷全国的拼词龙卷风，刮过48个城市，红色警报已经响起");
        mList.add("米果梅语将于2016年2月15日-3月15日举办，“猴年画候”，儿童绘画创意大赛在线人气投票活动，更为母校参与活动准备丰富奖品");
        mList.add("腊月初八是中国传统的腊八节，每逢这天，小朋友们都可以吃到香喷喷的腊八粥哦");
        mList.add("要快乐，要成长，就是这么任性，寒假班火热开启中，要给小朋友报名的爸爸妈妈们快来填写报名信息吧");
        mList.add("快来学籍单词吧，米果教育，单词王，非你莫属");
        handler = new Handler();
        zhouBianDongTaiFragmentAdapter = new ZhouBianDongTaiFragmentAdapter(getActivity());
        activity_zhou_bian_dong_tai_listview = (XListView) view.findViewById(R.id.activity_zhou_bian_dong_tai_listview);
        // 设置xlistview可以加载、刷新
        activity_zhou_bian_dong_tai_listview.setPullLoadEnable(true);
        activity_zhou_bian_dong_tai_listview.setPullRefreshEnable(true);
        activity_zhou_bian_dong_tai_listview.setXListViewListener(this);
        activity_zhou_bian_dong_tai_listview.setAdapter(zhouBianDongTaiFragmentAdapter);


    }

    @Override
    public void onRefresh() {
        // 模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity_zhou_bian_dong_tai_listview.stopRefresh();
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
                activity_zhou_bian_dong_tai_listview.stopLoadMore();
                Utils.showToast(getActivity(), "加载成功");
            }
        }, 1000);

    }

    /**
     * 周边动态适配器
     */
    public class ZhouBianDongTaiFragmentAdapter extends BaseAdapter {
        /**
         * 图片的url地址
         **/
        private String[] url = {
                "assets/processedimages/main_banner.png",
                "assets/processedimages/main_banner1.png",
                "assets/processedimages/main_banner2.png",
                "assets/processedimages/main_banner3.png",
                "assets/processedimages/main_banner4.png"
        };

        private BitmapUtils bitmapUtils;

        // 上下文对象
        private Context mContext;

        // 构造函数
        public ZhouBianDongTaiFragmentAdapter(Context mContext) {
            this.mContext = mContext;
            bitmapUtils = new BitmapUtils(mContext);
        }


        @Override
        public int getCount() {
            return url.length;
        }

        @Override
        public Object getItem(int position) {
            return url[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        // 每个convert view都会调用此方法，获得当前所需要的view样式
        @Override
        public int getItemViewType(int position) {
            int p = position;
            if (p == 0 || p == 1 || p == 2) {
                return TYPE_1;
            } else {
                return TYPE_2;
            }

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder2 holder2 = null;
            ViewHolder1 holder1 = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                inflater = LayoutInflater.from(mContext);
                // 按当前所需的样式，确定new的布局
                switch (type) {
                    case TYPE_1:
                        holder1 = new ViewHolder1();
                        convertView = inflater.inflate(R.layout.vishow_redlist_listview_item, parent, false);
                        holder1.vishow_item_recording_text = (TextView) convertView.findViewById(R.id.vishow_item_recording_text);
                        holder1.vishow_item_recording_pageviews = (TextView) convertView.findViewById(R.id.vishow_item_recording_pageviews);
                        holder1.vishow_item_recording_ranking = (ImageView) convertView.findViewById(R.id.vishow_item_recording_ranking);
                        holder1.vishow_item_bg = (RelativeLayout) convertView.findViewById(R.id.vishow_item_bg);
                        convertView.setTag(holder1);

                        break;
                    case TYPE_2:
                        holder2 = new ViewHolder2();
                        convertView = inflater.inflate(R.layout.vishow_redlist_listview_item, parent, false);
                        holder2.vishow_item_recording_text = (TextView) convertView.findViewById(R.id.vishow_item_recording_text);
                        holder2.vishow_item_recording_pageviews = (TextView) convertView.findViewById(R.id.vishow_item_recording_pageviews);
                        holder2.vishow_item_recording_ranking = (ImageView) convertView.findViewById(R.id.vishow_item_recording_ranking);
                        holder2.vishow_item_bg = (RelativeLayout) convertView.findViewById(R.id.vishow_item_bg);
                        convertView.setTag(holder2);

                }


            } else {
                switch (type) {
                    case TYPE_1:
                        holder1 = (ViewHolder1) convertView.getTag();
                        break;
                    case TYPE_2:
                        holder2 = (ViewHolder2) convertView.getTag();
                        break;
                }
            }

            // 设置资源
            switch (type) {
                case TYPE_1:
                    holder1.vishow_item_recording_ranking.setImageResource(text_img[position]);
                    holder1.vishow_item_recording_pageviews.setText(number[position].toString());
                    // 设置数据
                    bitmapUtils.display(holder1.vishow_item_bg, url[position]);

                    break;
                case TYPE_2:
                    holder2.vishow_item_recording_pageviews.setText(number[position].toString());
                    bitmapUtils.display(holder2.vishow_item_bg, url[position]);
                    break;
            }

            return convertView;
        }
    }


    public class ViewHolder1 {
        private ImageView vishow_item_recording_ranking;
        private TextView vishow_item_recording_text;
        private TextView vishow_item_recording_pageviews;
        private RelativeLayout vishow_item_bg;

    }

    public class ViewHolder2 {
        private ImageView vishow_item_recording_ranking;
        private TextView vishow_item_recording_text;
        private TextView vishow_item_recording_pageviews;
        private RelativeLayout vishow_item_bg;

    }
}
