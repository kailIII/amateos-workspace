package org.inftel.anmap.list;

import java.util.ArrayList;

import org.inftel.anmap.R;
import org.inftel.anmap.vos.Host;
import org.inftel.anmap.vos.Puerto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RedAdapter extends ArrayAdapter<Host> {

	private Context context;
	private ArrayList<Host> hosts;

	public final static String TAG = "RedAdapter";

	public RedAdapter(Context context, ArrayList<Host> hosts) {
		super(context, R.layout.red, hosts);

		this.context = context;
		this.hosts = hosts;

	}

	@Override
	public int getCount() {
		return hosts.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.red_fila, parent, false);

		// Obtención de los elementos de la UI de la fila
		TextView nombre = (TextView) rowView.findViewById(R.id.red_fila_nombre);
		TextView ip = (TextView) rowView.findViewById(R.id.red_fila_ip);
		TextView mac = (TextView) rowView.findViewById(R.id.red_fila_mac);
		TextView puertos = (TextView) rowView
				.findViewById(R.id.red_fila_puertos);
		ImageView imagen = (ImageView) rowView
				.findViewById(R.id.red_fila_imagen);

		// Host a pintar en la vista
		Host host = hosts.get(position);

		if (host.getRouter()) {
			imagen.setImageResource(R.drawable.router);
		} else {
			imagen.setImageResource(R.drawable.pc);
		}

		nombre.setText(host.getHostname());
		ip.setText(host.getIp());
		if (host.getFabricante() != null && !host.getFabricante().isEmpty()) {
			mac.setText(host.getMac() + " (" + host.getFabricante() + ")");
		} else {
			mac.setText(host.getMac() + " (" + host.getFabricante() + ")");
		}

		String puertosAbiertos = "TCP: ";
		if (host.getPuertos() != null && host.getPuertos().size() > 0) {
			for (Puerto p : host.getPuertos()) {
				puertosAbiertos += p.getNumero() + ",";
			}
			puertos.setTextColor(context.getResources().getColor(R.color.verde));
		} else {
			puertos.setTextColor(context.getResources().getColor(R.color.rojo));
			puertosAbiertos += "no hay puertos abiertos.";
		}

		puertos.setText(puertosAbiertos);

		return rowView;
	}

	/**
	 * Vacía la lista de hosts
	 */
	public void borrarHosts(){
		this.hosts = new ArrayList<Host>();
		this.notifyDataSetChanged();
	}
	
	/**
	 * Agrega un host a la lista
	 * 
	 * @param host
	 */
	public void addHost(Host host) {
		Log.d(TAG,"AGREGADO HOST - SIZE -> "+hosts.size());
		this.hosts.add(host);
		// Se repinta la vista
		this.notifyDataSetChanged();
	}

	/**
	 * Actualiza un host de la lista
	 * 
	 * @param host
	 */
	public void updateHost(Host host) {
		int index = hosts.indexOf(host);
		hosts.remove(index);
		hosts.add(index, host);

		this.notifyDataSetChanged();
	}

	
	/**
	 * Actualiza la lista de hosts
	 * 
	 * @param hosts
	 */
	public void updateList(ArrayList<Host> hosts) {
		this.hosts = hosts;
		// Se repinta la vista
		this.notifyDataSetChanged();
	}

	
	public Host getHost(int index){
		return hosts.get(index);
	}
	
}
