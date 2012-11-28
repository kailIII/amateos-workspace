package org.inftel.socialwind.client.mobile.activities;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.comm.DeviceRegistrar;
import org.inftel.socialwind.client.mobile.listeners.InitButtonOnClickListener;
import org.inftel.socialwind.client.mobile.models.HotspotModel;
import org.inftel.socialwind.client.mobile.models.SessionModel;
import org.inftel.socialwind.client.mobile.models.SurferModel;
import org.inftel.socialwind.client.mobile.utils.GetDataAsync;
import org.inftel.socialwind.client.mobile.utils.Util;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SocialWindActivity extends Activity {

    private static final String TAG = "SocialWindActivity";

    /**
     * Objetos modelo de datos
     */
    private SurferModel surfer;
    private HotspotModel hotspots;
    private SessionModel sessions;

    /**
     * Elementos de la UI
     */
    private TextView status;
    private ProgressBar loadingCircle;
    private Button initButton;
    private Context mContext = this;
    private Activity activity = this;

    /**
     * {@link BroadcastReceiver} para recibir respuestas del server
     */
    private final BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String accountName = intent.getStringExtra(DeviceRegistrar.ACCOUNT_NAME_EXTRA);
            int status = intent.getIntExtra(DeviceRegistrar.STATUS_EXTRA,
                    DeviceRegistrar.ERROR_STATUS);
            String message = null;
            String connectionStatus = Util.DISCONNECTED;
            if (status == DeviceRegistrar.REGISTERED_STATUS) {
                message = getResources().getString(R.string.registration_succeeded);
                connectionStatus = Util.CONNECTED;
            } else if (status == DeviceRegistrar.UNREGISTERED_STATUS) {
                message = getResources().getString(R.string.unregistration_succeeded);
            } else {
                message = getResources().getString(R.string.registration_error);
            }

            Log.d(TAG, "BroadcastReceiver response: " + message);
            // Set connection status
            SharedPreferences prefs = Util.getSharedPreferences(mContext);
            prefs.edit().putString(Util.CONNECTION_STATUS, connectionStatus).commit();

            // Display a notification
            Util.generateNotification(mContext, String.format(message, accountName));
        }
    };

    @Override
    /**
     * Creación de la activity
     */
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Register a receiver to provide register/unregister notifications
        registerReceiver(mUpdateUIReceiver, new IntentFilter(Util.UPDATE_UI_INTENT));
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = Util.getSharedPreferences(mContext);
        String connectionStatus = prefs.getString(Util.CONNECTION_STATUS, Util.DISCONNECTED);
        if (Util.DISCONNECTED.equals(connectionStatus)) {
            startActivity(new Intent(this, AccountsActivity.class));
        }
        setScreenContent();

        // se obtienen los datos del servidor
        // getDataFromServer();
    }

    /**
     * Finaliza la ejecución de la activity
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(mUpdateUIReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // Invoke the Register activity
        menu.getItem(0).setIntent(new Intent(this, AccountsActivity.class));
        return true;
    }

    /**
     * Inicializa los elementos de la pantalla
     */
    private void setScreenContent() {
        setContentView(R.layout.boot);

        status = (TextView) findViewById(R.id.statusTextView);
        loadingCircle = (ProgressBar) findViewById(R.id.initProgressBar);
        loadingCircle.setVisibility(View.INVISIBLE);
        initButton = (Button) findViewById(R.id.initButton);
        initButton.setOnClickListener(new InitButtonOnClickListener(this));
    }

    /**
     * Obtiene los datos del servidor de forma asincrona
     */
    public void getDataFromServer() {

        // se comprueba que se dispone de conexión a internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Boolean connected = false;
        try {
            connected = cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {

        }
        if (connected) {
            final SocialwindRequestFactory requestFactory = Util.getRequestFactory(mContext,
                    SocialwindRequestFactory.class);

            // Se utiliza una tarea asíncrona (nueva hebra) para evitar la congelación de la UI
            new GetDataAsync(this).execute(requestFactory);
        } else {
            status.setText(R.string.no_connection);
        }
    }
}
