package org.inftel.anmap.async;

import java.util.HashMap;

import org.inftel.anmap.list.RedAdapter;
import org.inftel.anmap.util.RedUtil;
import org.inftel.anmap.util.ThreadUtil;
import org.inftel.anmap.vos.Host;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class GetHostsAsync extends AsyncTask<String, Integer, Host> {

	private static final String TAG = "GetHostsAsync";

	private Activity redActivity;
	private RedAdapter adapter;
	private HashMap<String, String> puertosTCP;
    private HashMap<String, String> puertosUDP;
    
	private SharedPreferences prefs;



	public GetHostsAsync(Activity redActivity, RedAdapter adapter,HashMap<String, String> puertosTCP, HashMap<String, String> puertosUDP) {
		this.redActivity = redActivity;
		this.adapter = adapter;
		this.puertosTCP = puertosTCP;
		this.puertosUDP = puertosUDP;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		prefs = PreferenceManager.getDefaultSharedPreferences(redActivity);
	}

	@Override
	protected Host doInBackground(String... ips) {
		
		String[] ipPartes = ips[0].split("\\.");
		String ipIni = ipPartes[0] + "." + ipPartes[1] + "." + ipPartes[2]
				+ ".";

		for (int i = 1; i < 256; i++) {

			if (!(ipIni + i).equals(ips[0])) {
				final String ipScan = ipIni+i;

				new Thread(){
					@Override
					public void run() {
						super.run();
						// Se espera hasta que el número de hebras ejecutándose sea
						// menor que MAX_HEBRAS
						while (ThreadUtil.getHebras() >= ThreadUtil.MAX_HEBRAS) {}
						ThreadUtil.inicioHebra();
						Host host = RedUtil.findHost(redActivity,ipScan);
						ThreadUtil.finHebra();

						if (host != null) {
							Log.d(TAG, "Host obtenido NO NULL -> "+ host.getIp());
							ThreadUtil.sumarEscaneados();
							Log.d("THREAD","Escaneados -> "+ThreadUtil.getEscaneados());

							// Se realiza el escaneo de puertos
							int timeout = prefs.getInt("timeout_puertos", 500);

							 ScanPortTask scanPortTask = new ScanPortTask(redActivity, host.getIp(), timeout, host, puertosTCP, puertosUDP, adapter);
						     scanPortTask.execute();	
							
						}

					}
					
				}.start();
			}
		}
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
	}

	@Override
	protected void onPostExecute(Host host) {
	}
}