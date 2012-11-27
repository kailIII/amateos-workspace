package org.inftel.socialwind.client.mobile.utils;

import java.util.ArrayList;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.activities.SocialWindActivity;
import org.inftel.socialwind.client.mobile.activities.TabsActivity;
import org.inftel.socialwind.client.mobile.models.HotspotModel;
import org.inftel.socialwind.client.mobile.models.SessionModel;
import org.inftel.socialwind.client.mobile.models.SurferModel;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GetDataAsync extends AsyncTask<SocialwindRequestFactory, Integer, ArrayList<Object>> {

    private static final String TAG = "Async";

    // Constantes
    public static final int PROGRESS_SENDING_LOCATION = 1;
    public static final int PROGRESS_GETTING_SURFER = 2;
    public static final int PROGRESS_GETTING_HOTSPOTS = 3;
    public static final int PROGRESS_GETTING_SESSIONS = 4;

    // UI
    private TextView status;
    private ProgressBar loadingCircle;
    private SocialWindActivity activity;

    private Location location;

    // Modelos
    SurferModel surfer;
    HotspotModel hotspots;
    SessionModel sessions;

    /**
     * Constructor
     * 
     * @param activity
     */
    public GetDataAsync(SocialWindActivity activity) {
        this.activity = activity;
        status = (TextView) activity.findViewById(R.id.statusTextView);
        loadingCircle = (ProgressBar) activity.findViewById(R.id.initProgressBar);
        location = LocationUtil.getLocation(activity.getBaseContext());
    }

    protected void onPreExecute() {
        loadingCircle.setVisibility(View.VISIBLE);
        status.setText(R.string.contacting_server);
    }

    protected ArrayList<Object> doInBackground(SocialwindRequestFactory... requestFactory) {

        // array de objetos que se devuelve como resultado del AsyncTask
        final ArrayList<Object> result = new ArrayList<Object>();

        RequestUtil r = new RequestUtil(requestFactory[0]);

        /*********** Envío de localización ***************/

        if (location != null) {
            try {
                publishProgress(PROGRESS_SENDING_LOCATION);
                r.updateLocation(location);
            } catch (Exception e) {
                Log.d(TAG, "No ha sido posible enviar la localización: " + e.getMessage());
            }
        }

        /*********** Obtención del surferModel ***************/
        try {
            publishProgress(PROGRESS_GETTING_SURFER);
            SurferModel surferModel = r.getSurfer(new SurferModel());
            result.add(surferModel);
        } catch (Exception e) {
            Log.d(TAG, "No ha sido posible obtener el surfer: " + e.getMessage());
        }

        /*********** Obtención de los hotspots ***************/
        try {
            publishProgress(PROGRESS_GETTING_HOTSPOTS);
            HotspotModel hotspotModel = r.getHotspots(new HotspotModel(), location);
            result.add(hotspotModel);
        } catch (Exception e) {
            Log.d(TAG, "No ha sido posible obtener los hotspots: " + e.getMessage());
        }

        /*********** Obtención de las sesiones ***************/
        try {
            publishProgress(PROGRESS_GETTING_SESSIONS);
            SessionModel sessionModel = r.getSessions(new SessionModel());
            result.add(sessionModel);
        } catch (Exception e) {
            Log.d(TAG, "No ha sido posible obtener las sesiones: " + e.getMessage());
        }
        return result;

    }

    protected void onProgressUpdate(Integer... progress) {

        switch (progress[0]) {
        case PROGRESS_GETTING_SURFER:
            status.setText(R.string.getting_surfer);
            break;
        case PROGRESS_SENDING_LOCATION:
            status.setText(R.string.sending_location);
            break;
        case PROGRESS_GETTING_HOTSPOTS:
            status.setText(R.string.getting_hotspots);
            break;
        case PROGRESS_GETTING_SESSIONS:
            status.setText(R.string.getting_sessions);
            break;
        }
    }

    protected void onPostExecute(ArrayList<Object> result) {

        // se modifican elementos de la interfaz
        loadingCircle.setVisibility(View.INVISIBLE);

        // Se obtienen los modelos resultado de la obtención de datos del servidor
        surfer = (SurferModel) result.get(0);
        hotspots = (HotspotModel) result.get(1);
        sessions = (SessionModel) result.get(2);

        // **** LLAMADA A LA ACTIVITY PRINCIPAL DE LA APLICACIÓN ****//
        Intent i = new Intent(activity.getBaseContext(), TabsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Se pasan a la siguiente activity el modelo y controlador creados
        i.putExtra("hotspots", hotspots);
        i.putExtra("sessions", sessions);
        i.putExtra("profile", surfer);

        // Se inicia la activity
        activity.getBaseContext().startActivity(i);

    }

}
