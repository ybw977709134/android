package com.onemeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;



/**
 * 描述：我的文件夹管理
 * 作者：Administrator
 * 时间：2016/1/29 10:37
 * 备注：
 */
public class MyFolderActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回键
     **/
    private ImageView activity_my_folder_img_return;
    /**
     * 我的音频
     **/
    private RelativeLayout my_aduio_frequency;
    /**
     * 我的图片库
     **/
    private RelativeLayout my_pic_folder;
    /**
     * 我的音乐库
     **/
    private RelativeLayout my_music_folder;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_folder_main);
        initView();
    }

    /**
     * 描述：初始化组件
     * 作者：$angelyin
     * 时间：2016/1/29 10:50
     */
    private void initView() {
        activity_my_folder_img_return = (ImageView) findViewById(R.id.activity_my_folder_img_return);
        my_aduio_frequency = (RelativeLayout) findViewById(R.id.my_aduio_frequency);
        my_pic_folder = (RelativeLayout) findViewById(R.id.my_pic_folder);
        my_music_folder = (RelativeLayout) findViewById(R.id.my_music_folder);

        activity_my_folder_img_return.setOnClickListener(this);
        my_aduio_frequency.setOnClickListener(this);
        my_pic_folder.setOnClickListener(this);
        my_music_folder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_my_folder_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.my_aduio_frequency:
                //音频
                intent = new Intent(this, MyFolderAudioFrequencyActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.my_pic_folder:
                //我的图片库
                intent = new Intent(this, MyFolderPic01Activity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.my_music_folder:
                //我的音乐库
                intent = new Intent(this, MyFolderMusicActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;

        }
    }
}
