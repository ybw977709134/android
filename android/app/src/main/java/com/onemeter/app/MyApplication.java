package com.onemeter.app;


import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.onemeter.service.NetChangeReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * 自定义app启动
 * 
 * @author angelyin
 * @date 2015-10-9 下午7:50:54
 */
public class MyApplication extends Application {
	private static MyApplication app;
	/** 网络是否可用的标识 **/
	public static boolean isNetAvailable = false;
	/** 网络状态改变接收器 **/
	public NetChangeReceiver receiver;
	public static String ni_name="";
	public static String sex_name="";
	public static String user_name="";
	public static String user_email="";
	public static String user_miaoshu="";
	public static String dingweiLocation="";
	public static String user_imgpath="";

    //免费订阅item中button点击的位置
	public static List<String> free_position;
	//场景列表中button点击的位置和类型、图标地址
	public static List<String> changjing_position;
	public static List<String> changjing_type;
	public static List<String> changjing_cover;
	//主页推荐场景点击位置ID
	public static List<String> htuijian_changjing_id;
	//记录推荐订阅item点击位置ID
	public static List<String> tuijian_dingyue_id;
    //保存登陆的状态标示（0未登录、1已登陆）
	public static List<String>  islogin;
	File cacheDir;

	@Override
	public void onCreate() {
		super.onCreate();
		islogin=new ArrayList<String>();
		islogin.add(0,"未登录");
		free_position=new ArrayList<String>();
		changjing_position=new ArrayList<String>();
		changjing_type=new ArrayList<String>();
		changjing_cover=new ArrayList<String>();
		htuijian_changjing_id=new ArrayList<String>();
		tuijian_dingyue_id=new ArrayList<String>();
		isNetAvailable = isNetworkConnected();
		receiver = new NetChangeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addCategory("android.intent.category.DEFAULT");
		registerReceiver(receiver, filter);// 注册广播接收器receiver
		//缓存的路径
		cacheDir= StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
       //初始化图片设置
		initImageLoader(getApplicationContext());

	}


	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
				.discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)//线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100) //缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();//开始构建

		ImageLoader.getInstance().init(config);//全局初始化此配置
	}


	/**
	 * 判断网络状态是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		} else {
			return false;
		}
	}



	public static MyApplication getInstance() {
		if (app == null) {
			app = new MyApplication();
		}
		return app;
	}


}
