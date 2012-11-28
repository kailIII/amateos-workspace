package org.inftel.socialwind.client.web.mvp.activities;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.places.SpotPlace;
import org.inftel.socialwind.client.web.mvp.presenter.SpotPresenter;
import org.inftel.socialwind.client.web.mvp.view.SpotView;
import org.inftel.socialwind.shared.domain.SpotProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class SpotActivity extends AbstractActivity implements SpotPresenter {

    private ClientFactory clientFactory = GWT.create(ClientFactory.class);
    private EventBus eventBus;
    private SpotView spotView;
    private SocialwindRequestFactory swrf;
    private SpotProxy proxy;

    public SpotActivity(SpotPlace place, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
        this.proxy = place.getProxy();
    }

    @Override
    public void bind() {
        spotView.setPresenter(this);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.spotView = clientFactory.getSpotView();
        this.swrf = clientFactory.getSWRF();
        this.eventBus = clientFactory.getEventBus();
        this.onCargarSpot();
        bind();
        panel.setWidget(spotView.asWidget());
    }

    public void onCargarSpot() {
        spotView.cargaInterfaz(proxy);
    }

}
