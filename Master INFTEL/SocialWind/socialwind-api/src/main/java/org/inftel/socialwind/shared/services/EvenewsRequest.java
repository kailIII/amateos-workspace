package org.inftel.socialwind.shared.services;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import org.inftel.socialwind.shared.domain.EvenewsProxy;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author ibaca
 * 
 */
@ServiceName("org.inftel.socialwind.server.services.EvenewsService")
public interface EvenewsRequest extends RequestContext {

    Request<List<EvenewsProxy>> findEvenewsSince(Date fromDate);

    Request<List<EvenewsProxy>> findEvenewsEntries(int firstResult, int maxResults);

}
