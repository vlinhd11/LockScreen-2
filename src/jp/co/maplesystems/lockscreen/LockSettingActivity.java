package jp.co.maplesystems.lockscreen;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class LockSettingActivity extends Activity {

	ImageView lockview;
	ArrayList<Integer> xArray = new ArrayList<Integer>();	// x座標を格納
	ArrayList<Integer> yArray = new ArrayList<Integer>();	// y座標を格納

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// レイアウトの設定
		setContentView(R.layout.activity_lock_setting);

		findViewById(R.id.lock_view).setOnTouchListener(touchListener);

		try{
			String filename = "lock.png";
			String path = Environment.getExternalStorageDirectory().getPath() + File.separator
					 + "Lockscreen" + File.separator
					 + filename;
			FileInputStream file = new FileInputStream(path);
			BufferedInputStream buf = new BufferedInputStream(file);
			Bitmap mBitmap = BitmapFactory.decodeStream(buf);
			buf.close();
			ImageView imgview = (ImageView)findViewById(R.id.lock_view);
			imgview.setImageBitmap(mBitmap);
			imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private OnTouchListener touchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
				//タッチする
				case MotionEvent.ACTION_DOWN:
					xArray.add((int) Math.floor(x));
					yArray.add((int) Math.floor(y));
					break;
				//タッチしたまま動かす
				case MotionEvent.ACTION_MOVE:
					xArray.add((int) Math.floor(x));
					yArray.add((int) Math.floor(y));
					break;
				//指を離す
				case MotionEvent.ACTION_UP:
					xArray.add((int) Math.floor(x));
					yArray.add((int) Math.floor(y));
					AlertDialog.Builder adb = new AlertDialog.Builder(LockSettingActivity.this);
					adb.setTitle("確認");
					adb.setMessage("こちらで登録しますか？");
					// OKボタン
					adb.setPositiveButton(
							"OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									CoordinateSqlHelper coordinateHelper = new CoordinateSqlHelper(LockSettingActivity.this);
									SQLiteDatabase db = coordinateHelper.getReadableDatabase();
									try{
										db.execSQL("BEGIN TRANSACTION");
										db.delete("unlock_coordinate", "unlock_coordinate_group_id = 1", null);
										ContentValues cv = new ContentValues();
										cv.put("unlock_coordinate_group_id", 1);
										cv.put("x_coordinate", xArray.get(xArray.size()-1));
										cv.put("y_coordinate", yArray.get(yArray.size()-1));
										db.insert(
												"unlock_coordinate",
												null,
												cv
										);
										db.execSQL("COMMIT TRANSACTION");
									}catch(Exception e){
										db.execSQL("ROLLBACK TRANSACTION");
									}
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
									Log.v("READ", "x:" + c.getString(0) + " y:" + c.getShort(1) );
									c.close();
									// ギャラリー呼び出し
									Intent viewIntent = new Intent(LockSettingActivity.this, SettingActivity.class);
									startActivity(viewIntent);
								}
							}
					);
					// キャンセルボタン
					adb.setNegativeButton(
							"キャンセル",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									xArray = new ArrayList<Integer>();
									yArray = new ArrayList<Integer>();
								}
							}
					);
					adb.show();
					break;
			}
			return true;
		}
	};
}
