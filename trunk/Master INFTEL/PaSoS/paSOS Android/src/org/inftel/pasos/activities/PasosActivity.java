package org.inftel.pasos.activities;

import java.util.Observable;
import java.util.Observer;

import org.inftel.pasos.R;
import org.inftel.pasos.controlador.Controlador;
import org.inftel.pasos.modelo.Modelo;
import org.inftel.pasos.receiver.ProximityIntentReceiver;
import org.inftel.pasos.receiver.SMS_Receiver;
import org.inftel.pasos.utils.Utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class PasosActivity extends Activity implements Observer,
		TextToSpeech.OnInitListener {

	private static final String TAG = PasosActivity.class.getSimpleName();
	private int nivelBateria;
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1;
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000;

	private static final long EXPIRATION = -1;

	private static final String PROX_ALERT_INTENT = "org.inftel.pasos.receiver.ProximityAlert";
	private static final String SMS_RECEIVER_INTENT = "android.provider.Telephony.SMS_RECEIVED";

	private Modelo modelo;
	private Controlador controlador;

	private LocationManager locationManager;
	private MyLocationListener myLocationListener;

	private PendingIntent proximityIntent;
	private Intent intent;

	private SMS_Receiver sms_Receiver;
	private ProximityIntentReceiver proximityIntentReceiver;

	// TTS
	private TextToSpeech tts;
	private static final int TTS_CHECK_CODE = 1234;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initModeloControlador();

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TTS_CHECK_CODE);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE,
//				new MyLocationListener());

		IntentFilter filter = new IntentFilter(SMS_RECEIVER_INTENT);
		sms_Receiver = new SMS_Receiver(this);
		registerReceiver(sms_Receiver, filter);
		this.registerReceiver(this.infoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		ImageButton button = (ImageButton) findViewById(R.id.imageButton1);

		// Si hay conexi—n a Internet -> se asigna listener para enviar alarmas
		if (Utils.comprobarConexionInternet(getBaseContext())) {
			Log.d(TAG, "HAY CONEXION");
			button.setOnLongClickListener(new OnLongClickListener() {

				public boolean onLongClick(View v) {
					sendFrame();
					return true;
				}
			});
		} else { // Si no hay conexi—n a Internet -> se notifica
			Log.d(TAG, "NO HAY CONEXION");
			Toast.makeText(this, getString(R.string.no_conexion),
					Toast.LENGTH_LONG).show();

			if (controlador.getNotifVoz()) {
				tts.speak(getString(R.string.no_conexion),
						TextToSpeech.QUEUE_FLUSH, null);
			}
			if (controlador.getNotifVibracion()) {
				Utils.vibracion(getBaseContext(), 2);
			}
		}

	}

	@Override
	public void onDestroy() {
		//locationManager.removeProximityAlert(proximityIntent);
		//unregisterReceiver(sms_Receiver);
		//unregisterReceiver(proximityIntentReceiver);

		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();

	}

	private BroadcastReceiver infoReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			nivelBateria = intent.getIntExtra("level", 0);
			Log.d(TAG,"NIVEL BATERIA -> "+nivelBateria);
		}
	};

	// MENU
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		switch (item.getItemId()) {
		case R.id.menu_configuracion:

			intent = new Intent(this, PreferenciasActivity.class);
			startActivity(intent);

			return true;

		case R.id.menu_contactos:

			intent = new Intent(this, ContactosActivity.class);
			startActivity(intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendFrame() {

		Toast.makeText(this, getString(R.string.alarma_enviando),
				Toast.LENGTH_LONG).show();

		if (controlador.getNotifVoz()) {
			tts.speak(getString(R.string.alarma_enviando),
					TextToSpeech.QUEUE_FLUSH, null);
		}

		if (controlador.getNotifVibracion()) {
			Utils.vibracion(getBaseContext(), 0);
		}

		String location = Utils.currentLocation(this.getBaseContext());
		String fechaHora = Utils.getDateHour();
		String imei = Utils.getIMEI(this.getBaseContext());
		String trama = "$AU11" + fechaHora + location + imei+"&PB"+nivelBateria;
		Log.d(TAG, trama);

		boolean envio = Utils.sendMessage(trama, this);

		if (envio) {// La alarma se ha enviado con Žxito

			Toast.makeText(this, getString(R.string.alarma_enviada_exito),
					Toast.LENGTH_LONG).show();

			if (controlador.getNotifVoz()) {
				tts.speak(getString(R.string.alarma_enviada_exito),
						TextToSpeech.QUEUE_FLUSH, null);
			}
			if (controlador.getNotifVibracion()) {
				Utils.vibracion(getBaseContext(), 1);
			}
		} else { // La alarma no ha sido enviada

			Toast.makeText(this, getString(R.string.alarma_enviada_fallo),
					Toast.LENGTH_LONG).show();

			if (controlador.getNotifVoz()) {
				tts.speak(getString(R.string.alarma_enviada_fallo),
						TextToSpeech.QUEUE_FLUSH, null);
			}
			if (controlador.getNotifVibracion()) {
				Utils.vibracion(getBaseContext(), 2);
			}
		}

	}

	public void sendFrame(String type) {

		String location = Utils.currentLocation(this.getBaseContext());
		String fechaHora = Utils.getDateHour();
		String imei = Utils.getIMEI(this.getBaseContext());
		String trama = type + fechaHora + location + imei;
		Log.d(TAG, trama);
		Utils.sendMessage(trama, this.getBaseContext());

	}

	public void saveProximityAlertPoint(double longitude, double latitude,
			double radio) {

		if (proximityIntent != null) {
			Log.d(getClass().getSimpleName(), "removeProximityAlert");
			locationManager.removeProximityAlert(proximityIntent);
		} else {
			IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
			proximityIntentReceiver = new ProximityIntentReceiver();
			registerReceiver(proximityIntentReceiver, filter);
		}

		intent = new Intent(PROX_ALERT_INTENT);
		proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		Log.d(getClass().getSimpleName(), "configurando proximityAlert");
		locationManager.addProximityAlert(latitude, longitude, (float) radio,
				EXPIRATION, proximityIntent);

	}

	public void initiateTrackingProcess(Long minimunTimeBetweenUpdate) {
		Log.d(getClass().getSimpleName(),"Iniciando tracking process");
		myLocationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				minimunTimeBetweenUpdate, MINIMUM_DISTANCECHANGE_FOR_UPDATE,
				myLocationListener);
	}

	public void desactivateTrackingProcess() {
		locationManager.removeUpdates(myLocationListener);
	}

	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			sendFrame("$TE");
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		}

		public void onProviderDisabled(String s) {
		}

		public void onProviderEnabled(String s) {
		}
	}

	/**
	 * Inicializa el modelo y establece el controlador
	 */
	private void initModeloControlador() {

		modelo = new Modelo(this.getBaseContext());
		modelo.addObserver(this);
		this.controlador = new Controlador(modelo);
		Log.d(TAG, "Modelo y controlador establecidos");
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}

	public void onInit(int status) {

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TTS_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				tts = new TextToSpeech(this, this);
			} else { // TTS no est‡ instalado en el dispositivo -> Instalar
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

}
