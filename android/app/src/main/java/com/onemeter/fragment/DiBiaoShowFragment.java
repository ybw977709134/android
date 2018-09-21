package com.onemeter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onemeter.R;


/**
 * 描述：周边地图显示
 * 作者：angelyin
 * 时间：2016/2/17 14:04
 * 备注：
 */
public class DiBiaoShowFragment  extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_di_biao_show_main, container, false);
        initView();
        return view;
    }

    private void initView() {
    }
}
