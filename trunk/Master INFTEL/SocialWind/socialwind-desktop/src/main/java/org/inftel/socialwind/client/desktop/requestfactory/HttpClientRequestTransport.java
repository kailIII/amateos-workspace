package org.inftel.socialwind.client.desktop.requestfactory;

import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import static org.inftel.socialwind.client.desktop.requestfactory.StreamUtils.readStreamAsString;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Implementacion de {@link RequestTransport} para usar entre un cliente Standalone JRE y un
 * servidor Google AppEngine.
 */
public class HttpClientRequestTransport implements RequestTransport {

    private final URI uri;

    private final String cookie;

    /**
     * Contruye una instancia de transporte para RequestFactory basada en el cliente HTTP de apache.
     * 
     * @param uri
     *            la URI del servicio RequestFactory (e.g. http://app.appspot.com/gwtRequest)
     * @param cookie
     *            la coocke ACSID o SACSID usada para autenticarse
     * 
     * @see HttpClient
     */
    public HttpClientRequestTransport(URI uri, String cookie) {
        this.uri = uri;
        this.cookie = cookie;
    }

    public void send(String payload, TransportReceiver receiver) {
        // Cliente HTTP de Apache
        HttpClient client = new DefaultHttpClient();

        // Configurar cabeceras y cookie de autenticacion
        HttpPost post = new HttpPost();
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        post.setHeader("Cookie", cookie);
        post.setURI(uri);

        Throwable ex;
        try {
            // Contenido de la peticion
            post.setEntity(new StringEntity(payload, "UTF-8"));

            // Se envia la peticion
            HttpResponse response = client.execute(post);

            // Procesar respuesta
            if (200 == response.getStatusLine().getStatusCode()) {
                // Se ha recibido correctamente la solicitud
                String contents = readStreamAsString(response.getEntity().getContent());
                receiver.onTransportSuccess(contents);
            } else {
                // Se ha recibido un fallo desde el servidor
                ServerFailure sf = new ServerFailure(response.getStatusLine().getReasonPhrase());
                receiver.onTransportFailure(sf);
            }
            return;
        } catch (UnsupportedEncodingException e) {
            ex = e;
        } catch (ClientProtocolException e) {
            ex = e;
        } catch (IOException e) {
            ex = e;
        }
        receiver.onTransportFailure(new ServerFailure(ex.getMessage()));
    }
    
}
