package popcornchicken.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import popcornchicken.myapplication.R;
import popcornchicken.myapplication.Utility.Flight;

/**
 * Created by ianwind2 on 15/11/8.
 */
public class FlightAdapter extends BaseAdapter {
    Context context;
    List<Flight> flights;
    private static final String TAG = "FlightAdapter";
    private static LayoutInflater inflater = null;

    public FlightAdapter(Context context, ArrayList<Flight> flights){
        this.flights = flights;
        this.context = context;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return flights.size();
    }

    @Override
    public Object getItem(int position) {
        return flights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView");
        View vi = convertView;
        if (vi == null){
            vi = inflater.inflate(R.layout.flight_row_view, null);
        }

        TextView flight_date = (TextView) vi.findViewById(R.id.flight_date);
        TextView start_time   = (TextView) vi.findViewById(R.id.start_time);
        TextView end_time   = (TextView) vi.findViewById(R.id.end_time);
        TextView flight_duration   = (TextView) vi.findViewById(R.id.flight_duration);
        TextView flight_price = (TextView) vi.findViewById(R.id.flight_price);

        Flight current_flight = flights.get(position);
        flight_date.setText(current_flight.date);
        start_time.setText(current_flight.start_time);
        end_time.setText(current_flight.end_time);
        flight_duration.setText(String.valueOf(current_flight.duration));
        flight_price.setText(String.valueOf(current_flight.price));

        return vi;
    }
}