package com.emp.friskyplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.services.FriskyService;

public class ServiceToGuiCommunicationReceiver extends BroadcastReceiver {
	
	private FriskyPlayerActivity activity;
	private TextView titleTextView;
	
	public ServiceToGuiCommunicationReceiver(FriskyPlayerActivity activity) {
		super();
		this.activity = activity;
		
		IntentFilter filter = new IntentFilter();
        filter.addAction(FriskyService.STREAM_TITLE);
        activity.registerReceiver(this, filter);
        
        titleTextView = (TextView) activity.findViewById(R.id.bottom_bar_stream_title_textview);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(FriskyService.STREAM_TITLE)) {
			
			String title = intent.getExtras().getString("title");
            Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
            
            // Sets title on Activity
            titleTextView.setText(title);
            activity.setStreamTitle(title);
            
            // Sets title on Application object
            ((FriskyPlayerApplication) activity.getApplication()).getInstance().setStreamTitle(title);
		}
		
	}

	public void unregisterReceiver (){
		activity.unregisterReceiver(this);
	}
	
}
