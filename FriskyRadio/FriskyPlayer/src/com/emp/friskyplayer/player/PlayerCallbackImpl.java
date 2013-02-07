package com.emp.friskyplayer.player;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.async.FriskyM3UDownloaderThread;
import com.emp.friskyplayer.services.FriskyService;
import com.emp.friskyplayer.utils.PlayerConstants;
import com.emp.friskyplayer.utils.ServiceActionConstants;
import com.emp.friskyplayer.widget.WidgetUpdaterHelper;
import com.spoledge.aacdecoder.PlayerCallback;

public class PlayerCallbackImpl implements PlayerCallback {

	final static String TAG = "FriskyPlayerCallback";

	private Context context;
	private FriskyService service;

	// Integer buffer to store buffer values
	private int[] bufferProgressValues;
	private int bufferProgressValueIndex;
	private int bufferProgressSize = 5;

	public PlayerCallbackImpl(Context context, FriskyService service) {
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

		if (t instanceof java.lang.NullPointerException) {
			// Try to reconnect downloading m3u file
			new FriskyM3UDownloaderThread(context).start();
		} else {
			((FriskyPlayerApplication) service.getApplication()).getInstance()
					.setPlayerState(PlayerConstants.STATE_STOPPED);
			service.relaxResources(true);
			service.giveUpAudioFocus();
		}
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
			i.setAction(ServiceActionConstants.STREAM_TITLE);
			i.putExtra("title", value);
			context.sendBroadcast(i);
			service.updateNotification(value);
			Log.d(TAG, "Stream title received: " + value);

			// Sets stream title on Application object
			((FriskyPlayerApplication) service.getApplication()).getInstance()
					.setStreamTitle(value);
			
			// Updates MediaButton title
			service.updateMediaButtonTitle(value);

			// Updates Widget
			int state = ((FriskyPlayerApplication) service.getApplication()).getInstance().getPlayerState();
			WidgetUpdaterHelper.getInstance(context).updateWidgetInfo(state, value);
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

		if (!isPlaying) { // Loading state

			service.updateNotification(context.getResources().getString(
					R.string.loading));
			Intent i = new Intent();
			i.setAction(ServiceActionConstants.LOADING);
			i.putExtra("loading", true);
			context.sendBroadcast(i);

		} else {

			int playerState = ((FriskyPlayerApplication) service
					.getApplication()).getInstance().getPlayerState();

			if (playerState == PlayerConstants.STATE_LOADING
					|| playerState == PlayerConstants.STATE_STOPPED) {

				Intent i = new Intent();
				i.setAction(ServiceActionConstants.LOADING);
				i.putExtra("loading", false);
				context.sendBroadcast(i);
			}

			int bufferValue = (audioBufferSizeMs * 100) / audioBufferCapacityMs;

			bufferProgressValues[bufferProgressValueIndex] = bufferValue;
			bufferProgressValueIndex++;

			if (bufferProgressValueIndex == bufferProgressSize - 1) { // buffer
																		// is
																		// filled

				// calculate ponderate mean of buffer progress values
				int a = 0;
				int b = 0;
				for (int v = 1; v < bufferProgressSize; v++) {
					a += v * bufferProgressValues[v - 1];
					b += v;
				}

				int bufferProgress = a / b;

				// Broadcast bufferProgress
				Intent i2 = new Intent();
				i2.setAction(ServiceActionConstants.BUFFER_PROGRESS);
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

		((FriskyPlayerApplication) service.getApplication()).getInstance()
				.setPlayerState(PlayerConstants.STATE_PLAYING);

		Intent i = new Intent();
		i.setAction(ServiceActionConstants.LOADING);
		i.putExtra("loading", false);
		context.sendBroadcast(i);
	}

	/**
	 * This method is called when the player is stopped. Note: __after__ this
	 * method the method playerException might be also called.
	 */
	public void playerStopped(int perf) {

		((FriskyPlayerApplication) service.getApplication()).getInstance()
				.setPlayerState(PlayerConstants.STATE_STOPPED);
		
		((FriskyPlayerApplication) service.getApplication()).getInstance()
		.setStreamTitle("");
		service.relaxResources(true);
		service.giveUpAudioFocus();

		Intent i = new Intent();
		i.setAction(ServiceActionConstants.BUFFER_PROGRESS);
		i.putExtra("buffer", 0);
		context.sendBroadcast(i);

		Intent i2 = new Intent();
		i2.setAction(ServiceActionConstants.LOADING);
		i2.putExtra("loading", false);
		context.sendBroadcast(i2);
		
		// Updates Widget
		WidgetUpdaterHelper.getInstance(context).updateWidgetInfo(PlayerConstants.STATE_STOPPED, "");
	}

}
