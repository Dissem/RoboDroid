package ch.bfh.mobilecomputing.fs2014.robodroid;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import ch.bfh.mobilecomputing.fs2014.robodroid.JoystickView.JoystickEvent;
import ch.quantasy.remoteControl.MotorApplication;
import ch.quantasy.tinkerforge.tinker.agency.implementation.TinkerforgeStackAgency;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgentIdentifier;

public class MainActivity extends Activity implements JoystickEvent {
	private static final int VELOCITY_SCALE = 32767;
	private MotorApplication motorApplication = new MotorApplication();

	public MainActivity() {
		TinkerforgeStackAgentIdentifier identifier = new TinkerforgeStackAgentIdentifier(
				"RoboDroid");
		TinkerforgeStackAgent agent = TinkerforgeStackAgency.getInstance()
				.getStackAgent(identifier);
		agent.addApplication(motorApplication);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		JoystickView joystick = (JoystickView) findViewById(R.id.joystick);
		joystick.addEventListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onJoystickMove(float x, float y) {
		int vdc1 = (int) (x * VELOCITY_SCALE);
		int vdc2 = (int) (y * VELOCITY_SCALE);
		motorApplication.setVelocityDC1(vdc1);
		motorApplication.setVelocityDC2(vdc2);
	}

}
