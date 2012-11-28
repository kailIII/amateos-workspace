package org.inftel.socialwind.client.mobile.listeners;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.activities.ProfileActivity;
import org.inftel.socialwind.client.mobile.controllers.SurferController;
import org.inftel.socialwind.client.mobile.utils.UpdateSurferAsync;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ProfileModifyButtonOnClickListener implements OnClickListener {

    private ProfileActivity activity;
    private EditText displayName;
    private EditText fullName;
    private SurferController controller;

    /**
     * Constructor
     * 
     * @param activity
     * @param controller
     */
    public ProfileModifyButtonOnClickListener(ProfileActivity activity, SurferController controller) {
        this.activity = activity;
        this.controller = controller;
        displayName = (EditText) activity.findViewById(R.id.surferDisplayNameEditText);
        fullName = (EditText) activity.findViewById(R.id.surferFullNameEditText);

    }

    @Override
    public void onClick(View button) {
        // Se envia la información al servidor de forma asíncrona
        new UpdateSurferAsync(activity, controller).execute(displayName.getText().toString(),
                fullName.getText().toString());

    }

}
