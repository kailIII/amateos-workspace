package org.inftel.anmap.async;

import java.util.ArrayList;
import java.util.HashMap;

import org.inftel.anmap.activities.RedActivity;
import org.inftel.anmap.list.RedAdapter;
import org.inftel.anmap.util.ThreadUtil;
import org.inftel.anmap.vos.Host;
import org.inftel.anmap.vos.Puerto;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ScanPortTask extends AsyncPortScan {

	private Activity activity;
	private SharedPreferences prefs;
	private Host host;

	private HashMap<String, String> puertosTCP;
	private HashMap<String, String> puertosUDP;

	
	private RedAdapter adapter;

	public ScanPortTask(Activity activity, String ip, int _rate, Host host,
			HashMap<String, String> puertosTCP,
			HashMap<String, String> puertosUDP, RedAdapter adapter) {
		super(activity, ip, _rate);
		this.activity = activity;
		this.host = host;
		this.puertosTCP = puertosTCP;
		this.puertosUDP = puertosUDP;
		this.adapter = adapter;
	}

	@Override
	protected void onPreExecute() {

		prefs = PreferenceManager.getDefaultSharedPreferences(activity
				.getApplicationContext());
		
		int rangoPuertos = Integer.parseInt(prefs.getString("puertos", "1"));
		switch(rangoPuertos){
		case 0:
			port_start = 1;
			port_end = 65535;
			break;
		case 1:
			port_start = 1;
			port_end = 1023;
			break;
		case 2:
			port_start = 1024;
			port_end = 49151;
			break;
		case 3:
			port_start = 49152;
			port_end = 65535;
		}
		
		Log.d("prefs", "RANGO PORTS -> "+port_start+" - "+port_end);

		nb_port = port_end - port_start + 2;
	}

	@Override
	protected void onPostExecute(Void unused) {
		adapter.addHost(host);
		ThreadUtil.restarEscaneados();
		Log.d("THREAD","Escaneados -> "+ThreadUtil.getEscaneados());

		if(ThreadUtil.getEscaneados() == 0){
	        Toast.makeText(activity,"Scan finalizado...", Toast.LENGTH_LONG).show();
	        ((RedActivity) activity).progressBar.setVisibility(View.INVISIBLE);
		}
		cancel(true);
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		if (!isCancelled()) {
			if (values.length == 3) {
				final Integer port = (Integer) values[0];
				final int type = (Integer) values[1];
				if (!port.equals(new Integer(0))) {
					if (type == AsyncPortScan.OPEN) {
						Log.d(getClass().getSimpleName(), port + " OPEN");
						Log.d("SCANPUERTOS","ENCONTRADO PUERTO: "+port+" PARA IP -> "+host.getIp());
						if (host.getPuertos() != null) {
							host.getPuertos().add(
									new Puerto(port, puertosTCP.get(port
											.toString()), Puerto.TCP, 0));
							Log.d("SCANPUERTOS","PUERTOS NOT NULL -> "+host.getPuertos().size());

						} else {
							ArrayList<Puerto> puertos = new ArrayList<Puerto>();
							puertos.add(new Puerto(port, puertosTCP.get(port
									.toString()), Puerto.TCP, 0));
							host.setPuertos(puertos);
							Log.d("SCANPUERTOS","PUERTOS NULL -> "+host.getPuertos().size());

						}
					} else if (type == AsyncPortScan.CLOSED) {
					} else if (type == AsyncPortScan.UNREACHABLE) {
					} else if (type == AsyncPortScan.TIMEOUT) {
					} else if (type == AsyncPortScan.FILTERED) {
					}
				} else {

				}
			}
		}
	}
}
