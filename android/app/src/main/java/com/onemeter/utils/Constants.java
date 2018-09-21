package com.onemeter.utils;

/**
 * 描述：微秀api接口类
 * 作者：angelyin
 * 时间：2015/12/23 10:57
 * 备注：未标注的请求一律用get
 */
public class Constants {
    /**
     * 服务器的的路径(测试服务器)
     **/
    public static String SERVER_UL = "http://101.200.186.44:8080/";
//    /** 是否为开发模式 **/
//    public final static boolean DEVELOPER_MODE = false;

    /**
     * html5测试地址
     **/
    public final static String API_GET_HTML5_ADRESS = "https://www.baidu.com/";

    /**
     * 添加订阅
     **/
    public final static String API_ADD_DING_YUE = "mobile/subscribe/save?";
    /**
     * 订阅show(免费订阅热门学校)
     **/
    public final static String API_DING_YUE_HOT_SCHOOL = "mobile/subscribe/topMemberList?";
    /**
     * 单个热门学校详情
     **/
    public final static String API_ZHI_DING_HOT_SCHOOL_DETAIL = "mobile/subscribe/singleHotSchool?";
    /**
     * 我的订阅
     **/
    public final static String API_MY_DING_YUE = "mobile/subscribe/mySubscribe?";
    /**
     * 我的活动列表
     **/
    public final static String API_MY_HUO_DONG_LIST = "mobile/activity/list?";
    /**
     * 微秀热榜
     */
    public final static String API_HOT_LIST = "mobile/activity/hotHdList?";

    /**
     * 我的所有活动数据统计
     **/
    public final static String API_MY_ALL_HUO_DONG_COUNT = "mobile/activity/list?";
    /**
     * 我的单个活动数据统计
     **/
    public final static String API_MY_ZHI_DING_HUO_DONG_COUNT_STUDY="mobile/activity/applyAndScanCount?";

    /**
     * 根据ID查找场景设置相关内容
     **/

    public final static String API_FIND_BY_ID_CHANG_JING_SET_STUDY = "mobile/activity/findActivity?";

    /**
     * 根据id修改场景设置的相关内容
     **/
    //根据type来判断类型1，2，3
    public final static String API_CHANGE_BY_ID_CHANG_JING_SETTING = "mobile/activity/updateContent?";
    /**
     * 删除活动
     **/
    public final static String API_DELETE_BY_ID_HUO_DONG = "mobile/activity/delete?";
    /**
     * 复制活动
     */
    public final static String API_COPY_ACTIVITY_BY_ID_HUO_DONG = "mobile/activity/copyActivity?";

    /**
     * 欢迎引导图获取
     **/
    public final static String API_GET_GUIDE_PIC = "mobile/picture/guideList?";
    /**
     * 未登录时获取主菜单模板数据
     **/
    public final static String API_NOT_LOGIN_GET_LIST = "mobile/studyTemplate/outList?";

    /**
     * 趣相册分类
     **/
    public final static String API_QU_PHOTO_GET_CLASSIFY = "mobile/photoTemplate/groupList?";

    /***************************关于用户相关api接口***************************/


    /**
     * 获取我的音频
     **/
    public final static String API_GET_AUDIO_LIST = "mobile/audio/list?";
    /**
     * 创建我的音频
     *
     */
    public final static String API_GET_AUDIO_CREATE="mobile/audio/create";
    /**
     * 删除我的音频
     *
     */
    public final static String API_GET_AUDIO_DELETE="mobile/audio/deleteUserAudio?";
    /**
     * 获取我的音乐
     */
    public final static String API_GET_MUSIC_LIST="mobile/music/list?";
    /**
     * 创建音乐
     **/
    public final static String API_CREATE_MUSIC = "mobile/music/create?";
    /**
     * 删除音乐
     **/
    public final static String API_DELETE_USER_MUSIC = "mobile/music/deleteUserMusic?";

    /**
     * 我的图片库列表
     **/
    public final static String API_GET_USER_PIC_LIST = "mobile/picture/list?";

    /**
     * 趣相册分类模板
     */
    public final static String API_GET_QU_PIC_MODEL_LIST = "mobile/template/groupsList?";
    /**
     *创建趣相册接口
     */
     public  final static String API_GET_CREATE_QU_PIC="mobile/template/showPhoto?";
    /**
     * 删除我的图片
     **/
    public final static String API_DELETE_USER_PIC = "mobile/picture/delete?";
    /**
     * 我的消息列表
     **/
    public final static String API_MY_MESSAGE_CENTER_LIST = "mobile/message/list?";

    /**
     * 消息详情
     **/
    public final static String API_MY_MESSAGE_DETAIL = "mobile/message/readMessage?";
    /**
     * 删除消息
     **/
    public final static String API_MY_DELETE_MESSAGE = "mobile/message/deleteByMessageId?";
    /**
     * 意见反馈
     **/
    public final static String API_FEED_BACK = "mobile/feedback/save?";
    /**
     * 查看用户信息
     **/
    public final static String API_LOOK_USER_INFO = "phone/user/userInfo?";


    /**
     * 注册获取验证码
     **/
    public final static String API_GET_CODE_FOR_REGISTER = "mobile/sms/sendCode?";
    /**
     * 注册(post)
     **/
    public final static String API_POST_REGISTER = "phone/user/save";
    /**
     * 登录(post)
     **/
    public final static String API_POST_LOGIN = "user/phoneLogin";
    /**
     * 忘记密码(post)
     **/
    public final static String API_POST_FORGET_PASSWORD = "phone/user/forgetPassword";


    /**
     * 修改用户姓名、头像、性别、描述（post）
     **/

    public final static String API_UPDATE_USER = "phone/user/update";

    /**
     * 登录后修改密码(post)
     **/
    public final static String API_UPDATE_PASSWORD = "phone/user/resetPassword";
    /**
     * 一键上传七牛的接口
     */
    public  final static String  API_POST_UPLOAD_IMAGEFILE="mobile/qiNiu/upload";
    public static String FILE_DIR = "vishow";
    public static String TEMP_PICTURE = "camera";


    //分享相关
    // 微信appID
    public final static  String WX_APP_ID="wx4d8835765e1d4ad6";
    //QQ appID
    public final static  String QQ_APP_ID="1105314062";

}
