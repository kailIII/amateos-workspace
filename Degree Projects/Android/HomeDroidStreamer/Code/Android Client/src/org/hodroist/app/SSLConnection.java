package org.hodroist.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;


//Allow SSL connection with directory server
public class SSLConnection{
	
	
	public SSLConnection(){
	}
	
	public String connect(String serverIP, String username, String password) throws Exception{
		String result = "Cant connect to server";
		
		// always verify the host - dont check for certificate
		final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		        public boolean verify(String hostname, SSLSession session) {
		                return true;
		        }
		};
		
		//Create URL
		URL url=null;
		try {
			url = new URL("https://"+serverIP+"/streamer/getServerIP.php?user="+username+"&pass="+password);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Trust all certificates
        trustAllHosts();
        
        //Create connection
        HttpsURLConnection https = null;
		try {
			https = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Don not verify host name
        https.setHostnameVerifier(DO_NOT_VERIFY);

        //Read response
        BufferedReader in = new BufferedReader(new InputStreamReader(https.getInputStream()));        	
        String linea;
        while ((linea = in.readLine()) != null) {
        		result = linea;
        }

        Log.d("SSL","SSL Response from server -> "+result);
		
		return result;
	}

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                        return new java.security.cert.X509Certificate[] {};
	                }

	                public void checkClientTrusted(X509Certificate[] chain,
	                                String authType) throws CertificateException {
	                }

	                public void checkServerTrusted(X509Certificate[] chain,
	                                String authType) throws CertificateException {
	                }
	        } };

	        // Install the all-trusting trust manager
	        try {
	                SSLContext sc = SSLContext.getInstance("TLS");
	                sc.init(null, trustAllCerts, new java.security.SecureRandom());
	                HttpsURLConnection
	                                .setDefaultSSLSocketFactory(sc.getSocketFactory());
	        } catch (Exception e) {
	                e.printStackTrace();
	        }
	}


}
