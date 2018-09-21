package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.onemeter.R;
import com.onemeter.entity.QupicModelInfo;

import java.util.List;

/**
 * 描述：
 * 项目名称：android
 * 作者：Administrator
 * 时间：2016/4/27 15:33
 * 备注：
 */
public class QupicWebActivityAdapter extends BaseAdapter {

    Context  mContext;
    List<QupicModelInfo>  modelInfoList;
    private BitmapUtils bitmapUtils;

    public QupicWebActivityAdapter(Context mContext,List<QupicModelInfo>  modelInfoList){
    this.mContext=mContext;
        this.modelInfoList=modelInfoList;
        bitmapUtils = new BitmapUtils(mContext);
    }


    @Override
    public int getCount() {
        if(modelInfoList==null){
            return 0;
        }else {
           return modelInfoList.size();
        }

    }

    @Override
    public Object getItem(int position) {
        if(modelInfoList==null){
            return null;
        }else {
          return   modelInfoList.get(position);
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
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.quxiangce_item_gridview, null);
            holder.qupic_item_img= (ImageView) convertView.findViewById(R.id.qupic_item_img);
            holder.qupic_item_text= (TextView) convertView.findViewById(R.id.qupic_item_text);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        bitmapUtils.display(holder.qupic_item_img, modelInfoList.get(position).getCover());
        holder.qupic_item_text.setText(modelInfoList.get(position).getName());
        return convertView;
    }

   public  class  ViewHolder{
       @ViewInject(R.id.qupic_item_img)
     private ImageView  qupic_item_img;
       private TextView qupic_item_text;


   }

}
