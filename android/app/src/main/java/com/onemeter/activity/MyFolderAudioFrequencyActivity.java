package com.onemeter.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.MusicInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.swipe.SwipeLayout;
import com.onemeter.swipe.adapters.BaseSwipeAdapter;
import com.onemeter.utils.AudioRecorder2Mp3Util;
import com.onemeter.utils.Constants;
import com.onemeter.utils.SDcardTools;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 描述：我的音频管理页面
 * 作者：angelyin
 * 时间：2016/1/29 10:59
 * 备注：
 */
public class MyFolderAudioFrequencyActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private ImageView activity_my_folder_audio_img_return;
    private XListView activity_my_folder_audio_listview;
    private ImageView btn_recording;
    private Dialog dialog;
    private RadioButton btn_yinpin_local;//本地
    private RadioButton btn_yinpin_my;//我的
    private Handler handler;//异步数据加载控制
    private AudioFrequencyCenterAdapter mAudioFrequencyCenterAdapter;//适配器
    ProgressDialog prodialog;// 加载进度条对话框
    public List<MusicInfo> data;//数据源集合
    private MediaPlayer mp;//播放音频的媒体对象
    private int currIndex = 0;// 表示当前播放的音乐索引
    public int p;//位置
    int uid = UserLoginActivity.userInfos.get(0).getUid();
    /**
     * 录音部分的组件
     */
    private  RelativeLayout  activity_recorder_rel;//录音弹窗布局
    private TextView        activity_recorder_text;//录音状态文字
    private TextView        activity_recorder_img_btn_text;//录音图标变化识别标示(开始、停止、保存)
    private ImageView     activity_recorder_img_btn;//录音按钮图标
    private LinearLayout  activity_recorder_quxiao_btn;//取消录音
    private boolean canClean = false;//是否删除的标识
    private boolean isStart = false;//录音的状态标示
    AudioRecorder2Mp3Util util = null;//音频转MP3工具类对象
    private String fileAudioName; // 保存的音频文件的名字
    private String AudioName; // 设置音频的名称


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_folder_frequency_main);
        prodialog = new ProgressDialog(this);
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        mp.setOnErrorListener(this);
        data = new ArrayList<MusicInfo>();
//        mAduioList = new ArrayList<MusicInfo>();
        loadFileData();
        initView();

    }

    //扫描本机音频文件
    private void loadFileData() {
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)); // 标题
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 大小
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));//路径
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
                if (duration >= 1000 && duration <= 900000) {
                    MusicInfo music = new MusicInfo();
                    music.setTitle(title);
                    music.setSize(size);
                    music.setPath(url);
                    music.setDuration(duration);
                    data.add(music);
                }
            } while (cursor.moveToNext());
        }
    }


    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/29 11:07
     */
    private void initView() {
        dialog = new Dialog(this, R.style.AppTheme);
        btn_yinpin_local = (RadioButton) findViewById(R.id.btn_yinpin_local);
        btn_yinpin_my = (RadioButton) findViewById(R.id.btn_yinpin_my);
        activity_my_folder_audio_img_return = (ImageView) findViewById(R.id.activity_my_folder_audio_img_return);
        activity_my_folder_audio_listview = (XListView) findViewById(R.id.activity_my_folder_audio_listview);
        btn_recording = (ImageView) findViewById(R.id.btn_recording);
        handler = new Handler();
        btn_yinpin_local.setChecked(true);
        btn_yinpin_my.setChecked(false);
        btn_yinpin_local.setOnClickListener(this);
        btn_yinpin_my.setOnClickListener(this);
        // 设置xlistview可以加载、刷新
        activity_my_folder_audio_listview.setPullLoadEnable(false);
        activity_my_folder_audio_listview.setPullRefreshEnable(true);
        activity_my_folder_audio_listview.setXListViewListener(this);
        activity_my_folder_audio_listview.setOnItemClickListener(this);
        activity_my_folder_audio_img_return.setOnClickListener(this);
        btn_recording.setOnClickListener(this);
        //判断初始化的时候，直接显示本地的数据
        if (btn_yinpin_local.isChecked()) {
            getNetLocalData();
        } else {
            getNetMyData();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_my_folder_audio_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.btn_recording:
                //录音按钮
                RecorderPopuwindow();
                break;
            case R.id.btn_yinpin_local:
                //本地
                data.clear();
                mAudioFrequencyCenterAdapter.notifyDataSetChanged();
                getNetLocalData();
                break;
            case R.id.btn_yinpin_my:
                //我的
                data.clear();
                mAudioFrequencyCenterAdapter.notifyDataSetChanged();
                getNetMyData();
                break;
            case R.id.activity_recorder_quxiao_btn:
                //取消
                dialog.dismiss();
                changbeijing();

                break;
            case R.id.activity_recorder_img_btn:
                //点击录音按钮操作事件
                initRecordingEvent();
                break;

        }
    }

    /**
     * 初始化录音事件
     */
    private void initRecordingEvent() {
        if (!SDcardTools.isHaveSDcard()) {
         toast("请插入SD卡以便存储录音");
            return;
        }
         // 这种创建方式生成的文件名是随机的(音频路径)
        fileAudioName = SDcardTools.getSDPath() + "/"+"audio"+Utils.now();
        AudioName="audio"+Utils.now()+".mp3";
        if(activity_recorder_img_btn_text.getText().equals("开始")){
           startRecording();

        }else
             if(activity_recorder_img_btn_text.getText().equals("停止录音")){
              stopRecording();
        }

    }

    /**
     * 开始录音操作
     */
    private void startRecording() {
        if (util == null) {
            util = new AudioRecorder2Mp3Util(null,
                    fileAudioName+".raw",
                    fileAudioName+".mp3");
        }
        if (canClean) {
            util.cleanFile(AudioRecorder2Mp3Util.RAW);
        }
        Toast.makeText(this, "请说话", Toast.LENGTH_SHORT).show();
        util.startRecording();
        canClean = true;
        isStart=true;
        activity_recorder_text.setText("正在录音...");
        activity_recorder_img_btn_text.setText("停止录音");
        activity_recorder_img_btn.setImageResource(R.mipmap.btn_sound_cancel);
    }


    /**
     * 停止录音并 完成上传录音操作
     */
    private void stopRecording() {
        activity_recorder_text.setText("正在转换，请稍后...");
        util.stopRecordingAndConvertFile();
        activity_recorder_text.setText("录音转码成功");
        util.cleanFile(AudioRecorder2Mp3Util.RAW);
        // 如果要关闭可以
        util.close();
        util = null;
        isStart=false;
        activity_recorder_img_btn_text.setText("开始");
        activity_recorder_img_btn.setImageResource(R.mipmap.btn_sound_start);
        dialog.dismiss();
        changbeijing();
//        toast(fileAudioName + ".mp3");
        getNetPosttoQiNiu(fileAudioName + ".mp3");
//        getNetPostAudio(fileAudioName + ".mp3");
    }

    /**
     * 上传音频到七牛
     * @param path
     */
    private void getNetPosttoQiNiu(String path) {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", new File(path.toString()));
        params.addBodyParameter("userId", UserLoginActivity.userInfos.get(0).getUid() + "");
        new NetUtil(this).sendPostToServer(params, Constants.API_POST_UPLOAD_IMAGEFILE, MyFolderAudioFrequencyActivity.this, "上传音频到七牛");

    }


    /**
     * 提交录音文件到服务器
     * @param Audiopath
     */
    private void getNetPostAudio(String Audiopath) {

        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("member.id", uid + "");
        params.addBodyParameter("name", AudioName);
        params.addBodyParameter("url", Audiopath);
        new NetUtil(this).sendPostToServer(params,Constants.API_GET_AUDIO_CREATE, MyFolderAudioFrequencyActivity.this, "创建我的音频并上传");
    }

    /**
     * 初始化录音界面
     */
    private void RecorderPopuwindow() {
        View layoutView = MyFolderAudioFrequencyActivity.this.getLayoutInflater().inflate(
                R.layout.recorder_popuwindow_layout, null);
        dialog.setContentView(layoutView, new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.y = MyFolderAudioFrequencyActivity.this.getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp =  MyFolderAudioFrequencyActivity.this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        MyFolderAudioFrequencyActivity.this.getWindow().setAttributes(lp);
        //布局录音事件设置

        activity_recorder_rel= (RelativeLayout) layoutView.findViewById(R.id.activity_recorder_rel);
        activity_recorder_text= (TextView) layoutView.findViewById(R.id.activity_recorder_text);
        activity_recorder_text.setText("点击开始录音");
        activity_recorder_img_btn= (ImageView) layoutView.findViewById(R.id.activity_recorder_img_btn);
        activity_recorder_quxiao_btn= (LinearLayout) layoutView.findViewById(R.id.activity_recorder_quxiao_btn);
        activity_recorder_img_btn_text= (TextView) layoutView.findViewById(R.id.activity_recorder_img_btn_text);
        activity_recorder_img_btn_text.setAlpha(0.0f);
        activity_recorder_quxiao_btn.setOnClickListener(this);
        activity_recorder_img_btn.setOnClickListener(this);


    }

//    /**
//     * 获取外置sd卡
//     * @return
//     */
//    public  String   getSDpath() {
//        StringBuffer buffer;
//        File innerDir;
//        File root;
//        File[] list;
//        innerDir = Environment.getExternalStorageDirectory();
//        root = innerDir.getParentFile();
//        list = root.listFiles();
//        buffer = new StringBuffer();
//        for (File file : list) {
//            if (file.compareTo(innerDir) != 0&&file.canWrite()) {
//                buffer.append(file);
//            }
//        }
//
//        return  buffer.toString();
//
//    }

    /**
     * 改变弹窗背景
     */
    private void changbeijing() {
        WindowManager.LayoutParams lp = MyFolderAudioFrequencyActivity.this.getWindow().getAttributes();
        lp.alpha = 1f;
        MyFolderAudioFrequencyActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 从我的获取数据并初始化
     */
    private void getNetMyData() {
        mp.reset();
        //我的
        btn_yinpin_my.setTextColor(getResources().getColor(R.color.white));
        btn_yinpin_my.setChecked(true);
        btn_yinpin_my.setTextSize(18);
        //本地
        btn_yinpin_local.setChecked(false);
        btn_yinpin_local.setTextSize(16);
        btn_yinpin_local.setTextColor(getResources().getColor(R.color.white_02));
        data.clear();

        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_GET_AUDIO_LIST + "memberId=" + uid ;
        new NetUtil(this).sendGetToServer(urlvalue, MyFolderAudioFrequencyActivity.this, "我的音频列表");

    }

    /**
     * 从本地获取数据并初始化
     */
    private void getNetLocalData() {
        //本地
        btn_yinpin_local.setTextColor(getResources().getColor(R.color.white));
        btn_yinpin_local.setChecked(true);
        btn_yinpin_my.setTextSize(18);
        //我的
        btn_yinpin_my.setChecked(false);
        btn_yinpin_my.setTextSize(16);
        btn_yinpin_my.setTextColor(getResources().getColor(R.color.white_02));
        data.clear();
        loadFileData();

        if (null == data || data.size() == 0) {
            Toast.makeText(this, "没有扫描到音频文件!", Toast.LENGTH_LONG).show();
            return;
        }
        mAudioFrequencyCenterAdapter = new AudioFrequencyCenterAdapter(this, data);
        activity_my_folder_audio_listview.setAdapter(mAudioFrequencyCenterAdapter);
        mAudioFrequencyCenterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (btn_yinpin_local.isChecked()) {
            //本地
            start(position);
        } else {
            //我的
            start(position);
        }

    }

    /**
     * 初始化组件
     */
    private void start(int position) {
        if (data.size() > 0 && position - 1 < data.size()) {
            String SongPath = data.get(position - 1).getPath();
            mp.reset();
            try {
                mp.setDataSource(SongPath);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "播放列表为空", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRefresh() {
        // 模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                activity_my_folder_audio_listview.stopRefresh();
                toast("刷新成功");

            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {

    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mp != null) {
            mp.stop();
            //释放资源
            mp.release();
        }
        super.onDestroy();
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
            JSONObject jsonObject1=jsonObject.getJSONObject("data");

            /**************************************************************************/
            if (action.equals("我的音频列表")) {
                if (statu) {//成功
                    JSONArray jsonArray = jsonObject1.getJSONArray("content");
                    if (jsonArray.length() == 0) {
                        return;
                    }
                    data.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MusicInfo mfo = new MusicInfo();
                        int id = jsonArray.getJSONObject(i).getInt("id");
                        String name = jsonArray.getJSONObject(i).getString("name");
                        String url = jsonArray.getJSONObject(i).getString("url");
                        String path = "http://7xrula.com1.z0.glb.clouddn.com/" + url;
                        mfo.setMid(id);
                        mfo.setPath(path);
                        mfo.setTitle(name);
                        data.add(mfo);
                    }
                    mAudioFrequencyCenterAdapter = new AudioFrequencyCenterAdapter(this, data);
                    activity_my_folder_audio_listview.setAdapter(mAudioFrequencyCenterAdapter);
                    mAudioFrequencyCenterAdapter.notifyDataSetChanged();

                } else {
                    toast(message + "提交失败");

                }
            }
            /**************************************************************************************/
            if(action.equals("上传音频到七牛")){
                if(statu){
               toast("上传成功");
                }else {
                    toast(message + "上传七牛失败");
                }
            }



/********************************************************************************************/
            if (action.equals("我的音频删除")) {
                if (statu) {
                    toast("删除成功");

                } else {

                    toast(message + "删除失败");


                }


            }

          /***********************************************************/

            if(action.equals("创建我的音频并上传")) {
                if(statu){//成功
                toast("上传成功");

                    data.clear();
                    mAudioFrequencyCenterAdapter.notifyDataSetChanged();
                    getNetMyData();

                }else{//失败

                  toast(message);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 适配器
     */
    class AudioFrequencyCenterAdapter extends BaseSwipeAdapter {
        List<MusicInfo> dataa;

        /**
         * 点击播放录音
         **/
        private RelativeLayout audio_record_message_item_rel;
        /**
         * 录音文件标题
         **/
        private TextView audio_record_message_item_title;
        /**
         * 录音文件大小
         **/
        private TextView audio_record_message_item_mb;
        private Context mContext;
        private TextView delete;

        // 构造函数
        public AudioFrequencyCenterAdapter(Context mContext, List<MusicInfo> dataa) {
            this.mContext = mContext;
            this.dataa = dataa;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.audio_record_message_item_swipe;
        }


        @Override
        public void fillValues(final int position, View convertView) {
            //判断本地还是我的
            if (btn_yinpin_local.isChecked()) {
                delete = (TextView) convertView.findViewById(R.id.delete);
                delete.setText("停止");
            } else {
                delete = (TextView) convertView.findViewById(R.id.delete);
                delete.setText("删除");
            }

            audio_record_message_item_rel = (RelativeLayout) convertView.findViewById(R.id.audio_record_message_item_rel);
            audio_record_message_item_title = (TextView) convertView.findViewById(R.id.audio_record_message_item_title);
            audio_record_message_item_mb = (TextView) convertView.findViewById(R.id.audio_record_message_item_mb);
            audio_record_message_item_title.setText(data.get(position).getTitle().toString());
            String sizep = Utils.FormetFileSize(data.get(position).getSize());
            audio_record_message_item_mb.setText(sizep);


        }

        @Override
        public int getCount() {
            if (data == null) {
                return 0;
            } else {
                return data.size();
            }

        }

        @Override
        public Object getItem(int position) {
            if (data == null) {
                return null;
            } else {
                return data.get(position);
            }
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View generateView(final int position, ViewGroup parent) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.audio_frequency_listview_item,
                    parent, false);
            final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

            // 双击的回调函数
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                    toast(mAudioFrequencyCenterAdapter.getItem(position).toString());
                }
            });

            // 添加删除布局的点击事件
            v.findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // 点击完成之后，关闭删除menu
                    mp.reset();
                    //本地
                    if (btn_yinpin_local.isChecked()) {
                        mAudioFrequencyCenterAdapter.notifyDataSetChanged();
                        swipeLayout.close();
                    } else {
                        //我的
                        int mid = data.get(position).getMid();
                        data.remove(p);
                        mAudioFrequencyCenterAdapter.notifyDataSetChanged();
                        getNetDeleteMusic(mid);
                        p = position;
                        swipeLayout.close();
                    }


                }
            });
            return v;
        }

    }

    /**
     * 提交删除请求
     */
    private void getNetDeleteMusic(int mid) {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_GET_AUDIO_DELETE + "id=" + mid;
        new NetUtil(this).sendGetToServer(urlvalue, MyFolderAudioFrequencyActivity.this, "我的音频删除");

    }


}
