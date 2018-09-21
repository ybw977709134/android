package com.onemeter.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ParseException;
import android.os.Environment;
import android.os.Vibrator;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 工具类，包含一些常用的工具方法：<br>
 * 获取屏幕的尺寸<br>
 * 弹出Toast<br>
 * 震动<br>
 * 选择照片<br>
 * 裁切照片<br>
 * 格式化日期<br>
 * 获取临时文件的路径<br>
 *
 *
 */
public class Utils {
	/** *******************************************
	 * 得到系统时间
	 */
	public static String now()
	{
		Time localTime = new Time();
		localTime.setToNow();
		return localTime.format("%Y%m%d%H%M%S");
	}

	/**
	 * 控制软键盘隐藏
	 *
	 * @param
	 */
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * 验证手机号码的格式是否正确
	 *
	 * @author hutianfeng created at 2015/8/17
	 * @param phNum
	 * @return
	 */
	public static boolean isPhoneNum(String phNum) {
		/*
		 * 电信：189 181 180 177 153 133 1890 1330 1700 173联通：186 185 176 145 156 155
		 * 132 131 130 1860 1709 移动：139 138 137 136 135 134 147 188 187 184 183
		 * 182 1705 178 159 158 157 152 151 150 1391 1390
		 */
		String telRegex = "[1][34578]\\d{9}";
		if (TextUtils.isEmpty(phNum)) {
			return false;
		} else {
			return phNum.matches(telRegex);
		}

	}

	/**
	 * 验证邮箱格式是否正确
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)"
				+ "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	/**
	 * 密码正则表达式
	 *
	 * @param str
	 * @return
	 */
	public static boolean passWordd(String str) {
		Pattern pattern = Pattern.compile("[0-9a-zA-Z_-]{8,20}");
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
		return b;
	}


	/**
	 * 手机号码的正则表达式
	 */
	public static final String MOBILE = "^1[3458]\\d{9}";
	/**
	 * 获取json中指定key对应的value，只支持value为String类型，
	 * </br>例：获取json中courier对应的value{"courier"
	 * :{"identitycode":"111111111111111",
	 * "cellphone":"18516606694","name":"来忠"},"code":0}
	 *
	 * @author JPH
	 * @date 2015-5-22 下午4:31:40
	 * @param key
	 * @param jsonStr
	 * @return
	 */
	public static int page = 1;
	public static boolean isupdateChecked = false;

	public static String getInternalJson(String key, String jsonStr) {
		String targetJson = "";
		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(jsonStr))
			return targetJson;
		try {
			JSONObject object = new JSONObject(jsonStr);
			targetJson = object.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return targetJson == null ? "" : targetJson;
	}

	/**
	 * 获取屏幕的宽度
	 *
	 * @param activity
	 * @return int[widthPixels,heightPixels]
	 */
	public static int[] getScreenWidth(Activity activity) {
		// 获得屏幕宽度
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		int displayWidth = outMetrics.widthPixels;// 屏幕的宽度
		int displayHeight = outMetrics.heightPixels;// 屏幕高度
		return new int[] { displayWidth, displayHeight };
	}

	@SuppressLint("SimpleDateFormat")
	public static String parseDate(String stringdate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			try {
				calendar.setTime(formatter.parse(stringdate));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} catch (ParseException e) {
			Log.w("info", "解析错误！" + e.toString());
			e.printStackTrace();
		}
		Date date = new Date();
		date.setTime(calendar.getTimeInMillis());
		// Toast.makeText(this,formatter.format(date),Toast.LENGTH_SHORT).show();
		return formatter.format(date);
	}

	/**
	 * 弹出一个Toast
	 *
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToast(Context context, String text, int duration) {
		if (context == null)
			return;
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * 弹出一个Toast
	 *
	 * @param context
	 *
	 */
	public static void showToast(Context context, int resid) {
		if (context == null)
			return;
		Utils.showToast(context, context.getString(resid));
	}

	/**
	 * 弹出一个Toast,持续Toast.LENGTH_SHORT时间
	 *
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT);
	}

	/**
	 * 日期减少一天
	 *
	 * @author angelyin
	 * @date 2015-10-26 下午5:31:18
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 日期增加一天
	 *
	 * @author angelyin
	 * @date 2015-10-26 下午5:47:48
	 * @param date
	 * @return
	 */
	public static Date getAddDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 毫秒数格式化时间
	 * @param time
	 * @return
	 */

	public static String formaTime(long time){
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}


	/**
	 * 转换文件大小
	 *
	 * @return
	 * */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}




	/**
	 * 格式化日期
	 *
	 * @param date
	 * @param pattern
	 *            格式化模式，如：yyyy-MM-dd HH.ss.mm.ss
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,
				Locale.getDefault());
		return sdf.format(date);
	}

	/**
	 * 格式化日期，以默认的格式（yyyyMMddHHmmss）
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		// TODO Auto-generated method stub
		return formatDate(date, "yyyyMMddHHmmss");
	}


	/**
	 * 毫秒数转时间格式
	 * @param pattern
	 * @param dateTime
	 * @return
	 */
	public static String getFormatedDateTime(String pattern, long dateTime) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
		return sDateFormat.format(new Date(dateTime + 0));
	}

	/**
	 * 控制手机震动
	 *
	 * @param context
	 * @param duration
	 *            震动时间，单位毫秒
	 */
	public static void vibration(Context context, Integer... duration) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		if (duration != null && duration.length > 0) {
			vibrator.vibrate(duration[0]); // 长按振动
		} else {
			vibrator.vibrate(60); // 长按振动
		}
	}

	/**
	 * 获取临时文件的路径如：（/storage/emulated/0/ksudi/temp）
	 *
	 * @return
	 */
	public static File getTempFilePath() {
		File tempFile = new File(getAppFilePath() + "/temp/");
		if (!tempFile.exists()) {// 如果目录不存在则创建目录
			tempFile.mkdirs();
		}
		return tempFile;
	}

	public static String getAppFilePath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().getPath()
					+ "/ksudi";
		return Environment.getRootDirectory().getPath() + "/ksudi";
	}

	/**
	 * 不需要调到拨号界面，直接拨打电话
	 *
	 * @author JPH
	 * @date 2014-12-8 上午10:50:13
	 * @param context
	 * @param phoneNumber
	 */
//	public static void callFastPhone(Context context, String phoneNumber) {
//		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//				+ phoneNumber));
//		context.startActivity(intent);
//	}

	/**
	 * 清除应用的本地缓存
	 *
	 * @author angelyin
	 * @date 2014-12-10 下午8:35:59
	 * @param context
	 * @param cacheDir
	 * @return
	 */
	public static boolean clearCatch(Context context, File cacheDir) {
		// TODO Auto-generated method stub
		if (cacheDir == null) {
			return false;
		}
		;
		if (cacheDir.isDirectory()) {
			File[] files = cacheDir.listFiles();
			if (files == null)
				return false;
			for (int i = 0; i < files.length; i++) {
				File tempFile = files[i];
				if (tempFile != null) {
					if (tempFile.isFile()) {
						tempFile.delete();
					} else {
						clearCatch(context, tempFile);
					}
				}
			}
		} else {
			cacheDir.delete();
		}
		return true;
	}

	/**
	 * 获取缓存目录的大小
	 *
	 * @author JPH
	 * @date 2014-12-10 下午8:36:42
	 * @param directory
	 * @return
	 */
	public static long getCacheSize(File directory) {
		if (directory == null)
			return 0;
		long catchSize = 0;
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files == null)
				return 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					catchSize += getCacheSize(files[i]);
				} else {
					catchSize += files[i].length();
				}
			}
		} else {
			catchSize += directory.length();
		}
		return catchSize;
	}

	/**
	 * 发声音和震动
	 *
	 * @author Shoxz
	 * @date 2012-12-15 17:32
	 */
	public void sendSoundAndVibrate() {

	}

	/**
	 * 验证手机号是否正确
	 *
	 * @Author shawn
	 * @Date 2014年12月18日
	 * @param cellphone
	 * @return true为通过
	 */
	public static boolean cellphoneValid(String cellphone) {
		String rgx = "[1][3578]\\d{9}";
		Pattern p = Pattern.compile(rgx);
		Matcher m = p.matcher(cellphone);
		return m.matches();
	}

	/**
	 * 验证密码是否包含中文
	 *
	 * @Author shawn
	 * @Date 2014年12月18日
	 * @param password
	 * @return
	 */
	public static boolean passwordValid(String password) {
		String rgx = ".*?[\u4E00-\u9FA5]+.*?";
		Pattern p = Pattern.compile(rgx);
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/** 清楚通知 */
	public static void clearNotification(Context context, int noticeid) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(noticeid);
	}

//	/**
//	 * 弹出打开网络提示框
//	 *
//	 * @author JPH
//	 * @date 2015-2-2 下午3:38:28
//	 */
//	public static PopupWindow showOpenNetPopup(final Activity activity,
//											   View anchor) {
//		View menuView = LayoutInflater.from(activity).inflate(
//				R.layout.pop_open_net, null);
//		PopupWindow popupWindow = new PopupWindow(menuView,
//				ViewGroup.LayoutParams.MATCH_PARENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT, false);
//		popupWindow.showAsDropDown(anchor);
//		RelativeLayout rl_pop_net = (RelativeLayout) menuView
//				.findViewById(R.id.rl_pop_net);
//		rl_pop_net.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if (android.os.Build.VERSION.SDK_INT > 13) {// 3.2以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
//					activity.startActivity(new Intent(
//							android.provider.Settings.ACTION_SETTINGS));
//				} else {
//					activity.startActivity(new Intent(
//							android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//				}
//			}
//		});
//		return popupWindow;
//	}


	/**
	 * dp转换成px
	 *
	 * @author shawn
	 * @date 2015-3-3_14-30-42
	 * @param dp
	 * @param context
	 */
	public static int dp2px(int dp, Context context) {
		return ((int) ((context.getResources().getDisplayMetrics().density) * dp));
	}

	/**
	 * 将double形式的object转换成long
	 *
	 * @param object
	 * @return
	 * @author JPH
	 * @date 2015-4-28 下午4:44:14
	 */
	public static long doubleObjectToLong(Object object) {
		if (object == null)
			return -1000;
		return Long.parseLong(object.toString().replace(".0", ""));
	}

	/**
	 * 播放提示音
	 *
	 * @author JPH
	 * @date 2015-5-18 下午2:36:33
	 * @param context
	 * @param resId
	 *            要播放的提示音的资源id
	 */
	public static void playRing(Context context, int resId) {
		final SoundPool pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		final int soundID = pool.load(context, resId, 1);
		pool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
									   int status) {
				pool.play(soundID, 1, 1, 0, 0, 1);
			}
		});
	}

	/**
	 * 密码正则表达式
	 *
	 * @param str
	 * @return
	 */
	public static boolean passWord(String str) {
		Pattern pattern = Pattern.compile("[0-9a-zA-Z_-]{6,20}");
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
		return b;
	}

	/**
	 * 判断是否为手机号码 符合返回ture
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isMobile(String str) {
		return Regular(str, MOBILE);
	}

	/**
	 * 匹配是否符合正则表达式pattern 匹配返回true
	 *
	 * @param str
	 *            匹配的字符串
	 * @param pattern
	 *            匹配模式
	 * @return boolean
	 */
	private static boolean Regular(String str, String pattern) {
		if (null == str || str.trim().length() <= 0)
			return false;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}


}
