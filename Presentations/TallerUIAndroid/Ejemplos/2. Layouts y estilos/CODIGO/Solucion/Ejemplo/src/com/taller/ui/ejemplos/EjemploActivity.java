package com.taller.ui.ejemplos;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class EjemploActivity extends TabActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		// Inicialización de las activities de las tabs

		// Activity LINEAR LAYOUT
		intent = new Intent().setClass(this, LinearLayoutActivity.class);
		spec = tabHost
				.newTabSpec(getString(R.string.Linear))
				.setIndicator(getString(R.string.Linear), res.getDrawable(R.drawable.ic_tab_ejemplo))
				.setContent(intent);
		tabHost.addTab(spec);

		// Activity RELATIVE LAYOUT
		intent = new Intent().setClass(this, RelativeLayoutActivity.class);
		spec = tabHost
				.newTabSpec(getString(R.string.Relative))
				.setIndicator(getString(R.string.Relative),
						res.getDrawable(R.drawable.ic_tab_ejemplo))
				.setContent(intent);
		tabHost.addTab(spec);

		// Activity LISTVIEW
		intent = new Intent().setClass(this, ListViewActivity.class);
		spec = tabHost
				.newTabSpec(getString(R.string.ListView))
				.setIndicator(getString(R.string.ListView),
						res.getDrawable(R.drawable.ic_tab_ejemplo))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

	}
}