package org.inftel.socialwind.client.web.mvp.activities;

import java.util.List;

import org.inftel.socialwind.client.web.Location;
import org.inftel.socialwind.client.web.Utilidades;
import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.event.AppBusyEvent;
import org.inftel.socialwind.client.web.mvp.event.AppFreeEvent;
import org.inftel.socialwind.client.web.mvp.places.SpotPlace;
import org.inftel.socialwind.client.web.mvp.presenter.PlayasListPresenter;
import org.inftel.socialwind.client.web.mvp.view.PlayasListView;
import org.inftel.socialwind.shared.domain.SpotProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SpotRequest;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Implementacion del Presenter para el listado de playas
 * 
 * @author aljiru
 * 
 */
public class PlayasListActivity extends AbstractActivity implements PlayasListPresenter {

    private ClientFactory clientFactory = GWT.create(ClientFactory.class);
    private EventBus eventBus;
    private PlayasListView playasListView;
    private SocialwindRequestFactory swrf;
    private boolean hot;

    /**
     * Metodo constructor que inicializa las distintas variables que tienen parte en esta clase
     * 
     * @param playasListView
     *            Vista del listado de playas
     */
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.swrf = clientFactory.getSWRF();
        this.eventBus = clientFactory.getEventBus();
        this.playasListView = clientFactory.getPlayasListView();
        if (hot) {
            onCargarListadoHotspots();
        } else {
            this.onCargarListadoPlayas();
        }
        bind();
        panel.setWidget(playasListView.asWidget());
    }

    public PlayasListActivity(Place place, ClientFactory clientFactory, boolean hot) {
        this.clientFactory = clientFactory;
        this.hot = hot;
    }

    /**
     * Le pasa a la vista un objeto Presenter
     */
    public void bind() {
        playasListView.setPresenter(this);
    }

    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }

    /**
     * Metodo encargado de comunicarse con el servidor para obtener el listado de playas a mostrar.
     */
    public void onCargarListadoPlayas() {
        eventBus.fireEvent(new AppBusyEvent());
        SpotRequest sr = swrf.spotRequest();
        sr.findAllSpots().with("location").fire(new Receiver<List<SpotProxy>>() {
            @Override
            public void onSuccess(List<SpotProxy> response) {
                playasListView.addPlayas(response);

                eventBus.fireEvent(new AppFreeEvent());
            }

            @Override
            public void onFailure(ServerFailure error) {
                System.out.println(error.getMessage());
                eventBus.fireEvent(new AppFreeEvent());
            }
        });
    }

    @Override
    public void onPlayaSeleccionada(SpotProxy id) {
        goTo(new SpotPlace(id));
    }

    @Override
    public void onCargarListadoHotspots() {
        eventBus.fireEvent(new AppBusyEvent());
        SpotRequest sr = swrf.spotRequest();
        Location l = Utilidades.DEFAULT_LOCATION;

        sr.findNearbyHotSpots(l.getLatitud(), l.getLongitud()).with("location")
                .fire(new Receiver<List<SpotProxy>>() {
                    @Override
                    public void onSuccess(List<SpotProxy> response) {
                        playasListView.addPlayas(response);
                        eventBus.fireEvent(new AppFreeEvent());
                    }

                    @Override
                    public void onFailure(ServerFailure error) {
                        System.out.println(error.getMessage());
                        eventBus.fireEvent(new AppFreeEvent());
                    }
                });
    }

}
