package com.emp.friskyplayer.async;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.utils.PreferencesConstants;
import com.emp.friskyplayer.utils.ServiceActionConstants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Downloads M3U file from www.friskyradio.com and tries to reconnect using new
 * url
 * 
 * @author empollica
 * 
 */
public class FriskyM3UDownloaderThread extends Thread {

	final static String TAG = "M3U";
	private String friskyStreamingUrl;
	private Context mContext;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public FriskyM3UDownloaderThread(Context context) {
		super();
		this.mContext = context;
		friskyStreamingUrl = null;
	}

	/**
	 * Run method
	 */
	public void run() {

		// Notify activity
		Intent i = new Intent();
		i.setAction(ServiceActionConstants.STREAM_TITLE);
		i.putExtra(
				"title",
				mContext.getApplicationContext().getString(
						R.string.frisky_server_reconnect));
		mContext.sendBroadcast(i);

		boolean downloaded = downloadM3u();

		String storedUrl = PreferenceManager.getDefaultSharedPreferences(
				mContext).getString(PreferencesConstants.STREAMING_URL,
				PreferencesConstants.STREAMING_URL_DEFAULT);
		if (downloaded && friskyStreamingUrl != null
				&& !friskyStreamingUrl.equals(storedUrl)) {

				// Store new URL
				SharedPreferences.Editor editor = PreferenceManager
						.getDefaultSharedPreferences(mContext).edit();
				editor.putString(PreferencesConstants.STREAMING_URL,
						friskyStreamingUrl);
				editor.commit();
				Log.d(TAG, "M3U: stored new url: " + friskyStreamingUrl);

				// Restart service
				mContext.startService(new Intent(
						ServiceActionConstants.ACTION_STOP));
				mContext.startService(new Intent(
						ServiceActionConstants.ACTION_PLAY));
				Log.d(TAG, "M3U: trying to reconnect to new url: "
						+ friskyStreamingUrl);

		} else {
			Log.d(TAG, "URL is not valid");

			// Stop service
			mContext.startService(new Intent(ServiceActionConstants.ACTION_STOP));

			// Notify activity
			i = new Intent();
			i.setAction(ServiceActionConstants.STREAM_TITLE);
			i.putExtra(
					"title",
					mContext.getApplicationContext().getString(
							R.string.frisky_server_down));
			mContext.sendBroadcast(i);
		}

	}

	/**
	 * Downloads Frisky Radio AAC m3u file and get streaming server url
	 * 
	 * @return true when success or false when fail
	 */
	private boolean downloadM3u() {
		URL website = null;
		String friskyUrl = "";
		boolean result = false;

		try {
			website = new URL("http://www.friskyradio.com/frisky_aac.m3u");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ReadableByteChannel inChannel = null;
		try {
			inChannel = Channels.newChannel(website.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "M3U: www.friskyradio.com is down");
		}

		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = 0;
		try {
			bytesRead = inChannel.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (bytesRead != -1) {

			// System.out.println("Read " + bytesRead);
			buf.flip();

			while (buf.hasRemaining()) {

				char c = (char) buf.get();
				friskyUrl += c;
				// System.out.print(c);
			}

			buf.clear();
			try {
				bytesRead = inChannel.read(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		friskyUrl = friskyUrl.split("\n")[0];
		if (friskyUrl != null && friskyUrl != "") {

			try {
				// Checks if url is well formed
				URL url = new URL(friskyUrl);

				friskyStreamingUrl = friskyUrl;
				result = true;
				Log.d(TAG, "M3U: downloaded url: " + friskyUrl);
			} catch (MalformedURLException e) {
				Log.d(TAG, "M3U: downloaded url was not valid");
			}
			result = true;
		} else {
			Log.d(TAG, "M3U: downloaded url was NULL");
		}

		return result;
	}
}
