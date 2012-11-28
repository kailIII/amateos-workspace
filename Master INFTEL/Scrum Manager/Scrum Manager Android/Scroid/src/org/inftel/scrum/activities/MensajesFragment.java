package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.View.OnClickListener;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.lists.MensajesAdapter;

import org.inftel.scrum.modelXML.Mensajes;
import org.inftel.scrum.modelXML.Proyecto;
import org.inftel.scrum.modelXML.Usuario;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.drm.ProcessedData;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.R.id;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MensajesFragment extends SherlockListFragment {
	private MensajesAdapter adapter;
	private ArrayList<Mensajes> mensa;
	ProgressDialog d;
	EditText paraNuevo;
	EditText asuntoNuevo;
	EditText contenidoNuevo;

	BigDecimal mensajeaBorrar = null;

	// private String jSessionId;

	private ListView mensajes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("info", "on createView");
		View view = inflater.inflate(R.layout.mensajes, container, false);
		mensajes = (ListView) view.findViewById(android.R.id.list);
		d = ProgressDialog.show(this.getSherlockActivity(), getResources()
				.getString(R.string.espere), "", true, true);
		mensa = buscarMensajes();
		if (mensa != null) {
			adapter = new MensajesAdapter(this.getSherlockActivity()
					.getBaseContext(), mensa);
			setListAdapter(adapter);
		} else {
			ProgressDialog.show(this.getSherlockActivity().getApplicationContext(),
			 getResources().getString(R.string.Error),
			 getResources().getString(R.string.error_mensaje));
		}
		// buscarMensajes();
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Mensajes mensaje = mensa.get(position);
		Log.d("info", "a entrado coño");
		//Toast.makeText(this.getSherlockActivity().getBaseContext(),
		//		"SELECCIONADO: " + position, Toast.LENGTH_SHORT).show();
		crearDialogoMensaje(mensaje);
		if (mensaje.getLeido() == '0')
			seHaLeido(mensaje.getId());
		mensajeaBorrar = mensaje.getId();
		actualizar();

	}

	private void crearDialogoMensaje(Mensajes men) {
		final Dialog d = new Dialog(this.getSherlockActivity());
		d.setTitle("Mensaje:");
		d.setContentView(R.layout.dialogomensaje);

		TextView titulo = (TextView) d.findViewById(id.mensajeTitulo);
		TextView fecha = (TextView) d.findViewById(id.mensajeFecha);
		TextView contenido = (TextView) d.findViewById(id.mensajeContenido);
		Button cerrar = (Button) d.findViewById(id.AceptarVerMensaje);
		Button borrar = (Button) d.findViewById(id.borrarVerMensaje);
		//Button responder = (Button) d.findViewById(id.ResponderVerMensaje);

		titulo.setText("De: " + men.getOrigen().getNombre() + " "
				+ men.getOrigen().getApellidos());
		Calendar cal = Calendar.getInstance();
		cal.setTime(men.getFecha());
		fecha.setText("Fecha: " + cal.get(Calendar.DAY_OF_MONTH) + "/"
				+ cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
				+ ", Asunto: " + men.getAsunto());
		contenido.setText(men.getMensaje());
		contenido.setMovementMethod(new ScrollingMovementMethod());

		cerrar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				d.dismiss();
			}

		});

		borrar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				borrarMensaje();
				d.dismiss();

			}

		});

		/*responder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				crearDialogoMensajeNuevoResponder();
				d.dismiss();

			}

		});*/
		d.show();
	}

	private void crearDialogoMensajeNuevoResponder() {
		final Dialog d = new Dialog(this.getSherlockActivity());
		d.setTitle("Nuevo Mensaje:");
		d.setContentView(R.layout.dialogomensaje);

		TextView titulo = (TextView) d.findViewById(id.mensajeTitulo);
		TextView fecha = (TextView) d.findViewById(id.mensajeFecha);
		TextView contenido = (TextView) d.findViewById(id.mensajeContenido);
		Button enviar = (Button) d.findViewById(id.AceptarVerMensaje);
		Button cancelar = (Button) d.findViewById(id.borrarVerMensaje);

		contenido.setMovementMethod(new ScrollingMovementMethod());

		enviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				crearMensajeRespuesta();
				d.dismiss();

			}

		});
		cancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				d.dismiss();

			}

		});
		d.show();
	}

	private void crearDialogoMensajeNuevo() {
		final Dialog d = new Dialog(this.getSherlockActivity());
		d.setTitle("Nuevo Mensaje:");
		d.setContentView(R.layout.dialog_nuevo_mensaje);

		paraNuevo = (EditText) d.findViewById(id.paraEdit);
		asuntoNuevo = (EditText) d.findViewById(id.asuntoEdit);
		contenidoNuevo = (EditText) d.findViewById(id.contenidoEdit);

		Button enviar = (Button) d.findViewById(id.EnviarMensajeButton);
		Button cancelar = (Button) d.findViewById(id.CancelarMensajeButton);

		enviar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				crearMensajeRespuesta();
				d.dismiss();

			}

		});
		cancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				d.dismiss();

			}

		});
		d.show();
	}

	private void actualizar() {
		mensa = buscarMensajes();
		adapter = new MensajesAdapter(this.getSherlockActivity()
				.getBaseContext(), mensa);
		setListAdapter(adapter);
	}

	private void crearMensajeRespuesta() {
		Log.d("info", "entra en crearmensaje:");
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();

		Mensajes m = new Mensajes();

		SharedPreferences prefs = this.getActivity().getSharedPreferences(
				"loginPreferences", Context.MODE_PRIVATE);
		String origenUsuario = prefs.getString("usuario","default");
		
		m.setAsunto(asuntoNuevo.getText().toString());
		m.setMensaje(contenidoNuevo.getText().toString());
		
		
		Gson gson = new Gson();
		String json = gson.toJson(m);
		parameters.add(new BasicNameValuePair("accion",
				Constantes.CREAR_MENSAJE));
		parameters.add(new BasicNameValuePair("usuarioDestino", paraNuevo
				.getText().toString()));
		parameters.add(new BasicNameValuePair("usuarioOrigen", origenUsuario));
		parameters.add(new BasicNameValuePair("Mensaje", json));
		try {
			d.show();
			String datos = ServerRequest.send("", parameters, "CREAR_MENSAJE");

			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		actualizar();
	}

	private void borrarMensaje() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.ELIMINAR_MENSAJE));
		parameters.add(new BasicNameValuePair("identificador", mensajeaBorrar
				.toString()));
		try {
			d.show();
			String datos = ServerRequest.send("", parameters,
					"ELIMINAR_MENSAJE");
			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
		actualizar();
	}

	private void seHaLeido(BigDecimal id) {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.SET_MENSAJES_LEIDO));
		parameters.add(new BasicNameValuePair("identificador", id.toString()));
		try {
			d.show();
			String datos = ServerRequest.send("", parameters,
					"SET_MENSAJES_LEIDO");
			d.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private void buscarMensajes() {
	private ArrayList<Mensajes> buscarMensajes() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.GET_MENSAJES_RECIBIDOS));
		String datos = null;
		ArrayList<Mensajes> list = null;
		try {
			d.show();
			datos = ServerRequest
					.send("", parameters, "GET_MENSAJES_RECIBIDOS");
			d.dismiss();
			Log.d("info", "hdatos:" + datos);

			Type listOfTestObject = new TypeToken<List<Mensajes>>() {
			}.getType();
			list = new Gson().fromJson(datos, listOfTestObject);

			Log.d("info", "hoalaaaa:" + list.get(1).getMensaje());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private String[] getMensajes(ArrayList<Mensajes> list) {
		String[] mensajes = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			mensajes[i] = "hola";
			// Log.d("info", "holaaaaaa" + list.get(i).getOrigen().getNombre() +
			// ": (" + list.get(i).getFecha() +")"+ list.get(i).getAsunto());
			mensajes[i] = list.get(i).getOrigen().getNombre() + ": ("
					+ list.get(i).getFecha() + ")" + list.get(i).getAsunto();
		}
		return mensajes;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("actualizar").setIcon(R.drawable.ic_actualizar)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add("agregar").setIcon(R.drawable.ic_mas)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("Scroid", "Seleccionado menu: " + item);
		if (item.toString().equals("agregar")) {
			Log.d("info", "agregarmensajeeeeeee");
			crearDialogoMensajeNuevo();
			/*
			 * try { ServerRequest.logout(jSessionId); this.finish(); } catch
			 * (Exception e) { ProgressDialog.show(this.getApplicationContext(),
			 * getResources().getString(R.string.Error),
			 * getResources().getString(R.string.Error_finalizando), true,
			 * true); e.printStackTrace(); }
			 */
		}
		return super.onOptionsItemSelected(item);
	}

}
