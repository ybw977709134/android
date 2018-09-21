package com.onemeter.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.activity.AppFanKuiActivity;
import com.onemeter.activity.AppSettingActivity;
import com.onemeter.activity.DingYueActivity;
import com.onemeter.activity.MessageCenterActivity;
import com.onemeter.activity.MyFolderActivity;
import com.onemeter.activity.UserLoginActivity;
import com.onemeter.activity.UserSettingActivity;
import com.onemeter.activity.showDialogShareActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;



/**
 * 我的个人中心-》fragment
 */
public class MyFragment extends Fragment implements OnClickListener {
    private String fileName;

    //显示原型图像部分
    /**
     * 邮件
     **/
    private ImageView img_my_collection;
    /**
     * 用户图像
     **/
    private ImageView img_my_usehead;
    /**
     * 设置
     **/
    private ImageView img_my_setting;
    /**
     * 用户名
     **/
    private TextView txt_my_usename;

    //布局item部分
    /**
     * 我的订阅
     */
    private LinearLayout layt_my_dingyue;
    /**
     * 我的订单
     */
    private LinearLayout layt_my_order;
    /**
     * 我的潜客
     */
    private LinearLayout layt_my_qianke;

    /**
     * 我的音频
     **/
    private LinearLayout layt_my_yin_ping;
    /**
     * 给个好评
     **/
    private LinearLayout layt_my_hao_ping;
    /**
     * 意见反馈
     **/
    private LinearLayout layt_my_fan_kui;
    /**
     * 分享给好友
     **/
    private LinearLayout layt_my_share;
    /**
     * 设置
     **/
    private LinearLayout layt_my_setting;

    private View view;
    private Intent intent;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my,
                container, false);
        prodialog = new ProgressDialog(getActivity());
        initView();
        return view;
    }



    /**
     * 根据登录uid获取用户基本信息
     * 初始化数据
     */
    private void getNetUserInfo() {
        int userId = UserLoginActivity.userInfos.get(0).getUid();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(getActivity(), "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_LOOK_USER_INFO + "id=" + userId;
        new NetUtil(getActivity()).sendGetToServer(urlvalue, MyFragment.this, "myfragment获取用户信息");

    }


    @Override
    public void onResume() {
        super.onResume();
        fileName = MyApplication.user_imgpath;
        Bitmap bitmap = getImg(fileName);
        if (bitmap != null) {
            Bitmap output = toRoundCorner(bitmap, 250.0f);
            img_my_usehead.setImageBitmap(output);
        } else {
            img_my_usehead.setImageResource(R.mipmap.head);
        }

        txt_my_usename.setText(MyApplication.ni_name);

//        getNetUserInfo();
    }

    /**
     * 方法名称：初始化组件
     * 作者：$angelyin
     * 时间：2015/12/30 15:
     */
    private void initView() {
        txt_my_usename = (TextView) view.findViewById(R.id.txt_my_usename);
        txt_my_usename.setText(MyApplication.ni_name);
        layt_my_dingyue = (LinearLayout) view.findViewById(R.id.layt_my_dingyue);
        layt_my_order = (LinearLayout) view.findViewById(R.id.layt_my_order);
        layt_my_qianke = (LinearLayout) view.findViewById(R.id.layt_my_qianke);
        layt_my_hao_ping = (LinearLayout) view.findViewById(R.id.layt_my_hao_ping);
        layt_my_hao_ping.setVisibility(View.GONE);
        layt_my_yin_ping = (LinearLayout) view.findViewById(R.id.layt_my_yin_ping);
        layt_my_fan_kui = (LinearLayout) view.findViewById(R.id.layt_my_fan_kui);
        layt_my_share = (LinearLayout) view.findViewById(R.id.layt_my_share);
        layt_my_setting = (LinearLayout) view.findViewById(R.id.layt_my_setting);
        img_my_setting = (ImageView) view.findViewById(R.id.img_my_setting);
        img_my_usehead = (ImageView) view.findViewById(R.id.img_my_usehead);
        img_my_collection = (ImageView) view.findViewById(R.id.img_my_collection);

        layt_my_hao_ping.setOnClickListener(this);
        layt_my_yin_ping.setOnClickListener(this);
        layt_my_fan_kui.setOnClickListener(this);
        layt_my_share.setOnClickListener(this);
        layt_my_setting.setOnClickListener(this);
        img_my_setting.setOnClickListener(this);
        img_my_usehead.setOnClickListener(this);
        img_my_collection.setOnClickListener(this);
        layt_my_dingyue.setOnClickListener(this);
        layt_my_order.setOnClickListener(this);
        layt_my_qianke.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layt_my_dingyue:
                //订阅
//                Utils.showToast(getActivity(), "订阅");
                intent = new Intent(getActivity(), DingYueActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.layt_my_order:
                //订单
//                Utils.showToast(getActivity(), "订单");
                break;
            case R.id.layt_my_yin_ping:
                //我的文件夹
                intent = new Intent(getActivity(), MyFolderActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.layt_my_hao_ping:
                //好评
                Utils.showToast(getActivity(), "好评");
                break;
            case R.id.layt_my_fan_kui:
                //反馈
                intent = new Intent(getActivity(), AppFanKuiActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.layt_my_share:
                //分享
                intent = new Intent(getActivity(), showDialogShareActivity.class);
                startActivity(intent);
                break;
            case R.id.layt_my_setting:
                //关于应用的设置
                intent = new Intent(getActivity(), AppSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.img_my_setting:
                //关于账号的设置
                intent = new Intent(getActivity(), UserSettingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.img_my_usehead:
                //用户图像
                break;
            case R.id.img_my_collection:
                //消息
                intent = new Intent(getActivity(), MessageCenterActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
        }
    }


    public Bitmap getImg(String imgPath) {
        File mFile = new File(imgPath);
        // 若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            return bitmap;
        }
        return null;
    }

    public Bitmap toRoundCorner(Bitmap bitmap, float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 请求结束后根据此方法更新数据
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
            JSONObject jsonObject2 = jsonObject1.getJSONObject("user");


            /**************************************************************************/
            if (action.equals("myfragment获取用户信息")) {
                if (statu) {//成功

//
//                    if (jsonObject2.isNull("username")) {
//                        txt_my_usename.setText(jsonObject2.getString("phone"));
//                    } else {
//                        txt_my_usename.setText(jsonObject2.getString("username"));
//                    }

                } else {
                    Utils.showToast(getActivity(), message);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
