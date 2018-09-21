package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.onemeter.R;
import com.onemeter.entity.hotschool;
import com.onemeter.view.CircleImageView;

import java.util.List;


/**
 * 描述：
 * 项目名称：zhaosb
 * 作者：Administrator
 * 时间：2016/3/2 17:59
 * 备注：
 */
public class MyDingYueFragmentAdapter extends BaseAdapter {
    public List<hotschool> hoot;
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
    private String[] text = {
            "腊八节",
            "寒假班火热开启",
            "单词王非你莫属",
            "寻玉之旅",
            "寻玉之旅"
    };
    Context mContext;
    private BitmapUtils bitmapUtils;

    public MyDingYueFragmentAdapter(Context mContext,List<hotschool> hoot) {
        this.hoot=hoot;
        this.mContext = mContext;
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {
        if (hoot == null) {
            return 0;
        } else {
            return hoot.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (hoot == null) {
            return null;
        } else {
            return hoot.get(position);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_my_dingyue_item_layout, null);
            holder.img_my_dingyue_title = (CircleImageView) convertView.findViewById(R.id.img_my_dingyue_title);
            holder.text_my_dingyue_title = (TextView) convertView.findViewById(R.id.text_my_dingyue_title);
            holder.text_my_number_jiulanliang = (TextView) convertView.findViewById(R.id.text_my_number_jiulanliang);
            holder.my_dingyue_number = (TextView) convertView.findViewById(R.id.my_dingyue_number);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        holder.text_my_dingyue_title.setText(hoot.get(position).getSchool().toString());
        holder.text_my_number_jiulanliang.setText(hoot.get(position).getSubCount()+"");
        holder.my_dingyue_number.setText(hoot.get(position).getAllScanCount()+"");
        return convertView;
    }

    public class ViewHolder {
        private CircleImageView img_my_dingyue_title;//活动图标
        private TextView text_my_dingyue_title;//活动标题
        private TextView text_my_number_jiulanliang;//活动浏览量
        private TextView my_dingyue_number;//活动订阅数
    }
}
