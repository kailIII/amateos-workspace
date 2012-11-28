package org.inftel.scrum.listeners;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import org.inftel.scrum.R;
import org.inftel.scrum.activities.TareaActivity;
import org.inftel.scrum.lists.PokerCardsAdapter;
import org.inftel.scrum.modelXML.Tarea;
import org.inftel.scrum.modelXML.Usuario;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TareaOnClickListener implements OnClickListener {

	private static final String TAG = TareaOnClickListener.class
			.getSimpleName();
	private static final int FECHA_INICIO = 0;
	private static final int FECHA_FIN = 1;

	private TareaActivity activity;
	private Tarea tarea;
	private Context ctx;
	private ArrayList<Usuario> usuarios;

	/**
	 * Constructor
	 * 
	 * @param fragment
	 * @param tarea
	 */
	public TareaOnClickListener(TareaActivity activity, Tarea tarea) {
		this.activity = activity;
		this.tarea = tarea;
		this.ctx = activity;
	}

	/**
	 * Constructor
	 * 
	 * @param fragment
	 * @param tarea
	 * @param usuarios
	 */
	public TareaOnClickListener(TareaActivity activity, Tarea tarea,
			ArrayList<Usuario> usuarios) {
		this.activity = activity;
		this.tarea = tarea;
		this.ctx = activity;
		this.usuarios = usuarios;
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.tarea_descripcion_modificar:
			Log.d(TAG, "Pulsado -> descripcion");
			dialogDescripcion();
			break;

		case R.id.tarea_duracion_estimada_modificar:
			Log.d(TAG, "Pulsado -> duracion estimada");
			dialogDuracionEstimada();
			break;

		case R.id.tarea_duracion_restante_mas:
			Log.d(TAG, "Pulsado -> duracion restante MAS");
			tarea.setDuracion(tarea.getDuracion().add(BigInteger.ONE));
			activity.setValoresUI();
			break;

		case R.id.tarea_duracion_restante_menos:
			Log.d(TAG, "Pulsado -> duracion restante MENOS");
			tarea.setDuracion(tarea.getDuracion().subtract(BigInteger.ONE));
			if (tarea.getDuracion().compareTo(BigInteger.ZERO) == 0) {
				tarea.setDone('2'); // si quedan 0 horas -> tarea DONE
			}
			activity.setValoresUI();
			break;

		case R.id.tarea_estado_modificar:
			Log.d(TAG, "Pulsado -> estado");
			dialogEstado();
			break;

		case R.id.tarea_fechas_fin_modificar:
			Log.d(TAG, "Pulsado -> fecha FIN");
			dialogFecha(FECHA_FIN);
			break;

		case R.id.tarea_fechas_inicio_modificar:
			Log.d(TAG, "Pulsado -> fecha INICIO");
			dialogFecha(FECHA_INICIO);
			break;

		case R.id.tarea_usuario_modificar:
			Log.d(TAG, "Pulsado -> usuario");
			dialogUsuario();
			break;

		}

	}

	/**
	 * Muestra un dialog para seleccionar el usuario de entre la lista de
	 * usuarios
	 */
	private void dialogUsuario() {

		Log.d("TareaOnClick", "USUARIOS DIALOG -> " + usuarios);

		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.dialog_usuarios);
		RadioGroup rg = (RadioGroup) dialog
				.findViewById(R.id.dialog_usuarios_radiogroup);

		if (usuarios != null && usuarios.size() > 0) {
			dialog.setTitle(ctx.getString(R.string.dialog_usuarios_titulo));

			// Se agregan los usuarios al radio group
			RadioButton b;
			for (Usuario u : usuarios) {
				b = new RadioButton(ctx);
				b.setText(u.getEmail());
				b.setTextColor(ctx.getResources().getColor(R.color.blanco));
				rg.addView(b);
			}
			// Listener
			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(RadioGroup group, int checkedId) {

					RadioButton rb = (RadioButton) dialog
							.findViewById(checkedId);

					// Se obtiene el usuario seleccionado
					for (Usuario u : usuarios) {
						if (u.getEmail().equals(rb.getText())) {

							// Se establece el usuario en la tarea y se repinta
							// la UI
							tarea.setUsuarioId(u);
							activity.setValoresUI();
						}
					}
					dialog.dismiss();
				}
			});
		} else { // No existen usuarios
			dialog.setTitle(ctx.getString(R.string.dialog_usuarios_no_usuarios));
		}
		dialog.show();
	}

	/**
	 * Muestra un dialog para seleccionar el estado de la tarea
	 */
	private void dialogEstado() {

		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.dialog_estado);
		RadioGroup rg = (RadioGroup) dialog
				.findViewById(R.id.dialog_estado_radiogroup);

		dialog.setTitle(ctx.getString(R.string.dialog_estado_titulo));

		// Listener
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.dialog_estado_todo:
					tarea.setDone('0');
					break;
				case R.id.dialog_estado_doing:
					tarea.setDone('1');
					break;
				case R.id.dialog_estado_done:
					tarea.setDone('2');
					tarea.setFechaFin(new Date());
					break;
				}

				// Se repinta la UI
				activity.setValoresUI();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Muestra un dialog para seleccionar una fecha
	 */
	private void dialogFecha(final int tipoFecha) {

		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.dialog_fecha);
		final DatePicker dt = (DatePicker) dialog
				.findViewById(R.id.dialog_fecha_datepicker);

		Button b = (Button) dialog.findViewById(R.id.dialog_fecha_boton);

		dialog.setTitle(ctx.getString(R.string.dialog_fecha_titulo));

		// Listener
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Date date = new Date(dt.getYear() - 1900, dt.getMonth(), dt
						.getDayOfMonth());

				switch (tipoFecha) {
				case FECHA_INICIO:
					tarea.setInicio(date);
					break;
				case FECHA_FIN:
					tarea.setFechaFin(date);
					break;
				}
				activity.setValoresUI();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Muestra un dialog para seleccionar la duración estimada
	 */
	private void dialogDuracionEstimada() {

		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.dialog_duracion_estimada);
		dialog.setTitle(ctx.getString(R.string.dialog_duracion_estimada_titulo));

		GridView grid = (GridView) dialog
				.findViewById(R.id.dialog_duracion_estimada_gridview);
		grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		grid.setAdapter(new PokerCardsAdapter(ctx));

		grid.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parente, View v,
					int position, long id) {
				String carta = ((TextView) v).getText().toString();

				if (carta.equals("\u221E")) {
					carta = "1000";
				}
				tarea.setDuracionEstimado(new BigInteger(carta));
				tarea.setDuracion(new BigInteger(carta));
				activity.setValoresUI();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * Muestra un dialog para introducir la descripción de una tarea
	 */
	private void dialogDescripcion() {

		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.dialog_descripcion);
		dialog.setTitle(ctx.getString(R.string.dialog_descripcion_titulo));
		final EditText descripcion = (EditText) dialog
				.findViewById(R.id.dialog_descripcion_texto);
		Button boton = (Button) dialog
				.findViewById(R.id.dialog_descripcion_boton);

		boton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tarea.setDescripcion(descripcion.getText().toString());
				activity.setValoresUI();
				dialog.dismiss();
			}
		});

		dialog.show();
	}
}
