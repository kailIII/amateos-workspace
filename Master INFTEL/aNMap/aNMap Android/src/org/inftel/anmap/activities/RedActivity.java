package org.inftel.anmap.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.inftel.anmap.R;
import org.inftel.anmap.async.GetHostsAsync;
import org.inftel.anmap.list.RedAdapter;
import org.inftel.anmap.util.OUIparser;
import org.inftel.anmap.util.PortsParser;
import org.inftel.anmap.util.ThreadUtil;
import org.inftel.anmap.vos.Host;
import org.inftel.anmap.vos.LocalHost;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RedActivity extends ListActivity {

	private static final String TAG = "RedActivity";

	private ArrayList<Host> hosts;
	private LocalHost localHost;
	public ProgressBar progressBar;
	private RedAdapter adapter;
	
	private ImageView infoButton;
	private ImageView scanButton;
	private ImageView preferenciasButton;
	
	private HashMap<String, String> puertosTCP;
    private HashMap<String, String> puertosUDP;
    
    
	private static SharedPreferences prefs;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.red);
		initBar();
		
		// Se parsean el archivo de los puertos
		puertosTCP = new HashMap<String, String>();
		puertosUDP = new HashMap<String, String>();
		PortsParser.GetDataPorts(this, puertosTCP, puertosUDP);
		
		// Se parsea el archivo de los fabricantes para la MAC
		OUIparser.initialize(this);
		
		localHost = LocalHost.getInstance(this);
		localHost.setFabricante(OUIparser.getManufacturer(localHost.getMac()));
		
		Log.d(TAG, "OBTENIDO LOCALHOST -> "+localHost.getIp());
		hosts = new ArrayList<Host>();
		adapter = new RedAdapter(this, hosts);
		setListAdapter(adapter);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Se obtiene el número máximo de hebras a partir de las preferencias
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int hebras = prefs.getInt("hebras", 10);
		Log.d("prefs", "MAX HRBRAS -> "+hebras);
		ThreadUtil.MAX_HEBRAS = hebras;
	}


	/*
	 * Inicializa la barra de menú
	 */
	private void initBar(){
		infoButton = (ImageView) findViewById(R.id.red_bar_info);
		scanButton = (ImageView) findViewById(R.id.red_bar_scan);
		preferenciasButton = (ImageView) findViewById(R.id.red_bar_opciones);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.INVISIBLE);
		
		
		infoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Dialog infoDialog = new Dialog(RedActivity.this, R.style.dialogo);
				infoDialog.setContentView(R.layout.dialog_localhost);
				infoDialog.setTitle(R.string.dialog_localhost_titulo);
				
				TextView bssid = (TextView) infoDialog.findViewById(R.id.dialog_localhost_wifi_bssid);
				TextView enlace = (TextView) infoDialog.findViewById(R.id.dialog_localhost_wifi_enlace);
				TextView routerIp = (TextView) infoDialog.findViewById(R.id.dialog_localhost_router_ip);
				TextView routerMac = (TextView) infoDialog.findViewById(R.id.dialog_localhost_router_mac);
				TextView routerMacFab = (TextView) infoDialog.findViewById(R.id.dialog_localhost_router_mac_fabricante);
				TextView localhostIp = (TextView) infoDialog.findViewById(R.id.dialog_localhost_ip);
				TextView localhostMac = (TextView) infoDialog.findViewById(R.id.dialog_localhost_mac);
				TextView localhostMacFab = (TextView) infoDialog.findViewById(R.id.dialog_localhost_mac_fabricante);

				bssid.setText(localHost.getSsid());
				enlace.setText(localHost.getSpeed()+" mbps | "+localHost.getRssi()+" dBm");
				routerIp.setText(localHost.getGatewayIp());
				routerMac.setText(localHost.getBssid());
				routerMacFab.setText(OUIparser.getManufacturer(localHost.getBssid()));
				localhostIp.setText(localHost.getIp());
				localhostMac.setText(localHost.getMac());
				localhostMacFab.setText(localHost.getFabricante());
				
				infoDialog.show();
			}
		});
		
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
		        Toast.makeText(RedActivity.this,"Scan iniciado...", Toast.LENGTH_LONG).show();
		        progressBar.setVisibility(View.VISIBLE);
				adapter.borrarHosts();
				scan();
			}
		});
		
		preferenciasButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(RedActivity.this, PreferenciasActivity.class);
				startActivity(i);
			}
		});
	}
	
	
	/**
	 * Realiza un scaneo de la red para obtener los hosts
	 */
	public void scan(){
		new GetHostsAsync(this, adapter, puertosTCP, puertosUDP).execute(localHost.getIp());
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(RedActivity.this, PuertosActivity.class);
		Bundle b = new Bundle();
		b.putSerializable("host", adapter.getHost(position));
		i.putExtras(b);
		startActivity(i);
	}

}
