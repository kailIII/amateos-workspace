package org.inftel.socialwind.client.web.mvp;


import org.inftel.socialwind.client.web.mvp.activities.IntroduccionActivity;
import org.inftel.socialwind.client.web.mvp.activities.PerfilActivity;
import org.inftel.socialwind.client.web.mvp.activities.PlayasListActivity;
import org.inftel.socialwind.client.web.mvp.activities.SesionListActivity;
import org.inftel.socialwind.client.web.mvp.activities.SpotActivity;
import org.inftel.socialwind.client.web.mvp.places.HotspotListPlace;
import org.inftel.socialwind.client.web.mvp.places.IntroduccionPlace;
import org.inftel.socialwind.client.web.mvp.places.PerfilPlace;
import org.inftel.socialwind.client.web.mvp.places.PlayasListPlace;
import org.inftel.socialwind.client.web.mvp.places.SesionListPlace;
import org.inftel.socialwind.client.web.mvp.places.SpotPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppActivityMapper implements ActivityMapper {

    private ClientFactory clientFactory;

    public AppActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }

    public Activity getActivity(Place place) {
        if (place instanceof PlayasListPlace)
            return new PlayasListActivity((PlayasListPlace) place, clientFactory, false);
        else if (place instanceof PerfilPlace)
            return new PerfilActivity((PerfilPlace) place, clientFactory);
        else if (place instanceof IntroduccionPlace)
            return new IntroduccionActivity((IntroduccionPlace) place, clientFactory);
        else if (place instanceof SpotPlace)
            return new SpotActivity((SpotPlace) place, clientFactory);
        else if (place instanceof SesionListPlace)
            return new SesionListActivity((SesionListPlace) place, clientFactory);
        else if (place instanceof HotspotListPlace)
            return new PlayasListActivity((HotspotListPlace) place, clientFactory, true);
        else
            return null;
    }
}
