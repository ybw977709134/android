package com.onemeter.activity;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.MusicInfo;
import com.onemeter.net.NetUtil;
import com.onemeter.swipe.SwipeLayout;
import com.onemeter.swipe.adapters.BaseSwipeAdapter;
import com.onemeter.utils.Constants;
import com.onemeter.utils.Utils;
import com.onemeter.view.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：我的音乐管理页面
 * 作者：angelyin
 * 时间：2016/3/7 14:13
 * 备注：
 */
public class MyFolderMusicActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener, AdapterView.OnItemClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    /**
     * 返回键
     **/
    private ImageView activity_my_folder_music_img_return;
    /**
     * 本地音乐
     **/
    private RadioButton btn_music_local;
    /**
     * 我的音乐
     **/
    private RadioButton btn_music_my;
    /**
     * 加号
     **/
    private ImageView btn_add;
    /**
     * 列表组件
     **/
    private XListView activity_my_folder_music_listview;
    /**
     * 集合
     **/
    public static List<String> mMusicList;
    // 只是用来模拟异步获取数据
    private Handler handler;
    private MusicCenterAdapter mMusicCenterAdapter;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框

    public List<MusicInfo> data;
    private MediaPlayer mp;
    private int p;
    private int currIndex = 0;// 表示当前播放的音乐索引




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_folder_music_layout);
        prodialog = new ProgressDialog(this);
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        mp.setOnErrorListener(this);
        data = new ArrayList<MusicInfo>();
        initData();
        initView();

    }

    /**
     * 初始化数据
     */
    private void initData() {
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
     * 初始化组件
     */
    private void initView() {
        handler = new Handler();
        activity_my_folder_music_img_return = (ImageView) findViewById(R.id.activity_my_folder_music_img_return);
        btn_music_local = (RadioButton) findViewById(R.id.btn_music_local);
        btn_music_my = (RadioButton) findViewById(R.id.btn_music_my);
        btn_add = (ImageView) findViewById(R.id.btn_add);
        activity_my_folder_music_listview = (XListView) findViewById(R.id.activity_my_folder_music_listview);
        // 设置xlistview可以加载、刷新
        activity_my_folder_music_listview.setPullLoadEnable(false);
        activity_my_folder_music_listview.setPullRefreshEnable(true);
        activity_my_folder_music_listview.setXListViewListener(this);
        activity_my_folder_music_listview.setOnItemClickListener(this);


        btn_music_local.setChecked(true);
        btn_music_my.setChecked(false);
        btn_music_local.setOnClickListener(this);
        btn_music_my.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        activity_my_folder_music_img_return.setOnClickListener(this);
        //判断初始化的时候，直接显示本地的数据
        if (btn_music_local.isChecked()) {
            getNetMusicLocalData();
        } else {
            getNetMyMusicData();
        }

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
     * 我的音乐数据并初始化
     */
    private void getNetMyMusicData() {
        mp.reset();
        //我的
        btn_music_my.setTextColor(getResources().getColor(R.color.white));
        btn_music_my.setChecked(true);
        btn_music_my.setTextSize(18);
        //本地
        btn_music_local.setChecked(false);
        btn_music_local.setTextSize(16);
        btn_music_local.setTextColor(getResources().getColor(R.color.white_02));
        int uid = UserLoginActivity.userInfos.get(0).getUid();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_GET_MUSIC_LIST + "memberId=";
        new NetUtil(this).sendGetToServer(urlvalue, MyFolderMusicActivity.this, "我的音乐列表");
    }

    /**
     * 本地的音乐并初始化
     */
    private void getNetMusicLocalData() {
        //本地
        btn_music_local.setTextColor(getResources().getColor(R.color.white));
        btn_music_local.setChecked(true);
        btn_music_my.setTextSize(18);
        //我的
        btn_music_my.setChecked(false);
        btn_music_my.setTextSize(16);
        btn_music_my.setTextColor(getResources().getColor(R.color.white_02));
        data.clear();
        initData();
        if (null == data || data.size() == 0) {
            Toast.makeText(this, "没有扫描到音频文件!", Toast.LENGTH_LONG).show();
            return;
        }
        mMusicCenterAdapter = new MusicCenterAdapter(this);
        activity_my_folder_music_listview.setAdapter(mMusicCenterAdapter);
        mMusicCenterAdapter.notifyDataSetChanged();

    }


    @Override
    public void onRefresh() {
        // 模拟刷新数据，1s之后停止刷新
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                activity_my_folder_music_listview.stopRefresh();
                toast("刷新成功");
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_my_folder_music_img_return:
                //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.btn_music_local:
                //本地
                data.clear();
                mMusicCenterAdapter.notifyDataSetChanged();
                getNetMusicLocalData();
                break;
            case R.id.btn_music_my:
                //我的
                data.clear();
                mMusicCenterAdapter.notifyDataSetChanged();
                getNetMyMusicData();
                break;
            case R.id.btn_add:
                //加号
                toast("添加");
                break;


        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //判断初始化的时候，直接显示本地的数据
        if (btn_music_local.isChecked()) {
            start(position);
        } else {
            start(position);
//            toast(mMusicCenterAdapter.getItem(position).toString());
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
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    /**
     * 请求结束后此方法来更新数据
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
            if (action.equals("我的音乐列表")) {
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
                    mMusicCenterAdapter = new MusicCenterAdapter(this);
                    activity_my_folder_music_listview.setAdapter(mMusicCenterAdapter);
                    mMusicCenterAdapter.notifyDataSetChanged();


                } else {
                    toast(message + "提交失败");
                }
            }
/**************************************************************************************************/
        if(action.equals("我的音乐删除")){
            if(statu){//成功
                toast("删除成功");

            }else {//失败
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
    public class MusicCenterAdapter extends BaseSwipeAdapter {
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
        public MusicCenterAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.audio_record_message_item_swipe;
        }


        @Override
        public void fillValues(final int position, View convertView) {
            if (btn_music_local.isChecked()) {
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
            View v = LayoutInflater.from(mContext).inflate(R.layout.audio_frequency_listview_item, parent, false);
            final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
            // 双击的回调函数
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                    toast(mMusicCenterAdapter.getItem(position).toString());
                }
            });

            // 添加删除布局的点击事件
            v.findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    mp.reset();
                    // 点击完成之后，关闭删除menu
                    if (btn_music_local.isChecked()) {
                        mMusicCenterAdapter.notifyDataSetChanged();
                        swipeLayout.close();
                    } else {
                        //我的
                        int mid = data.get(position).getMid();
                        data.remove(p);
                        mMusicCenterAdapter.notifyDataSetChanged();
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
     * 请求提交删除音乐
     * @param mid
     */
    private void getNetDeleteMusic(int mid) {

        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_DELETE_USER_MUSIC + "id=" + mid;
        new NetUtil(this).sendGetToServer(urlvalue, MyFolderMusicActivity.this, "我的音乐删除");

    }

}
