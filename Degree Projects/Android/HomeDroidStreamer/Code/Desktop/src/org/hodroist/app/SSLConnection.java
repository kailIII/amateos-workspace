package org.hodroist.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author albertomateos
 */

//Allow SSL requests
public class SSLConnection {

    public SSLConnection(){
        
    }


    //execute a GET HTTPS request to url defined by uri param
    public String executeHttpsGet(URI uri) throws Exception {
        BufferedReader in = null;
        String result = "No result obtained from server";
        try {

            SSLContext sslContext = SSLContext.getInstance("SSL");

            // set up a TrustManager that trusts everything
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                        return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                                String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                                String authType) {
                }
            } }, new SecureRandom());

            SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Scheme httpsScheme = new Scheme("https", sf, 443);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(httpsScheme);

            HttpParams params = new BasicHttpParams();
            ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);


            HttpClient client = new DefaultHttpClient(cm, params);

            HttpGet request = new HttpGet();
            request.setURI(uri);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
            } finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //get ip of media server (this server)
    public String getMediaServerIP (){
        String mediaServerIP = null;

        try {
            InetAddress ip = InetAddress.getLocalHost();
            mediaServerIP = ip.getHostAddress();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return mediaServerIP;
    }

    //create an uri with base url and required params to connect to server
    public URI composeUri(String baseUrl, String userParam, String passParam, String ipParam) throws URISyntaxException{
        String url = baseUrl+"?user="+userParam+"&pass="+passParam+"&ip="+ipParam;
        URI uri = new URI(url);
        return uri;
    }

    //connect to server
    public String connect(String user, String pass) throws URISyntaxException, Exception{
        String result = executeHttpsGet(composeUri("https://localhost/streamer/mediaServerConnect.php",user,pass,getMediaServerIP()));
        return result;
    }

    //disconnect from server
    public String disconnect(String user, String pass) throws URISyntaxException, Exception{
        String result = executeHttpsGet(composeUri("https://localhost/streamer/mediaServerConnect.php",user,pass,""));
        return result;
    }
}
