package com.clubank.device;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import com.clubank.common.R;

public class AlarmAlertActivity extends BaseActivity {

	private static final int vibDuration = 10000;
	private Vibrator vib;
	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);

		Bundle b = getIntent().getExtras();
		String datetime = b.getString("datetime");
		String description = b.getString("description");
		vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(vibDuration);
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		mp = new MediaPlayer();
		try {
			mp.setDataSource(this, alert);
			final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mp.setAudioStreamType(AudioManager.STREAM_ALARM);
				mp.setLooping(true);
				mp.prepare();
				mp.start();
			}
		} catch (Exception e) {

		}
		setEText(R.id.datetime, datetime);
		setEText(R.id.description, description);
	}

	public void closeAlarm(View view) {
		if (vib != null)
			vib.cancel();
		if (mp != null) {
			mp.stop();
			mp.release();
		}
		finish();
	}
}
