package com.emp.friskyplayer.widget;

import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.utils.PlayerConstants;
import com.emp.friskyplayer.utils.PreferencesConstants;
import com.emp.friskyplayer.utils.ServiceActionConstants;

public class WidgetProvider extends AppWidgetProvider {

	public static String ACTION_WIDGET_PLAY_PAUSE_BUTTON_PRESSED = "play_pause_button_pressed";
	public static String ACTION_WIDGET_START_ACTIVITY = "start_activity";


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d("widget", "onUpdate method called");

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		
		// Gets Streaming title and state from preferences
		String streamingTitle = PreferenceManager.getDefaultSharedPreferences(context).getString(PreferencesConstants.STREAMING_TITLE, "");
		int state = PreferenceManager.getDefaultSharedPreferences(context).getInt(PreferencesConstants.PLAYER_STATE, PlayerConstants.STATE_STOPPED);
		
		// Update view values
		remoteViews.setTextViewText(R.id.widgetTitleTextView,streamingTitle);
		if (state == PlayerConstants.STATE_STOPPED){
			remoteViews.setImageViewResource(R.id.widgetPlayPauseImageView, R.drawable.ic_action_play);
		}else{
			remoteViews.setImageViewResource(R.id.widgetPlayPauseImageView, R.drawable.ic_action_pause);
		}
		
		
		
		// Views intent
		Intent playPauseButtonIntent = new Intent(context, WidgetProvider.class);
		playPauseButtonIntent
				.setAction(ACTION_WIDGET_PLAY_PAUSE_BUTTON_PRESSED);
		PendingIntent buttonPendingIntent = PendingIntent.getBroadcast(context,
				0, playPauseButtonIntent, 0);

		Intent startActivityIntent = new Intent(context, WidgetProvider.class);
		startActivityIntent.setAction(ACTION_WIDGET_START_ACTIVITY);
		PendingIntent startActivityPendingIntent = PendingIntent.getBroadcast(context,
				0, startActivityIntent, 0);

		// Sets play/pause button onClick listener
		remoteViews.setOnClickPendingIntent(R.id.widgetPlayPauseImageView,
				buttonPendingIntent);

		// Sets other views onClick listener
		remoteViews.setOnClickPendingIntent(R.id.widgetLogoImageView,
				startActivityPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widgetTitleTextView,
				startActivityPendingIntent);

		// "Commit" updates
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d("widget", "onReceive method called");

		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			// check, if play/pause button was clicked
			if (intent.getAction().equals(
					ACTION_WIDGET_PLAY_PAUSE_BUTTON_PRESSED)) {
				
				Log.d("widget", "onReceive: BUTTON PRESSED");
				context.startService(new Intent(ServiceActionConstants.ACTION_TOGGLE_PLAYBACK));

			} else if (intent.getAction().equals(ACTION_WIDGET_START_ACTIVITY)) {
				Log.d("widget", "onReceive: START ACTIVITY");
				// Starts FriskyPlayer activity
				Intent i = new Intent(context, FriskyPlayerActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		}
	}
}
