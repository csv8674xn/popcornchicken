package popcornchicken.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import popcornchicken.myapplication.R;
import popcornchicken.myapplication.Utility.Cluster;
import popcornchicken.myapplication.Utility.DownloadFlightsTask;
import popcornchicken.myapplication.Utility.Flight;

public class GetData extends ActionBarActivity {
    private static final String TAG = "GetData";
    private ArrayList<Cluster> clusters = new ArrayList<>();
    private ListView listview;
    private String cityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        listview = (ListView)findViewById(R.id.lv_flight_list);
        Intent intent = getIntent();
        String dest = intent.getExtras().getString("city_id");
        this.cityName = intent.getExtras().getString("city_name");
//        new DownloadCitiesTask(this).execute("http://ec2-52-88-224-149.us-west-2.compute.amazonaws.com:3000/validCities/600");
        new DownloadFlightsTask(this).execute("http://ec2-52-88-224-149.us-west-2.compute.amazonaws.com:3000/search?origin=SFOA&destination="+dest+"&departDate=2015-11-21&arriveDate=2015-11-30");
        TextView tv_dest = (TextView) findViewById(R.id.DestinationAirport);
        tv_dest.setText(this.cityName);
        setupActionBar();

    }

    private void setupActionBar() {
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.actionbar,
                null);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setElevation(0);
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CB0000")));
        android.support.v7.app.ActionBar.LayoutParams lp1 = new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT);
        mActionBar.setCustomView(actionBarLayout, lp1);
    }

    public void ProcessFlights(JSONObject json_obj){
        Hashtable<Integer, String> place_map = new Hashtable<>();
        Hashtable<Integer, String> carrier_map = new Hashtable<>();
        ArrayList<Flight> flights = new ArrayList<>();

        try {
            JSONObject place_obj = json_obj.getJSONObject("places");
            Iterator<String> iter = place_obj.keys();
            while(iter.hasNext()){
                Integer id = Integer.valueOf(iter.next());
                place_map.put(id, place_obj.optString("IataCode"));
            }

            JSONObject carrier_obj = json_obj.getJSONObject("carriers");
            iter = carrier_obj.keys();
            while(iter.hasNext()){
                Integer id = Integer.valueOf(iter.next());
                carrier_map.put(id, carrier_obj.optString("carriers"));
            }

            JSONArray depart_ary = json_obj.getJSONArray("depart");
            for (int i=0; i<depart_ary.length(); i++){
                JSONObject obj = depart_ary.getJSONObject(i);
                JSONObject OutboundLeg = obj.getJSONObject("OutboundLeg");

                flights.add(new Flight(
                        place_map.get(OutboundLeg.getInt("OriginId")),
                        place_map.get(OutboundLeg.getInt("DestinationId")),
                        OutboundLeg.getString("DepartureDate"),
                        obj.getString("QuoteDateTime"),
                        obj.getInt("MinPrice"),
                        obj.getBoolean("Direct")
                ));

                // Debug.
                Flight temp = new Flight(
                        place_map.get(OutboundLeg.getInt("OriginId")),
                        place_map.get(OutboundLeg.getInt("DestinationId")),
                        OutboundLeg.getString("DepartureDate"),
                        obj.getString("QuoteDateTime"),
                        obj.getInt("MinPrice"),
                        obj.getBoolean("Direct")
                );
                Log.d(TAG, temp.start_time + ", " + temp.end_time + ", " + temp.duration + ", " + temp.price + ", ");

            }

            JSONArray arrive_ary = json_obj.getJSONArray("arrive");
            for (int i=0; i<arrive_ary.length(); i++){
                JSONObject obj = depart_ary.getJSONObject(i);
                JSONObject OutboundLeg = obj.getJSONObject("OutboundLeg");

                flights.add(new Flight(
                        place_map.get(OutboundLeg.getInt("OriginId")),
                        place_map.get(OutboundLeg.getInt("DestinationId")),
                        OutboundLeg.getString("DepartureDate"),
                        obj.getString("QuoteDateTime"),
                        obj.getInt("MinPrice"),
                        obj.getBoolean("Direct")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listview.setAdapter(new popcornchicken.myapplication.adapters.FlightAdapter(this, flights));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
