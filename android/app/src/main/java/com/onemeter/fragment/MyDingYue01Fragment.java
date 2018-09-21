package com.onemeter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.onemeter.R;
import com.onemeter.adapter.MyDingYueFragment01Adapter;
import com.onemeter.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：我的订阅新页面
 * 项目名称：vishow_project
 * 作者：angelyin
 * 时间：2016/3/10 10:22
 * 备注：
 */
public class MyDingYue01Fragment extends Fragment implements ExpandableListView.OnChildClickListener {
    /**
     * 组数据
     **/
    private List<String> group = new ArrayList<String>();
    /**
     * 子item数据
     **/
    public List<List<String>> child = new ArrayList<List<String>>();
    /**
     * 二级列表组件
     **/
    private ExpandableListView my_dingyue_xlistview;
    /**
     * 适配器
     **/
    private MyDingYueFragment01Adapter myDingYueFragment01Adapter;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_ding_yue_01_main, container, false);
        initData();
        initView();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        group.add(0, "米果美语");
        group.add(1, "学点啥儿");
        group.add(2, "微营销活动");

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                List<String> list1 = new ArrayList<String>();
                list1.add("assets/processedimages/main_banner.png");
                list1.add("assets/processedimages/main_banner1.png");
                list1.add("assets/processedimages/main_banner2.png");
                list1.add("assets/processedimages/main_banner3.png");
                child.add(list1);
            }
            if (i == 1) {
                List<String> list2 = new ArrayList<String>();
                list2.add("assets/processedimages/main_banner2.png");
                list2.add("assets/processedimages/main_banner1.png");
                child.add(list2);
            }

            if (i == 2) {
                List<String> list3 = new ArrayList<String>();
                list3.add("assets/processedimages/main_banner1.png");
                list3.add("assets/processedimages/main_banner2.png");
                list3.add("assets/processedimages/main_banner4.png");
                child.add(list3);
            }

        }


    }


    /**
     * 初始化组件
     */
    private void initView() {
        myDingYueFragment01Adapter = new MyDingYueFragment01Adapter(getActivity(), group, child);
        my_dingyue_xlistview = (ExpandableListView) view.findViewById(R.id.my_dingyue_xlistview);
        my_dingyue_xlistview.setAdapter(myDingYueFragment01Adapter);

        my_dingyue_xlistview.setOnChildClickListener(this);


    }


    /**
     * 子选项点击事件监听
     *
     * @param parent
     * @param v
     * @param groupPosition
     * @param childPosition
     * @param id
     * @return
     */

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Utils.showToast(getActivity(), "组：" + groupPosition + "===子：" + childPosition, 1000);
        return true;
    }
}
