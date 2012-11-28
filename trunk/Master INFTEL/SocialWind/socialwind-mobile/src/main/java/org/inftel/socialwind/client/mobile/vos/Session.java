package org.inftel.socialwind.client.mobile.vos;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

    private static final long serialVersionUID = -8309234538237793766L;
    private Spot spot;
    private Date start;
    private Date end;

    /**
     * Constructor
     */
    public Session() {

    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

}
