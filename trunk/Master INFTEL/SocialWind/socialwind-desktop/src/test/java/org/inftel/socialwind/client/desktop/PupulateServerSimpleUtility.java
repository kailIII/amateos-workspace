package org.inftel.socialwind.client.desktop;

import org.inftel.socialwind.client.desktop.model.SurferPreferences;
import org.inftel.socialwind.client.desktop.requestfactory.SocialWindServer;
import org.inftel.socialwind.shared.domain.LocationProxy;
import org.inftel.socialwind.shared.domain.SessionProxy;
import org.inftel.socialwind.shared.domain.SpotProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SpotRequest;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class PupulateServerSimpleUtility {

    static SocialwindRequestFactory factory;

    static class L {
        public String name;
        public double lat;
        public double lon;

        public L(String name, double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
            this.name = name;
        }
    }

    static L[] locations;

    static {
        // Puntos varios para añadir y validar
        L malaga = new L("malaga", 36.723, -4.415);
        L playamar = new L("playamar", 36.6332, -4.4869);
        L elpalo = new L("elpalo", 36.7177, -4.3492);
        L almeria = new L("almeria", 36.836, -2.403);
        L inftel = new L("inftel", 36.71519, -4.47785);
        locations = new L[] { malaga, playamar, elpalo, almeria, inftel };
    }

    public static void main(String[] args) {

        // Usa la sesion q tenga configurada el usuario! asi q atento
        final SurferPreferences preferences = new SurferPreferences();
        final SocialWindServer server = new SocialWindServer(preferences);
        final SocialwindRequestFactory factory = server.connect();

        // createSpot(factory);
        updateLocation(factory);

        System.exit(0);
    }

    @SuppressWarnings("unused")
    private static void updateLocation(final SocialwindRequestFactory factory) {
        // rincon 36.717, -4.274
        // micasa 36.7341, -4.3924
        factory.surferRequest().updateSurferLocation(36.717, -4.274)
                .fire(new Receiver<SessionProxy>() {
                    @Override
                    public void onSuccess(SessionProxy response) {
                        System.out.println("success: " + response);
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        System.out.println("failure (fatal?"+error.isFatal()+"):" + error.getMessage());
                    }
                });
    }

    @SuppressWarnings("unused")
    private static void createSpot(final SocialwindRequestFactory factory) {
        SpotRequest request = factory.spotRequest();

        LocationProxy location = request.create(LocationProxy.class);
        location.setLatitude(36.7513);
        location.setLongitude(-3.8647);

        SpotProxy spot = request.create(SpotProxy.class);
        spot.setDescription("En invierno, cuando el viento esta de norte es el mejor spot");
        spot.setImgUrl("http://www.alanya-holidays.com/images/alanya_stor_06.jpg");
        spot.setLocation(location);
        spot.setName("Nerja");

        request.persist(spot);
        request.fire(new Receiver<Void>() {

            @Override
            public void onSuccess(Void response) {
                System.out.println("success");
            }

            @Override
            public void onFailure(ServerFailure error) {
                System.out.println("failure: " + error.getMessage());
                super.onFailure(error);
            }
        });
    }

}
