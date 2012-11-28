package org.inftel.socialwind.client.web.mvp;


import org.inftel.socialwind.client.web.mvp.places.HotspotListPlace;
import org.inftel.socialwind.client.web.mvp.places.SesionListPlace;
import org.inftel.socialwind.client.web.mvp.places.SpotPlace;
import org.inftel.socialwind.client.web.mvp.places.IntroduccionPlace;
import org.inftel.socialwind.client.web.mvp.places.PerfilPlace;
import org.inftel.socialwind.client.web.mvp.places.PlayasListPlace;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ PerfilPlace.Tokenizer.class, PlayasListPlace.Tokenizer.class, IntroduccionPlace.Tokenizer.class, SpotPlace.Tokenizer.class, HotspotListPlace.Tokenizer.class, SesionListPlace.Tokenizer.class })
public interface AppPlacesHistoryMapper extends PlaceHistoryMapper {
}
