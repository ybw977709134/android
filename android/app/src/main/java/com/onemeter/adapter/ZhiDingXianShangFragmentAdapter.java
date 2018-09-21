package com.onemeter.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.entity.VishowInfo;
import com.onemeter.utils.Utils;

import java.util.List;



/**
 * 描述：总活动统计适配器
 * 作者：angelyin
 * 时间：2016/1/23 16:53
 * 备注：
 */
public class ZhiDingXianShangFragmentAdapter extends BaseAdapter {
    public static List<VishowInfo> vfo;
    Context mContext;
    public ZhiDingXianShangFragmentAdapter(Context mContext,List<VishowInfo> vfo){
        this.mContext=mContext;
        this.vfo=vfo;
    }

    @Override
    public int getCount() {
        if (vfo == null) {
            return 0;
        } else {
            return vfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (vfo == null) {
            return null;
        } else {
            return vfo.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.zhi_ding_huo_dong_fragment_listview_item_layout, null);
        holder.zhiding_fragment_qianke_name= (TextView) convertView.findViewById(R.id.zhiding_fragment_qianke_name);
        holder.zhiding_fragment_qianke_call= (TextView) convertView.findViewById(R.id.zhiding_fragment_qianke_call);
        holder.zhiding_fragment_qianke_date= (TextView) convertView.findViewById(R.id.zhiding_fragment_qianke_date);
        holder.zhiding_fragment_qianke_time= (TextView) convertView.findViewById(R.id.zhiding_fragment_qianke_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        holder.zhiding_fragment_qianke_name.setText(vfo.get(position).getName().toString());
        holder.zhiding_fragment_qianke_call.setText(vfo.get(position).getScanCountId()+"");
        holder.zhiding_fragment_qianke_date.setText(vfo.get(position).getApplyCount()+"");
        String time= Utils.getFormatedDateTime("yyyy-MM-dd", vfo.get(position).getCreateDate());
        holder.zhiding_fragment_qianke_time.setText(time);
        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
      private TextView zhiding_fragment_qianke_name;
      private TextView zhiding_fragment_qianke_call;
      private TextView zhiding_fragment_qianke_date;
      private TextView zhiding_fragment_qianke_time;

    }
}
