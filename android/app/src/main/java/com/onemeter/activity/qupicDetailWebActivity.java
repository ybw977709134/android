package com.onemeter.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.adapter.Addqupicadpter;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.ImageFloder;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.PicUtils.CommonAdapter;
import com.onemeter.utils.PicUtils.ViewHolder;
import com.onemeter.utils.Utils;
import com.onemeter.view.ListImageDirPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 描述：一键生成趣相册模板页面
 * 项目名称：android
 * 作者：angelyin
 * 时间：2016/4/27 17:04
 */
public class qupicDetailWebActivity extends BaseActivity implements View.OnClickListener, ListImageDirPopupWindow.OnImageDirSelected, AdapterView.OnItemClickListener {
    private ImageView activity_qupic_detail_img_return;//返回键
    private TextView activity_qupic_detail_cancel;//取消
    private GridView mobai_pic_gridview;//显示手机相册图片组件
    private Button show_choose_pic_number;//显示选中图片数量
    private Button one_touch_pic;//一键生成去相册按钮
    private GridView add_pic_gridview;//显示选中图片列表项组件
    private Addqupicadpter mAddqupicadpter;
    //图片选择器的相关参数
    ProgressDialog prodialog;// 加载进度条对话框
    private int chooseCount;//选择图片的数量
    private ProgressDialog mProgressDialog;//进度条
    private int mPicsSize;//存储文件夹中的图片数量
    private File mImgDir; //图片数量最多的文件夹
    private List<String> mImgs;//所有的相册图片
    private QuPicFolderAdapter mAdapter;//手机图片适配器
    private HashSet<String> mDirPaths = new HashSet<String>();//临时的辅助类，用于防止同一个文件夹的多次扫描
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();//拿到所有图片的文件夹
    private int mScreenHeight;//屏幕的高度
    private ListImageDirPopupWindow mListImageDirPopupWindow;//选择图片文件夹弹窗
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    /**
     * 显示最新图片数据
     */
    private void data2View() {
        if (mImgDir == null) {
            Toast.makeText(this, "擦，一张图片没扫描到",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mImgs = Arrays.asList(mImgDir.list());
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new QuPicFolderAdapter(this, mImgs, R.layout.grid_item, mImgDir.getAbsolutePath());
        mobai_pic_gridview.setAdapter(mAdapter);
//        mImageCount.setText(totalCount + "张");
    }

    public  List<String>  imagepath=new ArrayList<String>();//请教成功后返回的图片集合
    private  int  num;
    private int groupId;//分类ID
    private String type;//模板类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qupic_detial_web_layout);
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        prodialog=new ProgressDialog(this);
        Intent intent = getIntent();
        type=intent.getStringExtra("type");
        groupId = intent.getIntExtra("groupId", 0);
//        toast("groupID："+groupId);
        initView();
        getImages();
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(this).inflate(R.layout.list_dir, null));
        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = qupicDetailWebActivity.this.getWindow().getAttributes();
                lp.alpha = 1.0f;
                qupicDetailWebActivity.this.getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }


    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = qupicDetailWebActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e("TAG", path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    //图片的张数
//                    totalCount += picSize;
                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();
                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();

    }

    /**
     * 初始化组件
     */
    private void initView() {
        imagepath.clear();
        activity_qupic_detail_img_return = (ImageView) findViewById(R.id.activity_qupic_detail_img_return);
        activity_qupic_detail_cancel = (TextView) findViewById(R.id.activity_qupic_detail_cancel);
        mobai_pic_gridview = (GridView) findViewById(R.id.mobai_pic_gridview);
        show_choose_pic_number = (Button) findViewById(R.id.show_choose_pic_number);
        one_touch_pic = (Button) findViewById(R.id.one_touch_pic);
        add_pic_gridview = (GridView) findViewById(R.id.add_pic_gridview);
        add_pic_gridview.setOnItemClickListener(this);
        activity_qupic_detail_img_return.setOnClickListener(this);
        activity_qupic_detail_cancel.setOnClickListener(this);
        one_touch_pic.setOnClickListener(this);
        show_choose_pic_number.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_qupic_detail_img_return:
                //返回键
                finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.activity_qupic_detail_cancel:
                //取消
                break;
            case R.id.show_choose_pic_number:
                //为底部的布局设置点击事件，弹出popupWindow
                mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(show_choose_pic_number, 0, 0);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = qupicDetailWebActivity.this.getWindow().getAttributes();
                lp.alpha = .3f;
                qupicDetailWebActivity.this.getWindow().setAttributes(lp);
                break;
            case R.id.one_touch_pic:
                //一键生成
                 getNetPosttoChooseImageFile();
                break;
        }
    }

    /**
     * 提交post请求 上传多图
     */

    private void getNetPosttoChooseImageFile() {

        num=0;
        if(mSelectedImage.size()==0){
            toast("您还没有选择图片");
            return;
        }
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();

        for(int i=0;i<mSelectedImage.size();i++){
             num++;
            if(num==mSelectedImage.size()){
                RequestParams params = new RequestParams();
                params.addBodyParameter("file", new File(mSelectedImage.get(i).toString()));
                params.addBodyParameter("userId", UserLoginActivity.userInfos.get(0).getUid() + "");
                new NetUtil(this).sendPostToServer(params, Constants.API_POST_UPLOAD_IMAGEFILE, qupicDetailWebActivity.this, "趣相册上传最后一张图片");
            }else {
                RequestParams params = new RequestParams();
                params.addBodyParameter("file", new File(mSelectedImage.get(i).toString()));
                params.addBodyParameter("userId", UserLoginActivity.userInfos.get(0).getUid() + "");
                new NetUtil(this).sendPostToServer(params, Constants.API_POST_UPLOAD_IMAGEFILE, qupicDetailWebActivity.this, "趣相册一键上传图片");
            }

        }

    }

    @Override
    public void selected(ImageFloder floder) {
        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png"))
//                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new QuPicFolderAdapter(this, mImgs, R.layout.grid_item, mImgDir.getAbsolutePath());
        mobai_pic_gridview.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mListImageDirPopupWindow.dismiss();
    }


    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public List<String> mSelectedImage = new ArrayList<String>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//          toast(mAddqupicadpter.getItem(position).toString());
        mSelectedImage.remove(mAddqupicadpter.getItem(position).toString());
        mAddqupicadpter.notifyDataSetChanged();
        chooseCount--;
        show_choose_pic_number.setText("已经添加了" + chooseCount + "张照片");
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 请求结束后此方法更新数据
     * @param result
     * @param isSuccess
     * @param api
     * @param action
     */
    public void onCompleted(String result, boolean isSuccess, String api, String action) {
        boolean statu = false;
        String message = null;
        boolean ss=false;
        JSONObject jsonObject = null;
        if (prodialog != null && prodialog.isShowing()) {
//            prodialog.dismiss();
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
           if(action.equals("趣相册一键上传图片")){
               if(statu){//成功
                   String  path  =jsonObject.getString("data");
                   imagepath.add(path);
               }else {
                   toast("上传失败");

               }
           }
            /*******************************************************************************/
            if(action.equals("趣相册上传最后一张图片")){
                prodialog.dismiss();
                if(statu){//成功
                    String  path  =jsonObject.getString("data");
                    imagepath.add(path);
//                    toast(imagepath.size() + "");
                    getNetPosttoServerforCreate();
                }else {
                    toast("上传失败");
                }
            }

            /******************************************************************************/
        if(action.equals("一键创建趣相册")){
            if(statu){//成功
           JSONObject   jsonObject1=jsonObject.getJSONObject("data");
                int  activityId= jsonObject1.getInt("id");
                String  type=jsonObject1.getString("type");
              Intent   intent=new Intent(qupicDetailWebActivity.this,QuxiangceHuoDongWeb.class);
                 intent.putExtra("type",type);
                intent.putExtra("activityId",activityId);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

            }else {//失败
             toast(message);

            }
        }





        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    /**
     * 创建趣相册
     */
    private void getNetPosttoServerforCreate() {
        String  pictures;
        String s="pictures=";
        StringBuffer sb = new StringBuffer(s);
        for(int i=0;i<imagepath.size();i++){
           String p=imagepath.get(i);
          sb.append(p);
            if (i != imagepath.size() - 1) {
                sb.append(",");
            }
        }
        pictures=sb.toString();
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        String urlvalue = Constants.API_GET_CREATE_QU_PIC + "pictures=" +pictures+"&type="+type+"&groupId="+groupId ;
        new NetUtil(this).sendGetToServer(urlvalue, qupicDetailWebActivity.this, "一键创建趣相册");

    }


    /**
     * 显示手机相册的适配器
     */

    class QuPicFolderAdapter extends CommonAdapter<String> {

        /**
         * 文件夹路径
         */
        private String mDirPath;

        public QuPicFolderAdapter(Context context, List<String> mDatas, int itemLayoutId, String dirPath) {
            super(context, mDatas, itemLayoutId);
            this.mDirPath = dirPath;
        }

        @Override
        public void convert(final ViewHolder helper, final String item) {
             final ImageView mImageView;
             final ImageView mSelect;
            //设置no_pic
            helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
            //设置no_selected
            helper.setImageResource(R.id.id_item_select,
                    R.drawable.picture_unselected);
            //设置图片
            helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
            mImageView = helper.getView(R.id.id_item_image);
            mSelect = helper.getView(R.id.id_item_select);
            mImageView.setColorFilter(null);
            //设置ImageView的点击事件
            mImageView.setOnClickListener(new View.OnClickListener() {
                //选择，则将图片变暗，反之则反之
                @Override
                public void onClick(View v) {
                    // 已经选择过该图片
                    if (mSelectedImage.contains(mDirPath + "/" + item)) {
                        mSelectedImage.remove(mDirPath + "/" + item);
                        mSelect.setImageResource(R.drawable.picture_unselected);
                        mImageView.setColorFilter(null);
                        chooseCount--;

                    } else {
                        // 未选择该图片
                        chooseCount++;
                        mSelectedImage.add(mDirPath + "/" + item);
                        mSelect.setImageResource(R.drawable.pictures_selected);
                        mImageView.setColorFilter(Color.parseColor("#77000000"));
                    }
                    show_choose_pic_number.setText("已经添加了" + chooseCount + "张照片");
                    mAddqupicadpter = new Addqupicadpter(getApplicationContext(), mSelectedImage);
                    add_pic_gridview.setAdapter(mAddqupicadpter);
//                     mAddqupicadpter.notifyDataSetChanged();

                }
            });

            /**
             * 已经选择过的图片，显示出选择过的效果
             */
            if (mSelectedImage.contains(mDirPath + "/" + item)) {
                mSelect.setImageResource(R.drawable.pictures_selected);
                mImageView.setColorFilter(Color.parseColor("#77000000"));
            }
        }
    }
}





