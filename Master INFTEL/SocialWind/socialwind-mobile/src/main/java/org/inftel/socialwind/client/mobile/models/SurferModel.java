package org.inftel.socialwind.client.mobile.models;

import java.io.Serializable;
import java.util.ArrayList;

public class SurferModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String displayName;
    private String gravatarHash;
    private double latitude;
    private double longitude;
    private String fullName;

    private final ArrayList<SurferListener> listeners = new ArrayList<SurferListener>();

    /**
     * Constructor
     */
    public SurferModel(String displayName, String fullName, String gravatarHash) {
        this.displayName = displayName;
        this.fullName = fullName;
        this.gravatarHash = gravatarHash;
    }

    /**
     * Constructor
     */
    public SurferModel() {
    }

    public String getDisplayName() {
        synchronized (this) {
            return displayName;
        }
    }

    public String getGravatarHash() {
        synchronized (this) {
            return gravatarHash;
        }
    }

    public double getLatitude() {
        synchronized (this) {

            return latitude;

        }
    }

    public double getLongitude() {
        synchronized (this) {
            return longitude;
        }
    }

    public String getFullName() {
        synchronized (this) {
            return fullName;
        }
    }

    public void setDisplayName(String displayName) {
        synchronized (this) {
            this.displayName = displayName;
        }
        // Se notifica a los listeners del cambio producido en el modelo
        synchronized (listeners) {
            for (SurferListener listener : listeners) {
                listener.onSurferUpdated(this);
            }
        }
    }

    public void setLatitude(double latitude) {
        synchronized (this) {
            this.latitude = latitude;
        }
        // Se notifica a los listeners del cambio producido en el modelo
        synchronized (listeners) {
            for (SurferListener listener : listeners) {
                listener.onSurferUpdated(this);
            }
        }
    }

    public void setLongitude(double longitude) {
        synchronized (this) {

            this.longitude = longitude;
        }
        // Se notifica a los listeners del cambio producido en el modelo
        synchronized (listeners) {
            for (SurferListener listener : listeners) {
                listener.onSurferUpdated(this);
            }
        }
    }

    public void setFullName(String fullName) {
        synchronized (this) {
            this.fullName = fullName;
        }
        // Se notifica a los listeners del cambio producido en el modelo
        synchronized (listeners) {
            for (SurferListener listener : listeners) {
                listener.onSurferUpdated(this);
            }
        }
    }

    public void setGravatarHash(String gravatarHash) {
        synchronized (this) {
            this.gravatarHash = gravatarHash;
        }
        // Se notifica a los listeners del cambio producido en el modelo
        synchronized (listeners) {
            for (SurferListener listener : listeners) {
                listener.onSurferUpdated(this);
            }
        }
    }

    /**
     * Agrega un listener al modelo
     * 
     * @param listener
     */
    public final void addListener(SurferListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Elimina un listener del modelo
     * 
     * @param listener
     */
    public final void removeListener(SurferListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    // Interfaz que permite informar a las vistas de cambios en el modelo
    public interface SurferListener {
        void onSurferUpdated(SurferModel surferModel);
    }
}
