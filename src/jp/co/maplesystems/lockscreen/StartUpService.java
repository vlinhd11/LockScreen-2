package jp.co.maplesystems.lockscreen;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class StartUpService extends Service{
	private BroadcastReceiver mScreenOnListener = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			// テストボタン押下時、ロック処理を呼ぶ
			LockUtil.getInstance().lock(getApplicationContext());
		}
	};

	@Override
	public void onStart(Intent intent, int startId) {
		//super.onStart(intent, startId);
		// ACTION_SCREEN_ONを受け取るBroadcastReceiverを登録
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenOnListener, filter);
	}

	@Override
	public void onDestroy() {
		// BroadcastReceiverを登録解除
		unregisterReceiver(mScreenOnListener);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
