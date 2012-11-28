package org.inftel.socialwind.client.web.mvp.activities;

import java.util.List;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.event.AppBusyEvent;
import org.inftel.socialwind.client.web.mvp.event.AppFreeEvent;
import org.inftel.socialwind.client.web.mvp.presenter.SesionListPresenter;
import org.inftel.socialwind.client.web.mvp.view.SesionListView;
import org.inftel.socialwind.shared.domain.SessionProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SurferRequest;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class SesionListActivity extends AbstractActivity implements SesionListPresenter {

    private ClientFactory clientFactory = GWT.create(ClientFactory.class);
    private EventBus eventBus;
    private SesionListView sesionListView;
    private SocialwindRequestFactory swrf;

    @Override
    public void bind() {
        sesionListView.setPresenter(this);
    }
    
    public SesionListActivity(Place place, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.swrf = clientFactory.getSWRF();
        this.eventBus = clientFactory.getEventBus();
        this.sesionListView = clientFactory.getSesionListView();
        onCargarSesiones();
        bind();
        panel.setWidget(sesionListView.asWidget());
    }

    @Override
    public void onCargarSesiones() {
        eventBus.fireEvent(new AppBusyEvent());
        SurferRequest sr = swrf.surferRequest();

        sr.findSessions().with("spot.location").fire(new Receiver<List<SessionProxy>>() {
            @Override
            public void onSuccess(List<SessionProxy> response) {
                sesionListView.addSesiones(response);
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
