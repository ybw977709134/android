package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.onemeter.R;
import java.util.List;

/**
 * 描述：添加选中图片的适配器
 * 项目名称：android
 * 作者：angelyin
 * 时间：2016/4/28 13:16
 * 备注：
 */
public class Addqupicadpter extends BaseAdapter{
    Context  mContext;
    List<String>  mpicpath;
    private BitmapUtils bitmapUtils;
    public  Addqupicadpter(Context mContext,List<String>  mpicpath){
        this.mContext=mContext;
        this.mpicpath=mpicpath;
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {
        if(mpicpath==null){
            return 0;
        }else {
            return  mpicpath.size();
        }

    }

    @Override
    public Object getItem(int position) {
        if(mpicpath==null){
            return  null;
        }else{
            return  mpicpath.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mypic_item_gridview, null);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        bitmapUtils.display(holder.id_mypic_item_image, mpicpath.get(position).toString());
        return convertView;
    }

    public class ViewHolder {
        @ViewInject(R.id.id_mypic_item_image)
        /**图标设置背景**/
        private ImageView id_mypic_item_image;
    }
}
