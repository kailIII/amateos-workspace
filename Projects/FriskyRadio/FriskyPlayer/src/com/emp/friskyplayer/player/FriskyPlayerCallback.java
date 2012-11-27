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

	// Integer buffer to store buffer values
	private int[] bufferProgressValues;
	private int bufferProgressValueIndex;
	private int bufferProgressSize = 5;

	
	public FriskyPlayerCallback(Context context, FriskyService service) {
		this.context = context;
		this.service = service;
		
		this.bufferProgressValues = new int[bufferProgressSize];
		this.bufferProgressValueIndex = 0;
	}

	/**
	 * This method is called when an exception is thrown by player.
	 */
	public void playerException(Throwable t) {

		Log.e(TAG, "Player exception: " + t.getMessage());

		((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);

		//TODO: descargar el m3u e intentar reproducir de nuevo
		//TODO: mostrar mensaje de error
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
		
		Log.d(TAG, "data received");
		
		if(!isPlaying){
			
			((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_LOADING);
			
			// TODO: implementar el estado loading!
			service.updateNotification(context.getResources().getString(R.string.loading));
			Intent i = new Intent();
			i.setAction(FriskyService.LOADING);
			i.putExtra("loading", true);
			context.sendBroadcast(i);
			
			Log.d(TAG,"Loading state");
			
		}else{
			
			Intent i = new Intent();
			i.setAction(FriskyService.LOADING);
			i.putExtra("loading", false);
			context.sendBroadcast(i);
			
			((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_PLAYING);

			int bufferValue = (audioBufferSizeMs*100)/audioBufferCapacityMs;
			
			bufferProgressValues[bufferProgressValueIndex] = bufferValue;
			bufferProgressValueIndex ++;
			
			if(bufferProgressValueIndex == bufferProgressSize-1){ //buffer is filled
				
				// calculate ponderate mean of buffer progress values
				int a = 0;
				int b = 0;
				for(int v = 1; v < bufferProgressSize; v++){
					a += v*bufferProgressValues[v-1];
					b += v;
				}
				
				int bufferProgress = a/b;
				
				// Broadcast bufferProgress
				Intent i2 = new Intent();
				i2.setAction(FriskyService.BUFFER_PROGRESS);
				i2.putExtra("buffer", bufferProgress);
				context.sendBroadcast(i2);
				
				
				bufferProgressValueIndex = 0;
				
			}
			
		}

	}

	/**
	 * This method is called when the player is started.
	 */
	public void playerStarted() {

		((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_PLAYING);
		Intent i = new Intent();
		i.setAction(FriskyService.LOADING);
		i.putExtra("loading", false);
		context.sendBroadcast(i);
	}

	/**
	 * This method is called when the player is stopped. Note: __after__ this
	 * method the method playerException might be also called.
	 */
	public void playerStopped(int perf) {

		((FriskyPlayerApplication) service.getApplication()).getInstance().setPlayerState(PlayerUtils.STATE_STOPPED);
		service.relaxResources(true);
		service.giveUpAudioFocus();
		
		Intent i = new Intent();
		i.setAction(FriskyService.BUFFER_PROGRESS);
		i.putExtra("buffer", 0);
		context.sendBroadcast(i);
		
		Intent i2 = new Intent();
		i2.setAction(FriskyService.LOADING);
		i2.putExtra("loading", false);
		context.sendBroadcast(i2);
	}

}
