package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Proyecto;
import org.inftel.scrum.modelXML.Sprint;
import org.inftel.scrum.modelXML.Tarea;
import org.inftel.scrum.modelXML.Usuario;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TablonActivity extends SherlockActivity {

	private String jSessionId;
	private ArrayList<Tarea> listaTareas = new ArrayList<Tarea>();
	private Usuario usuario;
	private Sprint sprintActual;
	ProgressDialog d;
	private String email;
	private Proyecto proyecto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.jSessionId = obtenerSessionId();
		setContentView(R.layout.tablon);
		getSupportActionBar().setTitle(R.string.Tablon);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		sprintActual = obtenerSprintActual();
		Log.d("Tablon", "SPRINT ACTUAL -> " + sprintActual);
		if (sprintActual != null) {
			Log.d("Tablon", "SPRINT ACTUAL NO NULL");
			this.listaTareas = obtenerTareas(sprintActual);

		}

		Bundle b = getIntent().getExtras();
		proyecto = (Proyecto) b.getSerializable("proyecto");

		findViewById(R.id.todoLayout).setOnDragListener(new MyDragListener());
		findViewById(R.id.doingLayout).setOnDragListener(new MyDragListener());
		findViewById(R.id.doneLayout).setOnDragListener(new MyDragListener());

		TextView todoTitulo = (TextView) findViewById(R.id.titulo1);
		TextView doingTitulo = (TextView) findViewById(R.id.titulo2);
		TextView doneTitulo = (TextView) findViewById(R.id.titulo3);
		todoTitulo.setTextSize(25);
		doingTitulo.setTextSize(25);
		doneTitulo.setTextSize(25);
		todoTitulo.setTextColor(getResources().getColor(R.color.blanco));
		doingTitulo.setTextColor(getResources().getColor(R.color.blanco));
		doneTitulo.setTextColor(getResources().getColor(R.color.blanco));

		this.usuario = cargarUsuario();
		cargarTareas();
	}

	public String getjSessionId() {
		return jSessionId;
	}

	public void setjSessionId(String jSessionId) {
		this.jSessionId = jSessionId;
	}

	public ArrayList<Tarea> getListaTareas() {
		return listaTareas;
	}

	public void setListaTareas(ArrayList<Tarea> listaTareas) {
		this.listaTareas = listaTareas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	private String obtenerSessionId() {
		Context context = this.getApplicationContext();
		SharedPreferences prefs = context.getSharedPreferences(
				"loginPreferences", Context.MODE_PRIVATE);
		return prefs.getString("jSessionId", "default_value");
	}

	public Usuario cargarUsuario() {
		Context context = this.getApplicationContext();
		SharedPreferences prefs = context.getSharedPreferences(
				"loginPreferences", Context.MODE_PRIVATE);
		this.email = prefs.getString("usuario", "default_value");
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.BUSCAR_USUARIO_POR_EMAIL));
		parameters.add(new BasicNameValuePair("email", email));
		String datos = "";
		try {
			// d.show();
			datos = ServerRequest.send(this.jSessionId, parameters,
					"BUSCAR_USUARIO_POR_EMAIL");
			// d.dismiss();
		} catch (Exception e) {
			ProgressDialog.show(this.getApplicationContext(), getResources()
					.getString(R.string.Error),
					getResources().getString(R.string.error_cargarUsuario),
					true, true);
			e.printStackTrace();
		}
		Type TestObject = new TypeToken<Usuario>() {
		}.getType();
		return new Gson().fromJson(datos, TestObject);
	}

	private void cargarTareas() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);

		LinearLayout todoLayout = (LinearLayout) findViewById(R.id.todoLayout);
		LinearLayout doingLayout = (LinearLayout) findViewById(R.id.doingLayout);
		LinearLayout doneLayout = (LinearLayout) findViewById(R.id.doneLayout);

		Log.d("Tablon", "LISTA TAREAS -> " + listaTareas);

		for (Tarea tarea : this.listaTareas) {
			Log.d("Tablon", "Obtenida tarea -> " + tarea);
			switch (tarea.getDone()) {
			case '0': {
				TextView todoTarea = new TextView(this.getApplicationContext());
				todoTarea.setId(tarea.getId().intValue());
				todoTarea.setTextColor(getResources().getColor(R.color.negro));
				todoTarea.setPadding(2, 2, 2, 2);
				todoTarea.setText(tarea.getDescripcion());
				todoTarea.setVisibility(View.VISIBLE);
				if (tarea.getPrioridad().equals(BigInteger.ZERO)) {
					todoTarea.setBackgroundColor(getResources().getColor(
							R.color.rojito));
				} else {
					todoTarea.setBackgroundColor(getResources().getColor(
							R.color.amarillito));
				}

				todoTarea.setPadding(10, 5, 5, 0);
				todoTarea.setOnTouchListener(new MyTouchListener());
				todoLayout.addView(todoTarea, params);
			}
				break;
			case '1': {
				if (tarea.getUsuarioId().getEmail().equals(this.email)) {
					TextView doingTarea = new TextView(
							this.getApplicationContext());
					doingTarea.setId(tarea.getId().intValue());
					doingTarea.setTextColor(getResources().getColor(
							R.color.negro));
					doingTarea.setText(tarea.getDescripcion());
					doingTarea.setVisibility(View.VISIBLE);
					if (tarea.getPrioridad().equals(BigInteger.ZERO)) {
						doingTarea.setBackgroundColor(getResources().getColor(
								R.color.rojito));
					} else {
						doingTarea.setBackgroundColor(getResources().getColor(
								R.color.amarillito));
					}
					doingTarea.setPadding(10, 5, 5, 0);
					doingTarea.setOnTouchListener(new MyTouchListener());
					doingLayout.addView(doingTarea, params);
				}
			}
				break;
			case '2': {
				TextView doneTarea = new TextView(this.getApplicationContext());
				doneTarea.setId(tarea.getId().intValue());
				doneTarea.setTextColor(getResources().getColor(R.color.negro));
				doneTarea.setText(tarea.getDescripcion());
				doneTarea.setVisibility(View.VISIBLE);
				if (tarea.getPrioridad().equals(BigInteger.ZERO)) {
					doneTarea.setBackgroundColor(getResources().getColor(
							R.color.rojito));
				} else {
					doneTarea.setBackgroundColor(getResources().getColor(
							R.color.amarillito));
				}
				doneTarea.setPadding(10, 5, 5, 0);
				doneTarea.setOnTouchListener(new MyTouchListener());
				doneLayout.addView(doneTarea, params);
			}
				break;
			}
		}
	}

	private ArrayList<Tarea> obtenerTareas(Sprint sprint) {
		ArrayList<Tarea> lista = new ArrayList<Tarea>();
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.BUSCAR_TAREAS_SPRINTS));
		parameters.add(new BasicNameValuePair("Sprint", sprint.getId()
				.toString()));
		String datos = "";
		try {
			// d.show();
			datos = ServerRequest.send(this.jSessionId, parameters,
					"BUSCAR_TAREAS_SPRINTS");
			// d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Type listOfTestObject = new TypeToken<List<Tarea>>() {
		}.getType();
		lista = new Gson().fromJson(datos, listOfTestObject);
		return lista;
	}

	private Sprint obtenerSprintActual() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.BUSCAR_SPRINTS));
		String datos = "";
		try {
			Log.d("Tablon", "Antes -> ");

			// d.show();
			Log.d("Tablon", "Despues -> ");

			datos = ServerRequest.send(this.jSessionId, parameters,
					"BUSCAR_SPRINTS");
			Log.d("Tablon", "DATOS -> " + datos);

			// d.dismiss();
		} catch (Exception e) {
			Log.d("Tablon", "FALLO -> " + e.getMessage());

			e.printStackTrace();
		}
		Type listOfTestObject = new TypeToken<List<Sprint>>() {
		}.getType();
		ArrayList<Sprint> listaSprints = new ArrayList<Sprint>();
		listaSprints = new Gson().fromJson(datos, listOfTestObject);
		Log.d("Tablon", "SPRINTS -> " + listaSprints);
		for (Sprint sprint : listaSprints) {
			Log.d("Tablon", "sprint obtenido -> " + sprint);

			if (sprint.getFin().after(new Date())) {
				return sprint;
			}
		}
		return null;
	}

	public Tarea buscarTarea(int id) {
		Log.d("Tablon", "ID BUSCAR ->" + id);
		BigDecimal iAux = new BigDecimal(id);
		for (Tarea t : listaTareas) {
			Log.d("Tablon", "TAREA ->" + t);
			if (t.getId().equals(iAux)) {
				Log.d("Tablon", "ENCONTRADA");
				return t;
			}
		}
		return null;
	}

	class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				// view.setVisibility(View.INVISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}

	class MyDragListener implements OnDragListener {

		private int indexTarea;

		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				v.setBackgroundColor(R.color.rojo);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				v.setBackgroundColor(R.color.azul_ice);
				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				View view = (View) event.getLocalState();
				ViewGroup owner = (ViewGroup) view.getParent();
				LinearLayout container = (LinearLayout) v;

				if ((owner.getId() == R.id.todoLayout)
						&& (container.getId() == R.id.doingLayout)) {
					Log.d("TablonActivity",
							"Moviendo tarea a DOING: " + view.getId());
					// view.setVisibility(View.VISIBLE);
					owner.removeView(view);
					container.addView(view);
					Tarea tarea = buscarTarea(view.getId());
					indexTarea = listaTareas.indexOf(tarea);
					Log.d("Tablon", "TAREA: " + tarea);
					tarea.setInicio(new Date());
					tarea.setDone('1');
					Usuario u = cargarUsuario();
					setUsuario(u);
					tarea.setUsuarioId(u);
					listaTareas.remove(indexTarea);
					listaTareas.add(tarea);
				} else if ((owner.getId() == R.id.doingLayout)
						&& (container.getId() == R.id.doneLayout)) {
					Log.d("TablonActivity",
							"Moviendo tarea a DONE: " + view.getId());
					owner.removeView(view);
					// view.setVisibility(View.VISIBLE);
					container.addView(view);
					Tarea tarea = buscarTarea(view.getId());
					indexTarea = listaTareas.indexOf(tarea);
					Log.d("Tablon", "TAREA: " + tarea);
					tarea.setFechaFin(new Date());
					tarea.setDone('2');
					Usuario u = cargarUsuario();
					setUsuario(u);
					tarea.setUsuarioId(u);
					listaTareas.remove(indexTarea);
					listaTareas.add(tarea);
				}
				// else {
				// view.setVisibility(View.VISIBLE);
				// }

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				// cambiar color
			default:
				break;
			}
			return true;
		}
	}

	public void guardar(View v) {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.EDITAR_LISTA_TAREAS));
		parameters.add(new BasicNameValuePair("Tareas", new Gson()
				.toJson(listaTareas)));
		parameters.add(new BasicNameValuePair("Proyecto", proyecto.getId()
				.toString()));
		parameters.add(new BasicNameValuePair("Sprint", sprintActual.getId()
				.toString()));

		try {
			ServerRequest.send(jSessionId, parameters, "EDITAR_LISTA_TAREAS");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void cancelar(View v) {
		LinearLayout todoLayout = (LinearLayout) findViewById(R.id.todoLayout);
		LinearLayout doingLayout = (LinearLayout) findViewById(R.id.doingLayout);
		LinearLayout doneLayout = (LinearLayout) findViewById(R.id.doneLayout);

		todoLayout.removeAllViews();
		doingLayout.removeAllViews();
		doneLayout.removeAllViews();

		TextView todoTitulo = new TextView(this.getApplicationContext());
		TextView doingTitulo = new TextView(this.getApplicationContext());
		TextView doneTitulo = new TextView(this.getApplicationContext());

		todoTitulo.setText("TODO");
		doingTitulo.setText("DOING");
		doneTitulo.setText("DONE");

		todoTitulo.setTextSize(25);
		doingTitulo.setTextSize(25);
		doneTitulo.setTextSize(25);

		todoTitulo.setTextColor(getResources().getColor(R.color.blanco));
		doingTitulo.setTextColor(getResources().getColor(R.color.blanco));
		doneTitulo.setTextColor(getResources().getColor(R.color.blanco));

		todoLayout.addView(todoTitulo);
		doingLayout.addView(doingTitulo);
		doneLayout.addView(doneTitulo);

		sprintActual = obtenerSprintActual();
		Log.d("Tablon", "SPRINT ACTUAL -> " + sprintActual);
		if (sprintActual != null) {
			Log.d("Tablon", "SPRINT ACTUAL NO NULL");
			this.listaTareas = obtenerTareas(sprintActual);

		}

		cargarTareas();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			this.onBackPressed();
		}
		return super.onOptionsItemSelected(item);

	}

}
