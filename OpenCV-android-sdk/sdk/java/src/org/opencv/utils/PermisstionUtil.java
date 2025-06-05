package org.opencv.utils;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 权限请求工具类
 */
@TargetApi(Build.VERSION_CODES.M)
public class PermisstionUtil {
	private static final String TAG = "PermisstionUtil";
	//日历
	public static String[] CALENDAR = {Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
	//相机
	public static String[] CAMERA = {Manifest.permission.CAMERA};
	//相册
	public static String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
	//联系人
	public static String[] CONTACTS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALL_LOG};
	//位置
	public static String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

	public static String[] ALERT_WINDOW = {Manifest.permission.SYSTEM_ALERT_WINDOW};//android.permission.SYSTEM_OVERLAY_WINDOW
	//麦克风
	public static String[] MICROPHONE = {Manifest.permission.RECORD_AUDIO};
	//手机
	public static String[] PHONE = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS};
	//传感器
	public static String[] SENSORS = {Manifest.permission.BODY_SENSORS};
	//短信
	public static String[] SMS = {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS};
	//文件读写
	public static String[] STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

	public static String[] PERMISSIONS_STORAGE_CAMERA_MEDIA_GOOGLE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
	public static String[] PERMISSIONS_STORAGE_CAMERA_MEDIA = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES};

	private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
	private static HashMap<String, Object> map = new HashMap<String, Object>();

	/**
	 * 版本检测,版本大于23android8
	 *
	 * @return
	 */
	public static boolean checkSDK() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	/**
	 * 检测是否打开通知权限
	 * */
	public static boolean checkNotification(Context context){
		NotificationManagerCompat notification = NotificationManagerCompat.from(context);
		boolean isEnabled = notification.areNotificationsEnabled();
		return isEnabled;
	}

	/**
	 * 打开通知权限
	 *
	 * @param context
	 */
	public static void openNotificationSettingsForApp(Context context) {
		// Links to this app's notification settings.
		Intent intent = new Intent();
		intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
		intent.putExtra("app_package", context.getPackageName());
		intent.putExtra("app_uid", context.getApplicationInfo().uid);
		// for Android 8 and above
		intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
		context.startActivity(intent);
	}

	/**
	 * 定位权限注册
	 */
	public static void requestPermission(Activity activity, OnPermissionResult onPermissionResult) {
		if (!PermisstionUtil.checkSDK()) {
			PermisstionUtil.requestPermissionsOfAndroid6(activity, PermisstionUtil.PERMISSIONS_STORAGE_CAMERA_MEDIA, 122, onPermissionResult);
		} else {
			PermisstionUtil.requestPermissionsOfAndroid8(activity, PermisstionUtil.PERMISSIONS_STORAGE_CAMERA_MEDIA, 123, "需要获取您手机的权限！", onPermissionResult);
		}
	}

	/**
	 * 定位权限注册
	 */
	public static void requestPermissionOfLocation(Activity activity, OnPermissionResult onPermissionResult) {
		if (!PermisstionUtil.checkSDK()) {
			PermisstionUtil.requestPermissionsOfAndroid6(activity, PermisstionUtil.LOCATION, 122, onPermissionResult);
		} else {
			PermisstionUtil.requestPermissionsOfAndroid8(activity, PermisstionUtil.LOCATION, 123, "需要获取您手机的权限！", onPermissionResult);
		}
	}

	/**
	 * 读写权限注册
	 */
	public static void requestPermissionOfStorage(Activity activity, OnPermissionResult onPermissionResult) {
		if (!PermisstionUtil.checkSDK()) {
			PermisstionUtil.requestPermissionsOfAndroid6(activity, PermisstionUtil.STORAGE, 122, onPermissionResult);
		} else {
			PermisstionUtil.requestPermissionsOfAndroid8(activity, PermisstionUtil.STORAGE, 123, "需要获取您手机的权限！", onPermissionResult);
		}
	}

	/**
	 * 通话记录权限注册
	 */
	public static void requestPermissionOfPhone(Activity activity, OnPermissionResult onPermissionResult) {
		if (!PermisstionUtil.checkSDK()) {
			PermisstionUtil.requestPermissionsOfAndroid6(activity, PermisstionUtil.MICROPHONE, 122, onPermissionResult);
		} else {
			PermisstionUtil.requestPermissionsOfAndroid8(activity, PermisstionUtil.MICROPHONE, 123, "需要获取您手机的权限！", onPermissionResult);
		}
	}

	/**
	 * 本地录音权限注册
	 */
	public static void requestPermissionOfMICROPHONE(Activity activity, OnPermissionResult onPermissionResult) {
		if (!PermisstionUtil.checkSDK()) {
			PermisstionUtil.requestPermissionsOfAndroid6(activity, PermisstionUtil.LOCATION, 122, onPermissionResult);
		} else {
			PermisstionUtil.requestPermissionsOfAndroid8(activity, PermisstionUtil.LOCATION, 123, "需要获取您手机的权限！", onPermissionResult);
		}
	}

	/**
	 * 本地相机权限注册
	 */
	public static void requestPermissionOfCAMERA(Activity activity, OnPermissionResult onPermissionResult) {
		if (!PermisstionUtil.checkSDK()) {
			PermisstionUtil.requestPermissionsOfAndroid6(activity, PermisstionUtil.CAMERA, 122, onPermissionResult);
		} else {
			PermisstionUtil.requestPermissionsOfAndroid8(activity, PermisstionUtil.CAMERA, 123, "需要获取您手机的权限！", onPermissionResult);
		}
	}

	private static void requestPermissionsOfAndroid6(Activity context, @NonNull String[] permissions, int requestCode, OnPermissionResult onPermissionResult) {
		if (Build.VERSION.SDK_INT >= 23) {
			if (permissions.length == 0) {
				return;
			}

			boolean needapply = false;
			for (int i = 0; i < permissions.length; i++) {
				int chechpermission = ContextCompat.checkSelfPermission(context, permissions[i]);
				if (chechpermission != PackageManager.PERMISSION_GRANTED) {
					needapply = true;
				}
			}
			if (needapply) {
				ActivityCompat.requestPermissions(context, permissions, 1);
			}
		}
	}


	/**
	 * 权限请求
	 *
	 * @param context
	 * @param permissions        需要请求的权限
	 * @param requestCode
	 * @param explainMsg         权限解释
	 * @param onPermissionResult
	 */
	public static void requestPermissionsOfAndroid8(@NonNull Context context, @NonNull String[] permissions, int requestCode, String explainMsg, OnPermissionResult onPermissionResult) {
		onPermissionResult = initOnPermissionResult(onPermissionResult, permissions, requestCode, explainMsg);
		if (permissions.length == 0) {
			invokeOnRequestPermissionsResult(context, onPermissionResult);
		} else if (context instanceof Activity || (Object) context instanceof Fragment) {
			if (checkSDK()) {
				onPermissionResult.deniedPermissions = getDeniedPermissions(context, permissions);
				if (onPermissionResult.deniedPermissions.length > 0) {//存在被拒绝的权限
					onPermissionResult.rationalePermissions = getRationalePermissions(context, onPermissionResult.deniedPermissions);
					if (onPermissionResult.rationalePermissions.length > 0) {//向用户解释请求权限的理由
						shouldShowRequestPermissionRationale(context, onPermissionResult);
					} else {
						invokeRequestPermissions(context, onPermissionResult);
					}
				} else {
					//所有权限允许
					onPermissionResult.grantResults = new int[permissions.length];
					for (int i = 0; i < onPermissionResult.grantResults.length; i++) {
						onPermissionResult.grantResults[i] = PackageManager.PERMISSION_GRANTED;
					}
					invokeOnRequestPermissionsResult(context, onPermissionResult);
				}
			} else {
				onPermissionResult.grantResults = getPermissionsResults(context, permissions);
				invokeOnRequestPermissionsResult(context, onPermissionResult);
			}
		}
	}

	/**
	 * 获取被拒绝的权限
	 *
	 * @param context
	 * @param permissions
	 *
	 * @return
	 */
	public static String[] getDeniedPermissions(Context context, String[] permissions) {
		List<String> list = new ArrayList<String>();
		for (String permission : permissions) {
			if (checkPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
				list.add(permission);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 获取权限请求结果
	 *
	 * @param context
	 * @param permissions
	 *
	 * @return
	 */
	private static int[] getPermissionsResults(Context context, String[] permissions) {
		int[] results = new int[permissions.length];
		for (int i = 0; i < results.length; i++) {
			results[i] = checkPermission(context, permissions[i]);
		}
		return results;
	}

	private static String[] getRationalePermissions(Context context, String[] deniedPermissions) {
		List<String> list = new ArrayList<String>();
		for (String permission : deniedPermissions) {
			if (context instanceof Activity) {
				if (((Activity) context).shouldShowRequestPermissionRationale(permission)) {
					list.add(permission);
				}
			} else if ((Object) context instanceof Fragment) {
				if (((Fragment) (Object) context).shouldShowRequestPermissionRationale(permission)) {
					list.add(permission);
				}
			} else {
				throw new IllegalArgumentException("context 只能是Activity或Fragment");
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 调用权限请求方法
	 *
	 * @param context
	 * @param onPermissionResult
	 */
	private static void invokeRequestPermissions(Context context, OnPermissionResult onPermissionResult) {
		if (context instanceof Activity) {
			((Activity) context).requestPermissions(onPermissionResult.deniedPermissions, onPermissionResult.requestCode);
		} else if ((Object) context instanceof Fragment) {
			((Fragment) (Object) context).requestPermissions(onPermissionResult.deniedPermissions, onPermissionResult.requestCode);
		}
	}

	/**
	 * 调用权限请求结果回调
	 *
	 * @param context
	 * @param onPermissionResult
	 */
	private static void invokeOnRequestPermissionsResult(Context context, OnPermissionResult onPermissionResult) {
		if (context instanceof Activity) {
			if (checkSDK()) {
				((Activity) context).onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
			} else if (context instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
				((ActivityCompat.OnRequestPermissionsResultCallback) context).onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
			} else {
				onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
			}
		} else if ((Object) context instanceof Fragment) {
			((Fragment) (Object) context).onRequestPermissionsResult(onPermissionResult.requestCode, onPermissionResult.permissions, onPermissionResult.grantResults);
		}
	}

	/**
	 * 显示权限解释
	 *
	 * @param context
	 * @param onPermissionResult
	 */
	private static void shouldShowRequestPermissionRationale(final Context context, final OnPermissionResult onPermissionResult) {
		invokeRequestPermissions(context, onPermissionResult);
//		final ESAlert alert = new ESAlert(context, "权限", "需要您手机的相关权限，请点击确定允许获取相关权限！", context.getResources().getColor(R.color.white), context.getResources().getColor(R.color.qianhei), 12);
//		alert.setOnAcceptButtonClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View view)
//			{
//				invokeRequestPermissions(context, onPermissionResult);
//			}
//		});
//		alert.setOnCancelButtonClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View view)
//			{
//				onPermissionResult.grantResults = getPermissionsResults(context, onPermissionResult.permissions);
//				invokeOnRequestPermissionsResult(context, onPermissionResult);
//				alert.dismiss();
//			}
//		});
//		alert.show();
	}

	/**
	 * 检查权限
	 *
	 * @param context
	 * @param permission
	 *
	 * @return
	 */
	private static int checkPermission(Context context, String permission) {
		int result = context.checkPermission(permission, Process.myPid(), Process.myUid());
		if (Manifest.permission.RECORD_AUDIO.equalsIgnoreCase(permission) && result == PackageManager.PERMISSION_GRANTED) {

		}
		return result;
	}

	/**
	 * 权限请求结果
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		synchronized (TAG) {
			OnPermissionResult onPermissionResult = (OnPermissionResult) map.get(String.valueOf(requestCode));
			if (onPermissionResult != null) {
				List<String> deniedPermissions = new ArrayList<String>();
				for (int i = 0; i < grantResults.length; i++) {
					if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
						deniedPermissions.add(permissions[i]);
					}
				}
				if (deniedPermissions.size() > 0) {
					onPermissionResult.onFail(requestCode);
				} else {
					onPermissionResult.onSuccess(requestCode);
				}
				map.remove(String.valueOf(requestCode));
			}
		}
	}

	/**
	 * 初始化权限请求回调
	 *
	 * @param onPermissionResult
	 * @param permissions
	 * @param requestCode
	 * @param explainMsg         @return
	 */
	private static OnPermissionResult initOnPermissionResult(OnPermissionResult onPermissionResult, String[] permissions, int requestCode, String explainMsg) {
		synchronized (TAG) {
			if (onPermissionResult == null) {
				onPermissionResult = new OnPermissionResult() {
					@Override
					public void onSuccess(int requestCode) {

					}

					@Override
					public void onFail(int requestCode) {

					}
				};
			}
			onPermissionResult.permissions = permissions;
			onPermissionResult.requestCode = requestCode;
			onPermissionResult.explainMsg = explainMsg;
			onPermissionResult.grantResults = new int[0];
			map.put(String.valueOf(requestCode), onPermissionResult);
			return onPermissionResult;
		}
	}



	/**
	 * 多组权限合并
	 *
	 * @param items
	 *
	 * @return
	 */
	public static String[] getPermissions(String[]... items)
	{
		int length = 0;
		for (String[] item : items)
		{
			length += item.length;
		}
		String[] result = new String[length];
		int i = 0;
		for (String[] item : items)
		{
			for (String itemIn : item)
			{
				result[i] = itemIn;
				i++;
			}
		}
		return result;
	}

	public abstract static class OnPermissionResult
	{
		int requestCode;
		String explainMsg;
		String[] permissions;
		String[] deniedPermissions;
		String[] rationalePermissions;
		int[] grantResults;

		//权限允许
		public abstract void onSuccess(int requestCode);

		//权限拒绝
		public abstract void onFail(int requestCode);
	}

	/**
	 * 获取应用程序通知状态
	 * **/
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static boolean isNotificationEnabled(Context context)
	{

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			//8.0手机以上
			if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE)
			{
				return false;
			}
		}

		String CHECK_OP_NO_THROW = "checkOpNoThrow";
		String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

		AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
		ApplicationInfo appInfo = context.getApplicationInfo();
		String pkg = context.getApplicationContext().getPackageName();
		int uid = appInfo.uid;

		Class appOpsClass = null;

		try
		{
			appOpsClass = Class.forName(AppOpsManager.class.getName());
			Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
			Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

			int value = (Integer) opPostNotificationValue.get(Integer.class);
			return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}

