package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Usuario;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CronometroActivity extends SherlockActivity {

	private Button botonStartStop;
	private Boolean cronometroIniciado = false;
	private CountDownTimer timer;
	private CountDownTimer timerTurno;

	private TextView cuenta;
	private TextView usuarioActual;
	private ImageView siguienteUsuario;

	private int minutosCuenta;
	private int minutosPorUsuario;

	private ArrayList<Usuario> usuarios;
	private ArrayList<Usuario> usuariosSeleccionados;
	private int usuarioActualPos;
	private Boolean notificacionTurno;
	private Boolean notificacionFin;
	ProgressDialog d;

	private String jSessionId;

	private static final int NOTIFICACION_FIN_REUNION = 0;
	private static final int NOTIFICACION_CAMBIO_TURNO = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.reunion_cronometro);
		getSupportActionBar().setTitle(R.string.reunion_cronometro);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//		d = ServerRequest.compruebaConexionServer(this);
		usuarioActualPos = 0;
		dialogConfiguracion();

		// Se inicializan los elementos de la UI
		initUI();

		// Se obtienen los usuarios del servidor
		usuarios = obtenerUsuarios();

		// Listeners de botones
		siguienteUsuario.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (usuarioActualPos < usuariosSeleccionados.size() - 1) {
					usuarioActualPos++;
					usuarioActual.setText(usuariosSeleccionados.get(
							usuarioActualPos).getEmail());
					timerTurno.cancel();
					configurarCuentaAtrasTurno();
				} else {
					usuarioActualPos = 0;
					usuarioActual.setText(usuariosSeleccionados.get(
							usuarioActualPos).getEmail());
					timerTurno.cancel();

				}

			}
		});

		botonStartStop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (cronometroIniciado) {
					Log.d("Cronometro", "STOP");
					timer.cancel();
					timerTurno.cancel();
					botonStartStop
							.setBackgroundResource(R.drawable.fondo_verde);
					botonStartStop.setText(R.string.cronometro_iniciar);
					cuenta.setText(minutosCuenta + ":00");
					cronometroIniciado = false;
					usuarioActualPos = 0;
					usuarioActual.setText(usuariosSeleccionados.get(
							usuarioActualPos).getEmail());
				} else {
					Log.d("Cronometro", "START");
					configurarCuentaAtras();
					configurarCuentaAtrasTurno();
					cuenta.setText(minutosCuenta + ":00");
					timer.start();
					botonStartStop.setBackgroundResource(R.drawable.fondo_rojo);
					botonStartStop.setText(R.string.cronometro_parar);
					usuarioActualPos = 0;
					usuarioActual.setText(usuariosSeleccionados.get(
							usuarioActualPos).getEmail());
					cronometroIniciado = true;
				}
			}
		});

	}

	@Override
	protected void onStop() {

		if (cronometroIniciado) {
			timer.cancel();
			timerTurno.cancel();
		}
		super.onStop();
	}

	/**
	 * Inicializa los elementos de la UI
	 * 
	 * @param view
	 */
	private void initUI() {
		cuenta = (TextView) findViewById(R.id.reunion_cronometro_texto_cuenta);
		cuenta.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 70);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/clock.ttf");
		cuenta.setTypeface(tf);
		cuenta.setText(minutosCuenta + ":00");

		usuarioActual = (TextView) findViewById(R.id.reunion_cronometro_usuarios_actual_valor);
		siguienteUsuario = (ImageView) findViewById(R.id.reunion_cronometro_usuarios_actual_siguiente);

		botonStartStop = (Button) findViewById(R.id.reunion_cronometro_boton);
		botonStartStop.setBackgroundResource(R.drawable.fondo_verde);
		botonStartStop.setText(R.string.cronometro_iniciar);
	}

	/**
	 * Obtiene el SessionId del sharedpreferences
	 * 
	 * @return
	 */
	private String obtenerSessionId() {
		SharedPreferences prefs = this.getSharedPreferences("loginPreferences",
				Context.MODE_PRIVATE);
		return prefs.getString("jSessionId", "default_value");
	}

	/**
	 * Obtiene los usuarios del servidor
	 * 
	 * @return
	 */
	private ArrayList<Usuario> obtenerUsuarios() {

		ArrayList<Usuario> list = new ArrayList<Usuario>();

		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.BUSCAR_USUARIO_GRUPO));
		String datos = null;
		try {
			d=ProgressDialog.show(this, getResources().getString(R.string.espere), getResources().getString(R.string.Buscando_usuarios), true, true);
			datos = ServerRequest.send(jSessionId, parameters,"BUSCAR_USUARIO_GRUPO");
			Type listOfTestObject = new TypeToken<List<Usuario>>() {
			}.getType();
			list = new Gson().fromJson(datos, listOfTestObject);
			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Crea el timer que permite llevar a cabo la cuenta atrás
	 */
	private void configurarCuentaAtras() {
		// Creación de la cuenta atrás
		timer = new CountDownTimer(minutosCuenta * 60 * 1000, 1000) {

			public void onTick(long millisUntilFinished) {
				/*
				 * Log.d("Cronometro", "Segundos restantes: " +
				 * millisUntilFinished / 1000);
				 */
				long min = (millisUntilFinished / 1000) / 60;
				long seg = (millisUntilFinished / 1000) % 60;
				String cuentaMostrar = "";
				if (min < 10) {
					cuentaMostrar += "0" + min + ":";
				} else {
					cuentaMostrar += min + ":";
				}

				if (seg < 10) {
					cuentaMostrar += "0" + seg;
				} else {
					cuentaMostrar += seg;
				}

				if (min < 1) {
					cuenta.setTextColor(getResources().getColor(R.color.rojo));
				}
				cuenta.setText(cuentaMostrar);
			}

			public void onFinish() {
				Log.d("Cronometro", "done!");

				timerTurno.cancel();
				Toast.makeText(getBaseContext(),
						getString(R.string.reunion_cronometro_fin_reunion),
						Toast.LENGTH_SHORT).show();

				Log.d("Cronometro", "Notificación fin");
				cuenta.setText("00:00");
				cuenta.setTextColor(getResources().getColor(R.color.negro));
				botonStartStop.setBackgroundResource(R.drawable.fondo_verde);
				botonStartStop.setText(R.string.cronometro_iniciar);
				cronometroIniciado = false;
				usuarioActualPos = 0;
				usuarioActual.setText("-");

				if (notificacionFin) {
					reproducirNotificacion(NOTIFICACION_FIN_REUNION);
				}
			}
		};
	}

	/**
	 * Crea el timer que permite llevar a cabo la cuenta atrás para el cambio de
	 * turno
	 */
	private void configurarCuentaAtrasTurno() {

		Log.d("Cronometro", "CONFIGURAR CUENTA ATRAS CAMBIO DE TURNO");
		// Creación de la cuenta atrás
		timerTurno = new CountDownTimer(minutosPorUsuario * 60 * 1000, 1000) {

			public void onTick(long millisUntilFinished) {
				Log.d("Cronometro", "Segundos restantes cambio turno: "
						+ millisUntilFinished / 1000);
			}

			public void onFinish() {
				Log.d("Cronometro", "CAMBIO TURNO!!");
				if (usuarioActualPos < usuariosSeleccionados.size() - 1) {
					usuarioActualPos++;
					usuarioActual.setText(usuariosSeleccionados.get(
							usuarioActualPos).getEmail());
					timerTurno.cancel();
					configurarCuentaAtrasTurno();
				} else {
					usuarioActualPos = 0;
					usuarioActual.setText(" - ");
				}

				Toast.makeText(getBaseContext(),
						getString(R.string.reunion_cronometro_cambio_turno),
						Toast.LENGTH_SHORT).show();

				if (notificacionTurno) {
					Log.d("Cronometro", "Notificación cambio de turno");
					reproducirNotificacion(NOTIFICACION_CAMBIO_TURNO);
				}

			}
		};

		timerTurno.start();
	}

	/**
	 * Reproduce una notificación sonora para alertar del cambio de turno o fin
	 * de la reunión
	 */
	private void reproducirNotificacion(int tipo) {
		MediaPlayer mp = null;

		switch (tipo) {
		case NOTIFICACION_CAMBIO_TURNO:
			mp = MediaPlayer.create(getBaseContext(), R.raw.notificacion_turno);
			break;
		case NOTIFICACION_FIN_REUNION:
			mp = MediaPlayer.create(getBaseContext(), R.raw.notificacion_fin);
			break;
		}

		mp.start();
		mp.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}

		});
	}

	/**
	 * Muestra un dialog para seleccionar los usuarios que van a participar en
	 * la reunión usuarios
	 */
	private void dialogConfiguracion() {

		Log.d("Cronometro", "DIALOG USUARIOS! ");

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_reunion_config);
		dialog.setTitle(this.getString(R.string.dialog_reunion_usuarios_titulo));

		final LinearLayout layoutSeleccionar = (LinearLayout) dialog
				.findViewById(R.id.dialog_reunion_usuarios_layout_seleccionar);
		layoutSeleccionar.setVisibility(View.INVISIBLE);

		final CheckBox cbFin = (CheckBox) dialog
				.findViewById(R.id.dialog_reunion_usuarios_notificaciones_fin);
		final CheckBox cbTurno = (CheckBox) dialog
				.findViewById(R.id.dialog_reunion_usuarios_notificaciones_turno);

		final TextView minutosPorUsuarioText = (TextView) dialog
				.findViewById(R.id.dialog_reunion_usuarios_tiempo_usuario_valor);

		Log.d("Cronometro", "Obtenidos elementos UI 1");

		ImageView minutosPorUsuarioMas = (ImageView) dialog
				.findViewById(R.id.dialog_reunion_usuarios_tiempo_usuario_mas);
		minutosPorUsuarioMas.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int m = Integer.parseInt(minutosPorUsuarioText.getText()
						.toString());

				if (m < 10) { // Máximo 10 mins por usuario
					m++;
					minutosPorUsuarioText.setText("" + m);
				}
			}
		});

		ImageView minutosPorUsuarioMenos = (ImageView) dialog
				.findViewById(R.id.dialog_reunion_usuarios_tiempo_usuario_menos);

		minutosPorUsuarioMenos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				int m = Integer.parseInt(minutosPorUsuarioText.getText()
						.toString());

				if (m > 1) { // Mínimo 1 min por usuario
					m--;
					minutosPorUsuarioText.setText("" + m);
				}
			}
		});

		Log.d("Cronometro", "Asignados listeners a botones + y -");

		final RadioGroup rg = (RadioGroup) dialog
				.findViewById(R.id.dialog_reunion_usuarios_radiogroup);
		// Listener
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.dialog_reunion_usuarios_todos:
					break;
				case R.id.dialog_reunion_usuarios_seleccionar:
					layoutSeleccionar.setVisibility(View.VISIBLE);
					if (layoutSeleccionar.getChildCount() < 1) {
						// Se agregan los usuarios a los checkboxes
						CheckBox cb;
						for (Usuario u : usuarios) {
							cb = new CheckBox(getBaseContext());
							cb.setText(u.getEmail());
							cb.setTextColor(getBaseContext().getResources()
									.getColor(R.color.blanco));
							layoutSeleccionar.addView(cb);
						}
					}
					break;
				}

			}
		});

		Log.d("Cronometro", "LISTENER RADIO GROUP");

		Button boton = (Button) dialog
				.findViewById(R.id.dialog_reunion_usuarios_boton);
		boton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				switch (rg.getCheckedRadioButtonId()) {
				case R.id.dialog_reunion_usuarios_seleccionar:
					Log.d("Cronometro", "NUMERO DE USUARIOS SELECCIONAR -> "
							+ layoutSeleccionar.getChildCount());
					usuariosSeleccionados = new ArrayList<Usuario>();

					for (int i = 0; i < layoutSeleccionar.getChildCount(); i++) {
						CheckBox c = (CheckBox) layoutSeleccionar.getChildAt(i);
						Log.d("Cronometro", "CHECKBOX -> " + c.getText()
								+ " - Seleccionado -> " + c.isChecked());
						if (c.isChecked()) {
							Log.d("Cronometro", "USUARIO -> "
									+ usuarios.get(i).getEmail());
							usuariosSeleccionados.add(usuarios.get(i));
						}
					}
					break;
				case R.id.dialog_reunion_usuarios_todos:
					Log.d("Cronometro", "SELECCIONADOS TODOS USUARIOS"+usuarios);
					usuariosSeleccionados = usuarios;
					break;
				}

				notificacionFin = cbFin.isChecked();
				notificacionTurno = cbTurno.isChecked();

				minutosPorUsuario = Integer.parseInt(minutosPorUsuarioText
						.getText().toString());

				minutosCuenta = usuariosSeleccionados.size()
						* minutosPorUsuario;
				cuenta.setText(minutosCuenta + ":00");

				usuarioActual.setText(usuariosSeleccionados.get(0).getEmail());

				dialog.dismiss();
			}
		});

		Log.d("Cronometro", "LISTENER BOTON");

		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(this.getString(R.string.opciones))
				.setIcon(R.drawable.ic_opciones)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
		} else if (item.getTitle().equals(this.getString(R.string.opciones))) {
			// PULSADO NUEVO
			if (!cronometroIniciado) {
				dialogConfiguracion();
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
