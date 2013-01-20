package com.emp.friskyplayer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.utils.PlayerConstants;

public class WidgetUpdaterHelper {

	private static WidgetUpdaterHelper instance = null;
	private Context ctx;

	protected WidgetUpdaterHelper(Context _ctx) {
		this.ctx = _ctx;
	}

	public static WidgetUpdaterHelper getInstance(Context _ctx) {
		if (instance == null) {
			instance = new WidgetUpdaterHelper(_ctx);
		}
		return instance;
	}

	public void updateWidgetInfo(int state, String title) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);

		ComponentName thisWidget = new ComponentName(ctx, WidgetProvider.class);
		int[] ids = appWidgetManager.getAppWidgetIds(thisWidget);

		for (int widgetId : ids) {

			RemoteViews remoteViews = new RemoteViews(
					this.ctx.getPackageName(), R.layout.widget_layout);
			// Sets view values
			remoteViews.setTextViewText(R.id.widgetTitleTextView,title);
			if (state == PlayerConstants.STATE_STOPPED){
				remoteViews.setImageViewResource(R.id.widgetPlayPauseImageView, R.drawable.ic_action_play);
			}else{
				remoteViews.setImageViewResource(R.id.widgetPlayPauseImageView, R.drawable.ic_action_pause);
			}

			// Register an onClickListener
			Intent clickIntent = new Intent(this.ctx, WidgetProvider.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0,
					clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widgetTitleTextView, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}
