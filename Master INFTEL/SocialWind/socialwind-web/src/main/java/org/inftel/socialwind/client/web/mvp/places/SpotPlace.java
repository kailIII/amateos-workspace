package org.inftel.socialwind.client.web.mvp.places;

import org.inftel.socialwind.shared.domain.SpotProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class SpotPlace extends Place {

    private SpotProxy proxy;

    public SpotPlace(SpotProxy token) {
        this.proxy = token;
    }

    public void setProxy(SpotProxy proxy) {
        this.proxy = proxy;
    }

    public SpotProxy getProxy() {
        return proxy;
    }

    public static class Tokenizer implements PlaceTokenizer<SpotPlace> {

        public SpotPlace getPlace(String token) {
            return null;
        }

        public String getToken(SpotPlace place) {
            if (place.proxy == null) {
                return "";
            } else {
                return place.proxy.getName().toLowerCase();
            }
        }
    }
}
