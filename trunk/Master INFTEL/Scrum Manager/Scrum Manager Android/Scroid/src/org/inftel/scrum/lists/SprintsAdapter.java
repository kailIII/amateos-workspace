package org.inftel.scrum.lists;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Sprint;
import org.inftel.scrum.modelXML.Tarea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SprintsAdapter extends ArrayAdapter<Sprint> {

	private Context context;
	private ArrayList<Sprint> sprints;
	private static final String TAG = "SprintsAdapter";

	public SprintsAdapter(Context context, ArrayList<Sprint> sprints) {
		super(context, R.layout.sprints, sprints);
		this.context = context;
		this.sprints = sprints;
	}

	@Override
	public int getCount() {
		return sprints.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.sprints_list, parent, false);

		// Obtenci�n de los elementos de la UI de la fila
		TextView inicio = (TextView) rowView
				.findViewById(R.id.sprints_list_item_inicio_label);
		TextView fin = (TextView) rowView
				.findViewById(R.id.sprints_list_item_fin_label);
		ImageView termometro = (ImageView) rowView
				.findViewById(R.id.sprints_list_item_termometro);
		TextView estado = (TextView) rowView
				.findViewById(R.id.sprints_list_item_estado);

		// Sprint a pintar en la vista
		Sprint sprint = sprints.get(position);

		double tareasFinalizadas = 0;
		for (Tarea t : sprint.getTareaList()) {
			if (t.getDone() == '2') {
				tareasFinalizadas++;
			}
		}

		double totales = (double) sprint.getTareaList().size();
		double porcentajeTareasFinalizadas = (tareasFinalizadas / totales) * 100;
		if (Double.isNaN(porcentajeTareasFinalizadas)) {
			porcentajeTareasFinalizadas = 0;
		}

		// Se establecen los valores de las vistas
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

		inicio.setText(context.getString(R.string.inicio)
				+ df.format(sprint.getInicio()));
		fin.setText(context.getString(R.string.fin)
				+ df.format(sprint.getFin()));

		if (sprint.getInicio().after(new Date())) { // No iniciado
			estado.setText(R.string.estado_no_iniciado);
		} else {
			if (sprint.getFin().before(new Date())) { // Finalizado
				estado.setText(R.string.estado_finalizado);
				estado.setBackgroundResource(R.color.verde);
			} else { // En curso
				estado.setText(R.string.estado_en_curso);
				estado.setBackgroundResource(R.color.rojo);
			}
		}

		if (porcentajeTareasFinalizadas <= 34) {
			termometro.setImageResource(R.drawable.termometro_rojo);
		} else if (porcentajeTareasFinalizadas > 66) {
			termometro.setImageResource(R.drawable.termometro_verde);
		} else {
			termometro.setImageResource(R.drawable.termometro_amarillo);
		}

		return rowView;
	}

	public void updateList(ArrayList<Sprint> sprints) {
		this.sprints = sprints;
		// Se repinta la vista
		this.notifyDataSetChanged();
	}
}
