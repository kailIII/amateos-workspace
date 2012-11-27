package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

import java.util.Date;

/**
 * 
 * @author ibaca
 * 
 */
@ProxyForName(value = "org.inftel.socialwind.server.domain.Session", locator = "org.inftel.socialwind.server.locators.EntityLocator")
public interface SessionProxy extends EntityProxy {

    EntityProxyId<SessionProxy> stableId();

    Date getStart();

    Date getEnd();

    SpotProxy getSpot();

}
