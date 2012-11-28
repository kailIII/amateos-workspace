package org.inftel.scrum.lists;

import java.math.BigInteger;
import java.util.ArrayList;

import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Tarea;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TareasAdapter extends ArrayAdapter<Tarea> {

	private Context context;
	private ArrayList<Tarea> tareas;
	private float tamTexto;
	private static final String TAG = "TareasAdapter";

	public TareasAdapter(Context context, ArrayList<Tarea> tareas) {
		super(context, R.layout.tareas, tareas);
		this.context = context;
		this.tareas = tareas;
	}

	@Override
	public int getCount() {
		return tareas.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.tareas_list, parent, false);

		Log.d(TAG, "ENTRA ADAPTER. POS -> " + position);

		// Obtención de los elementos de la UI de la fila
		TextView descripcion = (TextView) rowView
				.findViewById(R.id.tareas_list_item_descripcion);
		ImageView prioridad = (ImageView) rowView
				.findViewById(R.id.tareas_list_item_prioridad);
		TextView estado = (TextView) rowView
				.findViewById(R.id.tareas_list_item_estado);

		// Tarea a pintar en la vista
		Tarea tarea = tareas.get(position);

		// Se establecen los valores de las vistas
		descripcion.setText(tarea.getDescripcion());

		Log.d(TAG, "PRIORIDAD -> " + tarea.getPrioridad());

		if (tarea.getPrioridad() != null
				&& tarea.getPrioridad().equals(BigInteger.ZERO)) {
			prioridad.setImageResource(R.drawable.urgent);
		}

		if (tarea.getDone().equals('0')) {
			estado.setText(context.getString(R.string.todo));
			estado.setBackgroundResource(R.color.rojo);
		} else if (tarea.getDone().equals('1')) {
			estado.setText(context.getString(R.string.doing));
			estado.setBackgroundResource(R.color.amarillo);
		} else {
			estado.setText(context.getString(R.string.done));
			estado.setBackgroundResource(R.color.verde);
		}

		return rowView;
	}

	public void updateList(ArrayList<Tarea> tareas) {
		this.tareas = tareas;
		// Se repinta la vista
		this.notifyDataSetChanged();
	}
}
