package org.inftel.socialwind.client.web;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.shared.domain.SessionProxy;
import org.inftel.socialwind.shared.domain.SpotProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SpotRequest;
import org.inftel.socialwind.shared.services.SurferRequest;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class Utilidades {

    public static final Location DEFAULT_LOCATION = new Location(36.723, -4.415);

    public static String calculaDistancia(Location p1, Location p2) {
        double degtorad = 0.01745329;
        double radtodeg = 57.29577951;

        double lat = p1.getLatitud();
        double lon = p1.getLongitud();
        double lat2 = p2.getLatitud();
        double lon2 = p2.getLongitud();

        double dlong = (lon - lon2);
        double dvalue = (Math.sin(lat * degtorad) * Math.sin(lat2 * degtorad))
                + (Math.cos(lat * degtorad) * Math.cos(lat2 * degtorad) * Math
                        .cos(dlong * degtorad));

        double dd = Math.acos(dvalue) * radtodeg;

        int km = (int) (dd * 111.302);

        return Integer.toString(km);
    }

    public static void creaSpot(SpotProxy spot) {
        ClientFactory clientFactory = GWT.create(ClientFactory.class);
        SocialwindRequestFactory swrf = clientFactory.getSWRF();
        SpotRequest request = swrf.spotRequest();
        request.persist(spot).fire();
    }

    public static void creaSesion(Location spotTemp) {
        ClientFactory clientFactory = GWT.create(ClientFactory.class);
        SocialwindRequestFactory swrf = clientFactory.getSWRF();
        SurferRequest request = swrf.surferRequest();
        request.updateSurferLocation(spotTemp.getLatitud(), spotTemp.getLongitud()).fire(
                new Receiver<SessionProxy>() {
                    public void onFailure(ServerFailure error) {
                        System.out.println(error.getMessage());
                    }

                    public void onSuccess(SessionProxy response) {
                        System.out.println("Creando sesión....");
                        ClientFactory clientFactory = GWT.create(ClientFactory.class);
                        SocialwindRequestFactory swrf = clientFactory.getSWRF();
                        SurferRequest request = swrf.surferRequest();
                        request.updateSurferLocation(DEFAULT_LOCATION.getLatitud(),
                                DEFAULT_LOCATION.getLongitud()).fire(new Receiver<SessionProxy>() {
                            public void onFailure(ServerFailure error) {
                                System.out.println(error.getMessage());
                            }

                            public void onSuccess(SessionProxy response) {
                                System.out.println("Sesión creada.");
                            }
                        });
                    }
                });

    }
}
