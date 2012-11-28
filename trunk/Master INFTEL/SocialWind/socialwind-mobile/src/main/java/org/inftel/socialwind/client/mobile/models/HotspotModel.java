package org.inftel.socialwind.client.mobile.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.inftel.socialwind.client.mobile.vos.Spot;

public class HotspotModel implements Serializable {

    private static final long serialVersionUID = -7628389750495621901L;
    private ArrayList<Spot> hotspots;
    private final ArrayList<HotspotListener> listeners = new ArrayList<HotspotListener>();

    /**
     * Constructor
     */
    public HotspotModel() {
        hotspots = new ArrayList<Spot>();
    }

    /**
     * Constructor
     */
    public HotspotModel(ArrayList<Spot> hotspots) {
        this.hotspots = hotspots;
    }

    /**
     * Devueve el array de hotspots
     * 
     * @return
     */
    public ArrayList<Spot> getHotspots() {
        synchronized (this) {
            return hotspots;
        }
    }

    /**
     * Establece el array de hotspots
     * 
     * @param hotspots
     */
    public void setHotspots(ArrayList<Spot> hotspots) {
        synchronized (this) {
            this.hotspots = hotspots;
        }
        // Se notifica a los listeners del cambio producido en el modelo
        synchronized (listeners) {
            for (HotspotListener listener : listeners) {
                listener.onHotspotsUpdated(this);
            }
        }
    }

    /**
     * Agrega un listener al modelo
     * 
     * @param listener
     */
    public final void addListener(HotspotListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Elimina un listener del modelo
     * 
     * @param listener
     */
    public final void removeListener(HotspotListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    // Interfaz que permite informar a las vistas de cambios en el modelo
    public interface HotspotListener {
        void onHotspotsUpdated(HotspotModel hotspotModel);
    }

}
