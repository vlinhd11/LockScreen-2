package jp.co.maplesystems.lockscreen;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class LockUtil extends Activity implements OnTouchListener {

	// シングルトン
	private static LockUtil instance = new LockUtil();

	private LockUtil(){
	}

	public static LockUtil getInstance(){
		return instance;
	}

	// ロック画面用
	private View mLockView = null;
	private WindowManager mWindowManager = null;
	private Boolean isLock = true;

	public void lock( Context _c ){
		// 設定値を取得
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(_c);
		boolean isEnabled = sp.getBoolean("is_enabled", false);

		if (isEnabled && isLock == true){
			isLock = false;
			// ロック有効の場合
			// ロック画面作成
			mLockView = LayoutInflater.from(_c).inflate(R.layout.activity_lock, null);
			// ボタン押下時のリスナーを設定
			//mLockView.findViewById(R.id.button_unlock).setOnClickListener(this);

			// 画面タッチイベント
			mLockView.setOnTouchListener(this);

			// ロック画面表示用のパラメータ
			LayoutParams params = new LayoutParams();

			// 全画面
			params.width	= LayoutParams.WRAP_CONTENT;
			params.height	= LayoutParams.WRAP_CONTENT;
			// SystemAlert
			params.type		= LayoutParams.TYPE_SYSTEM_ALERT;
			// 透過
			params.format	= PixelFormat.TRANSLUCENT;

			try{
				String filename = "lock.png";
				String path = Environment.getExternalStorageDirectory().getPath() + File.separator
						 + "Lockscreen" + File.separator
						 + filename;
				FileInputStream file = new FileInputStream(path);
				BufferedInputStream buf = new BufferedInputStream(file);
				Bitmap mBitmap = BitmapFactory.decodeStream(buf);
				buf.close();
				ImageView imgview = (ImageView)mLockView.findViewById(R.id.lock_view);
				imgview.setImageBitmap(mBitmap);
				imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// WindowManagerに追加
			if ( mWindowManager == null ){
				mWindowManager = (WindowManager)_c.getSystemService(Context.WINDOW_SERVICE);
			}
			mWindowManager.addView(mLockView, params);
		}
	}

	public void unlock(){
		// WindowManagerから削除
		mWindowManager.removeView(mLockView);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		String action = "";

		switch(event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			action = "Touch Down";
			break;
		case MotionEvent.ACTION_MOVE:
			action = "Touch Move";
			break;
		case MotionEvent.ACTION_UP:
			action = "Touch Up";
			break;
		case MotionEvent.ACTION_CANCEL:
			action = "Touch Cancel";
			break;
		}
		Log.v("value", "1");
		// SQLiteDatabase クラスを使ってデータベースを取得 or 作成する
		CoordinateSqlHelper coordinateHelper = new CoordinateSqlHelper(this.getApplicationContext());
		Log.v("value", "2");
		SQLiteDatabase db = coordinateHelper.getReadableDatabase();
		Log.v("value", "3");
		Cursor c = db.query(
				"unlock_coordinate",
				new String[] { "x_coordinate", "y_coordinate" },
				"unlock_coordinate_group_id = ?",
				new String[]{"1"},
				null,
				null,
				"id ASC",
				null
		);
		c.moveToFirst();
		if((Math.floor(x)-20) <= Integer.valueOf(c.getString(0))
				&& Integer.valueOf(c.getString(0)) <= (Math.floor(x)+20)
				&& (Math.floor(y)-20) <= Integer.valueOf(c.getString(1))
				&& Integer.valueOf(c.getString(1)) <= (Math.floor(y)+20)
				){
			// ロック解除
			unlock();
			isLock = true;
		}
		c.close();
		Log.v("value", "x:"+Math.floor(x));
		Log.v("value", "x:"+c.getString(0));
		Log.v("value", "y:"+Math.floor(y));
		Log.v("value", "y:"+c.getString(1));
		return false;
	}

	// キーガード用
	private KeyguardManager mKeyguard = null;
	private KeyguardManager.KeyguardLock mLock = null;

	public void disableKeyguard( Context _c ){
		// 初期化して
		if ( mKeyguard == null ){
			mKeyguard = (KeyguardManager)_c.getSystemService(Context.KEYGUARD_SERVICE);
			mLock = mKeyguard.newKeyguardLock("LockUtil");
		}
		// キーガードを無効化
		mLock.disableKeyguard();
	}

	public void enableKeyguard(){
		// キーガードを有効化
		if ( mLock != null ){
			mLock.reenableKeyguard();
		}else{
			// nullの場合は無効化されて無い
		}
	}
}
