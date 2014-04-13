package ch.quantasy.remoteControl;

import android.util.Log;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import ch.quantasy.tinkerforge.tinker.core.implementation.TinkerforgeDevice;

import com.tinkerforge.BrickDC;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class MotorApplication extends AbstractTinkerforgeApplication {
	public static final MotorApplication INSTANCE = new MotorApplication();

	private BrickDC dc1;
	private BrickDC dc2;

	private MotorApplication() {
		// private
	}

	@Override
	public void deviceConnected(TinkerforgeStackAgent tinkerforgeStackAgent,
			Device device) {
		if (TinkerforgeDevice.getDevice(device) == TinkerforgeDevice.DC)
			if (dc1 == null) {
				dc1 = (BrickDC) device;
				try {
					dc1.enable();
					dc1.setAcceleration(10000);
				} catch (TimeoutException | NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (dc2 == null) {
				dc2 = (BrickDC) device;
				try {
					dc2.enable();
					dc2.setAcceleration(10000);
				} catch (TimeoutException | NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	}

	@Override
	public void deviceDisconnected(TinkerforgeStackAgent tinkerforgeStackAgent,
			Device device) {
		if (TinkerforgeDevice.areEqual(dc1, device))
			dc1 = null;
		if (TinkerforgeDevice.areEqual(dc2, device))
			dc2 = null;

	}

	public void fullStop() {
		try {
			if (dc1 != null)
				dc1.fullBrake();
			if (dc2 != null)
				dc2.fullBrake();
			Log.d("motor", "Full Stop!");
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setVelocityDC1(int velocity) {
		if (dc1 == null)
			return;
		try {
			dc1.setVelocity((short) velocity);
			Log.d("motor", "Velocity DC1: " + velocity);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setVelocityDC2(int velocity) {
		if (dc2 == null)
			return;
		try {
			dc2.setVelocity((short) velocity);
			Log.d("motor", "Velocity DC2: " + velocity);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
