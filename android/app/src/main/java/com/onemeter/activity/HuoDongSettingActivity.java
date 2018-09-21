package com.onemeter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.changjingsettingInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：场景设置页面
 * 作者：angelyin
 * 时间：2016/1/18 10:31
 * 备注：
 */
public class HuoDongSettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView activity_huodong_setting_img_return;//返回键
    private EditText activity_changjing_setting_main_title;//标题
    private EditText activity_huodong_setting_main_change_text;//内容
    private ImageView activity_huodong_setting_main_img;//图片
    private TextView activity_huodong_setting_main_change_time;//时间
    private Button activity_huodong_setting_main_delete_btn;//删除
    private Button activity_huodong_setting_main_bianji_btn;//编辑
    private Button activity_huodong_setting_main_share_and_look_btn;//分享并预览


    //单个场景的ID和类型
    String type;
    int activityId;
    String imagpath;
    ProgressDialog prodialog;// 加载进度条对话框
    public static List<changjingsettingInfo> cjst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_huo_dong_setting);
        prodialog = new ProgressDialog(this);
        cjst = new ArrayList<changjingsettingInfo>();
        type = MyApplication.changjing_type.get(0).toString();
        activityId = Integer.valueOf(MyApplication.changjing_position.get(0).toString()).intValue();
        imagpath=MyApplication.changjing_cover.get(0).toString();
        initDta();
        initView();
    }

    /**
     * 提交请求获取场景设置相关信息
     */
    private void initDta() {
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_FIND_BY_ID_CHANG_JING_SET_STUDY + "activityId=" + activityId + "&type=" + type;
        new NetUtil(this).sendGetToServer(urlvalue, HuoDongSettingActivity.this, "查找场景设置");
    }


    /**
     * 描述： 初始化组件
     * 作者：$angelyin
     * 时间：2016/1/18 11:39
     */
    private void initView() {
        activity_huodong_setting_main_img = (ImageView) findViewById(R.id.activity_huodong_setting_main_img);
        activity_huodong_setting_main_change_time = (TextView) findViewById(R.id.activity_huodong_setting_main_change_time);
        activity_huodong_setting_img_return = (ImageView) findViewById(R.id.activity_huodong_setting_img_return);
        activity_huodong_setting_main_delete_btn = (Button) findViewById(R.id.activity_huodong_setting_main_delete_btn);
        activity_huodong_setting_main_bianji_btn = (Button) findViewById(R.id.activity_huodong_setting_main_bianji_btn);
        activity_huodong_setting_main_share_and_look_btn = (Button) findViewById(R.id.activity_huodong_setting_main_share_and_look_btn);
        BitmapUtils bitmapUtils=new BitmapUtils(this);
        bitmapUtils.display(activity_huodong_setting_main_img, imagpath);
        activity_huodong_setting_img_return.setOnClickListener(this);
        activity_huodong_setting_main_delete_btn.setOnClickListener(this);
        activity_huodong_setting_main_bianji_btn.setOnClickListener(this);
        activity_huodong_setting_main_share_and_look_btn.setOnClickListener(this);

        activity_changjing_setting_main_title = (EditText) findViewById(R.id.activity_changjing_setting_main_title);
        activity_changjing_setting_main_title.setEnabled(false);
        activity_huodong_setting_main_change_text = (EditText) findViewById(R.id.activity_huodong_setting_main_change_text);
        activity_huodong_setting_main_change_text.setEnabled(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_huodong_setting_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.activity_huodong_setting_main_delete_btn:
                //删除按钮
//                toast("删除");
                getNetDeletePost();

                break;
            case R.id.activity_huodong_setting_main_bianji_btn:
                //编辑按钮
//                toast("编辑");
                if (activity_changjing_setting_main_title.isEnabled()) {
                    activity_changjing_setting_main_title.setEnabled(false);
                    activity_huodong_setting_main_bianji_btn.setText("编辑");
                } else {
                    activity_changjing_setting_main_title.setEnabled(true);
                    activity_huodong_setting_main_bianji_btn.setText("保存");
                }

                if (activity_huodong_setting_main_change_text.isEnabled()) {
                    activity_huodong_setting_main_change_text.setEnabled(false);
                    activity_huodong_setting_main_bianji_btn.setText("编辑");
                } else {
                    activity_huodong_setting_main_change_text.setEnabled(true);
                    activity_huodong_setting_main_bianji_btn.setText("保存");
                }

                if(activity_huodong_setting_main_bianji_btn.getText().equals("编辑")){
                    getNetChangePost();
                }
                break;
            case R.id.activity_huodong_setting_main_share_and_look_btn:
                //分享并预览
                toast("分享并预览");
                break;

        }
    }

    /**
     * 提交删除活动信息
     */
    private void getNetDeletePost(){
        prodialog.setMessage("删除中");
        prodialog.show();
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        String urlvalue = Constants.API_DELETE_BY_ID_HUO_DONG +"id=" + activityId + "&type=" + type;
        new NetUtil(this).sendGetToServer(urlvalue, HuoDongSettingActivity.this, "删除场景活动");

    }

    /**
     * 场景修改后提交
     */
    private void getNetChangePost() {
        String name=activity_changjing_setting_main_title.getText().toString();
        String content=activity_huodong_setting_main_change_text.getText().toString();
        if (!MyApplication.isNetAvailable) {
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        String urlvalue = Constants.API_CHANGE_BY_ID_CHANG_JING_SETTING + "id=" + activityId + "&name="+name+"&subhead="+content+"&type=" + type;
        new NetUtil(this).sendGetToServer(urlvalue, HuoDongSettingActivity.this, "修改场景设置");

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
            Utils.showToast(this, getResources().getString(R.string.msg_request_error));
            return;
        }
        //从result中提取应答的状态码
        try {
            jsonObject = new JSONObject(result);
            statu = jsonObject.getBoolean("success");
            message = jsonObject.getString("msg");

            /**************************************************************************/
            if (action.equals("查找场景设置")) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONObject jsonObject2 = jsonObject1.getJSONObject("music");
                if (statu) {//成功
//                toast("成功" + result);
                    changjingsettingInfo cfo = new changjingsettingInfo();
                    if (jsonObject1.isNull("id")) {
                    } else {
                        int id = jsonObject1.getInt("id");
                        cfo.setId(id);
                    }
                    if (jsonObject1.isNull("name")) {
                    } else {
                        String name = jsonObject1.getString("name");
                        cfo.setName(name);
                    }
                    if (jsonObject1.isNull("scanCount")) {
                    } else {
                        String scanCount = jsonObject1.getString("scanCount");
                        cfo.setScanCount(scanCount);
                    }
                    if (jsonObject1.isNull("createDate")) {
                    } else {
                        Long createDate = jsonObject1.getLong("createDate");
                        cfo.setCreateDate(createDate);

                    }
                    if (jsonObject1.isNull("type")) {
                    } else {
                        String typpe = jsonObject1.getString("type");
                        cfo.setType(typpe);
                    }
                    if (jsonObject1.isNull("memberId")) {
                    } else {
                        int memberId = jsonObject1.getInt("memberId");
                        cfo.setMemberId(memberId);
                    }
                    if (jsonObject1.isNull("subhead")) {
                        cfo.setSubhead("未设置");
                    } else {
                        String subhead = jsonObject1.getString("subhead");
                        cfo.setSubhead(subhead);
                    }
                    if (jsonObject1.isNull("applyCount")) {
                    } else {
                        int applyCount = jsonObject1.getInt("applyCount");
                        cfo.setApplyCount(applyCount);
                    }
                    if (jsonObject2.isNull("id")) {
                    } else {
                        int musicId = jsonObject2.getInt("id");
                        cfo.setMusicID(musicId);
                    }
                    if (jsonObject2.isNull("name")) {
                    } else {
                        String musicName = jsonObject2.getString("name");
                        cfo.setMusicName(musicName);
                    }
                    if (jsonObject2.isNull("url")) {
                    } else {
                        String musicurl = jsonObject2.getString("url");
                        cfo.setMusicURL(musicurl);
                    }
                    cjst.add(cfo);

                    activity_changjing_setting_main_title.setText(cjst.get(0).getName().toString());
                    activity_huodong_setting_main_change_text.setText(cjst.get(0).getSubhead().toString());
                    String datatime = Utils.getFormatedDateTime("yyyy-MM-dd", cjst.get(0).getCreateDate());
                    activity_huodong_setting_main_change_time.setText(datatime);

                } else {
                    toast(message);
                }
            }
            /******************************************************************************************************/

            if(action.equals("修改场景设置")){
                if(statu){//成功
                    toast("修改成功");
                    finish();
                }else {
                    toast(message);
                }
            }

            /*******************************************************************************************/
            if(action.equals("删除场景活动")){
                if(statu){//成功
                    toast("删除成功");
                    finish();
//                    Intent  intent=new Intent(HuoDongSettingActivity.this,MainActivity.class);
//                    startActivity(intent);

                }else {
                    toast(message);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
