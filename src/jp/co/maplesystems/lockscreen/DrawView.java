package jp.co.maplesystems.lockscreen;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener{

	private Paint paint;

	private ArrayList<Path> pathList = new ArrayList<Path>();
	private Path path;

	public DrawView(Context context) {
		super(context);
		setOnTouchListener(this);
		paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(10);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Path path : pathList) {
			canvas.drawPath(path, paint);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// タッチしたとき
			path = new Path();
			path.moveTo(event.getX(), event.getY());
			pathList.add(path);
			break;
		case MotionEvent.ACTION_MOVE:
			// タッチしたまま動かしたとき
			path.lineTo(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			// 指を離したとき
			path.lineTo(event.getX(), event.getY());
			break;
		default:
		}
		invalidate();
		return true;
	}
}