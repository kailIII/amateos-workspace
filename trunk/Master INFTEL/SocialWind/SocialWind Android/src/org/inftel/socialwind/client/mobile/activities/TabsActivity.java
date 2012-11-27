package org.inftel.socialwind.client.mobile.activities;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.models.HotspotModel;
import org.inftel.socialwind.client.mobile.models.SessionModel;
import org.inftel.socialwind.client.mobile.models.SurferModel;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {

    private static final String TAG = TabsActivity.class.getSimpleName();

    HotspotModel hotspots;
    SessionModel sessions;
    SurferModel surfer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se obtienen los datos que se pasan en el Intent
        hotspots = (HotspotModel) getIntent().getExtras().getSerializable("hotspots");
        sessions = (SessionModel) getIntent().getExtras().getSerializable("sessions");
        surfer = (SurferModel) getIntent().getExtras().getSerializable("profile");

        // Se inicializan las tabs
        setMainUi();

    }

    /**
     * Establece la vista principal de la interfaz de usuario
     */
    private void setMainUi() {
        setContentView(R.layout.tabs);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        // Inicialización de las activities de las tabs

        // Activity Hotspots
        intent = new Intent().setClass(this, HotspotsActivity.class);
        intent.putExtra("hotspots", hotspots);
        spec = tabHost
                .newTabSpec(getString(R.string.hotspots))
                .setIndicator(getString(R.string.hotspots),
                        res.getDrawable(R.drawable.ic_tab_hotspots)).setContent(intent);
        tabHost.addTab(spec);

        // Activity Sessions
        intent = new Intent().setClass(this, SessionsActivity.class);
        intent.putExtra("sessions", sessions);

        spec = tabHost
                .newTabSpec(getString(R.string.sessions))
                .setIndicator(getString(R.string.sessions),
                        res.getDrawable(R.drawable.ic_tab_sessions)).setContent(intent);
        tabHost.addTab(spec);

        // Activity Profile
        intent = new Intent().setClass(this, ProfileActivity.class);
        intent.putExtra("profile", surfer);

        spec = tabHost
                .newTabSpec(getString(R.string.profile))
                .setIndicator(getString(R.string.profile),
                        res.getDrawable(R.drawable.ic_tab_profile)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

}