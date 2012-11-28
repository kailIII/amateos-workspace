package org.inftel.scrum.services;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.activities.MensajesFragment;
import org.inftel.scrum.activities.ScroidActivity;
import org.inftel.scrum.modelXML.Mensajes;
import org.inftel.scrum.modelXML.Usuario;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServicioMensajes extends Service implements Runnable {

	public static final int APP_ID_NOTIFICATION = 0;
	private static final int NOTIF_ALERTA_ID = 1;
	private NotificationManager mManager;

	Usuario usuario;
	Mensajes mensajes;

	/**
	 * MŽtodo del hilo as’ncrono, que obtiene un numero aleatorio y comprueba su
	 * paridad
	 */
	public void run() {
		buscarMensajes();		
	}

	/**
	 * prepara y lanza la notificacion
	 */
	private void Notificar(Mensajes mensaje) {

		// Prepara la actividad que se abrira cuando el usuario pulse la
		// notificacion
		//Intent intentNot = new Intent(this, ScroidActivity.class);

		Log.d("info", "notificacion ");
		Log.d("info", "Notificacion Recibida:" + mensaje.getId().toString()
				+ ", " + mensaje.getMensaje());

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notManager = (NotificationManager) getSystemService(ns);

		// Configuramos la notificaciï¿½n
		CharSequence textoEstado = "Mensaje "+ mensaje.getDestino().getGrupoId().getNombre();
		long hora = System.currentTimeMillis();

		Notification notif = new Notification(R.drawable.ic_launcher, textoEstado, hora);

		// Configuramos el Intent
		Context contexto = getApplicationContext();
		CharSequence titulo = mensaje.getOrigen().getNombre() + ":"; 
		Log.d("info","orignnn:"+mensaje.getOrigen().getNombre());
		
		
		Calendar cal= Calendar.getInstance();
		cal.setTime(mensaje.getFecha());
		
		CharSequence descripcion = "("+ cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR)+"): "+ mensaje.getAsunto();

		Intent notIntent = new Intent(contexto, ScroidActivity.class);

		PendingIntent contIntent = PendingIntent.getActivity(contexto, 0,
				notIntent, 0);

		notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);

		// AutoCancel: cuando se pulsa la notificaiï¿½n ï¿½sta desaparece
		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		// Aï¿½adir sonido, vibraciï¿½n y luces
		 notif.defaults |= Notification.DEFAULT_SOUND;
		
		// Enviar notificaciï¿½n
		notManager.notify(NOTIF_ALERTA_ID, notif);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		// Creamos un hilo que obtendra la informaci—n de forma as’ncrona
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		
		return super.onUnbind(intent);
	}

	private void buscarMensajes() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.GET_MENSAJES_RECIBIDOS));
		try {
			String datos = ServerRequest.send("", parameters,
					"GET_MENSAJES_RECIBIDOS");
			Type listOfTestObject = new TypeToken<List<Mensajes>>() {
			}.getType();
			ArrayList<Mensajes> list = new Gson().fromJson(datos,
					listOfTestObject);
			
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getNotificado() == '0') {
					Notificar(list.get(i));
					ponerNotificacion(list.get(i).getId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ponerNotificacion(BigDecimal id) {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.SET_MENSAJES_NOTIFICA));
		parameters.add(new BasicNameValuePair("identificador",
				id.toString()));
		try {
			 String datos = ServerRequest.send("", parameters,
			 "SET_MENSAJES_NOTIFICA");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}