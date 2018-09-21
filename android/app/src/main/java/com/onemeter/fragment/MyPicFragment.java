package com.onemeter.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.onemeter.R;
import com.onemeter.activity.UserLoginActivity;
import com.onemeter.adapter.MyPicAdapter;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.picturesInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：我的图片库
 * 项目名称：android
 * 作者：angelyin
 * 时间：2016/4/11 17:28
 * 备注：
 */
public class MyPicFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    View view;
    private GridView id_mypic_gridView;
    private MyPicAdapter myPicAdapter;
    ProgressDialog prodialog;// 加载进度条对话框
    int uid;
    private int p;
    public List<picturesInfo> pictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypic_main, container, false);
        uid = UserLoginActivity.userInfos.get(0).getUid();
        prodialog = new ProgressDialog(getActivity());
        pictures = new ArrayList<picturesInfo>();
        initData();
        initView();
        return view;
    }


    /**
     * 初始化提交数据
     */
    private void initData() {
        pictures.clear();
        int isPublic = 0;
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_GET_USER_PIC_LIST + "isPublic=" + isPublic + "&memberId=" + uid;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, MyPicFragment.this, "我的图片库列表");

    }

    /**
     * 初始化组件
     */
    private void initView() {
        id_mypic_gridView = (GridView) view.findViewById(R.id.id_mypic_gridView);
        id_mypic_gridView.setOnItemClickListener(this);
        id_mypic_gridView.setOnItemLongClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Utils.showToast(getActivity(), pictures.get(position).getName());
    }

    /**
     * 请求结束后此方法更新数据
     *
     * @param result
     * @param isSuccess
     * @param api
     * @param action
     */
    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        JSONObject jsonObject = null;
        if (prodialog != null && prodialog.isShowing()) {
            prodialog.dismiss();
            Log.i("Onemeter", "关闭prodialog");
        }
        if (!isSuccess) {// 请求不成功
            Utils.showToast(getActivity(), getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            /**************************************************************************/
            if (action.equals("我的图片库列表")) {
                if (statu) {//成功
                    JSONArray jsonArray = jsonObject1.getJSONArray("content");
                    if (jsonArray.length() == 0) {
                        Utils.showToast(getActivity(), "没有图片");
                        return;
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        picturesInfo pfo = new picturesInfo();
                        int id = jsonArray.getJSONObject(i).getInt("id");
                        String name = jsonArray.getJSONObject(i).getString("name");
                        String url = jsonArray.getJSONObject(i).getString("url");
                        String urll = "http://7xrula.com1.z0.glb.clouddn.com/" + url;
                        boolean flag = jsonArray.getJSONObject(i).getBoolean("isPublic");
                        pfo.setPid(id);
                        pfo.setIsPublic(flag);
                        pfo.setName(name);
                        pfo.setUrl(urll);
                        pictures.add(pfo);
                    }
                    myPicAdapter = new MyPicAdapter(getActivity(), pictures);
                    id_mypic_gridView.setAdapter(myPicAdapter);

                } else {
                    Utils.showToast(getActivity(), message + "提交失败");

                }

            }
            /*******************************************************************************/
            if (action.equals("我的图片库删除")) {
                if (statu) {//成功
                    Utils.showToast(getActivity(), "删除成功");

                } else {//失败
                    Utils.showToast(getActivity(), "删除失败");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        p = position;
        ItemDeleteDialog(position);
        return false;
    }

    /**
     * @param position
     */
    private void ItemDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = pictures.get(position).getPid();
                getNetDeleteItem(id);
                pictures.remove(p);
                myPicAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 提交请求删除选中图片
     */
    private void getNetDeleteItem(int id) {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_DELETE_USER_PIC + "id=" + id;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, MyPicFragment.this, "我的图片库删除");


    }
}
