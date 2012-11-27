package org.inftel.anmap.activities;

import org.inftel.anmap.R;
import org.inftel.anmap.list.PuertosAdapter;
import org.inftel.anmap.vos.Host;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PuertosActivity extends ListActivity {

	private static final String TAG = "PuertosActivity";

	private PuertosAdapter adapter;
	private Host host;

	private ImageView infoButton;
	private ImageView preferenciasButton;
	
    

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.puertos);
		initBar();

		host = (Host) getIntent().getExtras().getSerializable("host");
		
		adapter = new PuertosAdapter(this, host.getPuertos());
		setListAdapter(adapter);
		
		
	}

	/*
	 * Inicializa la barra de menú
	 */
	private void initBar() {
		infoButton = (ImageView) findViewById(R.id.red_bar_info);
		preferenciasButton = (ImageView) findViewById(R.id.red_bar_opciones);

		infoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Dialog infoDialog = new Dialog(PuertosActivity.this, R.style.dialogo);
				infoDialog.setContentView(R.layout.dialog_victima);
				infoDialog.setTitle(R.string.dialog_victima_titulo);

				TextView hostname = (TextView) infoDialog
						.findViewById(R.id.dialog_victima_hostname_value);
				TextView ip = (TextView) infoDialog
						.findViewById(R.id.dialog_victima_ip_value);
				TextView mac = (TextView) infoDialog
						.findViewById(R.id.dialog_victima_mac_value);
				TextView router = (TextView) infoDialog
						.findViewById(R.id.dialog_victima_router_value);

				hostname.setText(host.getHostname());
				ip.setText(host.getIp());
				mac.setText(host.getMac() + " (" + host.getFabricante() + ")");

				if (host.getRouter()) {
					router.setText(getString(R.string.si));
				} else {
					router.setText(getString(R.string.no));
				}

				infoDialog.show();
			}
		});

		preferenciasButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(PuertosActivity.this, PreferenciasActivity.class);
				startActivity(i);
			}
		});
	}
	
	

}
	
