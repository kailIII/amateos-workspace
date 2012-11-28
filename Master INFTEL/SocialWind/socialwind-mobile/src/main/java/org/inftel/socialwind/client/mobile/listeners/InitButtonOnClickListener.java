package org.inftel.socialwind.client.mobile.listeners;

import org.inftel.socialwind.client.mobile.activities.SocialWindActivity;

import android.view.View;
import android.view.View.OnClickListener;

public class InitButtonOnClickListener implements OnClickListener {

    SocialWindActivity activity;

    /**
     * Constructor
     * @param activity
     */
    public InitButtonOnClickListener(SocialWindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

        activity.getDataFromServer();
    }

}
