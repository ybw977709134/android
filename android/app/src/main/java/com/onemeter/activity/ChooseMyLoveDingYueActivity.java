package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.hotschool;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.GrapeGridview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * 描述：订阅你感兴趣的发布页面
 * 项目名称：zhaosb_project
 * 作者：angelyin
 * 时间：2016/2/18 12:07
 * 备注：
 */
public class ChooseMyLoveDingYueActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 返回键
     **/
    private ImageView activity_choose_mylove_img_return;
    /**
     * 加号
     **/
    private ImageView mylove_add;
    /**
     * 网格列表组件
     **/
    private GrapeGridview first_mylove_grapegridview;
    /**
     * 进入主菜单按钮
     **/
    private Button btn_menu_main;
    Intent intent;
    private ChooseMyLoveDingYueAdapter chooseMyLoveDingYueAdapter;
    /**
     * 记录选中的条目数量
     **/
    private int checkNum;
    /**
     * 标题栏
     **/
    private TextView title_text;
    /**
     * 选中的item
     **/
    private List<String> ischeckbox;
    private List<String> allschoolId;

    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框
    /**
     * 填充数据的集合
     **/
    List<hotschool> mschoollist;

    private List<String> isnotChecked;
    private int userid = 2;
    String ids;
    String urlValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_mylove_dingyue_main);
        String  islogin=MyApplication.islogin.get(0);
        toast(islogin);
        prodialog = new ProgressDialog(this);
        mschoollist = new ArrayList<hotschool>();
        getNetAddDingYuedata();

    }

    /**
     * 发送请求获取订阅机构信息
     */
    private void getNetAddDingYuedata() {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlValue = Constants.API_DING_YUE_HOT_SCHOOL;
        new NetUtil(this).sendGetToServer(urlValue, ChooseMyLoveDingYueActivity.this, "获取所有订阅机构");

    }

    /**
     * 初始化组件
     */
    private void initView() {
        ischeckbox = new ArrayList<String>();
        allschoolId = new ArrayList<String>();
        isnotChecked = new ArrayList<String>();
        for (int e = 0; e < mschoollist.size(); e++) {
            int iip = mschoollist.get(e).getSchoolId();
            allschoolId.add(iip + "");
        }

        activity_choose_mylove_img_return = (ImageView) findViewById(R.id.activity_choose_mylove_img_return);
        mylove_add = (ImageView) findViewById(R.id.mylove_add);
        first_mylove_grapegridview = (GrapeGridview) findViewById(R.id.first_mylove_grapegridview);
        btn_menu_main = (Button) findViewById(R.id.btn_menu_main);
        title_text = (TextView) findViewById(R.id.title_text);
        chooseMyLoveDingYueAdapter = new ChooseMyLoveDingYueAdapter(mschoollist,this);
        first_mylove_grapegridview.setAdapter(chooseMyLoveDingYueAdapter);
        activity_choose_mylove_img_return.setOnClickListener(this);
        mylove_add.setOnClickListener(this);
        btn_menu_main.setOnClickListener(this);
        first_mylove_grapegridview.setOnItemClickListener(this);
        for (int i = 0; i < chooseMyLoveDingYueAdapter.getIsSelected().size(); i++) {
            isnotChecked.add(i + "");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu_main:
//                finish();
                //进入主菜单前提交 用户偏好请求
                getAddDingYueinfo();
                break;
            case R.id.activity_choose_mylove_img_return:
                //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.mylove_add:
                //加号

                break;
        }
    }

    /**
     * 提交添加订阅的信息
     */
    private void getAddDingYueinfo() {

        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        if (ids == null) {
            String s1 = "ids=";
            StringBuffer sb1 = new StringBuffer(s1);
            for (int i = 0; i < allschoolId.size(); i++) {
                String mun = allschoolId.get(i);
                sb1.append(mun);
                if (i != allschoolId.size() - 1) {
                    sb1.append(",");
                }
            }
            ids = sb1.toString();
            urlValue = "mobile/subscribe/save?subscriber.id=" + userid + "&" + ids;
        } else {
            urlValue = "mobile/subscribe/save?subscriber.id=" + userid + "&" + ids;
        }

        new NetUtil(this).sendGetToServer(urlValue, ChooseMyLoveDingYueActivity.this, "提交选中机构");

    }


    /**
     * 点击网格列表item事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChooseMyLoveDingYueAdapter.ViewHolder holder = (ChooseMyLoveDingYueAdapter.ViewHolder) view.getTag();
        //改变CheckBox的状态
        holder.btn_choose_mylove_checkbox.toggle();
        // 将CheckBox的选中状况记录下来
//           toast(position+"");
        chooseMyLoveDingYueAdapter.getIsSelected().put(position, holder.btn_choose_mylove_checkbox.isChecked());
        //调整选定条目
        if (holder.btn_choose_mylove_checkbox.isChecked() == true) {
            holder.btn_choose_checkbox_rel_01.setVisibility(View.GONE);
            holder.btn_choose_checkbox_rel.setVisibility(View.GONE);
            holder.btn_choose_mylove_checkbox.setVisibility(View.GONE);
//            toast(position + "");
            if (ischeckbox.contains(position+ "")) {
                for (int i = 0; i < ischeckbox.size(); i++) {
                    if (ischeckbox.get(i).toString().equals(position + "")) {
                        ischeckbox.remove(i);
                    }
                }
            }
            checkNum--;
            isnotChecked.add(position + "");

        } else {
            holder.btn_choose_checkbox_rel.setVisibility(View.VISIBLE);
            holder.btn_choose_mylove_checkbox.setVisibility(View.VISIBLE);
            holder.btn_choose_checkbox_rel_01.setVisibility(View.VISIBLE);
//            toast(position + "");
            ischeckbox.add(position + "");
            if (isnotChecked.contains(position + "")) {
                for (int i = 0; i < isnotChecked.size(); i++) {
                    if (isnotChecked.get(i).toString().equals(position + "")) {
                        isnotChecked.remove(i);
                    }
                }
            }

            checkNum++;
        }

        String s = "ids=";
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < isnotChecked.size(); i++) {
            String p = isnotChecked.get(i).toString();
            int pp = Integer.valueOf(p);
            String mun = allschoolId.get(pp);
            sb.append(mun);
            if (i != isnotChecked.size() - 1) {
                sb.append(",");
            }
        }
        ids = sb.toString();
        title_text.setText("已选中" + checkNum + "项:" + sb.toString());

    }

    /**
     * 提交请求后此方法更新数据
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
            Utils.showToast(this, getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");

            /**************************************************************************/
            if (action.equals("获取所有订阅机构")) {
                if (statu) {//成功
//                    toast("获取成功：" + result);
                    //解析
                    JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonObject1.length(); i++) {
                        int id = jsonObject1.getJSONObject(i).getInt("id");
                        String school = jsonObject1.getJSONObject(i).getString("school");
                        String headImage = jsonObject1.getJSONObject(i).getString("headImage");
                        hotschool hsl = new hotschool();
                        hsl.setSchoolId(id);
                        hsl.setSchool(school);
                        hsl.setHeadImage(headImage);
                        mschoollist.add(hsl);

                    }

                    initView();

                } else {//失败
                    toast(message);
                }
            }

            /*****************************************************************************/
            if (action.equals("提交选中机构")) {
                if (statu) {
//                    toast("提交机构成功");
                    ids = "";
                    this.finish();
                    intent = new Intent(ChooseMyLoveDingYueActivity.this, MainActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

                } else {
                    toast(message);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



  static class  ChooseMyLoveDingYueAdapter extends BaseAdapter {

    // 填充数据的list
    private List<hotschool> list;
    // 用来控制CheckBox的选中状况
    public static HashMap<Integer, Boolean> isSelected;

    /**
     * 图片的url地址
     **/
    private String[] url = {
            "assets/processedimages/logo1.png",
            "assets/processedimages/logo2.png",
            "assets/processedimages/logo3.png",
            "assets/processedimages/logo1.png",
            "assets/processedimages/logo2.png",
            "assets/processedimages/logo3.png"
    };
    private String[] text = {
            "拼词龙卷风",
            "猴年画候",
            "腊八节",
            "寒假班火热开启",
            "单词王非你莫属",
            "寻玉之旅"
    };
    private BitmapUtils bitmapUtils;
    Context mContext;

    public ChooseMyLoveDingYueAdapter(List<hotschool> list, Context mContext) {
        this.mContext = mContext;
        this.list = list;
        bitmapUtils = new BitmapUtils(mContext);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }


    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, true);
        }
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_mylove_gridview_item_layout, null);
            holder.jigou_title_text = (TextView) convertView.findViewById(R.id.jigou_title_text);
            holder.jigou_title_img = (LinearLayout) convertView.findViewById(R.id.jigou_title_img);
            holder.img_jigou_logo = (ImageView) convertView.findViewById(R.id.img_jigou_logo);
            holder.jigou_text = (TextView) convertView.findViewById(R.id.jigou_text);
            holder.btn_choose_checkbox_rel = (LinearLayout) convertView.findViewById(R.id.btn_choose_checkbox_rel);
            holder.btn_choose_mylove_checkbox = (CheckBox) convertView.findViewById(R.id.btn_choose_mylove_checkbox);
            holder.btn_choose_checkbox_rel_01 = (LinearLayout) convertView.findViewById(R.id.btn_choose_checkbox_rel_01);
            holder.btn_choose_checkbox_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.free_position.clear();
                    MyApplication.free_position.add(list.get(position).getSchoolId()+"");
                Intent intent=new Intent(mContext,FreeDingYueActivity.class);
                    mContext.startActivity(intent);

                }
            });
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btn_choose_mylove_checkbox.setChecked(getIsSelected().get(position));
        holder.btn_choose_checkbox_rel_01.setVisibility(View.GONE);
        holder.btn_choose_checkbox_rel.setVisibility(View.GONE);
        holder.btn_choose_mylove_checkbox.setVisibility(View.GONE);
        bitmapUtils.display(holder.img_jigou_logo, "assets/processedimages/logo1.png");
        return convertView;
    }





    /**
     * 组件对象
     */
    public class ViewHolder {
        /**
         * 机构标题
         **/
        public TextView jigou_title_text;
        /**
         * 机构标题图片背景布局
         **/
        public LinearLayout jigou_title_img;
        /**
         * 机构logo图标
         **/
        private ImageView img_jigou_logo;
        /**
         * 机构描述
         **/
        public TextView jigou_text;
        /**
         * 免费订阅的按钮
         **/
        public LinearLayout btn_choose_checkbox_rel;
        /**
         * 选中组件
         **/
        public CheckBox btn_choose_mylove_checkbox;
        /**
         * 底色布局背景
         **/
        public LinearLayout btn_choose_checkbox_rel_01;

    }



    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public  void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        ChooseMyLoveDingYueAdapter.isSelected = isSelected;
    }
}


}
