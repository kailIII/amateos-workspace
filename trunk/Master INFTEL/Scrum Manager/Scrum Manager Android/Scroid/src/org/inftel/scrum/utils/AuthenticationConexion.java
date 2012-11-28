package org.inftel.scrum.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.activities.AuthenticatorActivity;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class AuthenticationConexion {
	
	
	 private static final String TAG = "AuthenticationConexion";
	 private static HttpClient mHttpClient;
	 
	    /**
	     * Executes the network requests on a separate thread.
	     * 
	     * @param runnable The runnable instance containing network mOperations to
	     *        be executed.
	     */
	    public static Thread performOnBackgroundThread(final Runnable runnable) {
	        final Thread t = new Thread() {
	            @Override
	            public void run() {
	                try {
	                    runnable.run();
	                } finally {

	                }
	            }
	        };
	        t.start();
	        return t;
	    }
	    
	    /**
	     * Connects to the server, authenticates the provided username and
	     * password.
	     * 
	     * @param username The user's username
	     * @param password The user's password
	     * @param handler The hander instance from the calling UI thread.
	     * @param context The context of the calling Activity.
	     * @return boolean The boolean result indicating whether the user was
	     *         successfully authenticated.
	     * @throws Exception 
	     */
	    public static boolean authenticate(String username, String password,
	        Handler handler, final Context context) throws Exception {

       	        String jSesionId = ServerRequest.login(username, password);
	            
	            if (jSesionId !=null) {
	                if (Log.isLoggable(TAG, Log.VERBOSE)) {
	                    Log.v(TAG, "Validado con éxito");
	                }
	                
	                    
	                sendResult(true, handler, context,username,password);
	                
	                return true;
	            } else {
	                if (Log.isLoggable(TAG, Log.VERBOSE)) {
	                    Log.v(TAG, "Error de validación ");
	                }
	                sendResult(false, handler, context,username, password);
	                return false;
	            }
	        
	                   
	    }

	    /**
	     * Sends the authentication response from server back to the caller main UI
	     * thread through its handler.
	     * 
	     * @param result The boolean holding authentication result
	     * @param handler The main UI thread's handler instance.
	     * @param context The caller Activity's context.
	     */
	    private static void sendResult(final Boolean result, final Handler handler,
	        final Context context,final String user,final String pass) {
	        if (handler == null || context == null) {
	            return;
	        }
	        handler.post(new Runnable() {
	            public void run() {
	                ((AuthenticatorActivity) context).onAuthenticationResult(result,user,pass);
	            }
	        });
	    }

	    /**
	     * Attempts to authenticate the user credentials on the server.
	     * 
	     * @param username The user's username
	     * @param password The user's password to be authenticated
	     * @param handler The main UI thread's handler instance.
	     * @param context The caller Activity's context
	     * @return Thread The thread on which the network mOperations are executed.
	     */
	    public static Thread attemptAuth(final String username,
	        final String password, final Handler handler, final Context context) {
	        final Runnable runnable = new Runnable() {
	            public void run() {
	                try {
						authenticate(username, password, handler, context);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        };
	        // run on background thread.
	        return AuthenticationConexion.performOnBackgroundThread(runnable);
	    }



}
