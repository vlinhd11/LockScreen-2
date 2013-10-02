package jp.co.maplesystems.lockscreen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UnlockGroupSqlHelper extends SQLiteOpenHelper {
	final static private int DB_VERSION = 1;

	// テーブル作成用SQL
	private static final String CREATE_TABLE_SQL = 
			"CREATE TABLE unlock_group ("+
			"	id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"	unlock_coordinate_group_id INTEGER,"+
			"	x_coordinate INTEGER,"+
			"	y_coordinate INTEGER"+
			");";
	
	private static final String DROP_TABLE_SQL = 
			"DROP TABLE unlock_coordinate;"
	;
	
	public UnlockGroupSqlHelper(Context context){
		super(context, null, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		// table create
		db.execSQL(
				CREATE_TABLE_SQL
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// テーブル削除
		db.execSQL(
					DROP_TABLE_SQL
		);
	}
}
