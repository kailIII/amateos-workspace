package org.inftel.socialwind.client.mobile.activities;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.controllers.SessionController;
import org.inftel.socialwind.client.mobile.lists.SessionArrayAdapter;
import org.inftel.socialwind.client.mobile.models.SessionModel;
import org.inftel.socialwind.client.mobile.models.SessionModel.SessionsListener;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class SessionsActivity extends ListActivity implements SessionsListener {

    private static final String TAG = SessionsActivity.class.getSimpleName();

    private SessionModel sessions;
    private SessionController controller;

    // private SessionController controller;

    private SessionArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sessions);

        // Se obtienen los datos que se pasan en el Intent
        sessions = (SessionModel) getIntent().getExtras().getSerializable("sessions");

        // se establece el modelo
        setModel(sessions);

        adapter = new SessionArrayAdapter(this, controller.getSessions());
        setListAdapter(adapter);
    }

    /**
     * Establece el modelo para la vista de sesiones
     * 
     * @param model
     */
    public void setModel(SessionModel model) {

        if (model == null) {
            throw new NullPointerException("SessionModel");
        }

        SessionModel oldModel = this.sessions;
        if (oldModel != null) {
            oldModel.removeListener(this);
        }
        this.sessions = model;
        this.sessions.addListener(this);
        this.controller = new SessionController(this.sessions);

    }

    /**
     * Se ejecuta cuando se produce un cambio en el modelo
     */
    @Override
    public void onSessionsUpdated(SessionModel sessionModel) {

        Log.d(TAG, "Sessions updated!");

        adapter.updateList(sessionModel.getSessions());
        adapter.notifyDataSetChanged();
    }
}
