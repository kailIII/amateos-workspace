package org.inftel.socialwind.client.desktop.requestfactory;

import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

import static org.inftel.socialwind.client.desktop.requestfactory.StreamUtils.readStreamAsString;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.inftel.socialwind.client.desktop.model.SurferPreferences;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialWindServer {

    private static final String GOOGLE_CLIENT_LOGIN = "https://www.google.com/accounts/ClientLogin";
    SurferPreferences preferences;
    String cookie;

    public SocialWindServer(SurferPreferences preferences) {
        this.preferences = preferences;
    }
    
    /**
     * Se conecta a appengine
     */
    public SocialwindRequestFactory connect() {
        String username = preferences.getUserName();
        String password = preferences.getPassword();
        String appurl = preferences.getAppUrl();
        String appid = preferences.getAppId();

        PostResponse authResponse;
        try {
            // Autenticar usuario/password en servidores google
            List<String[]> postParams = getClientLoginPostParams(username, password, appid);
            authResponse = executePost(GOOGLE_CLIENT_LOGIN, postParams);
        } catch (IOException e) {
            throw new RuntimeException(
                    "No se ha podido acceder al servidor de autenticación de google");
        }

        String url;
        try {
            // Obtener token de authorizacion de google
            String token = processAuthResponse(authResponse, username);
            url = appurl + "/_ah/login" + "?auth=" + URLEncoder.encode(token, "UTF-8");
            // + "&continue=http://localhost/";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("La maquina JVM no soporta la codificación UTF-8");
        }

        try {
            // Obtener cookie del usuario en appengine
            List<Cookie> cookies = getAppEngineLoginCookies(url);
            // Se supone que solo viene una, sino habra que obtener la adecuada
            cookie = cookies.get(0).getName() + "=" + cookies.get(0).getValue();
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido iniciar session en el servidor appengine");
        }

        return getRequestFactory(cookie, SocialwindRequestFactory.class);

    }

    private List<String[]> getClientLoginPostParams(String email, String password, String appid) {
        List<String[]> result = new ArrayList<String[]>();
        result.add(new String[] { "Email", email });
        result.add(new String[] { "Passwd", password });
        result.add(new String[] { "source", appid });
        result.add(new String[] { "service", "ah" });
        result.add(new String[] { "accountType", "HOSTED_OR_GOOGLE" });
        return result;
    }

    private String processAuthResponse(PostResponse authResponse, String email) {
        if (authResponse.statusCode == 200 || authResponse.statusCode == 403) {
            Map<String, String> responseMap = parseClientLoginResponse(authResponse.body);
            if (authResponse.statusCode == 200) {
                return responseMap.get("Auth");
            } else {
                String reason = responseMap.get("Error");
                if ("BadAuthentication".equals(reason)) {
                    String info = responseMap.get("Info");
                    if (info != null && !info.isEmpty()) {
                        reason = reason + " " + info;
                    }
                }
                throw new RuntimeException("Login failed. Reason: " + reason);
            }
        } else if (authResponse.statusCode == 401) {
            throw new RuntimeException("Email \"" + email + "\" and password do not match.");
        } else {
            throw new RuntimeException("Bad authentication response: " + authResponse.statusCode);
        }
    }

    /**
     * Parses the key-value pairs in the ClientLogin response.
     */
    private Map<String, String> parseClientLoginResponse(String body) {
        Map<String, String> response = new HashMap<String, String>();
        for (String line : body.split("\n")) {
            int eqIndex = line.indexOf("=");
            if (eqIndex > 0) {
                response.put(line.substring(0, eqIndex), line.substring(eqIndex + 1));
            }
        }
        return response;
    }

    private static class PostResponse {
        final int statusCode;
        final String body;

        PostResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
    }

    private PostResponse executePost(String url, List<String[]> postParams) throws IOException {
        // Peticion POST
        HttpPost post = new HttpPost(url);

        // Parametros
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (String[] param : postParams) {
            formParams.add(new BasicNameValuePair(param[0], param[1]));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, HTTP.UTF_8);
        post.setEntity(entity);
        // post.setParams(params);

        // Realizar consulta de autenticacion
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(post);
        String content = readStreamAsString(response.getEntity().getContent());
        return new PostResponse(response.getStatusLine().getStatusCode(), content);
    }

    private List<Cookie> getAppEngineLoginCookies(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpClientParams.setRedirecting(get.getParams(), false);

        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);

        if (response.getStatusLine().getStatusCode() == 302) {
            return new ArrayList<Cookie>(client.getCookieStore().getCookies());
        } else {
            throw new RuntimeException("respuesta desconocida de app engine: "
                    + response.getStatusLine().getStatusCode());
        }
    }

    private <T extends RequestFactory> T getRequestFactory(String cookie, Class<T> factoryClass) {
        String uriString = preferences.getAppUrl() + preferences.getRequestFactoryMethod();
        try {
            // Crear la nueva instancia y conectarla con el servidor
            T requestFactory = RequestFactorySource.create(factoryClass);
            String authCookie = cookie;
            URI uri = new URI(uriString);
            RequestTransport rt = new HttpClientRequestTransport(uri, authCookie);
            requestFactory.initialize(new SimpleEventBus(), rt);
            return requestFactory;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Fallo al generar la URI del server (" + uriString + ")");
        }
    }

    static void enableHttpLog() {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty(
                "org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
    }
}
