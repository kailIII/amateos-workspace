package org.inftel.socialwind.client.desktop;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.inftel.socialwind.shared.domain.EvenewsProxy;
import org.inftel.socialwind.shared.services.EvenewsRequest;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class EvenewsPuller implements PropertyChangeListener {

    private Timer timer = new Timer("Evenews Puller Service", true);

    private TimerTask worker;

    public EvenewsPuller() {
    }

    public void start(SocialwindRequestFactory factory) {
        worker = new PullerTask(factory);
        timer.schedule(worker, 10 * 1000, 60 * 1000);
    }

    public void stop() {
        worker.cancel();
        worker = null;
        timer.purge();
    }

    private class PullerTask extends TimerTask {

        private SocialwindRequestFactory factory;
        private Date lastRequest = new Date();

        public PullerTask(SocialwindRequestFactory factory) {
            this.factory = factory;
        }

        @Override
        public void run() {
            EvenewsRequest request = factory.evenewsRequest();
            System.out.println("consultando evenews desde " + lastRequest.toString());
            request.findEvenewsSince(lastRequest).fire(new EvenewsReceiver());
        }

        private class EvenewsReceiver extends Receiver<List<EvenewsProxy>> {
            @Override
            public void onSuccess(List<EvenewsProxy> result) {
                System.out.println("se han recibido " + result.size() + " evenews");
                // lastRequest = new Date(); // FIXME esto es poco serio!
                for (EvenewsProxy evenewsProxy : result) {
                    updateLastRequest(evenewsProxy.getDate());
                    PopupHelper.getPopupHelper().queuePopup("Notificación SocialWind",
                            evenewsProxy.getMessage());
                }
            }

            @Override
            public void onFailure(ServerFailure error) {
                System.out.println("error haciendo pulling de evenews: " + error.getMessage());
            }
        }

        /** Si la fecha pasada es posterior, se guarda para no consultarla mas */
        public void updateLastRequest(Date expectedLast) {
            if (lastRequest.before(expectedLast)) {
                lastRequest = expectedLast;
            }

        }

    }

    public void propertyChange(PropertyChangeEvent event) {
        // Controlamos los cambios en swrf
        if (event.getPropertyName().equals("requestFactory")) {
            if (event.getNewValue() instanceof SocialwindRequestFactory) {
                // Si se crea factoria, se inicia puller
                SocialwindRequestFactory factory = (SocialwindRequestFactory) event.getNewValue();
                start(factory);
            } else {
                // Si no se detiene
                stop();
            }
        }

    }

}
