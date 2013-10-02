package jp.co.maplesystems.lockscreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends Activity implements OnClickListener {

	private static final int REQUEST_GALLERY = 0;
	//private ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// レイアウトの設定
		setContentView(R.layout.activity_setting);

		// ロック開始(停止)ボタン設定
		Button buttonIsenabled = (Button)findViewById(R.id.button_isenabled);
		buttonIsenabled.setOnClickListener(this);

		// 解除時間・画面設定ボタンの設定
		Button buttonLockSetting = (Button)findViewById(R.id.button_lock_setting);
		buttonLockSetting.setText(R.string.setting_button_lock_setting);
		buttonLockSetting.setOnClickListener(this);
		
		// 解除キー設定ボタン設定
		Button buttonLockKey = (Button)findViewById(R.id.button_lock_key);
		buttonLockKey.setText(R.string.setting_button_lock_key);

		// サポートボタン設定
		Button buttonSupport = (Button)findViewById(R.id.button_support);
		buttonSupport.setText(R.string.setting_button_support);

		final Intent intent = new Intent(SettingActivity.this, StartUpService.class);

		// 設定値を反映
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (sp.getBoolean("is_enabled", false)){
			LockUtil.getInstance().disableKeyguard(this);
			startService(intent);
			buttonIsenabled.setText(R.string.setting_button_isenabled_stop);
		}else{
			LockUtil.getInstance().enableKeyguard();
			stopService(intent);
			buttonIsenabled.setText(R.string.setting_button_isenabled_start);
		}
	}

	@Override
	public void onClick(View v) {
		switch ( v.getId() ){
			case R.id.button_isenabled:
				final Intent intent = new Intent(SettingActivity.this, StartUpService.class); 
				// 設定値を反映
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
				Button buttonIsenabled = (Button)findViewById(R.id.button_isenabled);
				Editor e = sp.edit();
				if (sp.getBoolean("is_enabled", false)){
					e.putBoolean("is_enabled", false);
					LockUtil.getInstance().enableKeyguard();
					stopService(intent);
					buttonIsenabled.setText(R.string.setting_button_isenabled_start);
				}else{
					e.putBoolean("is_enabled", true);
					LockUtil.getInstance().disableKeyguard(this);
					startService(intent);
					buttonIsenabled.setText(R.string.setting_button_isenabled_stop);
				}
				e.commit();
				break;
			/*case R.id.button_gallery:
				// ギャラリー呼び出し
				Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
				gallaryIntent.setType("image/*");
				gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(gallaryIntent, REQUEST_GALLERY);
				break;*/
			case R.id.button_lock_setting:
				// ギャラリー呼び出し
				Intent viewIntent = new Intent(SettingActivity.this, SettingMainActivity.class);
				startActivity(viewIntent);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
			try {
				InputStream is = getContentResolver().openInputStream(data.getData());
				Bitmap img = BitmapFactory.decodeStream(is);
				is.close();

				String filename = "lock.png";
				String path = Environment.getExternalStorageDirectory().getPath() + File.separator
						 + getString(R.string.app_name) + File.separator
						 + filename;
				File file = new File(path);

				// 上の階層(アプリ名のディレクトリ)が存在しなかったら作成
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdir();
				}

				OutputStream os = new FileOutputStream(file);
				img.compress(CompressFormat.PNG, 100, os); // 拡張子、品質、出力先
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
			}
		} 
	}
}