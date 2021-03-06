package com.mcluck.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.media.AudioManager;
import android.widget.Toast;

public class AndroidVolume extends CordovaPlugin {
	@Override
	public boolean execute(
		String action,
		JSONArray args,
		CallbackContext callbackContext
	) throws JSONException {
		if ("setAlarm".equals(action)) {
			setAlarmVolume(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("setAll".equals(action)) {
			setAllVolumes(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("setDTMF".equals(action)) {
			setDTMFVolume(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("setMusic".equals(action)) {
			setMusicVolume(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("setNotification".equals(action)) {
			setNotificationVolume(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("setRinger".equals(action)) {
			setRingerVolume(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("setSystem".equals(action)) {
			setSystemVolume(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("setVoiceCall".equals(action)) {
			setVoiceCallVolume(args.getDouble(0), args.getBoolean(1), callbackContext);
			return true;
		} else if ("getDTMF".equals(action)) {
			getDTMFVolume(callbackContext);
			return true;
		} else if ("getMusic".equals(action)) {
			getMusicVolume(callbackContext);
			return true;
		} else if ("getNotification".equals(action)) {
			getNotificationVolume(callbackContext);
			return true;
		} else if ("getRinger".equals(action)) {
			getRingerVolume(callbackContext);
			return true;
		} else if ("getSystem".equals(action)) {
			getSystemVolume(callbackContext);
			return true;
		} else if ("getVoiceCall".equals(action)) {
			getVoiceCallVolume(callbackContext);
			return true;
		}

		return false;
	}

	public void setVolume(
		int streamType,
		String volumeType,
		double volume, //changed int to double
		boolean showToast,
		CallbackContext callbackContext
	) {
		AudioManager manager = (AudioManager)this.cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
		int max = manager.getStreamMaxVolume(streamType);
		//double newVolume = volume;
		if (volume >= 0 && volume <=1) { 
			//double percent = (double)volume; //no more divided by 100 as parameter is now 0 to 1
			int newVolume = (int)Math.round(volume * max);
			int volumePerc = (int)Math.round(volume*100);
			manager.setStreamVolume(streamType, newVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			if (showToast) {
				if (volumeType.length() > 0) {
					volumeType += " ";
				}
				Toast.makeText(
					webView.getContext(),
					volumeType + "Volume: "+ volume +" " + volumePerc + " ("+String.valueOf(newVolume)+")",
					Toast.LENGTH_LONG
				).show();
			}
			if (callbackContext != null) {
				callbackContext.success(volumePerc);
			}
			
		} else {
			Toast.makeText(
				webView.getContext(),
				volumeType + " Volume out of range (0-1): " + String.valueOf(volume),
				Toast.LENGTH_LONG
			).show();
		}
	}

	public void getVolume(int streamType, CallbackContext callbackContext) {
		AudioManager manager = (AudioManager)this.cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
		int max = manager.getStreamMaxVolume(streamType);
		int volume = manager.getStreamVolume(streamType);
		//if (volume != 0) {
			double percent = volume / (double)max;
			int volumePerc = (int)Math.round(percent*100); //not multiplied by 100 as now is 0-1
		//}
		callbackContext.success(volumePerc);
	}

	public void setAllVolumes(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_ALARM, "", volume, false, null);
		setVolume(AudioManager.STREAM_DTMF, "", volume, false, null);
		setVolume(AudioManager.STREAM_MUSIC, "", volume, false, null);
		setVolume(AudioManager.STREAM_NOTIFICATION, "", volume, false, null);
		setVolume(AudioManager.STREAM_RING, "", volume, false, null);
		setVolume(AudioManager.STREAM_SYSTEM, "", volume, false, null);
		setVolume(AudioManager.STREAM_VOICE_CALL, "", volume, showToast, callbackContext);
	}

	public void getAlarmVolume(CallbackContext callbackContext) {
		getVolume(AudioManager.STREAM_ALARM, callbackContext);
	}

	public void setAlarmVolume(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_ALARM, "Alarm", volume, showToast, callbackContext);
	}

	public void getDTMFVolume(CallbackContext callbackContext) {
		getVolume(AudioManager.STREAM_DTMF, callbackContext);
	}

	public void setDTMFVolume(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_DTMF, "DTMF", volume, showToast, callbackContext);
	}

	public void getMusicVolume(CallbackContext callbackContext) {
		getVolume(AudioManager.STREAM_MUSIC, callbackContext);
	}

	public void setMusicVolume(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_MUSIC, "Music", volume, showToast, callbackContext);
	}

	public void getNotificationVolume(CallbackContext callbackContext) {
		getVolume(AudioManager.STREAM_NOTIFICATION, callbackContext);
	}

	public void setNotificationVolume(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_NOTIFICATION, "Notification", volume, showToast, callbackContext);
	}

	public void getRingerVolume(CallbackContext callbackContext) {
		getVolume(AudioManager.STREAM_RING, callbackContext);
	}

	public void setRingerVolume(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_RING, "Ringer", volume, showToast, callbackContext);
	}

	public void getSystemVolume(CallbackContext callbackContext) {
		getVolume(AudioManager.STREAM_SYSTEM, callbackContext);
	}

	public void setSystemVolume(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_SYSTEM, "System", volume, showToast, callbackContext);
	}

	public void getVoiceCallVolume(CallbackContext callbackContext) {
		getVolume(AudioManager.STREAM_VOICE_CALL, callbackContext);
	}

	public void setVoiceCallVolume(
		int volume,
		boolean showToast,
		CallbackContext callbackContext
	) {
		setVolume(AudioManager.STREAM_VOICE_CALL, "Voice Call", volume, showToast, callbackContext);
	}
}
