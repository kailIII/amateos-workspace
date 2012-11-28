package org.inftel.socialwind.client.web.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class IntroduccionPlace extends Place {
    public IntroduccionPlace(){
    }

    public static class Tokenizer implements PlaceTokenizer<IntroduccionPlace>{

            public IntroduccionPlace getPlace(String token) {
                    return new IntroduccionPlace();
            }

            public String getToken(IntroduccionPlace place) {
                    return "";
            }
    }
}
