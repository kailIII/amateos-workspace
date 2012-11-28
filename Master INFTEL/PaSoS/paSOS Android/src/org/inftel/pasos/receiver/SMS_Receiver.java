package org.inftel.pasos.receiver;

import org.inftel.pasos.activities.PasosActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMS_Receiver extends BroadcastReceiver {

	PasosActivity pasosActivity;

	public SMS_Receiver(PasosActivity pasosActivity) {
		this.pasosActivity = pasosActivity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		String LN = "";
		String LT = "";
		String LR = "";

		Log.w("Recibiendo mensaje", "mensajeeeeee");
		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage SMS[] = new SmsMessage[messages.length];
		for (int n = 0; n < messages.length; n++) {
			SMS[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}

		if (SMS[0].getOriginatingAddress().equals("666")) {
			String bodySMS = SMS[0].getMessageBody();

			// $ZCid:xyz&LN2122050457&LT137253204&LR1000
			String[] trozos = bodySMS.split("&");

			if (trozos[0].contains("$ZC")) {

				for (int i = 1; i < trozos.length; i++) {

					if (trozos[i].substring(0, 2).equals("LN")) {
						LN = trozos[i].substring(2);
					} else if (trozos[i].substring(0, 2).equals("LT")) {
						LT = trozos[i].substring(2);
					} else if (trozos[i].substring(0, 2).equals("LR")) {
						LR = trozos[i].substring(2);
					} else if (trozos[i].substring(0, 2).equals("RI")) {
						//saveAlarmCenterIP(trozos[i].substring(2), context);
					}

				}
			} else if (trozos[0].contains("$TR91")) {
				//$TRdd:sss
				//$TR91:30
				trozos = trozos[0].split(":");				
				Long minimunTimeBetweenUpdate= Long.parseLong(trozos[1]);
				pasosActivity.initiateTrackingProcess(minimunTimeBetweenUpdate);				
				
			}else if (trozos[0].contains("$TS00")) {
				//$TSdd
				//$TS00
				pasosActivity.desactivateTrackingProcess();				
				
			}
			

			if (!LN.equals("") && !LR.equals("") && !LT.equals("")) {

				Log.d(getClass().getSimpleName(),
						"Longitud: " + Location.convert(format(LN)));
				Log.d(getClass().getSimpleName(),
						"Latitud: " + Location.convert(format(LT)));
				Log.d(getClass().getSimpleName(),
						"Radio: " + Float.parseFloat(LR));
				pasosActivity.saveProximityAlertPoint(
						Location.convert(format(LN)),
						Location.convert(format(LT)), Float.parseFloat(LR));
				// proxAlertActivity.saveProximityAlertPoint(null);
			}

		}

	}

	private String format(String frame) {

		String signe = "";
		String degrees = "";
		String minutes = "";
		String seconds = "";
		String coordinate;
		Float tenThousandthsMin;

		if (frame.startsWith("1")) {
			signe = "";
		} else {
			signe = "-";
		}

		if (frame.length() == 10) {
			degrees = frame.substring(1, 4);
			minutes = frame.substring(4, 6);
			tenThousandthsMin = Float.parseFloat(frame.substring(6));
			seconds = Float.toString((float) (tenThousandthsMin * 0.006));

		} else if (frame.length() == 9) {
			degrees = "0" + frame.substring(1, 3);
			minutes = frame.substring(3, 5);
			tenThousandthsMin = Float.parseFloat(frame.substring(5));
			seconds = Float.toString((float) (tenThousandthsMin * 0.006));
		}
		coordinate = signe + degrees + ":" + minutes + ":" + seconds;

		return coordinate;
	}

	private void saveAlarmCenterIP(String ip, Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				"ConfigurationSendMessage", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("IP", ip);
		editor.commit();
	}

}
