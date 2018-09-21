package com.onemeter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onemeter.R;


/**
 * 描述：引导页面一
 * 项目名称：vishow_project
 * 作者：angelyin
 * 时间：2016/3/11 13:42
 * 备注：
 */
public class OneFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guide_one_main, container, false);
        initView();
        return view;
    }

    /**
     * 初始化组件
     */
    private void initView() {

    }


}
