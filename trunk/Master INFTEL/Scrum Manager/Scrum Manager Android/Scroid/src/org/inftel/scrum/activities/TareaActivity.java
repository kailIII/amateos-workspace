package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.listeners.TareaOnClickListener;
import org.inftel.scrum.modelXML.Proyecto;
import org.inftel.scrum.modelXML.Sprint;
import org.inftel.scrum.modelXML.Tarea;
import org.inftel.scrum.modelXML.Usuario;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TareaActivity extends SherlockActivity {

	public static final String TAG = TareaActivity.class.getSimpleName();

	private TextView descripcion;
	private TextView usuario;
	private TextView estado;
	private TextView fechaInicio;
	private TextView fechaFin;
	private TextView duracionEstimada;
	private TextView duracionRestante;
	private TextView totalHoras;
	private String jSessionId;
	private ImageView prioridad;
	private ImageView descripcionMod;
	private ImageView usuarioMod;
	private ImageView estadoMod;
	private ImageView fechaInicioMod;
	private ImageView fechaFinMod;
	private ImageView duracionEstimadaMod;
	private ImageView duracionRestanteMas;
	private ImageView duracionRestanteMenos;
	ProgressDialog d;
	private Tarea tarea;
	private Tarea tareaBackup;
	private Sprint sprint;
	private Proyecto proyecto;
	private ArrayList<Usuario> usuarios;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setTitle(R.string.detalle_tarea);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.tarea);

		// Se obtiene el jSessionId
		this.jSessionId = obtenerSessionId();

		// Se recuperan el proyecto y el sprint
		Bundle b = getIntent().getExtras();
		proyecto = (Proyecto) b.getSerializable("proyecto");
		sprint = (Sprint) b.getSerializable("sprint");
		Log.d(TAG, "SPRINT OBTENIDO -> " + sprint);
		Log.d(TAG, "PROYECTO OBTENIDO -> " + proyecto);

		// Se recupera la tarea y se crea un backup por si se realizan cambios
		// pero no se desea guardarlos
		tareaBackup = (Tarea) b.getSerializable("tarea");
		Log.d(TAG, "TAREA OBTENIDA -> " + tareaBackup + " - ID -> "
				+ tareaBackup.getId());

		// Se recuperan los usuarios
		usuarios = obtenerUsuarios();

		// Se recuperan las fechas
		Date inicio = obtenerFechaInicio();
		Log.d(TAG, "FECHA INICIO OBTENIDA -> " + inicio);
		tareaBackup.setInicio(inicio);

		Date fin = obtenerFechaFin();
		Log.d(TAG, "FECHA FIN OBTENIDA -> " + fin);
		tareaBackup.setFechaFin(fin);

		Usuario usuario = obtenerUsuarioTarea();
		Log.d(TAG, "USUARIO OBTENIDO -> " + usuario);
		tareaBackup.setUsuarioId(usuario);

		tarea = new Tarea();
		copiarTarea(tareaBackup, tarea);
		// Se inicializan los elementos de la interfaz
		initUI();
		setValoresUI();

	}

	/**
	 * Inicializa los elemento de la UI
	 */
	private void initUI() {
		descripcion = (TextView) findViewById(R.id.tarea_descripcion);
		usuario = (TextView) findViewById(R.id.tarea_usuario_valor);
		estado = (TextView) findViewById(R.id.tarea_estado_valor);
		fechaInicio = (TextView) findViewById(R.id.tarea_fechas_inicio_valor);
		fechaFin = (TextView) findViewById(R.id.tarea_fechas_fin_valor);
		duracionEstimada = (TextView) findViewById(R.id.tarea_duracion_estimada_valor);
		duracionRestante = (TextView) findViewById(R.id.tarea_duracion_restante_valor);
		totalHoras = (TextView) findViewById(R.id.tarea_duracion_total_valor);

		prioridad = (ImageView) findViewById(R.id.tarea_prioridad);
		prioridad.setOnClickListener(new TareaOnClickListener(this, tarea));

		descripcionMod = (ImageView) findViewById(R.id.tarea_descripcion_modificar);
		descripcionMod
				.setOnClickListener(new TareaOnClickListener(this, tarea));

		usuarioMod = (ImageView) findViewById(R.id.tarea_usuario_modificar);
		usuarioMod.setOnClickListener(new TareaOnClickListener(this, tarea,
				usuarios));

		estadoMod = (ImageView) findViewById(R.id.tarea_estado_modificar);
		estadoMod.setOnClickListener(new TareaOnClickListener(this, tarea));

		fechaInicioMod = (ImageView) findViewById(R.id.tarea_fechas_inicio_modificar);
		fechaInicioMod
				.setOnClickListener(new TareaOnClickListener(this, tarea));

		fechaFinMod = (ImageView) findViewById(R.id.tarea_fechas_fin_modificar);
		fechaFinMod.setOnClickListener(new TareaOnClickListener(this, tarea));

		duracionEstimadaMod = (ImageView) findViewById(R.id.tarea_duracion_estimada_modificar);
		duracionEstimadaMod.setOnClickListener(new TareaOnClickListener(this,
				tarea));

		duracionRestanteMas = (ImageView) findViewById(R.id.tarea_duracion_restante_mas);
		duracionRestanteMas.setOnClickListener(new TareaOnClickListener(this,
				tarea));

		duracionRestanteMenos = (ImageView) findViewById(R.id.tarea_duracion_restante_menos);
		duracionRestanteMenos.setOnClickListener(new TareaOnClickListener(this,
				tarea));

	}

	/**
	 * Establece los valores de los elementos de la UI, mostrando cada uno de
	 * ellos cuando sea necesario
	 */
	public void setValoresUI() {

		// PRIORIDAD
		if (tarea.getPrioridad() != null
				&& tarea.getPrioridad().equals(BigInteger.ZERO)) {
			prioridad.setVisibility(View.VISIBLE);
			prioridad.setImageResource(R.drawable.urgent);
		} else {
			prioridad.setVisibility(View.INVISIBLE);
		}

		// DESCRIPCIÓN
		if (tarea.getDescripcion() != null && !tarea.getDescripcion().isEmpty()) {
			descripcion.setText(tarea.getDescripcion());
		} else {
			descripcion.setText(R.string.tarea_no_descripcion);
		}
		if (tarea.getDone() != null && tarea.getDone() == '0') {
			descripcionMod.setVisibility(View.VISIBLE);
			descripcionMod.setImageResource(R.drawable.ic_modificar);
		} else {
			descripcionMod.setVisibility(View.INVISIBLE);
		}

		// USUARIO
		if (tarea.getUsuarioId() != null
				&& tarea.getUsuarioId().getEmail() != null
				&& !tarea.getUsuarioId().getEmail().isEmpty()) {
			usuario.setText(tarea.getUsuarioId().getEmail());
		} else {
			usuario.setText("-");
		}
		if (tarea.getDone() != null && tarea.getDone() == '0') {
			usuarioMod.setVisibility(View.VISIBLE);
			usuarioMod.setImageResource(R.drawable.ic_modificar);
		} else {
			usuarioMod.setVisibility(View.INVISIBLE);
		}

		// ESTADO
		if (tarea.getDone() != null && tarea.getDone() == '0') {
			estado.setText(R.string.todo);
			estado.setTextColor(getResources().getColor(R.color.rojo));
			estadoMod.setImageResource(R.drawable.ic_modificar);
			estadoMod.setVisibility(View.VISIBLE);
		} else if (tarea.getDone() != null && tarea.getDone() == '1') {
			estado.setText(R.string.doing);
			estado.setTextColor(getResources().getColor(R.color.amarillo));
			estadoMod.setImageResource(R.drawable.ic_modificar);
			estadoMod.setVisibility(View.VISIBLE);
		} else if (tarea.getDone() != null && tarea.getDone() == '2') {
			estado.setText(R.string.done);
			estado.setTextColor(getResources().getColor(R.color.verde));
			estadoMod.setVisibility(View.INVISIBLE);
		} else {
			estado.setText("-");
			estadoMod.setImageResource(R.drawable.ic_modificar);
			estadoMod.setVisibility(View.VISIBLE);
		}

		// FECHA INICIO
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

		if (tarea.getInicio() == null) {
			fechaInicio.setText("-");
		} else {
			fechaInicio.setText(df.format(tarea.getInicio()));
		}
		// Si la tarea está en estado To do -> se puede modificar la fecha
		// inicio
		if (tarea.getDone() != null && tarea.getDone() == '0') {
			fechaInicioMod.setVisibility(View.VISIBLE);
			fechaInicioMod.setImageResource(R.drawable.ic_modificar);
		} else {
			fechaInicioMod.setVisibility(View.INVISIBLE);
		}

		// FECHA FIN
		if (tarea.getFechaFin() == null) {
			fechaFin.setText("-");
		} else {
			fechaFin.setText(df.format(tarea.getFechaFin()));
		}
		// Si la tarea está en estado Done -> se puede modificar la fecha
		// inicio
		if (tarea.getDone() != null && tarea.getDone() == '2') {
			fechaFinMod.setVisibility(View.VISIBLE);
			fechaFinMod.setImageResource(R.drawable.ic_modificar);
		} else {
			fechaFinMod.setVisibility(View.INVISIBLE);
		}

		// DURACION ESTIMADA
		if (tarea.getDuracionEstimado() == null) {
			duracionEstimada.setText("-");
		} else {
			if (tarea.getDuracionEstimado().equals(new BigInteger("1000"))) { // Estimada
				// infinita
				duracionEstimada.setText("\u221E");
			} else {
				duracionEstimada
						.setText(tarea.getDuracionEstimado().toString());
			}
		}

		// Si la tarea está en estado To do -> se puede modificar la duración
		// estimada
		if (tarea.getDone() != null && tarea.getDone() == '0') {
			duracionEstimadaMod.setVisibility(View.VISIBLE);
			duracionEstimadaMod.setImageResource(R.drawable.ic_modificar);
		} else {
			duracionEstimadaMod.setVisibility(View.INVISIBLE);
		}

		// DURACION RESTANTE
		if (tarea.getDuracionEstimado() == null
				|| tarea.getDuracionEstimado().equals(new BigInteger("1000"))) { // Restante
																					// =
			duracionRestante.setText("-");
			duracionRestanteMas.setVisibility(View.INVISIBLE);
			duracionRestanteMenos.setVisibility(View.INVISIBLE);
		} else {
			duracionRestante.setText(tarea.getDuracion().toString());

			if (tarea.getDuracion().compareTo(BigInteger.ZERO) == 1
					&& tarea.getDone() == '1') { // Horas
				// restantes
				// > 0
				duracionRestanteMas.setImageResource(R.drawable.ic_arriba);
				duracionRestanteMas.setVisibility(View.VISIBLE);
				duracionRestanteMenos.setImageResource(R.drawable.ic_abajo);
				duracionRestanteMenos.setVisibility(View.VISIBLE);
			} else {
				duracionRestanteMas.setVisibility(View.INVISIBLE);
				duracionRestanteMenos.setVisibility(View.INVISIBLE);
			}
		}

		// TOTAL HORAS
		if (tarea.getTotalHoras() == null) {
			totalHoras.setText("-");
		} else {
			totalHoras.setText(tarea.getTotalHoras().toString());
		}
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
	 * Actualiza una tarea en el servidor
	 * 
	 * @param t
	 * @return
	 */
	private Boolean modificarTarea(Tarea t) {

		Boolean modificada = false;

		String jsonTarea = new Gson().toJson(t);
		// String jsonProyecto = new Gson().toJson(proyecto);
		// String jsonSprint = new Gson().toJson(sprint);
		Log.d(TAG, "ID TAREA MODIFICAR-> " + t.getId());
		Log.d(TAG, "JSON TAREA -> " + jsonTarea);
		Log.d(TAG, "JSON TAREA INICIO-> " + t.getInicio());
		Log.d(TAG, "JSON TAREA FIN -> " + t.getFechaFin());

		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair("accion", Constantes.EDITAR_TAREA));
		parameters.add(new BasicNameValuePair("Tarea", jsonTarea));
		parameters.add(new BasicNameValuePair("Proyecto", proyecto.getId()
				.toString()));
		parameters.add(new BasicNameValuePair("Sprint", sprint.getId()
				.toString()));

		if (t.getUsuarioId() != null) {
			parameters.add(new BasicNameValuePair("Usuario", t.getUsuarioId()
					.getEmail()));
		}

		try {
			d.show();
			ServerRequest.send(jSessionId, parameters, "EDITAR_TAREA");
			d.dismiss();
			modificada = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return modificada;
	}

	/**
	 * Obtiene los usuarios del servidor
	 * 
	 * @return
	 */
	private ArrayList<Usuario> obtenerUsuarios() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.BUSCAR_USUARIO_GRUPO));
		parameters.add(new BasicNameValuePair("Tarea", tareaBackup.getId()
				.toString()));

		String datos = null;
		try {
			d.show();
			datos = ServerRequest.send(jSessionId, parameters,
					"BUSCAR_USUARIO_GRUPO");
			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Type listOfTestObject = new TypeToken<List<Usuario>>() {
		}.getType();
		ArrayList<Usuario> list = new Gson().fromJson(datos, listOfTestObject);
		return list;
	}

	/**
	 * Obtiene la fecha de inicio de la tarea del servidor
	 * 
	 * @return
	 */
	private Date obtenerFechaInicio() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.OBTENER_FECHA_INICIO_TAREA));
		parameters.add(new BasicNameValuePair("Tarea", tareaBackup.getId()
				.toString()));

		String datos = null;
		try {
			d.show();
			datos = ServerRequest.send(jSessionId, parameters,
					"OBTENER_FECHA_INICIO_TAREA");
			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date fechaInicio = new Gson().fromJson(datos, Date.class);
		return fechaInicio;
	}

	/**
	 * Obtiene la fecha de fin de la tarea del servidor
	 * 
	 * @return
	 */
	private Date obtenerFechaFin() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.OBTENER_FECHA_FIN_TAREA));
		parameters.add(new BasicNameValuePair("Tarea", tareaBackup.getId()
				.toString()));

		String datos = null;
		try {
			d.show();
			datos = ServerRequest.send(jSessionId, parameters,
					"OBTENER_FECHA_FIN_TAREA");
			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date fechaFin = new Gson().fromJson(datos, Date.class);
		return fechaFin;
	}

	/**
	 * Obtiene el usuario de la tarea
	 * 
	 * @return
	 */
	private Usuario obtenerUsuarioTarea() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.OBTENER_USUARIO_TAREA));
		parameters.add(new BasicNameValuePair("Tarea", tareaBackup.getId()
				.toString()));

		String datos = null;
		try {
			d.show();
			datos = ServerRequest.send(jSessionId, parameters,
					"OBTENER_USUARIO_TAREA");
			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Usuario usuario = new Gson().fromJson(datos, Usuario.class);
		return usuario;
	}

	/**
	 * Realiza una copia del objeto tarea
	 * 
	 * @param inicial
	 * @param copia
	 */
	private void copiarTarea(Tarea inicial, Tarea copia) {

		copia.setId(inicial.getId());
		copia.setDescripcion(inicial.getDescripcion());
		copia.setDone(inicial.getDone());
		copia.setDuracion(inicial.getDuracion());
		copia.setDuracionEstimado(inicial.getDuracionEstimado());
		copia.setFechaFin(inicial.getFechaFin());
		copia.setInicio(inicial.getInicio());
		copia.setPrioridad(inicial.getPrioridad());
		copia.setTotalHoras(inicial.getTotalHoras());
		copia.setUsuarioId(inicial.getUsuarioId());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(this.getString(R.string.guardar))
				.setIcon(R.drawable.ic_guardar)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();
		} else if (item.getTitle().equals(this.getString(R.string.guardar))) {

			Log.d(TAG, "ANTES DE ENVIAR");
			Log.d(TAG, "TAREA BACKUP FECHA -> " + tareaBackup.getFechaFin());
			Log.d(TAG, "TAREA FECHA -> " + tarea.getFechaFin());
			Boolean modificada = modificarTarea(tarea);
			if (modificada) {
				Log.d(TAG, "MODIFICADA!");

				Toast.makeText(getBaseContext(),
						getString(R.string.tarea_guardada), Toast.LENGTH_SHORT)
						.show();
				copiarTarea(tarea, tareaBackup);
				Log.d(TAG, "COPIADA TAREA");
				Log.d(TAG, "TAREA BACKUP FECHA -> " + tareaBackup.getFechaFin());
				Log.d(TAG, "TAREA FECHA -> " + tarea.getFechaFin());
			} else {
				Toast.makeText(getBaseContext(),
						getString(R.string.tarea_guardada_error),
						Toast.LENGTH_SHORT).show();
			}

		}
		return super.onOptionsItemSelected(item);
	}

}
