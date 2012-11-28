package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.lists.TareasAdapter;
import org.inftel.scrum.modelXML.Proyecto;
import org.inftel.scrum.modelXML.Tarea;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProyectoFragment extends SherlockListFragment {
	TextView nombreProyecto;
	TextView descripcionProyecto;
	TextView fechaEntrega;
	ListView listaTareas;
	ProgressBar barra;
	private TareasAdapter adapter;
	private ArrayList<Tarea> tareas;
	Proyecto p;
	private String jSessionId;
	ProgressDialog d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.proyecto, container, false);
		View view2 = inflater.inflate(R.layout.dialogtareas, container, false);
		nombreProyecto = (TextView) view.findViewById(R.id.nombreProyecto);
		descripcionProyecto = (TextView) view
				.findViewById(R.id.descripcionProyecto);
		fechaEntrega = (TextView) view.findViewById(R.id.fechaProyecto);
		barra = (ProgressBar) view.findViewById(R.id.ProgressBar1);
		try {
			Context context = getActivity().getApplicationContext();
			SharedPreferences prefs = context.getSharedPreferences(
					"loginPreferences", Context.MODE_PRIVATE);
			this.jSessionId = prefs.getString("jSessionId", "default_value");
			Log.i("ProyectFragment", "JSessionId=" + this.jSessionId);

//			d = ServerRequest.compruebaConexionServer(this.getActivity());
			realizarPeticion();
			pedirBackLog();
		} catch (Exception e) {
			ProgressDialog.show(this.getActivity().getApplicationContext(),
					getResources().getString(R.string.Error), getResources()
							.getString(R.string.Error_Indeterminado), true,
					true);
		}

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("actualizar").setIcon(R.drawable.ic_actualizar)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getTitle().toString().compareTo("actualizar") == 0) {
			realizarPeticion();
			pedirBackLog();
		}
		return true;
	}

	public void pedirBackLog() {
		try {
			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair("accion",
					Constantes.VER_BACKLOG));
			parameters.add(new BasicNameValuePair("NOMBRE_PROYECTO", p
					.getNombre()));

			d=ProgressDialog.show(this.getSherlockActivity(), getResources().getString(R.string.espere), "", true, true);
			String datos = ServerRequest.send(this.jSessionId, parameters,
					"VER_BACKLOG");
			d.dismiss();
			Type listOfTestObject = new TypeToken<List<Tarea>>() {
			}.getType();
			ArrayList<Tarea> list = new Gson()
					.fromJson(datos, listOfTestObject);
			p.setTareaList(list);
			barra.setMax(p.getTareaList().size());
			int done = calcularTareasDone();
			barra.setProgress(done);
			tareas = (ArrayList<Tarea>) p.getTareaList();
			adapter = new TareasAdapter(this.getSherlockActivity()
					.getBaseContext(), tareas);
			setListAdapter(adapter);
		} catch (Exception e) {
			ProgressDialog.show(this.getActivity().getBaseContext(),
					getResources().getString(R.string.Error), getResources()
							.getString(R.string.Error_sin_proyectos), true,
					true);
		}
	}

	private int calcularTareasDone() {
		int done = 0;
		for (Tarea t : p.getTareaList()) {
			if (t.getDone().equals('2'))
				done++;
		}
		return done;
	}

	private void realizarPeticion() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair("accion", Constantes.VER_PROYECTO));
		try {

			d=ProgressDialog.show(this.getSherlockActivity(), getResources().getString(R.string.espere), "", true, true);
			String datos = ServerRequest.send(this.jSessionId, parameters,
					"VER_PROYECTO");
			d.dismiss();
			if (datos.compareTo("null") != 0) {
				p = new Gson().fromJson(datos, Proyecto.class);
				nombreProyecto.setText(p.getNombre());
				descripcionProyecto.setText(p.getDescripcion());
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				fechaEntrega.setText(formato.format(p.getEntrega()));
			} else {
				ProgressDialog.show(getActivity(),
						getResources().getString(R.string.Error),
						getResources().getString(R.string.Error_sin_proyectos),
						true, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Tarea t = p.getTareaList().get(position);
		Dialog dialog = new Dialog(getSherlockActivity());
		dialog.setTitle(t.getDescripcion());
		dialog.setContentView(R.layout.dialogtareas);
		try {
			((TextView) dialog.findViewById(R.id.textTareaDuracionEstimada))
					.setText(t.getDuracionEstimado().toString());
		} catch (Exception e) {
			((TextView) dialog.findViewById(R.id.textTareaDuracionEstimada))
			.setText("0");
		}
		try {
			((TextView) dialog.findViewById(R.id.textTareaDuracion)).setText(t
					.getDuracion().toString());
		} catch (Exception e) {
			((TextView) dialog.findViewById(R.id.textTareaDuracion)).setText("0");
		}
		try {
			((TextView) dialog.findViewById(R.id.textTareaDescripcion)).setText(t
					.getDescripcion());
		} catch (Exception e) {
			((TextView) dialog.findViewById(R.id.textTareaDescripcion)).setText("");
		}
		try {
			((TextView) dialog.findViewById(R.id.textTareaViewPrioridad)).setText(t
					.getPrioridad().toString());
		} catch (Exception e) {
			((TextView) dialog.findViewById(R.id.textTareaViewPrioridad)).setText("0");
		}
		try {
			((TextView) dialog.findViewById(R.id.textTareaDone)).setText(t
					.getDone().toString());
		} catch (Exception e) {
		}
		try {
			((TextView) dialog.findViewById(R.id.textTotalHoras)).setText(t
					.getTotalHoras().toString());
		} catch (Exception e) {
		}
		try {
			((TextView) dialog.findViewById(R.id.textFechaInicio)).setText(t
					.getInicio().toString());
		} catch (Exception e) {
			((TextView) dialog.findViewById(R.id.textFechaInicio))
					.setText(getResources().getString(
							R.string.estado_no_iniciado));
		}
		try {
			((TextView) dialog.findViewById(R.id.textFechaFin)).setText(t
					.getInicio().toString());
		} catch (Exception e) {
			((TextView) dialog.findViewById(R.id.textFechaFin))
					.setText(getResources().getString(
							R.string.estado_finalizado));
		}
		dialog.show();
	}

	public String[] obtenerArray(ArrayList<Tarea> tareas) {
		String[] array = new String[tareas.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = tareas.get(i).getDescripcion();
		}
		return array;
	}
}
