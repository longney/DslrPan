package com.longney.ui.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.longney.event.LoginFinishEvent;
import com.longney.ui.MainActivity;
import com.longney.ui.PushActivity;
import com.longney.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import de.greenrobot.event.EventBus;

public class MyApplication extends Application {
	public static boolean debug = true;
	public static MyApplication mApp;
	public static MyApplication getInstance() {
		return mApp;
	}

	/**
	 * 初始化ImageLoader
	 */
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = ImageLoaderConfiguration
				.createDefault(context);
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;

		String publicId = "KfXe7VhmCUAaaQdDko6Xpd94-gzGzoHsz";
		String publicKey = "9miax0Iw1GSBzkNnKUBo2tdr";

		AVOSCloud.initialize(this, publicId, publicKey);
		AVInstallation.getCurrentInstallation().saveInBackground();
		AVOSCloud.setDebugLogEnabled(debug);
		AVAnalytics.enableCrashReport(this, !debug);

		initImageLoader(mApp);
		if (MyApplication.debug) {
			Logger.level = Logger.VERBOSE;
		} else {
			Logger.level = Logger.NONE;
		}
		PushService.setDefaultPushCallback(this, PushActivity.class);
		PushService.subscribe(this, "public", PushActivity.class);
	}
	
	  public static void goMainActivityFromActivity(Activity fromActivity) {
		    EventBus eventBus = EventBus.getDefault();
		    eventBus.post(new LoginFinishEvent());
		    Intent intent = new Intent(fromActivity, MainActivity.class);
		    fromActivity.startActivity(intent);
		  }
}
