package org.inftel.socialwind.client.web.mvp;

import org.inftel.socialwind.client.web.mvp.view.IntroduccionView;
import org.inftel.socialwind.client.web.mvp.view.PerfilView;
import org.inftel.socialwind.client.web.mvp.view.PlayasListView;
import org.inftel.socialwind.client.web.mvp.view.SesionListView;
import org.inftel.socialwind.client.web.mvp.view.SpotView;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;

public interface ClientFactory {
    public EventBus getEventBus();
    public SocialwindRequestFactory getSWRF();
    PlayasListView getPlayasListView();
    PerfilView getPerfilView();
    IntroduccionView getIntroduccionView();
    SpotView getSpotView();
    SesionListView getSesionListView();
    PlaceController getPlaceController();
    public SurferProxy getSurfer();
    public void setSurfer(SurferProxy surfer);
}
