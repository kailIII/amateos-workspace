package org.inftel.socialwind.client.web.mvp.event;

import com.google.gwt.event.shared.EventHandler;

public interface AppBusyHandler extends EventHandler{
	  public void onAppBusyEvent(AppBusyEvent event);
}
