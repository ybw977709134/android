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
import com.onemeter.entity.hotchangjingInfo;

import java.util.List;



/**
 * 描述：
 * 项目名称：xzsb_project
 * 作者：Administrator
 * 时间：2016/1/19 19:22
 * 备注：
 */
public class HomeGridViewAdapter extends BaseAdapter {
    public List<hotchangjingInfo> hcjfo;

    Context mContext;
    private BitmapUtils bitmapUtils;
    public  HomeGridViewAdapter(Context mContext,List<hotchangjingInfo> hcjfo){
        this.hcjfo=hcjfo;
        this.mContext=mContext;
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {
        if (hcjfo == null) {
            return 0;
        } else {
            return hcjfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (hcjfo == null) {
            return null;
        } else {
            return hcjfo.get(position);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_notlogin_gridview_item_layout, null);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
            bitmapUtils.display(holder.not_login_gridview_item_img_bg,hcjfo.get(position).getBanner());
//        holder.not_login_gridview_item_text.setText(hcjfo.get(position).getBanner().toString());
        return convertView;
    }

    public class  ViewHolder {
        @ViewInject(R.id.not_login_gridview_item_img_bg)
        /**图标设置背景**/
        private FrameLayout not_login_gridview_item_img_bg;
        @ViewInject(R.id.not_login_gridview_item_text)
        /**文本内容**/
        private TextView not_login_gridview_item_text;
    }
}
