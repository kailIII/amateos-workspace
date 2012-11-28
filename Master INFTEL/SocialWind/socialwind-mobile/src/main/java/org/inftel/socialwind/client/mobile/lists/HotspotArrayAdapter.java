package org.inftel.socialwind.client.mobile.lists;

import java.util.ArrayList;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.utils.LocationUtil;
import org.inftel.socialwind.client.mobile.vos.Spot;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HotspotArrayAdapter extends ArrayAdapter<Spot> {
    private final Context context;
    private final ArrayList<Spot> hotspots;
    private Location location;

    /**
     * Constructor
     * 
     * @param context
     * @param hotspots
     */
    public HotspotArrayAdapter(Context context, ArrayList<Spot> hotspots) {
        super(context, R.layout.row_hotspots, hotspots);
        this.context = context;
        this.hotspots = hotspots;
        location = LocationUtil.getLocation(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_hotspots, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        name.setText(hotspots.get(position).getName());

        TextView description = (TextView) rowView.findViewById(R.id.description);
        description.setText(hotspots.get(position).getDescription());

        TextView surferCount = (TextView) rowView.findViewById(R.id.surferCountTextView);
        surferCount.setText(hotspots.get(position).getSurferCurrentCount().toString());

        TextView distance = (TextView) rowView.findViewById(R.id.distanceTextView);
        try {
            distance.setText("("
                    + LocationUtil.getDistanceToSpot(location.getLatitude(), location
                            .getLongitude(), hotspots.get(position).getLatitude(),
                            hotspots.get(position).getLongitude()) + " km)");
        } catch (NullPointerException e) {
            Log.d("Adapter", "No existe localización del usuario");
            distance.setTag("");
        }
        return rowView;
    }

    /**
     * Actualiza la listview de hotspots
     * 
     * @param hotspots
     */
    public void updateList(ArrayList<Spot> hotspots) {
        this.hotspots.clear();
        this.hotspots.addAll(hotspots);
    }
}
