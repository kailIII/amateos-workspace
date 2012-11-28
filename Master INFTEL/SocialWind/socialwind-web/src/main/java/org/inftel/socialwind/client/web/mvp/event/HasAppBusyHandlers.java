package org.inftel.socialwind.client.web.mvp.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;


public interface HasAppBusyHandlers extends HasHandlers {
  public HandlerRegistration addHasAppBusyHandler(AppBusyHandler handler); 
}
