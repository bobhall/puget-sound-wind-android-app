package com.example.bhall.pugetsoundwind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by bhall on 8/13/16.
 */
public class StationAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;

    ArrayList<StationItem> stations;

    public StationAdapter(Context context, ArrayList<StationItem> stations) {
        this.stations = stations;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if (null == view) {
            view = inflater.inflate(R.layout.station_list_view, null);
        }

        ImageView station_image = (ImageView) view.findViewById(R.id.ndbcLandStation);
        if (stations.get(position).station_type == StationItem.StationType.FERRY) {
            station_image.setImageResource(R.drawable.wa_ferry_logo);
        }
        else if (stations.get(position).station_type == StationItem.StationType.NOAA) {
            station_image.setImageResource(R.drawable.noaa_logo);
        }
        else {
            station_image.setImageResource(R.drawable.coast_guard_logo);
        }

        TextView station_text = (TextView) view.findViewById(R.id.stationName);
        station_text.setText(stations.get(position).name);

        TextView weather_text = (TextView) view.findViewById(R.id.observationValue);
        weather_text.setText(stations.get(position).weather);

        TextView time_text = (TextView) view.findViewById(R.id.timeStamp);
        time_text.setText(stations.get(position).timestamp);

        return view;
    }
}
