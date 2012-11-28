package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyForName;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyForName(value = "org.inftel.socialwind.server.domain.DeviceInfo")
public interface DeviceInfoProxy extends ValueProxy {

    String getId();

    void setId(String id);

    String getRegistrationId();

    void setRegistrationId(String registrationId);

}
