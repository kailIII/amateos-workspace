package org.inftel.socialwind.shared.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.ProxyForName;

/**
 * 
 * @author ibaca
 * 
 */
@ProxyForName(value = "org.inftel.socialwind.server.domain.Surfer", locator = "org.inftel.socialwind.server.locators.EntityLocator")
public interface SurferProxy extends EntityProxy {

    /** Id para capa de presentacion */
    EntityProxyId<SurferProxy> stableId();

    /** El nombre completo de usuario, o lo mas completo posible */
    String getFullName();

    /** Modifica el nombre completo de usuario */
    void setFullName(String fullName);

    /** El apodo del usuario, debe ser corto */
    String getDisplayName();

    /** Modifica el apodo del usuario */
    void setDisplayName(String displayName);

    /** Obtiene el correo de usuario, vinculado a la cuenta google autorizada */
    String getEmail();

    // Esto no debe modificarse, asocia el usuario con la cuenta de google
    // void setEmail(String email);

    /** http://en.gravatar.com/site/implement/hash/ */
    String getGravatarHash();

    /** Ultima posicion almacenada del usuario */
    LocationProxy getLocation();

}
