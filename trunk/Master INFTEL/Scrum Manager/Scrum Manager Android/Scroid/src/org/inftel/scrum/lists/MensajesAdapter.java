package org.inftel.scrum.lists;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Mensajes;
import org.inftel.scrum.modelXML.Sprint;
import org.inftel.scrum.modelXML.Tarea;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MensajesAdapter extends ArrayAdapter<Mensajes> {

	private Context context;
	private ArrayList<Mensajes> mensajes;
	private float tamTexto;
	private static final String TAG = "SprintsAdapter";

	public MensajesAdapter(Context context, ArrayList<Mensajes> mensajes) {
		super(context, R.layout.mensajes, mensajes);
		this.context = context;
		this.mensajes = mensajes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.mensajes_list, parent, false);

		Log.d(TAG, "ENTRA ADAPTER. POS -> " + position);

		// Obtenci—n de los elementos de la UI de la fila
		TextView origen = (TextView) rowView
				.findViewById(R.id.mensajes_list_item_origen_label);
		TextView asunto = (TextView) rowView
				.findViewById(R.id.sprints_list_item_fin_label);
		ImageView termometro = (ImageView) rowView
				.findViewById(R.id.sprints_list_item_termometro);
		TextView estado = (TextView) rowView
				.findViewById(R.id.sprints_list_item_estado);

		// Sprint a pintar en la vista
		Mensajes mensaje = mensajes.get(position);

		// Se establecen los valores de las vistas
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

		origen.setText(context.getString(R.string.de)
				+ mensaje.getOrigen().getEmail());
		asunto.setText(context.getString(R.string.asunto) + mensaje.getAsunto());

		if (mensaje.getLeido()== '1') {
			termometro.setImageResource(R.drawable.sobre_abierto);
		} else {
			termometro.setImageResource(R.drawable.sobre_cerrado);
		}

		/*
		 * 
		 * double tareasFinalizadas = 0; for (Tarea t : sprint.getTareaList()) {
		 * if (t.getDone() == '2') { tareasFinalizadas++; } }
		 * 
		 * double totales = (double) sprint.getTareaList().size(); double
		 * porcentajeTareasFinalizadas = (tareasFinalizadas / totales) * 100;
		 * 
		 * // Se establecen los valores de las vistas DateFormat df =
		 * DateFormat.getDateInstance(DateFormat.MEDIUM);
		 * 
		 * inicio.setText(context.getString(R.string.inicio) +
		 * df.format(sprint.getInicio()));
		 * fin.setText(context.getString(R.string.fin) +
		 * df.format(sprint.getFin()));
		 * 
		 * if (sprint.getInicio().after(new Date())) { // No iniciado
		 * estado.setText(R.string.estado_no_iniciado); } else { if
		 * (sprint.getFin().before(new Date())) { // Finalizado
		 * estado.setText(R.string.estado_finalizado);
		 * estado.setBackgroundResource(R.color.verde); } else { // En curso
		 * estado.setText(R.string.estado_en_curso);
		 * estado.setBackgroundResource(R.color.rojo); } }
		 * 
		 * if (porcentajeTareasFinalizadas < 33) {
		 * termometro.setImageResource(R.drawable.termometro_rojo); } else if
		 * (porcentajeTareasFinalizadas > 66) {
		 * termometro.setImageResource(R.drawable.termometro_verde); } else {
		 * termometro.setImageResource(R.drawable.termometro_amarillo); }
		 */

		return rowView;
	}

	public void updateList(ArrayList<Mensajes> mensajes) {
		this.mensajes = mensajes;
		// Se repinta la vista
		this.notifyDataSetChanged();
	}
}
