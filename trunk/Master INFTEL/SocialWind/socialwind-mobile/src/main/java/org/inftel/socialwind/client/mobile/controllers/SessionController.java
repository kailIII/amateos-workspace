package org.inftel.socialwind.client.mobile.controllers;

import java.util.ArrayList;

import org.inftel.socialwind.client.mobile.models.SessionModel;
import org.inftel.socialwind.client.mobile.vos.Session;

public class SessionController {

    private SessionModel model;

    /**
     * Constructor
     * 
     * @param model
     */
    public SessionController(SessionModel model) {
        this.model = model;
    }

    public ArrayList<Session> getSessions() {
        return model.getSessions();
    }

    /**
     * Actualiza las sesiones del modelo
     * 
     * @param sessions
     */
    public void updateSessions(ArrayList<Session> sessions) {
        model.setSessions(sessions);
    }
}
