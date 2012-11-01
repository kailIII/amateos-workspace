package com.emp.friskyplayer.player;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.services.FriskyService;
import com.emp.friskyplayer.utils.PlayerUtils;
import com.spoledge.aacdecoder.PlayerCallback;

public class FriskyPlayerCallback implements PlayerCallback {

	final static String TAG = "FriskyPlayerCallback";

	private Context context;
	private FriskyService service;

	public FriskyPlayerCallback(Context context, FriskyService service) {
		this.context = context;
		this.service = service;
	}

	/**
	 * This method is called when an exception is thrown by player.
	 */
	public void playerException(Throwable t) {

		Log.e(TAG, "Player exception: " + t.getMessage());

		((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);

		//TODO: descargar el m3u e intentar reproducir de nuevo
		//TODO: mostrar mensaje de error
		//TODO: borra la linea de debajo
		//service.changeState(PlayerUtils.STATE_STOPPED);
		service.relaxResources(true);
		service.giveUpAudioFocus();
	}

	/**
	 * This method is called when metadata frame is received
	 * 
	 * @param key
	 *            represents type of metadata stream
	 * @param value
	 *            the received text
	 */
	public void playerMetadata(String key, String value) {

		if ("StreamTitle".equals(key) || "icy-name".equals(key)
				|| "icy-description".equals(key)) {
			
			Intent i = new Intent();
			i.setAction(FriskyService.STREAM_TITLE);
			i.putExtra("title", value);
			context.sendBroadcast(i);
			service.updateNotification(value);
			Log.d(TAG, "Stream title received: " + value);
			
			//Sets stream title on Application object
			((FriskyPlayerApplication) service.getApplication()).getInstance().setStreamTitle(value);

		}

	}

	/**
	 * This method is called periodically by PCMFeed.
	 * 
	 * @param isPlaying
	 *            false means that the PCM data are being buffered, but the
	 *            audio is not playing yet
	 * 
	 * @param audioBufferSizeMs
	 *            the buffered audio data expressed in milliseconds of playing
	 * @param audioBufferCapacityMs
	 *            the total capacity of audio buffer expressed in milliseconds
	 *            of playing
	 */
	public void playerPCMFeedBuffer(boolean isPlaying, int audioBufferSizeMs,
			int audioBufferCapacityMs) {
		
		if(!isPlaying){
			
			((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_LOADING);
			
			//TODO: borrar la linea de debajo
			//service.changeState(PlayerUtils.STATE_LOADING);
			
			// TODO: implementar el estado loading!
			service.updateNotification(context.getResources().getString(R.string.loading));
		}else{
			((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_PLAYING);

			//TODO: borrar la linea de debajo
			//service.changeState(PlayerUtils.STATE_PLAYING);
		}

	}

	/**
	 * This method is called when the player is started.
	 */
	public void playerStarted() {

		((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_PLAYING);

		//TODO: borrar la linea de debajo
		//service.changeState(PlayerUtils.STATE_PLAYING);

	}

	/**
	 * This method is called when the player is stopped. Note: __after__ this
	 * method the method playerException might be also called.
	 */
	public void playerStopped(int perf) {

		((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);

		//TODO: borrar la linea de debajo	
		//service.changeState(PlayerUtils.STATE_STOPPED);
		service.relaxResources(true);
		service.giveUpAudioFocus();
	}

}
