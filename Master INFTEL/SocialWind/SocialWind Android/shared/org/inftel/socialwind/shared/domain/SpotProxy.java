package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

/**
 * 
 * @author ibaca
 * 
 */
@ProxyForName(value = "org.inftel.socialwind.server.domain.Spot", locator = "org.inftel.socialwind.server.locators.EntityLocator")
public interface SpotProxy extends EntityProxy {

    EntityProxyId<SpotProxy> stableId();

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String getImgUrl();

    void setImgUrl(String imgUrl);

    void setLocation(LocationProxy location);

    Boolean getHot();

    public Integer getSurferCount();

    public Integer getSurferCurrentCount();

    LocationProxy getLocation();

}
