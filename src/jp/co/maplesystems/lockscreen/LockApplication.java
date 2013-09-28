package jp.co.maplesystems.lockscreen;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

public class LockApplication extends Application {

	private LockReceiver mReceiver = null;
	@Override
	public void onCreate() {
		super.onCreate();
		// ACTION_SCREEN_OFFを受け取るためのIntentFilter
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		
		// LockReceiverでBroadcastを受け取るよう登録
		mReceiver = new LockReceiver();
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		// 一応解除
		unregisterReceiver(mReceiver);
	}
}
