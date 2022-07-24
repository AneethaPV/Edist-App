package com.example.edist;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SocialDistanceAlert extends Service {

	private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
	int NOTIFICATION_ID = 234;
	NotificationManager notificationManager;
	Handler hnd;

	public void notification_popup() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String CHANNEL_ID = "my_channel_01";
			CharSequence name = "my_channel";
			String Description = "This is my channel";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			mChannel.setDescription(Description);
			mChannel.enableLights(true);
			mChannel.setLightColor(Color.RED);
			mChannel.enableVibration(true);
			mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//			mChannel.setVibrationPattern(new long[]{0, 800, 200, 1200, 300, 2000, 400, 4000});
			mChannel.setShowBadge(false);
			notificationManager.createNotificationChannel(mChannel);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_01")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("Covid 19")
				.setContentText("Keep social distance..!");

//		Intent resultIntent = new Intent(getApplicationContext(), Details.class);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//		stackBuilder.addParentStack(MainActivity.class);
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}


	Runnable rn = new Runnable() {
		@Override
		public void run() {

			if (adpt.size() > 0) {
				Toast.makeText(getApplicationContext(), "Heloooo", Toast.LENGTH_SHORT).show();

//				MediaPlayer mPlayer2 ;//= MediaPlayer.create(getApplicationContext(), R.raw.noti_sound);
//				mPlayer2.start();
				MediaPlayer	mPlayer2 = MediaPlayer.create(SocialDistanceAlert.this, R.raw.notification_sound);
				mPlayer2.start();
//				notification_popup();
				String n = "";
				for (int i = 0; i < adpta.size(); i++) {
					n = n + "\n" + adpta.get(i);

				}

//                qa.setText(n);


			}

			adpt.clear();

			registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
			BTAdapter.startDiscovery();


			hnd.postDelayed(rn, 10000);


		}
	};


	ArrayList<String> adpt;
	ArrayList<String> adpta;



	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		adpt = new ArrayList<String>();
		adpta = new ArrayList<String>();


		hnd = new Handler();

		registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		BTAdapter.startDiscovery();

		hnd.post(rn);


	}

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
				String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);


				if (Math.abs(rssi) < 70) {
					if (!adpt.contains(name)) {
						adpt.add(name);
						adpta.add(name + "--" + Math.abs(rssi));

					}

				}

				Log.d("aaaaaaaaa", name + "--" + rssi);


				Toast.makeText(getApplicationContext(), name + "-----" + rssi + "", Toast.LENGTH_LONG).show();

			}


			BTAdapter.cancelDiscovery();

			//process new device.


		}
	};
}
