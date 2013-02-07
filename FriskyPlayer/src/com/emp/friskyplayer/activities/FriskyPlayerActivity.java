package com.emp.friskyplayer.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.Window;
import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.listeners.PlayButtonOnClickListener;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.receivers.ServiceToGuiCommunicationReceiver;
import com.emp.friskyplayer.services.FriskyService;
import com.emp.friskyplayer.utils.PlayerConstants;

public class FriskyPlayerActivity extends SherlockActivity {

	private ImageButton playPauseButton;
	private ImageButton shareButton;
	private TextView streamTitleTextView;
	private ActionBar ab;
	private ProgressBar bufferProgressBar;

	private ServiceToGuiCommunicationReceiver serviceToGuiReceiver;

	private FriskyService friskyService;
	private ServiceConnection serviceConnection;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setSupportProgressBarIndeterminateVisibility(false);

		serviceConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className,
					IBinder binder) {
				friskyService = ((FriskyService.FriskyServiceBinder) binder)
						.getService();
			}

			public void onServiceDisconnected(ComponentName arg0) {
				friskyService = null;
			}
		};

		doBindService();
		
		Log.d("App", "onCreate ACTIVITY");
	}

	private void doBindService() {
		bindService(new Intent(this, FriskyService.class), serviceConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.d("App", "onResume ACTIVITY");

		setContentView(R.layout.main);

		initActionBar();
		initGuiElements();
		setSupportProgressBarIndeterminateVisibility(false);
		serviceToGuiReceiver = new ServiceToGuiCommunicationReceiver(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister receiver and release resources
		serviceToGuiReceiver.unregisterReceiver();
		serviceToGuiReceiver = null;
	}

	/**
	 * Initiates ActionBar and sets title
	 */
	private void initActionBar() {
		ab = getSupportActionBar();
		String text = "<font color='#d84395'>frisky</font><font color='#cfcdce'>Player</font>";
		ab.setTitle(Html.fromHtml(text));
	}

	/**
	 * Initiates elements of GUI and sets listeners
	 */
	private void initGuiElements() {

		// Gets state and stream title from Application object
		int playerState = ((FriskyPlayerApplication) getApplication())
				.getInstance().getPlayerState();
		String streamTitle = ((FriskyPlayerApplication) getApplication())
				.getInstance().getStreamTitle();

		playPauseButton = (ImageButton) findViewById(R.id.bottom_bar_play_button);
		shareButton = (ImageButton) findViewById(R.id.bottom_bar_share_button);

		// Sets button image based on player state
		if (playerState == PlayerConstants.STATE_STOPPED) {
			playPauseButton.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_action_play));
		} else {
			playPauseButton.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_action_pause));
		}

		playPauseButton.setOnClickListener(new PlayButtonOnClickListener(this,
				playPauseButton));

		shareButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = createShareIntent();
				startActivity(Intent.createChooser(i,
						getResources().getString(R.string.share_question)));
			}
		});

		streamTitleTextView = (TextView) findViewById(R.id.bottom_bar_stream_title_textview);
		streamTitleTextView.setMovementMethod(new ScrollingMovementMethod());
		streamTitleTextView.setSelected(true);
		streamTitleTextView.setText(streamTitle);

		bufferProgressBar = (ProgressBar) findViewById(R.id.bottom_bar_buffer_progressbar);
		bufferProgressBar.setMax(100);
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.menu_share, menu);

		// Set file with share history to the provider and set the share intent.
		MenuItem actionItem = menu.findItem(R.id.menu_share);
		actionItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getApplicationContext(), FriskyPlayerPreferencesActivity.class);
				startActivity(intent);
				return false;
			}
		});
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Creates an Intent to share the streaming using an external app such as:
	 * Twitter, Facebook, Gmail...
	 */
	private Intent createShareIntent() {

		String streamTitle = ((FriskyPlayerApplication) getApplication())
				.getInstance().getStreamTitle();

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide what to do with
		// it.
		shareIntent.putExtra(Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.share_subject));

		// Gets state and stream title from Application object
		int playerState = ((FriskyPlayerApplication) getApplication())
				.getInstance().getPlayerState();
		if (playerState == PlayerConstants.STATE_PLAYING) {
			shareIntent
					.putExtra(
							Intent.EXTRA_TEXT,
							getResources().getString(R.string.share_text_start)
									+ " "
									+ streamTitle
									+ " "
									+ getResources().getString(
											R.string.share_text_end));
		} else {
			shareIntent
					.putExtra(
							Intent.EXTRA_TEXT,
							getResources().getString(R.string.share_text_start)
									+ " FriskyRadio "
									+ getResources().getString(
											R.string.share_text_end));
		}
		return shareIntent;
	}

	/**
	 * Sets StreamTitleTextView text
	 * 
	 * @param streamTitle
	 */
	public void setStreamTitle(String streamTitle) {
		streamTitleTextView.setText(streamTitle);
	}

	/**
	 * Returns binded FriskyService
	 * 
	 * @return
	 */
	public FriskyService getFriskyService() {
		return friskyService;
	}

}