package org.inftel.socialwind.client.web.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PerfilPlace extends Place {
    public PerfilPlace(){
    }

    public static class Tokenizer implements PlaceTokenizer<PerfilPlace>{

            public PerfilPlace getPlace(String token) {
                    return null;
            }

            public String getToken(PerfilPlace place) {
                    return "";
            }
    }
}
