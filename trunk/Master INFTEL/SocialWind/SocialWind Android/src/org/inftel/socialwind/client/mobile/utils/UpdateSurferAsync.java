package org.inftel.socialwind.client.mobile.utils;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.activities.ProfileActivity;
import org.inftel.socialwind.client.mobile.controllers.SurferController;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class UpdateSurferAsync extends AsyncTask<String, Void, String[]> {

    private static final String TAG = "Async";
    private ProfileActivity activity;
    private ProgressBar loadingCircle;
    private SurferController controller;
    private EditText displayName;
    private EditText fullName;

    private final SocialwindRequestFactory requestFactory;

    /**
     * Constructor
     * 
     * @param activity
     */
    public UpdateSurferAsync(ProfileActivity activity, SurferController controller) {
        this.activity = activity;
        this.controller = controller;
        loadingCircle = (ProgressBar) activity.findViewById(R.id.imageProgressBar);
        displayName = (EditText) activity.findViewById(R.id.surferDisplayNameEditText);
        fullName = (EditText) activity.findViewById(R.id.surferFullNameEditText);

        requestFactory = Util.getRequestFactory(activity.getBaseContext(),
                SocialwindRequestFactory.class);
    }

    @Override
    protected void onPreExecute() {
        loadingCircle.setVisibility(View.VISIBLE);
    }

    @Override
    protected String[] doInBackground(String... names) {

        try {
            RequestUtil r = new RequestUtil(requestFactory);
            r.editSurfer(names[0], names[1]);
        } catch (Exception e) {
            Log.d(TAG, "No ha sido posible editar el surfer: " + e.getMessage());
        }
        return names;
    }

    @Override
    protected void onPostExecute(String[] names) {
        super.onPostExecute(names);
        loadingCircle.setVisibility(View.INVISIBLE);
        displayName.setText(controller.getDisplayName());
        fullName.setText(controller.getFullName());

        // Una vez realizados los cambios en el servidor se actualiza el modelo local
        controller.setDisplayName(names[0]);
        controller.setFullName(names[1]);
    }
}
