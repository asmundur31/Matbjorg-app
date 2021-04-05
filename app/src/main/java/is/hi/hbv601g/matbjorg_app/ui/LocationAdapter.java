package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Location;

public class LocationAdapter extends ArrayAdapter<Location> {
    public LocationAdapter(Context context, ArrayList<Location> locations) {
        super(context, 0, locations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Location loc = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_location_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.LocationName);
        TextView location = (TextView) convertView.findViewById(R.id.LocationLocation);
        name.setText(loc.getName());
        String stringLocation = "Longitude: " + loc.getLongitude() + "\nLatitude: " + loc.getLatitude();
        location.setText(stringLocation);
        return convertView;
    }
}
