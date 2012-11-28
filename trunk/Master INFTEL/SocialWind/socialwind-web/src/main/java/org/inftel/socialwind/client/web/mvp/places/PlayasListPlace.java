package org.inftel.socialwind.client.web.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class PlayasListPlace extends Place {
    public PlayasListPlace(){
    }

    public static class Tokenizer implements PlaceTokenizer<PlayasListPlace>{

            public PlayasListPlace getPlace(String token) {
                    return new PlayasListPlace();
            }

            public String getToken(PlayasListPlace place) {
                    return "";
            }
    }
}
