package org.inftel.pasos.activities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.inftel.pasos.R;
import org.inftel.pasos.controlador.Controlador;
import org.inftel.pasos.list.ContactosAdapter;
import org.inftel.pasos.modelo.Modelo;
import org.inftel.pasos.utils.Utils;
import org.inftel.pasos.vos.ContactoEnvio;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ZoomControls;

public class ContactosActivity extends ListActivity implements Observer {

	private static final String TAG = ContactosActivity.class.getSimpleName();

	private ContactosAdapter adapter;
	private ArrayList<ContactoEnvio> contactos;
	private ZoomControls zoom;
	private ListView listView;
	private Context context;

	private Modelo modelo;
	private Controlador controlador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getBaseContext();

		initModeloControlador();
		initLayout();

		zoom = (ZoomControls) findViewById(R.id.zoomControlsContactos);
		zoom.setOnZoomInClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.d(TAG, "ZOOM IN!");
				controlador.aumentarTexto();
			}
		});
		zoom.setOnZoomOutClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.d(TAG, "ZOOM OUT!");
				controlador.disminuirTexto();
			}
		});

		// PETICIÓN CONTACTOS
		contactos = Utils.getContactosFromServer(context);
		//getContactos();

		adapter = new ContactosAdapter(this, contactos);
		setListAdapter(adapter);

		listView = (ListView) findViewById(R.layout.contactos);

	}

	private void getContactos() {
		ContactoEnvio c1 = new ContactoEnvio();
		ContactoEnvio c2 = new ContactoEnvio();
		ContactoEnvio c3 = new ContactoEnvio();
		ContactoEnvio c4 = new ContactoEnvio();

		c1.setNombre("Pepe Martinez");
		c1.setEmail("pepe@gmail.com");
		c1.setTelefonoContacto(new BigInteger("10"));

		c2.setNombre("Manolo Martinez");
		c2.setEmail("pepe@gmail.com");
		c2.setTelefonoContacto(new BigInteger("10"));

		c3.setNombre("Juan Martinez");
		c3.setEmail("pepe@gmail.com");
		c3.setTelefonoContacto(new BigInteger("10"));

		c4.setNombre("Sergio Martinez");
		c4.setEmail("pepe@gmail.com");
		c4.setTelefonoContacto(new BigInteger("10"));

		contactos = new ArrayList<ContactoEnvio>();
		contactos.add(c1);
		contactos.add(c2);
		contactos.add(c3);
		contactos.add(c4);
	}

	private void initLayout() {
		setTheme(controlador.getTema());
		setContentView(R.layout.contactos);
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
		adapter.setTamTexto(controlador.getTamTexto());
		adapter.notifyDataSetChanged();
	}

	// MENU
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_contactos, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		switch (item.getItemId()) {
		case R.id.menu_alarma:

			intent = new Intent(this, PasosActivity.class);
			startActivity(intent);

			return true;

		case R.id.menu_configuracion:

			intent = new Intent(this, PreferenciasActivity.class);
			startActivity(intent);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onContactoClick(View v) {

		TextView tv = (TextView) v;
		Log.d(TAG, "PULSADO -> " + tv.getText());

		ContactoEnvio c = null;
		for (ContactoEnvio con : contactos) {
			if (con.getNombre().equals(tv.getText().toString())) {
				c = con;
			}
		}
		Log.d(TAG, "OBTENIDO -> " + c);

		// REALIZAR LLAMADA
		if (c != null) {
			try {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+c.getTelefonoContacto().toString()));
				startActivity(intent);
			} catch (Exception e) {
				Log.d(TAG, "No ha sido posible realizar la llamada: "+e.getMessage());
			}
		}

	}
}
