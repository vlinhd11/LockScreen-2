package jp.co.maplesystems.lockscreen;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SettingMainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// レイアウトの設定
		setContentView(R.layout.activity_setting_main);
		setTitle(R.string.setting_main_title_lock_main);

		// デフォルト画像サムネ設定
		ImageButton imgButton = (ImageButton)findViewById(R.id.image_button_default);
		imgButton.setOnClickListener(this);

		// 新規作成ボタン設定
		Button buttonNew = (Button)findViewById(R.id.button_new);
		buttonNew.setText(R.string.setting_main_button_new);
		buttonNew.setOnClickListener(this);

		// 編集ボタン設定
		Button buttonEdit = (Button)findViewById(R.id.button_edit);
		buttonEdit.setText(R.string.setting_main_button_edit);
		buttonEdit.setOnClickListener(this);

		try {
			String filename = "lock.png";
			String path = Environment.getExternalStorageDirectory().getPath() + File.separator
					 + "Lockscreen" + File.separator
					 + filename;
			FileInputStream file;
			file = new FileInputStream(path);
			BufferedInputStream buf = new BufferedInputStream(file);
			Bitmap mBitmap = BitmapFactory.decodeStream(buf);
			buf.close();
			imgButton.setImageBitmap(mBitmap);
			imgButton.setScaleType(ImageView.ScaleType.FIT_XY);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void onClick(View v){
		switch ( v.getId() ){
		// デフォルトサムネ
		case R.id.image_button_default:
			final Intent intent = new Intent(SettingMainActivity.this, LockSettingActivity.class);
			startActivity(intent);
			break;
		// 新規作成ボタン
		case R.id.button_new:
			break;
		// 編集ボタン
		case R.id.button_edit:
			break;
		default:
			break;
		}
	}
}