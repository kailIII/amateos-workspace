package com.emp.friskyplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.utils.PlayerConstants;
import com.emp.friskyplayer.utils.ServiceActionConstants;

/**
 * Handles the reception of intents from service to activity
 * @author empollica
 *
 */
public class ServiceToGuiCommunicationReceiver extends BroadcastReceiver {

	final static String TAG = "ServiceToGuiCommunicationReceiver";

	private FriskyPlayerActivity activity;
	private TextView titleTextView;
	private ProgressBar bufferProgressBar;

	public ServiceToGuiCommunicationReceiver() {
		super();
	}

	public ServiceToGuiCommunicationReceiver(FriskyPlayerActivity activity) {
		super();
		this.activity = activity;

		IntentFilter filter = new IntentFilter();
		filter.addAction(ServiceActionConstants.STREAM_TITLE);
		activity.registerReceiver(this, filter);

		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ServiceActionConstants.BUFFER_PROGRESS);
		activity.registerReceiver(this, filter2);

		IntentFilter filter3 = new IntentFilter();
		filter3.addAction(ServiceActionConstants.LOADING);
		activity.registerReceiver(this, filter3);

		titleTextView = (TextView) activity
				.findViewById(R.id.bottom_bar_stream_title_textview);
		bufferProgressBar = (ProgressBar) activity
				.findViewById(R.id.bottom_bar_buffer_progressbar);

	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ServiceActionConstants.STREAM_TITLE)) {

			String title = intent.getExtras().getString("title");

			// Sets title on Activity
			titleTextView.setText(title);
			activity.setStreamTitle(title);

			// Sets title on Application object
			((FriskyPlayerApplication) activity.getApplication()).getInstance()
					.setStreamTitle(title);

			// if server is down -> enable stop state.
			if (title.equals(activity.getApplicationContext().getString(
					R.string.frisky_server_down))) {
				activity.setSupportProgressBarIndeterminateVisibility(false);
				((FriskyPlayerApplication) activity.getApplication())
						.getInstance().setPlayerState(
								PlayerConstants.STATE_STOPPED);
				((ImageButton) activity
						.findViewById(R.id.bottom_bar_play_button))
						.setImageDrawable(activity.getResources().getDrawable(
								R.drawable.ic_action_play));
				
				Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
				
			} else if (title.equals("")
					&& ((FriskyPlayerApplication) activity.getApplication())
							.getInstance().getPlayerState() != PlayerConstants.STATE_PLAYING) {
				// Streaming has stopped: change play/button state
				((ImageButton) activity
						.findViewById(R.id.bottom_bar_play_button))
						.setImageDrawable(activity.getResources().getDrawable(
								R.drawable.ic_action_play));
			} else {
				Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
				((ImageButton) activity
						.findViewById(R.id.bottom_bar_play_button))
						.setImageDrawable(activity.getResources().getDrawable(
								R.drawable.ic_action_pause));
			}
		} else if (intent.getAction().equals(
				ServiceActionConstants.BUFFER_PROGRESS)) {

			int bufferProgress = intent.getExtras().getInt("buffer");
			bufferProgressBar.setProgress(bufferProgress);

			if (((FriskyPlayerApplication) activity.getApplication())
					.getInstance().getPlayerState() != PlayerConstants.STATE_PLAYING) {
				activity.startService(new Intent(
						ServiceActionConstants.ACTION_STOP));
			}
		} else if (intent.getAction().equals(ServiceActionConstants.LOADING)) {
			Boolean loading = intent.getExtras().getBoolean("loading");
			activity.setSupportProgressBarIndeterminateVisibility(loading);
		}

	}

	public void unregisterReceiver() {
		activity.unregisterReceiver(this);
	}

}
