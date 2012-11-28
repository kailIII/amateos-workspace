package org.inftel.socialwind.client.web.mvp;

import org.inftel.socialwind.client.web.mvp.i18nview.IntroduccionViewImpl;
import org.inftel.socialwind.client.web.mvp.view.IntroduccionView;
import org.inftel.socialwind.client.web.mvp.view.PerfilView;
import org.inftel.socialwind.client.web.mvp.view.PerfilViewImpl;
import org.inftel.socialwind.client.web.mvp.view.PlayasListView;
import org.inftel.socialwind.client.web.mvp.view.PlayasListViewImpl;
import org.inftel.socialwind.client.web.mvp.view.SesionListView;
import org.inftel.socialwind.client.web.mvp.view.SesionListViewImpl;
import org.inftel.socialwind.client.web.mvp.view.SpotView;
import org.inftel.socialwind.client.web.mvp.view.SpotViewImpl;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;

/**
 * Clase encargada de implementar el patron singleton para los objetos mas pesados de la aplicacion
 * 
 * @author aljiru
 * 
 */
public class ClientFactoryImpl implements ClientFactory {

    /** Bus encargado de gestionar los distintos eventos de la aplicacion */
    private static EventBus eventBus;

    /** Vista encargada de mostrar el listados de playas */
    private static PlayasListView playasListView;

    /** Vista encargada de mostrar el perfil */
    private static PerfilView perfilView;
    
    /** Objeto necesario para realizar la comunicacion con el servidor */
    private static SocialwindRequestFactory swrf;
    
    /** Objeto necesario para realizar los cambios de actividad */
    private static PlaceController placeController;

    private static IntroduccionView introduccionView;

    private static SpotView spotView;

    private static SesionListView sesionView;    

    private static SurferProxy surfer;

    @Override
    public EventBus getEventBus() {
        if (eventBus == null)
            eventBus = new SimpleEventBus();
        return eventBus;
    }

    @Override
    public PlayasListView getPlayasListView() {
        if (playasListView == null)
            playasListView = new PlayasListViewImpl();
        return playasListView;
    }

    public PerfilView getPerfilView() {
        if (perfilView == null)
            perfilView = new PerfilViewImpl();
        return perfilView;
    }

    public IntroduccionView getIntroduccionView() {
        if (introduccionView == null)
            introduccionView = new IntroduccionViewImpl();
        return introduccionView;
    }

    @Override
    public SocialwindRequestFactory getSWRF() {
        if (swrf == null) {
            swrf = GWT.create(SocialwindRequestFactory.class);
            DefaultRequestTransport rt = new DefaultRequestTransport();
            // rt.setRequestUrl("http://socialwindgwt.appspot.com/gwtRequest");
            swrf.initialize(eventBus, rt);
        }
        return swrf;
    }

    @Override
    public PlaceController getPlaceController() {
        if (placeController == null)
            placeController = new PlaceController(getEventBus());
        return placeController;
    }

    @Override
    public SpotView getSpotView() {
        if (spotView == null)
            spotView = new SpotViewImpl();
        return spotView;
    }

    public SurferProxy getSurfer() {
        return surfer;
    }

    public void setSurfer(SurferProxy surfero) {
        surfer = surfero;
    }

    @Override
    public SesionListView getSesionListView() {
        if (sesionView == null)
            sesionView = new SesionListViewImpl();
        return sesionView;
    }

}
