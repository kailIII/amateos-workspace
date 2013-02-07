package com.emp.friskyplayer.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TimeOutSeekBarPreference extends Preference implements
		OnSeekBarChangeListener {

	public static int maximum = 120;
	public static int interval = 5;
	private float defaultValue = 30;
	private TextView valueTextView;

	/*
	 * CONSTRUCTORS
	 */
	public TimeOutSeekBarPreference(Context context) {
		super(context);
	}
	
	public TimeOutSeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimeOutSeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	@Override
	protected View onCreateView(ViewGroup parent) {

		LinearLayout layout = new LinearLayout(getContext());

		
		// Title
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		
		TextView titleTextView = new TextView(getContext());
		titleTextView.setText(getTitle());
		titleTextView.setTextSize(15);
		titleTextView.setGravity(Gravity.LEFT);
		titleTextView.setLayoutParams(params);
		
		//SeekBar
		params = new LinearLayout.LayoutParams(80,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = 0;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.weight = 1.0f;
		
		SeekBar bar = new SeekBar(getContext());
		bar.setMax(maximum);
		bar.setProgress((int) this.defaultValue);
		bar.setLayoutParams(params);
		bar.setOnSeekBarChangeListener(this);
		
		
		// ValueTextView
		params = new LinearLayout.LayoutParams(30,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		layout.setPadding(15, 5, 10, 5);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		this.valueTextView = new TextView(getContext());
		this.valueTextView.setTextSize(12);
		this.valueTextView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
		this.valueTextView.setLayoutParams(params);
		this.valueTextView.setPadding(2, 5, 0, 0);
		this.valueTextView.setText(bar.getProgress() + " mins");

		layout.addView(titleTextView);
		layout.addView(bar);
		layout.addView(this.valueTextView);
		layout.setId(android.R.id.widget_frame);

		return layout;
	}

	
	
	/*
	 * CHANGE LISTENER
	 */
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		progress = (Math.round(((float) progress) / interval) * interval)+5;

		if (!callChangeListener(progress)) {
			seekBar.setProgress((int) this.defaultValue);
			return;
		}

		seekBar.setProgress(progress);
		this.defaultValue = progress;
		this.valueTextView.setText(progress + "");
		updatePreference(progress);

		notifyChanged();

	}

	public void onStartTrackingTouch(SeekBar arg0) {

	}

	public void onStopTrackingTouch(SeekBar arg0) {

	}

	@Override
	protected Object onGetDefaultValue(TypedArray ta, int index) {

		int dValue = (int) ta.getInt(index, 50);

		return validateValue(dValue);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

		int temp = restoreValue ? getPersistedInt(50) : (Integer) defaultValue;

		if (!restoreValue)
			persistInt(temp);

		this.defaultValue = temp;
	}

	private int validateValue(int value) {

		if (value > maximum)
			value = maximum;
		else if (value < 0)
			value = 0;
		else if (value % interval != 0)
			value = Math.round(((float) value) / interval) * interval;

		return value;
	}

	private void updatePreference(int newValue) {

		SharedPreferences.Editor editor = getEditor();
		editor.putInt(getKey(), newValue);
		editor.commit();
	}

}
