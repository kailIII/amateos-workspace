package org.inftel.socialwind.client.mobile.activities;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.controllers.HotspotController;
import org.inftel.socialwind.client.mobile.lists.HotspotArrayAdapter;
import org.inftel.socialwind.client.mobile.models.HotspotModel;
import org.inftel.socialwind.client.mobile.models.HotspotModel.HotspotListener;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class HotspotsActivity extends ListActivity implements HotspotListener {

    private static final String TAG = HotspotsActivity.class.getSimpleName();

    private HotspotModel hotspots;
    private HotspotController controller;

    private HotspotArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hotspots);

        // Se obtienen los datos que se pasan en el Intent
        hotspots = (HotspotModel) getIntent().getExtras().getSerializable("hotspots");

        // se establece el modelo
        setModel(hotspots);

        adapter = new HotspotArrayAdapter(this, controller.getHotspots());
        setListAdapter(adapter);
    }

    /**
     * Establece el modelo para la vista de hostposts
     * 
     * @param model
     */
    public void setModel(HotspotModel model) {

        if (model == null) {
            throw new NullPointerException("HotspotModel");
        }

        HotspotModel oldModel = this.hotspots;
        if (oldModel != null) {
            oldModel.removeListener(this);
        }
        this.hotspots = model;
        this.hotspots.addListener(this);
        this.controller = new HotspotController(this.hotspots);
    }

    /**
     * Se ejecuta cuando se produce un cambio en el modelo
     */
    @Override
    public void onHotspotsUpdated(HotspotModel hotspotModel) {
        Log.d(TAG, "Hotspots updated!");
        adapter.updateList(hotspotModel.getHotspots());
        adapter.notifyDataSetChanged();

    }
}
