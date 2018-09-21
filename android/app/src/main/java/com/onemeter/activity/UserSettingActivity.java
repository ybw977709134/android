package com.onemeter.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.onemeter.R;
import com.onemeter.app.BaseActivity;
import com.onemeter.app.MyApplication;
import com.onemeter.net.NetUtil;
import com.onemeter.utils.Constants;
import com.onemeter.utils.FileUtils;
import com.onemeter.utils.PicUtils.ImageUtils;
import com.onemeter.utils.Utils;
import com.onemeter.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 账号设置页面
 */
public class UserSettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView activity_setting_img_return;
    private CircleImageView user_setting_pic_img;
    private RelativeLayout user_setting_pic_rel;
    private RelativeLayout user_setting_name;
    private RelativeLayout user_setting_sex;
    private RelativeLayout user_setting_email;
    private RelativeLayout user_setting_miaoshu;
    /**设置性别**/
    private TextView text_sex;
    /**设置昵称**/
    private TextView nice_text;
    /**设置账号**/
    private TextView user_zhanghao;
    /**设置邮箱**/
    private TextView text_email;
    /**个性签名**/
    private TextView text_miaoshu;
    Intent intent;
    private String fileName;
    private Dialog dialog;
    private Button bt_pz;
    private Button bt_xc;
    private Button bt_qx;
    private static int CAMERA_REQUEST_CODE = 1;//camera_request_code  相机返回码
    private static int GARLLY_REQUEST_CODE = 0;//carlly_request_code  相册返回码
    private String  picPath;
    /**
     * 进度条弹框
     **/
    ProgressDialog prodialog;// 加载进度条对话框
    Context  mContext;
    private BitmapUtils bitmapUtils;
    private  String  path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_settings);
        prodialog = new ProgressDialog(this);
        bitmapUtils=new BitmapUtils(this);
//        getNetData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        text_sex.setText(MyApplication.sex_name);
        nice_text.setText(MyApplication.ni_name);
        user_zhanghao.setText(MyApplication.user_name);
        text_email.setText(MyApplication.user_email);
        text_miaoshu.setText(MyApplication.user_miaoshu);
    }




    /**
     * 描述：初始化
     * 作者：$angelyin
     * 时间：2016/1/18 20:10
     */
    private void initView() {
        dialog = new Dialog(this, R.style.head_dialog);
        text_sex = (TextView) findViewById(R.id.text_sex);
        nice_text = (TextView) findViewById(R.id.nice_text);
        user_zhanghao = (TextView) findViewById(R.id.user_zhanghao);
        text_email = (TextView) findViewById(R.id.text_email);
        text_miaoshu = (TextView) findViewById(R.id.text_miaoshu);


        text_sex.setText(MyApplication.sex_name);
        nice_text.setText(MyApplication.ni_name);
        user_zhanghao.setText(MyApplication.user_name);
        text_email.setText(MyApplication.user_email);
        text_miaoshu.setText(MyApplication.user_miaoshu);

        user_setting_pic_img = (CircleImageView) findViewById(R.id.user_setting_pic_img);
        activity_setting_img_return = (ImageView) findViewById(R.id.activity_setting_img_return);
        user_setting_pic_rel = (RelativeLayout) findViewById(R.id.user_setting_pic_rel);
        user_setting_name = (RelativeLayout) findViewById(R.id.user_setting_name);
        user_setting_sex = (RelativeLayout) findViewById(R.id.user_setting_sex);
        user_setting_email = (RelativeLayout) findViewById(R.id.user_setting_email);
        user_setting_miaoshu = (RelativeLayout) findViewById(R.id.user_setting_miaoshu);

        fileName =MyApplication.user_imgpath;
        Bitmap bitmap = getImg(fileName);
        if (bitmap != null) {
            Bitmap output = toRoundCorner(bitmap, 250.0f);
            user_setting_pic_img.setImageBitmap(output);
        } else {
            user_setting_pic_img.setImageResource(R.mipmap.ic_launcher);
        }

        activity_setting_img_return.setOnClickListener(this);
        user_setting_name.setOnClickListener(this);
        user_setting_pic_rel.setOnClickListener(this);
        user_setting_sex.setOnClickListener(this);
        user_setting_email.setOnClickListener(this);
        user_setting_miaoshu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_setting_img_return:
                //返回键
                this.finish();
                this.overridePendingTransition(R.anim.activity_move_in, R.anim.activity_move_out);
                break;
            case R.id.user_setting_name:
                //名称
                intent = new Intent(this, ChangeNiceActivity.class);
                intent.putExtra("ni_name",MyApplication.ni_name);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.user_setting_pic_rel:
                //头像
                photoOrPicDialog();
                break;
            case R.id.user_setting_sex:
                //性别
                intent = new Intent(this, ChoseSexActivity.class);
                intent.putExtra("sex_name",MyApplication.sex_name);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.bt_pz:
                //相机
                enterCamera();
                break;
            case R.id.bt_xc:
                enterAlbum();
                break;
            case R.id.bt_qx:
                dialog.dismiss();
                break;
            case R.id.user_setting_miaoshu:
                //个性签名
                intent = new Intent(this, ChangeMiaoShuActivity.class);
                intent.putExtra("miaoshu",MyApplication.user_miaoshu);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;
            case R.id.user_setting_email:
                //邮箱
                intent = new Intent(this, BindEmaiActivity.class);
                intent.putExtra("email",MyApplication.user_email);
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
                break;

        }

    }


    /**
     * 选值回传
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("SdCardPath")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    /**
                     * 当选择的图片不为空的话，在获取到图片的途径
                     */
                    Uri uri = data.getData();
                    startPhotoZoom(uri);
                    break;
                case 1:
                    File picture = new File(picPath);
                    startPhotoZoom(Uri.fromFile(picture));
                    break;
                case 2:
                    getImageToView(data);
                    break;
            }


        }


    }

    /**
     * //     * 描述：设置用户头像的弹出框
     * //     * 作者：$angelyin
     * //     * 时间：2016/1/21 16:04
     * //
     */
    private void photoOrPicDialog() {
        View layoutView = this.getLayoutInflater().inflate(
                R.layout.my_fragment_photo_pic_main, null);
        dialog.setContentView(layoutView, new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.y = this.getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        bt_pz = (Button) layoutView.findViewById(R.id.bt_pz);
        bt_pz.setOnClickListener(this);
        bt_xc = (Button) layoutView.findViewById(R.id.bt_xc);
        bt_xc.setOnClickListener(this);
        bt_qx = (Button) layoutView.findViewById(R.id.bt_qx);
        bt_qx.setOnClickListener(this);

    }

    /**
     * 进入拍照
     */
    private void enterCamera() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FileUtils fileUtils = new FileUtils();
        File file = null;
        try {
            file = File.createTempFile(
                    Constants.TEMP_PICTURE,//temp_picture
                    ".jpg",
                    fileUtils.creatSDDir(Constants.FILE_DIR
                            + File.separator + "cachefiles"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        picPath = file.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(file));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();

    }

    /**
     * 进入相册
     */
    private void enterAlbum() {
        intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, GARLLY_REQUEST_CODE);
        dialog.dismiss();
    }

    public Bitmap getImg(String imgPath) {
//        toast(imgPath+"");
        File mFile = new File(imgPath);
        // 若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            return bitmap;
        }
        return null;
    }


    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        int sdkVersion = Integer.valueOf(Build.VERSION.SDK);
        Log.d("sdkVersion:", String.valueOf(sdkVersion));
        Log.d("KITKAT:", String.valueOf(Build.VERSION_CODES.KITKAT));
        if (sdkVersion >= 19) {  // 或者 android.os.Build.VERSION_CODES.KITKAT这个常量的值是19
            picPath = uri.getPath();//5.0直接返回的是图片路径 Uri.getPath is ：  /document/image:46 ，5.0以下是一个和数据库有关的索引值
            System.out.println("path:" + picPath);
            // path_above19:/storage/emulated/0/girl.jpg 这里才是获取的图片的真实路径
            picPath = ImageUtils.getPath_above19(this, uri);
            System.out.println("path_above19:" + picPath);
        } else {
            picPath = ImageUtils.getFilePath_below19(uri,this);
        }

        startActivityForResult(intent, 2);
    }

    /**
     * 获取图片路径显示头像
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.compressImage(photo);
            Bitmap output = toRoundCorner(photo, 250.0f);
            user_setting_pic_img.setImageBitmap(output);
            FileUtils fileUtils = new FileUtils();
            fileUtils.creatSDDir(Constants.FILE_DIR);
            File file = null;
            try {
                //图片裁剪后的路径
                file = fileUtils.createFileInSDCard(java.util.UUID
                                .randomUUID().toString() + ".png",
                        Constants.FILE_DIR);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    if (photo.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                        out.flush();
                        out.close();
                    }
                    path=file.getAbsolutePath();
                     getNetPostUserTouXiang(file.getAbsolutePath());
//                    toast(file.getAbsolutePath()+"");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 上传用户头像到服务器
     */
    private void getNetPostUserTouXiang(String  imgpath) {
        if (!MyApplication.isNetAvailable) {// 网络不可用
            Utils.showToast(this, "网络不可用，请打开网络");
            return;
        }
        prodialog.setMessage("加载中");
        prodialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", UserLoginActivity.userInfos.get(0).getUid()+"");
        params.addBodyParameter("headImage",imgpath);
        params.addBodyParameter("type","修改头像" );
        new NetUtil(this).sendPostToServer(params, Constants.API_UPDATE_USER, UserSettingActivity.this, "用户头像上传");
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
     * 发送请求后此方法更新数据
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
            if (action.equals("用户头像上传")) {
                if (statu) {//成功
              toast("上传成功");
                    MyApplication.user_imgpath=path;
                } else {
                    Utils.showToast(this, "上传失败"+message);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
