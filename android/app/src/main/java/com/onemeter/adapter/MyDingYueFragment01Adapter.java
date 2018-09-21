package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.onemeter.R;

import java.util.List;


/**
 * 描述：适配器（我的订阅）
 * 项目名称：vishow_project
 * 作者：Administrator
 * 时间：2016/3/10 10:56
 * 备注：
 */
public class MyDingYueFragment01Adapter extends BaseExpandableListAdapter {
    private Context context;
    /**
     * 组item信息
     **/
    private List<String> group;
    /**
     * 子item信息
     **/
    private List<List<String>> child;
    private BitmapUtils bitmapUtils;

    public MyDingYueFragment01Adapter(Context context, List<String> group, List<List<String>> child) {
        this.context = context;
        this.group = group;
        this.child = child;
        bitmapUtils = new BitmapUtils(context);
    }

    public void setData(List<List<String>> list) {
        child = list;
    }

    @Override
    public int getGroupCount() {
        return child.size();
    }


    @Override
    public List<String> getGroup(int groupPosition) {
        return child.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    /**
     * 获取当前父item下的子item的个数
     **/
    @Override
    public int getChildrenCount(int groupPosition) {

        return child.get(groupPosition).size();
    }

    /**
     * 得到子item的关联数据
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public String getChild(int groupPosition, int childPosition) {

        return child.get(groupPosition).get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    /**
     * 显示：group
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder1 viewHolder1;
        if (convertView == null) {
            viewHolder1 = new ViewHolder1();
            convertView = LayoutInflater.from(context).inflate(R.layout.my_dingyue_group_list_item, null);
            viewHolder1.img_my_dingyue_title = (ImageView) convertView.findViewById(R.id.img_my_dingyue_title);
            viewHolder1.text_my_dingyue_title = (TextView) convertView.findViewById(R.id.text_my_dingyue_title);
            viewHolder1.text_my_number_jiulanliang = (TextView) convertView.findViewById(R.id.text_my_number_jiulanliang);
            viewHolder1.my_dingyue_number = (TextView) convertView.findViewById(R.id.my_dingyue_number);
            viewHolder1.my_dingyue_status = (TextView) convertView.findViewById(R.id.my_dingyue_status);

            convertView.setTag(viewHolder1);
        } else {
            viewHolder1 = (ViewHolder1) convertView.getTag();
        }

        //设置数据
        viewHolder1.text_my_dingyue_title.setText(group.get(groupPosition));

        return convertView;
    }


    /**
     * 显示子itrem
     **/
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder2 viewHolder2;
        if (convertView == null) {
            viewHolder2 = new ViewHolder2();
            convertView = LayoutInflater.from(context).inflate(R.layout.my_dingyue_child_list_item, null);
            viewHolder2.my_dingyue_child_item_text = (TextView) convertView.findViewById(R.id.my_dingyue_child_item_text);
            viewHolder2.my_dingyue_child_item_img_bg = (FrameLayout) convertView.findViewById(R.id.my_dingyue_child_item_img_bg);
            ViewUtils.inject(viewHolder2, convertView);
            convertView.setTag(viewHolder2);
        } else {
            viewHolder2 = (ViewHolder2) convertView.getTag();
        }
        //设置子item的数据
//        viewHolder2.my_dingyue_child_item_text.setText(getChild(groupPosition, childPosition).toString());
        bitmapUtils.display(viewHolder2.my_dingyue_child_item_img_bg, getChild(groupPosition, childPosition));
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {//行是否具有唯一id
        return false;
    }

    /**
     * 组 对象
     */
    class ViewHolder1 {
        /**
         * 图片
         **/
        private ImageView img_my_dingyue_title;
        /**
         * 标题
         **/
        private TextView text_my_dingyue_title;
        /**
         * 浏览量
         **/
        private TextView text_my_number_jiulanliang;
        /**
         * 订阅数
         **/
        private TextView my_dingyue_number;
        /**
         * 订阅状态
         **/
        private TextView my_dingyue_status;


    }

    /**
     * 子 对象
     */
    class ViewHolder2 {
        private TextView my_dingyue_child_title;
        private TextView my_dingyue_child_item_text;
        private FrameLayout my_dingyue_child_item_img_bg;


    }

}
