package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Reuniones;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.CalendarView;
import org.inftel.scrum.utils.Cell;
import org.inftel.scrum.utils.Constantes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ReunionFragment extends SherlockFragment implements
		CalendarView.OnCellTouchListener {

	public static final String TAG = "ReunionFragment";
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	private CalendarView mView = null;
	private TextView mAnyo;
	private TextView mMes;
	private Button mSiguiente;
	private Button mAnterior;
	private Button crearReunion;
	private Button enviarReunion;
	private TextView lugar;
	private TextView asunto;
	private EditText asuntoEdit;
	private EditText lugarEdit;
	private TextView mensaje;
	private int diaTocado;
	private Dialog dialogoEnviar;
	private Dialog dialog;
	int cont = 0;
	private Reuniones r;
	private ArrayList<Reuniones> reuniones = new ArrayList<Reuniones>();
	ProgressDialog d;

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
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.reuniones, container, false);
		mView = (CalendarView) view.findViewById(R.id.calendar);
		mAnyo = (TextView) view.findViewById(R.id.anyoLabel);
		mMes = (TextView) view.findViewById(R.id.mesLabel);
		mAnterior = (Button) view.findViewById(R.id.anteriorButton);
		mSiguiente = (Button) view.findViewById(R.id.siguienteButton);
//		 d=ServerRequest.compruebaConexionServer(this.getActivity());
		realizarPeticion();

		mMes.setText(Calendar.getInstance().getDisplayName(Calendar.MONTH,
				Calendar.LONG, Locale.ENGLISH));

		mAnyo.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		mView.setOnCellTouchListener(this);

		mAnterior.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				mView.previousMonth();
				mMes.setText(DateUtils.getMonthString(mView.getMonth(),
						DateUtils.LENGTH_LONG));
				mAnyo.setText(String.valueOf(mView.getYear()));

			}
		});

		mSiguiente.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				mView.nextMonth();
				mMes.setText(DateUtils.getMonthString(mView.getMonth(),
						DateUtils.LENGTH_LONG));
				mAnyo.setText(String.valueOf(mView.getYear()));

			}
		});

		return view;
	}

	public void realizarPeticion() {

		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.BUSCAR_REUNIONES));
		try {
			d=ProgressDialog.show(this.getSherlockActivity(), getResources().getString(R.string.espere), "", true, true);
			String datos = ServerRequest.send("", parameters,
					"BUSCAR_REUNIONES");
			d.dismiss();
			Type listOfTestObject = new TypeToken<List<Reuniones>>() {
			}.getType();
			reuniones = new Gson().fromJson(datos, listOfTestObject);

			anyadirReunionesCalendario();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void anyadirReunionesCalendario() {

		for (int i = 0; i < reuniones.size(); i++) {

			Calendar fecha = formateaFecha(reuniones.get(i).getFecha());
			mView.addReuniones(fecha.get(Calendar.DAY_OF_MONTH),
					fecha.get(Calendar.MONTH) + 1);
		}

	}

	public Calendar formateaFecha(Date fecha) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);

		return cal;
	}

	public Reuniones buscarReunionByFecha(Calendar fecha) {

		Reuniones r = new Reuniones();
		boolean encontrada = false;

		for (int i = 0; i < reuniones.size() && !encontrada; i++) {

			Calendar cal = formateaFecha(reuniones.get(i).getFecha());

			int dia = cal.get(Calendar.DAY_OF_MONTH);
			int mes = cal.get(Calendar.MONTH + 1);

			if (dia == fecha.get(Calendar.DAY_OF_MONTH)
					&& mes == fecha.get(Calendar.MONTH + 1)) {

				Reuniones r2 = reuniones.get(i);
				encontrada = true;
				r.setAsunto(r2.getAsunto());
				r.setLugar(r2.getLugar());
				r.setUsuarioId(r2.getUsuarioId());

			}

		}

		if (encontrada) {
			return r;
		} else
			return null;

	}

	public void onTouch(Cell cell) {

		diaTocado = cell.getDayOfMonth();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, diaTocado);
		c.set(Calendar.MONTH, mView.getMonth());

		Reuniones reu = buscarReunionByFecha(c);

		if (reu != null) {

			Toast.makeText(
					this.getActivity(),
					"Asunto: " + reu.getAsunto() + "\nLugar: " + reu.getLugar()
							+ " \nCreada por:" + reu.getUsuarioId().getEmail(),
					Toast.LENGTH_LONG).show();
		} else {
			
			crearReunion();
			if(diaTocado > Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && c.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)){
			
				dialog.show();
			}
			else{
				if(c.get(Calendar.MONTH) != Calendar.getInstance().get(Calendar.MONTH)){
						dialog.show();
				
				}
			}
		}
	}
	
	public void crearReunion(){
		
		dialog = new Dialog(getSherlockActivity());
		dialog.setContentView(R.layout.dialog_nueva_reunion);

		mensaje = (TextView) dialog.findViewById(R.id.mensajeNuevaReunion); 
		crearReunion = (Button) dialog
				.findViewById(R.id.crearReunionButton);

		Log.d(TAG, "crearReunion: " + crearReunion);

		crearReunion.setOnClickListener(new OnClickListener() { 

			public void onClick(View v) {

				crearDialogoNuevaReunion();
			}

		});

		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG, "entrando en onCancel...");
				realizarPeticion();
			}

		});

	}

	public void crearDialogoNuevaReunion() {

		dialogoEnviar = new Dialog(getSherlockActivity());
		dialogoEnviar.setContentView(R.layout.dialog_enviar_reunion);

		enviarReunion = (Button) dialogoEnviar
				.findViewById(R.id.enviarNuevaReunionButton);
		lugar = (TextView) dialogoEnviar.findViewById(R.id.lugarReunionLabel);
		asunto = (TextView) dialogoEnviar.findViewById(R.id.asuntoLabel);
		asuntoEdit = (EditText) dialogoEnviar.findViewById(R.id.asuntoEdit);
		lugarEdit = (EditText) dialogoEnviar.findViewById(R.id.lugarEdit);

		enviarReunion.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				enviarReunion();
				realizarPeticion();
				dialogoEnviar.dismiss();
				dialog.dismiss();

			}

		});

		dialogoEnviar.show();
	}

	public void enviarReunion() {

		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion",
				Constantes.CREAR_REUNIONES));
		try {

			Reuniones r = new Reuniones();
			r.setAsunto(asuntoEdit.getText().toString());
			r.setLugar(lugarEdit.getText().toString());

			Date fecha = new Date();
			fecha.setDate(diaTocado);
			fecha.setMonth(mView.getMonth());

			r.setFecha(fecha);

			Gson gson = new Gson();
			String json = gson.toJson(r);

			parameters.add(new BasicNameValuePair("CREAR_REUNIONES", json));
			try {
				d=ProgressDialog.show(this.getSherlockActivity(), getResources().getString(R.string.espere), "",true, true);
				ServerRequest.send("", parameters, Constantes.CREAR_REUNIONES);
				d.dismiss();
			} catch (Exception e) {

				e.printStackTrace();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		menu.add(this.getString(R.string.cronometro))
				.setIcon(R.drawable.ic_reloj)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add("actualizar").setIcon(R.drawable.ic_actualizar)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Log.d("Scroid", "Seleccionado menu: " + item);
		if (item.getTitle().equals(this.getString(R.string.cronometro))) {
			/*
			 * CronometroFragment f = new CronometroFragment();
			 * 
			 * FragmentTransaction transaction = getFragmentManager()
			 * .beginTransaction(); transaction.replace(android.R.id.content,
			 * f); transaction.addToBackStack("cronometroFragment");
			 * transaction.commit();
			 */

			Intent i = new Intent(this.getSherlockActivity(),CronometroActivity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

}
