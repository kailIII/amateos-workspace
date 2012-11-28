package org.inftel.scrum.services;


import org.inftel.scrum.utils.Authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AuthenticatorService extends Service {

	private static final String TAG = "AuthenticationService";
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Servicio de Autenticacion iniciado.");
        }
        mAuthenticator = new Authenticator(this);
    }
    
    @Override
    public void onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Servicio de Autenticacion parado.");
        }
    }

    
	@Override
	public IBinder onBind(Intent intent) {
		
		if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG,
                "getBinder()...  obteniendo el AccountAuthenticator para el intent "
                    + intent);
        }
        return mAuthenticator.getIBinder();
	}

}
