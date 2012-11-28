package org.inftel.socialwind.client.web.mvp.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class HotspotListPlace extends Place {
    public HotspotListPlace() {        
    }
    
    public static class Tokenizer implements PlaceTokenizer<HotspotListPlace>{

        public HotspotListPlace getPlace(String token) {
                return new HotspotListPlace();
        }

        public String getToken(HotspotListPlace place) {
                return "";
        }
}
}
