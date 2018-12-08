package com.trivediinfoway.theinnontheriver;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/*
 * Portions (c) 2009 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Coby Plain coby.plain@gmail.com, Ali Muzaffar ali@muzaffar.me
 */

public class Compass extends Activity {//implements SensorEventListener{

	private static final String TAG = "Compass";
	private static boolean DEBUG = false;
	private SensorManager mSensorManager;
	Sensor magnetometer;
	private Sensor mSensor;
	private DrawSurfaceView mDrawView;
	LocationManager locMgr;
	SensorEvent event_global;
	static float x, y, z;
	private long lastUpdate;
	boolean sensorHasChanged;
	float[] rotMat = new float[9];
	float[] vals = new float[3];

	private final SensorEventListener mListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			if (DEBUG)
				Log.d(TAG, "sensorChanged (" + event.values[0] + ", " + event.values[1] + ", " + event.values[2] + ")");

			event_global = event;
			if (mDrawView != null) {

				x = event_global.values[0];
				y = event_global.values[1];
			//	z = event_global.values[2];
				mDrawView.setOffset(event.values[0]);
				mDrawView.invalidate();
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		else*/
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		//accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	//	magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

	//	mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		setContentView(R.layout.activity_compass);

		mDrawView = (DrawSurfaceView) findViewById(R.id.drawSurfaceView);

		locMgr = (LocationManager) this.getSystemService(LOCATION_SERVICE); // <2>
		LocationProvider high = locMgr.getProvider(locMgr.getBestProvider(
				LocationUtils.createFineCriteria(), true));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(Compass.this,
					android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

				ActivityCompat.requestPermissions(Compass.this,
						new String[]{android.Manifest.permission.CAMERA},
						1);
			}
		}
		// using high accuracy provider... to listen for updates
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		locMgr.requestLocationUpdates(high.getName(), 0, 0f,
				new LocationListener() {
					public void onLocationChanged(Location location) {
						// do something here to save this new location
						Log.d(TAG, "Location Changed");
						mDrawView.setMyLocation(location.getLatitude(), location.getLongitude());
						mDrawView.invalidate();
						//Log.e("size...", MainActivity.lat_list.size()+":size");
					}

					public void onStatusChanged(String s, int i, Bundle bundle) {

					}

					public void onProviderEnabled(String s) {
						// try switching to a different provider
					}

					public void onProviderDisabled(String s) {
						// try switching to a different provider
					}
				});
	}

	@Override
	protected void onResume() {
		if (DEBUG)
			Log.d(TAG, "onResume");
		super.onResume();

		mSensorManager.registerListener(mListener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);

		//SENSOR_DELAY_FASTEST
		/*mSensorManager.registerListener(mListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);*/
		/*mSensorManager.registerListener(mListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(mListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_UI);*/
	}
	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		mSensorManager.unregisterListener(mListener);
	}
	@Override
	protected void onStop() {
		if (DEBUG)
			Log.d(TAG, "onStop");
		mSensorManager.unregisterListener(mListener);
		super.onStop();
	}

	/*@Override
	public void onSensorChanged(SensorEvent event) {*/
		/*if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			getAccelerometer(event);
		}*/
		/*if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			if (event.values[0] >= -4 && event.values[0] <= 4) {
				//near
				//near
				Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
			} else {
				//far
				Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
			}
		}*/
		/*sensorHasChanged = false;
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
			SensorManager.getRotationMatrixFromVector(rotMat,
					event.values);
			SensorManager
					.remapCoordinateSystem(rotMat,
							SensorManager.AXIS_X, SensorManager.AXIS_Y,
							rotMat);
			SensorManager.getOrientation(rotMat, vals);
			azimuth = deg(vals[0]); // in degrees [-180, +180]
			pitch = deg(vals[1]);
			roll = deg(vals[2]);
			sensorHasChanged = true;
		}*/
	//}
	private void getAccelerometer(SensorEvent event) {
		float[] values = event.values;
		// Movement
		float x = values[0];
		float y = values[1];
		float z = values[2];

		float accelationSquareRoot = (x * x + y * y + z * z)
				/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
		long actualTime = event.timestamp;
		if (accelationSquareRoot >= 2) //
		{
			if (actualTime - lastUpdate < 200) {
				return;
			}
			lastUpdate = actualTime;
			Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
					.show();
			/*if (color) {
				view.setBackgroundColor(Color.GREEN);
			} else {
				view.setBackgroundColor(Color.RED);
			}
			color = !color;*/
		}
	}

	/*@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}*/
}
