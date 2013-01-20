package com.emp.friskyplayer.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.emp.friskyplayer.utils.ConnectionUtils;
import com.emp.friskyplayer.utils.PlayerConstants;
import com.emp.friskyplayer.utils.PreferencesConstants;

public class FriskyPlayerApplication extends Application {
	
	final static String TAG = "FriskyPlayerApplication";

	private FriskyPlayerApplication application;
	private SharedPreferences mPrefs;
	private int playerState;
	private String streamTitle;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		application = this;
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		// Sets playerState to STOPPED by default
		playerState = PlayerConstants.STATE_STOPPED;
		
		// Sets streamTitle to "" by default
		streamTitle = "";
		
		// Sets Network connection type
		Editor editor = mPrefs.edit();
		editor.putString(PreferencesConstants.CONNECTION_TYPE, ConnectionUtils.getNetworkType(this));
		editor.commit();
	}

	

	public FriskyPlayerApplication getInstance() {
		return application;
	}

	public synchronized int getPlayerState() {
		return playerState;
	}

	public synchronized void setPlayerState(int playerState) {
		this.playerState = playerState;
		Editor editor = mPrefs.edit();
		editor.putInt(PreferencesConstants.PLAYER_STATE, playerState);
		editor.commit();
	}

	public String getStreamTitle() {
		return streamTitle;
	}

	public void setStreamTitle(String streamTitle) {
		this.streamTitle = streamTitle;
		Editor editor = mPrefs.edit();
		editor.putString(PreferencesConstants.STREAMING_TITLE, streamTitle);
		editor.commit();
	}

}
