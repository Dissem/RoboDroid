package ch.bfh.mobilecomputing.fs2014.robodroid;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {
	private int centerX, centerY;
	private float posX, posY;

	private int stickRadius;
	private int radius;

	private Paint linePaint;
	private Paint stickPaint;

	private List<JoystickEvent> eventListeners = new LinkedList<>();

	public JoystickView(Context context) {
		super(context);
		init();
	}

	public JoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(Color.LTGRAY);
		linePaint.setStyle(Paint.Style.STROKE);

		stickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		stickPaint.setColor(Color.DKGRAY);
		stickPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// get the diameter
		int d = Math.min(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));

		setMeasuredDimension(d, d);

		// before measure, get the center of view
		posX = centerX = (int) getWidth() / 2;
		posY = centerY = (int) getHeight() / 2;

		radius = d / 2;
		stickRadius = (int) (radius * 0.25);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		centerX = (getWidth()) / 2;
		centerY = (getHeight()) / 2;

		canvas.drawCircle(centerX, centerY, radius * 1.0f, linePaint);
		canvas.drawCircle(centerX, centerY, radius * 0.5f, linePaint);

		canvas.drawLine(centerX, centerY - radius, centerX, centerY + radius,
				linePaint);
		canvas.drawLine(centerX - radius, centerY, centerX + radius, centerY,
				linePaint);

		// painting the move button
		canvas.drawCircle(posX, posY, stickRadius, stickPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			posX = centerX;
			posY = centerY;
			fireJoystickMove(0, 0);
			invalidate();
			return true;
		}
		posX = event.getX();
		posY = event.getY();
		float x = posX - centerX;
		float y = posY - centerY;
		float abs = (float) Math.sqrt((posX - centerX) * (posX - centerX)
				+ (posY - centerY) * (posY - centerY));

		if (abs > radius - stickRadius) {
			float f = (radius - stickRadius) / abs;
			x = x * f;
			y = y * f;
			posX = x + centerX;
			posY = y + centerY;
		}
		fireJoystickMove(x, y);
		invalidate();
		return true;
	}

	private void fireJoystickMove(float x, float y) {
		float max = radius - stickRadius;
		float rX = x / max;
		float rY = y / max;
		for (JoystickEvent event : eventListeners)
			event.onJoystickMove(rX, rY);
	}

	public void addEventListener(JoystickEvent eventListener) {
		eventListeners.add(eventListener);
	}

	public static interface JoystickEvent {
		/**
		 * Returns relative values x, y between -1 and 1;
		 * 
		 * @param x
		 * @param y
		 */
		public void onJoystickMove(float x, float y);
	}
}
