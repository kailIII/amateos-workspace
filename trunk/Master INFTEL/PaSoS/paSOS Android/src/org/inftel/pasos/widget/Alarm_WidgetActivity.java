package org.inftel.pasos.widget;


import org.inftel.pasos.R;
import org.inftel.pasos.utils.Utils;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Alarm_WidgetActivity extends AppWidgetProvider {
	
	RemoteViews remoteViews;
	AppWidgetManager appWidgetManager;
	ComponentName thisWidget;
	private static final String ACTION = "sendAlarm";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		this.appWidgetManager = appWidgetManager;
		remoteViews = new RemoteViews(context.getPackageName(),R.layout.miwidget);
		thisWidget = new ComponentName(context, Alarm_WidgetActivity.class);

	
		Intent intent = new Intent(context, Alarm_WidgetActivity.class); 
		intent.setAction(ACTION); 

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.LinearLayout01, pendingIntent);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			
			Toast.makeText(context, "Mensaje Enviado", Toast.LENGTH_LONG).show();
			Log.d(getClass().getSimpleName(), "Enviando señal de alarma");
			
			String location = Utils.currentLocation(context);
			String fechaHora = Utils.getDateHour();
			String imei = Utils.getIMEI(context);
			String trama = "$AU11" + fechaHora + location + imei;
			boolean envio = Utils.sendMessage(trama, context);
			
		}

		super.onReceive(context, intent);
	}
	private Location currentLocation(Context context){
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = 
	            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        if (location==null) {
	        	Log.d(getClass().getSimpleName(),"No hay localizacion conocida.Abortando...");
	            return null;
	        }
	     return location;  
	}
	private String getIMEI(Context context){   	
    	TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	String IMEI = tm.getDeviceId();
    	return IMEI;
    }
	
}
