package com.onemeter.net;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.onemeter.activity.AppFanKuiActivity;
import com.onemeter.activity.BindEmaiActivity;
import com.onemeter.activity.ByPhoneResetPasswordActivity;
import com.onemeter.activity.ChangeMiaoShuActivity;
import com.onemeter.activity.ChangeNiceActivity;
import com.onemeter.activity.ChangePassWordActivity;
import com.onemeter.activity.ChooseMyLoveDingYueActivity;
import com.onemeter.activity.ChoseSexActivity;
import com.onemeter.activity.FreeDingYueActivity;
import com.onemeter.activity.HomeTJDingYuedetail;
import com.onemeter.activity.HuoDongSettingActivity;
import com.onemeter.activity.MessageCenterActivity;
import com.onemeter.activity.MessageCenterDetailActivity;
import com.onemeter.activity.MyFolderAudioFrequencyActivity;
import com.onemeter.activity.MyFolderMusicActivity;
import com.onemeter.activity.QupicWebActivity;
import com.onemeter.activity.SmsFindPassWordActivity;
import com.onemeter.activity.TuiJianDingYueDetailActivity;
import com.onemeter.activity.UserLoginActivity;
import com.onemeter.activity.UserRegisterActivity;
import com.onemeter.activity.UserSettingActivity;
import com.onemeter.activity.ViShowRedlistActivity;
import com.onemeter.activity.ZhidingSharedetailActivity;
import com.onemeter.activity.qupicDetailWebActivity;
import com.onemeter.fragment.ChangJingFragment;
import com.onemeter.fragment.HomeFragment;
import com.onemeter.fragment.MyDingYueFragment;
import com.onemeter.fragment.MyFragment;
import com.onemeter.fragment.MyPicFragment;
import com.onemeter.fragment.TotalHuoDongFragment;
import com.onemeter.fragment.TuiJianDingYueFragment;
import com.onemeter.fragment.ZhiDingHuoDongFragment;
import com.onemeter.utils.Constants;


/**
 * 和服务器进行数据交互的工具类
 *
 * @author angelyin
 * @date 2015-6-1 下午3:35:19
 */
public class NetUtil {
    private HttpUtils hu;

    public NetUtil(Context context) {
        hu = new HttpUtils(10000);
        hu.configCurrentHttpCacheExpiry(1000);
    }

    /**
     * 向服务期发送Post请求(此方法只能在UI线程中调用)
     *
     * @param observer 被观察者
     * @param params   请求参数
     * @param api      请求的接口
     */
    public void sendPostToServer(RequestParams params, final String api,
                                 final Object observer, String action) {
        send(params, api, observer, action, HttpMethod.POST);
    }

    public void send(RequestParams params, final String api,
                     final Object observer, final String action, HttpMethod method) {
        StringBuffer sb = new StringBuffer(Constants.SERVER_UL);
        sb.append(api);
        Log.i("Onemeter", "post,api:" + sb.toString());
        hu.send(method, sb.toString(), params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                Log.i("Onemeter", "conn...");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if (isUploading) {
                    Log.i("Onemeter", "upload: " + current + "/" + total);
                } else {
                    Log.i("Onemeter", "reply: " + current + "/" + total);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                handlePostReslut(api, responseInfo.result, true, observer,
                        action);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i("Onemeter", error.getEntity() + ":" + msg);
                handlePostReslut(api, msg, false, observer, action);
            }
        });
    }

/***********************************************************************************************/
    /**
     * 向服务器发送get请求
     *
     * @param api
     * @param observer
     * @param action
     */
    public void sendGetToServer(final String api, final Object observer, String action) {
        sendd(api, observer, action, HttpMethod.GET);
    }

    public void sendd(final String api, final Object observer, final String action, HttpMethod method) {
        StringBuffer sb = new StringBuffer(Constants.SERVER_UL);
        sb.append(api);

        Log.i("Onemeter", sb.toString());
        hu.send(method, sb.toString(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                handlePostReslut(api, responseInfo.result, true, observer, action);
                Log.i("Onemeter", "成功：" + responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i("Onemeter", error.getEntity() + ":" + msg);
                handlePostReslut(api, msg, false, observer, action);
            }
        });
    }

    /**
     * 根据请求代码（即请求的方法）来处理Post、get等请求服务器返回的结果
     *
     * @param api
     * @param result
     * @param isSuccess
     * @param observer
     */
    protected void handlePostReslut(String api, String result,
                                    boolean isSuccess, Object observer, String action) {
        // 将服务器返回的结果交给业务逻辑页处理
        Log.i("Onemeter", "api:" + api + "\nresult:" + result);
        if (action.equals("修改密码")) {
            if (observer instanceof ChangePassWordActivity) {
                ((ChangePassWordActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("意见反馈")) {
            if (observer instanceof AppFanKuiActivity) {
                ((AppFanKuiActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }

        if (action.equals("注册发送验证码")) {
            if (observer instanceof UserRegisterActivity) {
                ((UserRegisterActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("注册提交")) {
            if (observer instanceof UserRegisterActivity) {
                ((UserRegisterActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }

        if (action.equals("登入提交")) {
            if (observer instanceof UserLoginActivity) {
                ((UserLoginActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }
        if (action.equals("忘记密码发送验证码")) {
            if (observer instanceof SmsFindPassWordActivity) {
                ((SmsFindPassWordActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }
        if (action.equals("忘记密码重置提交")) {
            if (observer instanceof ByPhoneResetPasswordActivity) {
                ((ByPhoneResetPasswordActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("获取所有订阅机构")) {
            if (observer instanceof ChooseMyLoveDingYueActivity) {
                ((ChooseMyLoveDingYueActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }

        if (action.equals("提交选中机构")) {
            if (observer instanceof ChooseMyLoveDingYueActivity) {
                ((ChooseMyLoveDingYueActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }
        if (action.equals("修改姓名")) {
            if (observer instanceof ChangeNiceActivity) {
                ((ChangeNiceActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("修改描述")) {
            if (observer instanceof ChangeMiaoShuActivity) {
                ((ChangeMiaoShuActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("修改邮箱")) {
            if (observer instanceof BindEmaiActivity) {
                ((BindEmaiActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("myfragment获取用户信息")) {
            if (observer instanceof MyFragment) {
                ((MyFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("消息列表")) {
            if (observer instanceof MessageCenterActivity) {
                ((MessageCenterActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }
        if (action.equals("消息详情")) {
            if (observer instanceof MessageCenterDetailActivity) {
                ((MessageCenterDetailActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }

        if (action.equals("订阅showr热门学校")) {
            if (observer instanceof TuiJianDingYueFragment) {
                ((TuiJianDingYueFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("订阅推荐学校信息")) {
            if (observer instanceof TuiJianDingYueDetailActivity) {
                ((TuiJianDingYueDetailActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("推荐学校详情")) {
            if (observer instanceof TuiJianDingYueDetailActivity) {
                ((TuiJianDingYueDetailActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("修改性别")) {
            if (observer instanceof ChoseSexActivity) {
                ((ChoseSexActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("微秀热榜")) {
            if (observer instanceof ViShowRedlistActivity) {
                ((ViShowRedlistActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }

        if (action.equals("场景列表")) {
            if (observer instanceof ChangJingFragment) {
                ((ChangJingFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("免费订阅详情")) {
            if (observer instanceof FreeDingYueActivity) {
                ((FreeDingYueActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("我的订阅")) {
            if (observer instanceof MyDingYueFragment) {
                ((MyDingYueFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("单个活动统计")) {
            if (observer instanceof ZhiDingHuoDongFragment) {
                ((ZhiDingHuoDongFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("总活动统计")) {
            if (observer instanceof TotalHuoDongFragment) {
                ((TotalHuoDongFragment) observer).onCompleted(result, isSuccess, api, action);
            }

        }

        if (action.equals("查找场景设置")) {
            if (observer instanceof HuoDongSettingActivity) {
                ((HuoDongSettingActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("修改场景设置")) {
            if (observer instanceof HuoDongSettingActivity) {
                ((HuoDongSettingActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if (action.equals("删除场景活动")) {
            if (observer instanceof HuoDongSettingActivity) {
                ((HuoDongSettingActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if (action.equals("订阅推荐热门学校")) {
            if (observer instanceof HomeFragment) {
                ((HomeFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("主页推荐学校详情")){
            if (observer instanceof HomeTJDingYuedetail) {
                ((HomeTJDingYuedetail) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("指定活动分享")){
            if (observer instanceof ZhidingSharedetailActivity) {
                ((ZhidingSharedetailActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if(action.equals("我的图片库列表")){
            if (observer instanceof MyPicFragment) {
                ((MyPicFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if(action.equals("我的音频列表")){
            if (observer instanceof MyFolderAudioFrequencyActivity) {
                ((MyFolderAudioFrequencyActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("我的音频删除")){
            if (observer instanceof MyFolderAudioFrequencyActivity) {
                ((MyFolderAudioFrequencyActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if(action.equals("我的音乐列表")){
            if (observer instanceof MyFolderMusicActivity) {
                ((MyFolderMusicActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("我的音乐删除")){
            if (observer instanceof MyFolderMusicActivity) {
                ((MyFolderMusicActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("我的图片库删除")){
            if (observer instanceof MyPicFragment) {
                ((MyPicFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("首页删除场景活动")){
            if (observer instanceof ChangJingFragment) {
                ((ChangJingFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("首页复制场景活动")){
            if(observer instanceof ChangJingFragment){
                ((ChangJingFragment) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("趣相册分类模板列表")){
           if(observer instanceof QupicWebActivity){
               ((QupicWebActivity) observer).onCompleted(result, isSuccess, api, action);
           }
        }

        if(action.equals("趣相册一键上传图片")){
           if(observer  instanceof qupicDetailWebActivity){
               ((qupicDetailWebActivity) observer).onCompleted(result, isSuccess, api, action);
           }
        }
        if(action.equals("趣相册上传最后一张图片")){
            if(observer  instanceof qupicDetailWebActivity){
                ((qupicDetailWebActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("一键创建趣相册")){
            if(observer  instanceof qupicDetailWebActivity){
                ((qupicDetailWebActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }

        if(action.equals("用户头像上传")){
            if(observer  instanceof  UserSettingActivity){
                ((UserSettingActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }

        if(action.equals("创建我的音频并上传")){
            if(observer  instanceof  MyFolderAudioFrequencyActivity){
                ((MyFolderAudioFrequencyActivity) observer).onCompleted(result, isSuccess, api, action);
            }
        }
        if(action.equals("上传音频到七牛")){
            if(observer  instanceof  MyFolderAudioFrequencyActivity){
                ((MyFolderAudioFrequencyActivity) observer).onCompleted(result, isSuccess, api, action);
            }

        }

    }

}
