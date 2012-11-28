package org.inftel.socialwind.client.web.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class SesionListPlace extends Place {
    public SesionListPlace(){
    }

    public static class Tokenizer implements PlaceTokenizer<SesionListPlace>{

            public SesionListPlace getPlace(String token) {
                    return new SesionListPlace();
            }

            public String getToken(SesionListPlace place) {
                    return "";
            }
    }
}
