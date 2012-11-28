package org.inftel.socialwind.client.mobile.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.inftel.socialwind.client.mobile.vos.Session;

public class SessionModel implements Serializable {

    private static final long serialVersionUID = -8929589184956677645L;
    private ArrayList<Session> sessions;
    private final ArrayList<SessionsListener> listeners = new ArrayList<SessionsListener>();

    /**
     * Constructor
     */
    public SessionModel() {
        sessions = new ArrayList<Session>();
    }

    /**
     * Constructor
     */
    public SessionModel(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    /**
     * Devueve el array de sessions
     * 
     * @return
     */
    public ArrayList<Session> getSessions() {
        synchronized (this) {
            return sessions;
        }
    }

    /**
     * Establece el array de sesiones
     * 
     * @param sessions
     */
    public void setSessions(ArrayList<Session> sessions) {
        synchronized (this) {
            this.sessions = sessions;
        }

        // Se notifica a los listeners del cambio producido en el modelo
        synchronized (listeners) {
            for (SessionsListener listener : listeners) {
                listener.onSessionsUpdated(this);
            }
        }
    }

    /**
     * Agrega un listener al modelo
     * 
     * @param listener
     */
    public final void addListener(SessionsListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Elimina un listener del modelo
     * 
     * @param listener
     */
    public final void removeListener(SessionsListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    // Interfaz que permite informar a las vistas de cambios en el modelo
    public interface SessionsListener {
        void onSessionsUpdated(SessionModel sessionModel);
    }

}
