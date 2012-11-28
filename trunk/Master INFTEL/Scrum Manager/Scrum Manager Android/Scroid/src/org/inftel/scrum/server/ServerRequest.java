
package org.inftel.scrum.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.inftel.scrum.R;
import org.inftel.scrum.utils.Constantes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ServerRequest {
    private static final String TAG = "ServerRequest";
    private static int timeoutConnection = 30000;
    private static int timeoutSocket = 30000;

    // Envia parametros al servlet y recibe una respuesta en JSON o null
    public static String send(String jsessionId, ArrayList<BasicNameValuePair> parameters,String accion) throws Exception {
        URL url;
        try {
            url = new URL(Constantes.URL_SERVLET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: url mal formada");
            throw e;
        }
        HttpParams httpParameters = new BasicHttpParams();

        // Socket not connected timeout
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        // Operation timeout
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpResponse response;
        try {
            Log.i(TAG, "Sending request to servlet: " + accion);
            HttpPost httppost = new HttpPost(url.toURI());
            UrlEncodedFormEntity data = new UrlEncodedFormEntity(parameters);
            httppost.setHeader("JSESSIONID", jsessionId);
            httppost.setEntity(data);
            response = httpClient.execute(httppost);
            Log.i(TAG, "response received!");
            if (response.containsHeader(accion)) {
                Log.i(TAG, "response header found!");
                Log.i(TAG, response.getFirstHeader(accion).getValue().toString());
                return response.getFirstHeader(accion).getValue().toString();
            }
        } catch (InterruptedIOException e) {
            Log.e(TAG, " ERROR(InterruptedIOException): network timeout!");
            throw new InterruptedIOException(TAG
                    + "ERROR(InterruptedIOException): network timeout!");
        } catch (IOException ioe) {
            Log.e(TAG, " ERROR:(IOException) network timeout!");
            throw new IOException(TAG + "ERROR:(IOException) network timeout!");
        } catch (Exception e) {
            Log.e(TAG, "ERROR in request!!");
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    // Peticion de login y devuelve el JsessionId o null
    public static String login(String usuario, String password) throws Exception {
        URL url;
        try {
            url = new URL(Constantes.URL_SERVLET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: url mal formada");
            throw e;
        }
        HttpParams httpParameters = new BasicHttpParams();
        // Socket not connected timeout
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Operation timed out
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        try {
            if ((usuario.isEmpty()) || (usuario == null) || (password.isEmpty())
                    || (password == null)) {
                throw new RuntimeException("ERROR: invalid credentials!");
            }
            parameters.add(new BasicNameValuePair("accion", Constantes.DO_LOGIN));
            parameters.add(new BasicNameValuePair("Usuario", usuario));
            parameters.add(new BasicNameValuePair("Password", password));
            Log.i(TAG, "Login in...");
            HttpPost httppost = new HttpPost(url.toURI());
            UrlEncodedFormEntity data = new UrlEncodedFormEntity(parameters);
            httppost.setEntity(data);
            httpClient.execute(httppost);

            List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("jsessionid")) {
                    Log.i(TAG, "jsessionid:" + cookie.getValue());
                    return cookie.getValue();
                }
            }
        } catch (InterruptedIOException e) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new InterruptedIOException(TAG + " ERROR: network timeout!");
        } catch (IOException ioe) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new IOException(TAG + " ERROR: network timeout!");
        } catch (Exception e) {
            Log.e(TAG, "ERROR in login!!");
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    // Solicita al servlet una imagen PNG de una estadistica
    public static Bitmap getChartImage(String jsessionId, String accion) throws Exception {
        URL url;
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("accion", accion));
        try {
            url = new URL(Constantes.URL_SERVLET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: url mal formada");
            throw e;
        }
        HttpParams httpParameters = new BasicHttpParams();
        // Socket not connected timeout
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Operation time out
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpResponse response;
        try {
            Log.i(TAG, "Sending request to servlet...");
            HttpPost httppost = new HttpPost(url.toURI());
            UrlEncodedFormEntity data = new UrlEncodedFormEntity(parameters);
            httppost.setHeader("JSESSIONID", jsessionId);
            httppost.setEntity(data);
            response = httpClient.execute(httppost);
            Log.i(TAG, "response received!");
            Bitmap result = BitmapFactory.decodeStream(response.getEntity().getContent());
            Log.i(TAG, "bytes recibidos: " + response.getEntity().getContentLength());
            return result;

        } catch (InterruptedIOException e) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new InterruptedIOException(TAG + " ERROR: network timeout!");
        } catch (IOException ioe) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new IOException(TAG + " ERROR: network timeout!");
        } catch (Exception e) {
            Log.e(TAG, "ERROR in request!!");
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] getPdf(String jsessionId, String accion) throws Exception {
        URL url;
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("accion", accion));
        try {
            url = new URL(Constantes.URL_SERVLET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: url mal formada");
            throw e;
        }
        HttpParams httpParameters = new BasicHttpParams();
        // Socket not connected timeout
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Operation time out
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpResponse response;
        try {
            Log.i(TAG, "Sending request to servlet...");
            HttpPost httppost = new HttpPost(url.toURI());
            UrlEncodedFormEntity data = new UrlEncodedFormEntity(parameters);
            httppost.setHeader("JSESSIONID", jsessionId);
            httppost.setEntity(data);
            response = httpClient.execute(httppost);
            Log.i(TAG, "response received!");
            Log.i(TAG, "bytes recibidos: " + response.getEntity().getContentLength());
            byte[] array = new byte[(int) response.getEntity().getContentLength()];
            array = IOUtils.toByteArray(response.getEntity().getContent());
            // response.getEntity().getContent().read(array);
            System.out.println("el array: " + array);
            return array;

        } catch (InterruptedIOException e) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new InterruptedIOException(TAG + " ERROR: network timeout!");
        } catch (IOException ioe) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new IOException(TAG + " ERROR: network timeout!");
        } catch (Exception e) {
            Log.e(TAG, "ERROR in request!!");
            e.printStackTrace();
            throw e;
        }
    }

    // Peticion de logout, devuelve true o peta
    public static boolean logout(String jSessionId) throws Exception {
        URL url;
        try {
            url = new URL(Constantes.URL_SERVLET);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "ERROR: url mal formada");
            throw e;
        }
        HttpParams httpParameters = new BasicHttpParams();
        // Socket not connected timeout
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Operation timed out
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        try {
            parameters.add(new BasicNameValuePair("accion", Constantes.DO_LOGOUT));
            Log.i(TAG, "Logout in progress...");
            HttpPost httppost = new HttpPost(url.toURI());
            UrlEncodedFormEntity data = new UrlEncodedFormEntity(parameters);
            httppost.setEntity(data);
            httpClient.execute(httppost);
            return true;

        } catch (InterruptedIOException e) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new InterruptedIOException(TAG + " ERROR: network timeout!");
        } catch (IOException ioe) {
            Log.e(TAG, "ERROR: network timeout!");
            throw new IOException(TAG + " ERROR: network timeout!");
        } catch (Exception e) {
            Log.e(TAG, "ERROR in login!!");
            e.printStackTrace();
            throw e;
        }
    }
}
