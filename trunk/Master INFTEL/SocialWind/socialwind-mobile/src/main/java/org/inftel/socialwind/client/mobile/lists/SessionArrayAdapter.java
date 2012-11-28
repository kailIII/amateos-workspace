package org.inftel.socialwind.client.mobile.lists;

import java.util.ArrayList;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.vos.Session;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SessionArrayAdapter extends ArrayAdapter<Session> {
    private final Context context;
    private final ArrayList<Session> sessions;

    /**
     * Constructor
     * 
     * @param context
     * @param sessions
     */
    public SessionArrayAdapter(Context context, ArrayList<Session> sessions) {
        super(context, R.layout.row_sessions, sessions);
        this.context = context;
        this.sessions = sessions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_sessions, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.sessionName);
        name.setText(sessions.get(position).getSpot().getName());
        TextView description = (TextView) rowView.findViewById(R.id.sessionDescription);
        description.setText(sessions.get(position).getSpot().getDescription());
        ImageView hotspot = (ImageView) rowView.findViewById(R.id.hotspotImageView);
        TextView lastVisit = (TextView) rowView.findViewById(R.id.lastVisit);

        try {
            String[] date = sessions.get(position).getEnd().toGMTString().split(" ");
            lastVisit.setText(date[0] + "/" + date[1] + "/" + date[2]);
        } catch (Exception e) {
            Log.d("Adapter", "No existe fecha final de sesión");
            lastVisit.setText("-/-/-");
        }

        if (sessions.get(position).getSpot().getHot()) {
            hotspot.setVisibility(View.VISIBLE);
        } else {
            hotspot.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }

    /**
     * Actualiza la listview de sessions
     * 
     * @param sessions
     */
    public void updateList(ArrayList<Session> sessions) {
        this.sessions.clear();
        this.sessions.addAll(sessions);
    }
}
