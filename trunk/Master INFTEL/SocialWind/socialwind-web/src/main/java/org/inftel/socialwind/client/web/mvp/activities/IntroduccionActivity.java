package org.inftel.socialwind.client.web.mvp.activities;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.places.IntroduccionPlace;
import org.inftel.socialwind.client.web.mvp.presenter.IntroduccionPresenter;
import org.inftel.socialwind.client.web.mvp.view.IntroduccionView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class IntroduccionActivity extends AbstractActivity implements IntroduccionPresenter {

    private ClientFactory clientFactory = GWT.create(ClientFactory.class);
    private IntroduccionView introduccionView;
    
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.introduccionView = clientFactory.getIntroduccionView();
        bind();
        panel.setWidget(introduccionView.asWidget());
    }
    
    public IntroduccionActivity(IntroduccionPlace place, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
}
    
    /**
     * Le pasa a la vista un objeto Presenter
     */
    public void bind() {
        introduccionView.setPresenter(this);
    }
    
//    public void goTo(Place place) {
//        clientFactory.getPlaceController().goTo(place);
//    }    

}
