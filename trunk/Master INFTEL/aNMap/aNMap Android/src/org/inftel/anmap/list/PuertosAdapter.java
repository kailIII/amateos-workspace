package org.inftel.anmap.list;

import java.util.ArrayList;

import org.inftel.anmap.R;
import org.inftel.anmap.vos.Host;
import org.inftel.anmap.vos.Puerto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PuertosAdapter extends ArrayAdapter<Puerto> {

	public final static String TAG = "Puertosdapter";

	private Context context;
	private ArrayList<Puerto> puertos;

	public PuertosAdapter(Context context, ArrayList<Puerto> puertos) {
		super(context, R.layout.puertos, puertos);

		this.context = context;
		this.puertos = puertos;
	}

	@Override
	public int getCount() {
		if (puertos != null) {
			return puertos.size();
		} else {
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.puertos_fila, parent, false);

		// Obtención de los elementos de la UI de la fila
		TextView puerto = (TextView) rowView
				.findViewById(R.id.puertos_fila_puerto);
		TextView protocol = (TextView) rowView
				.findViewById(R.id.puertos_fila_descripcion);
		TextView rw = (TextView) rowView.findViewById(R.id.puertos_fila_rw);

		// Puerto a pintar en la vista
		Puerto p = puertos.get(position);

		puerto.setText(p.getNumero() + "/" + p.getTipo());

		if (p.getProtocolo() != null && !p.getProtocolo().isEmpty()) {
			protocol.setText(p.getProtocolo());
		} else {
			protocol.setText(context.getString(R.string.protocolo_desconocido));
		}

		switch (p.getRw()) {
		case Puerto.READ:
			rw.setText("r/-");
			break;
		case Puerto.WRITE:
			rw.setText("-/w");
			break;
		case Puerto.READ_WRITE:
			rw.setText("r/w");
			break;

		default:
			rw.setText("-/-");
			break;
		}

		return rowView;
	}

	/**
	 * Agrega un puerto a la lista
	 * 
	 * @param puerto
	 */
	public void addHost(Puerto puerto) {
		this.puertos.add(puerto);
		// Se repinta la vista
		this.notifyDataSetChanged();
	}

	/**
	 * Actualiza la lista de puertos
	 * 
	 * @param puertos
	 */
	public void updateList(ArrayList<Puerto> puertos) {
		this.puertos = puertos;
		// Se repinta la vista
		this.notifyDataSetChanged();
	}

}
