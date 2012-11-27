package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "org.inftel.socialwind.server.domain.Location")
public interface LocationProxy extends ValueProxy {

    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);

}
