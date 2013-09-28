package jp.co.maplesystems.lockscreen;

import android.app.Activity;
import android.os.Bundle;

public class LockActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// テストボタン押下時、ロック処理を呼ぶ
		LockUtil.getInstance().lock(getApplicationContext());
	}
}