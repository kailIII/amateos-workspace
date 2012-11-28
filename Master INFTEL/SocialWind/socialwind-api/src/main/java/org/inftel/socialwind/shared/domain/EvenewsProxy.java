package org.inftel.socialwind.shared.domain;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

/**
 * 
 * @author ibaca
 * 
 */
@ProxyForName(value = "org.inftel.socialwind.server.domain.Evenews", locator = "org.inftel.socialwind.server.locators.EntityLocator")
public interface EvenewsProxy extends EntityProxy {

    /** Id para capa de presentacion */
    EntityProxyId<EvenewsProxy> stableId();

    /** Mensage que trasmite <code>evenews</code> */
    String getMessage();

    /** Posicion a la que se asocia el <code>evenews</code> */
    LocationProxy getLocation();
    
    /** Fecha en la que se creo el evenews */
    Date getDate();

}
