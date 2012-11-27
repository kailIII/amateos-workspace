package org.inftel.socialwind.client.mobile.activities;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.controllers.SurferController;
import org.inftel.socialwind.client.mobile.listeners.ProfileModifyButtonOnClickListener;
import org.inftel.socialwind.client.mobile.models.SurferModel;
import org.inftel.socialwind.client.mobile.models.SurferModel.SurferListener;
import org.inftel.socialwind.client.mobile.utils.GetGravatarImageAsync;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ProfileActivity extends Activity implements SurferListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    SurferModel model;
    SurferController controller;

    private EditText displayNameEditText;
    private EditText fullNameEditText;
    private Button modifyButton;
    private ImageView profileImg;
    private ProgressBar loadingCircle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Se obtienen el surfer y la localización a partir del Intent
        SurferModel model = (SurferModel) getIntent().getExtras().getSerializable("profile");
        Log.d(TAG, "SURFER OBTENIDO -> " + model.getDisplayName());

        // Se establece el modelo
        setModel(model);

        // se obtienen los elementos de la interfaz
        displayNameEditText = (EditText) findViewById(R.id.surferDisplayNameEditText);
        fullNameEditText = (EditText) findViewById(R.id.surferFullNameEditText);

        displayNameEditText.setText(controller.getDisplayName());
        fullNameEditText.setText(controller.getFullName());
        profileImg = (ImageView) findViewById(R.id.profileImageView);

        // se descarga la imagen de perfil de Gravatar de forma asíncrona
        new GetGravatarImageAsync(this).execute(controller.getGravatarHash());

        modifyButton = (Button) findViewById(R.id.surferModifyButton);
        modifyButton.setOnClickListener(new ProfileModifyButtonOnClickListener(this, controller));

    }

    /**
     * Establece el modelo para la vista de User
     * 
     * @param model
     */
    public void setModel(SurferModel model) {

        if (model == null) {
            throw new NullPointerException("SurferModel");
        }

        SurferModel oldModel = this.model;
        if (oldModel != null) {
            oldModel.removeListener(this);
        }
        this.model = model;
        this.model.addListener(this);
        this.controller = new SurferController(this.model);

    }

    @Override
    public void onSurferUpdated(SurferModel surferModel) {
        Log.d(TAG, "Profile updated! -> " + surferModel.getDisplayName());
        displayNameEditText.setText(controller.getDisplayName());
        fullNameEditText.setText(controller.getFullName());
    }

}