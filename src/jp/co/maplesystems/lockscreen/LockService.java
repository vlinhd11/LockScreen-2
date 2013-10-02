package jp.co.maplesystems.lockscreen;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class LockService extends Service{
	//private static String TAG = "LockService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//super.onStart(intent, startId);
		if ( intent != null && intent.getAction() != null ){

			// ロック処理呼ぶ
			LockUtil.getInstance().lock(this);
			if ( Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ){
				// 端末起動時
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
				if ( sp.getBoolean("is_enabled", false) ){
					// ロックが有効になっている場合、キーガードを無効化
					LockUtil.getInstance().disableKeyguard(this);
				}
			}
		}
	}
	
	public void onDestroy() {
		// 終了時にゴミをここで消す。
	}
}
