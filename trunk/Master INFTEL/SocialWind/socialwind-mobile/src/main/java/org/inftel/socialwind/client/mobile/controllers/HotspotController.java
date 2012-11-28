package org.inftel.socialwind.client.mobile.controllers;

import java.io.Serializable;
import java.util.ArrayList;

import org.inftel.socialwind.client.mobile.models.HotspotModel;
import org.inftel.socialwind.client.mobile.vos.Spot;

public class HotspotController implements Serializable {

    private static final long serialVersionUID = 1196041083668069897L;
    private HotspotModel model;

    /**
     * Constructor
     * 
     * @param model
     */
    public HotspotController(HotspotModel model) {
        this.model = model;
    }

    public ArrayList<Spot> getHotspots() {
        return model.getHotspots();
    }

    /**
     * Actualiza los hotspots del modelo
     * 
     * @param hotspots
     */
    public void updateHotspots(ArrayList<Spot> hotspots) {
        model.setHotspots(hotspots);
    }
}
