package ch.bfh.mobilecomputing.fs2014.robodroid;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.bfh.mobilecomputing.fs2014.robodroid.JoystickView.JoystickEvent;
import ch.quantasy.remoteControl.MotorApplication;

/**
 * A placeholder fragment containing a simple view.
 */
public class SimpleJoystickFragment extends Fragment implements JoystickEvent {
	private static final int VELOCITY_SCALE = 32767;
	private TextView log;

	private MotorApplication motor;

	public SimpleJoystickFragment() {
		motor = MotorApplication.INSTANCE;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_joystick_simple,
				container, false);
		JoystickView joystick = (JoystickView) rootView
				.findViewById(R.id.joystick);
		joystick.addEventListener(this);

		log = (TextView) rootView.findViewById(R.id.log);

		return rootView;
	}

	@Override
	public void onJoystickMove(final float x, final float y) {
		int vdc1 = (int) (x * VELOCITY_SCALE);
		int vdc2 = (int) (y * VELOCITY_SCALE);
		motor.setVelocityDC1(vdc1);
		motor.setVelocityDC2(vdc2);

		log.setText(String.format("x=%.4f\ny=%+.4f\nα=%+.4f\nv=%+.4f", x, y,
				JoystickView.getAngle(x, y), JoystickView.getValue(x, y)));
	}
}