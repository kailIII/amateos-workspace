package org.inftel.socialwind.client.desktop;

import org.inftel.socialwind.client.desktop.view.PopupMessages;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PopupHelper {
    
    private static PopupHelper singletone;

    private final BlockingQueue<String[]> queue = new ArrayBlockingQueue<String[]>(100);

    public PopupHelper() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    while (true) {
                        processQueue();
                    }
                } catch (InterruptedException ignore) {
                }
            }
        }, "PopupHelper").start();
    }

    public void queuePopup(String title, String message) {
        queue.offer(new String[] { title, message });
    }

    private void processQueue() throws InterruptedException {
        System.out.println("esperando cola de popups");
        String[] data = queue.take();
        PopupMessages popup = new PopupMessages(data[0], new Date().toString(), data[1]);
        popup.setVisible(true);
        
        // 10seg mostrando popup
        Thread.sleep(10 * 1000);
        popup.setVisible(false);
        popup.dispose();
        
        // 2seg por si se va a mostrar otro, q no sea inmediato
        Thread.sleep(2 * 1000);
    }

    public static synchronized PopupHelper getPopupHelper() {
        if (singletone == null) {
            singletone = new PopupHelper();
        } 
        return singletone;
    }

}
