package com.emp.friskyplayer.utils;

import java.util.Observable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

public class SleepModeManager extends Observable {

	private boolean sleepModeIsRunning;
	private CountDownTimer sleepModeCountdownTimer;
	private int countdownValue;
	private long minsToZero;
	private long secsToZero;
	private Context context;

	public SleepModeManager(Context context) {
		super();
		sleepModeIsRunning = false;
		this.context = context;
	}

	/**
	 * Enables sleep mode
	 */
	public boolean enableSleepMode(int countdownValue) {
		
		if (!sleepModeIsRunning) {

			sleepModeCountdownTimer = new CountDownTimer(
					countdownValue * 60 * 1000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					setMinsToZero((millisUntilFinished / 1000) / 60);
					setSecsToZero((millisUntilFinished / 1000) % 60);
				}

				@Override
				public void onFinish() {
		            context.startService(new Intent(ServiceActionConstants.ACTION_STOP));
					disableSleepMode();
					setSleepModeIsRunning(false);
					Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
					editor.putBoolean(PreferencesConstants.SLEEP_MODE, false);
					editor.commit();
					setMinsToZero(0);
					setSecsToZero(0);
				}
			};
			sleepModeCountdownTimer.start();
			
			setSleepModeIsRunning(true);
		}
		return sleepModeIsRunning;
	}

	/**
	 * Disables sleep mode
	 */
	public boolean disableSleepMode() {

		if (sleepModeIsRunning) {
			if(sleepModeCountdownTimer != null){
				sleepModeCountdownTimer.cancel();
			}
			setSleepModeIsRunning(false);
		}
		return sleepModeIsRunning;
	}

	public long getSecsToZero() {
		return secsToZero;
	}

	public void setSecsToZero(long secsToZero) {
		this.secsToZero = secsToZero;
		setChanged();
		notifyObservers();
	}

	public boolean isSleepModeIsRunning() {
		return sleepModeIsRunning;
	}
	
	public void setSleepModeIsRunning(boolean sleepModeIsRunning) {
		this.sleepModeIsRunning = sleepModeIsRunning;
	}

	public long getMinsToZero() {
		return minsToZero;
	}

	public void setMinsToZero(long minsToZero) {
		this.minsToZero = minsToZero;
		setChanged();
		notifyObservers();
	}
}
