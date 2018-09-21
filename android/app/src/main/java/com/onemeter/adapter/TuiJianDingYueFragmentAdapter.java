package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.onemeter.R;
import com.onemeter.entity.hotdyInfo;

import java.util.List;


/**
 * 描述：推荐订阅列表适配器
 * 项目名称：zhaosb
 * 作者：angelyin
 * 时间：2016/2/29 17:14
 * 备注：
 */
public class TuiJianDingYueFragmentAdapter extends BaseAdapter {
    public static List<hotdyInfo> mhfo;

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

    public TuiJianDingYueFragmentAdapter(Context mContext, List<hotdyInfo> mhfo) {
        this.mContext = mContext;
        this.mhfo = mhfo;
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {
        if (mhfo == null) {
            return 0;
        } else {
            return mhfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mhfo == null) {
            return null;
        } else {
            return mhfo.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_tuijian_dingyue_item_layout, null);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        bitmapUtils.display(holder.not_login_gridview_item_img_bg, mhfo.get(position).getBanner());
//        holder.not_login_gridview_item_text.setText(mhfo.get(position).getSchool().toString());
        return convertView;
    }

    public class ViewHolder {
        @ViewInject(R.id.not_login_gridview_item_img_bg)
        /**图标设置背景**/
        private FrameLayout not_login_gridview_item_img_bg;
        @ViewInject(R.id.not_login_gridview_item_text)
        /**文本内容**/
        private TextView not_login_gridview_item_text;
    }
}
