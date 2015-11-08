package popcornchicken.myapplication.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



import popcornchicken.myapplication.R;
import popcornchicken.myapplication.Utility.Cluster;
import popcornchicken.myapplication.Utility.DownloadCitiesTask;
import popcornchicken.myapplication.Utility.DownloadEventsTask;
import popcornchicken.myapplication.Utility.Event;
import popcornchicken.myapplication.adapters.EventAdapter;
//import popcornchicken.myapplication.adapters.EventAdapter;

/**
 * Created by Yao-Jung on 2015/11/7.
 */
public class MapActivity extends ActionBarActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private LatLng userLatLng;
    private View hiddenPanel;
    private ListView listView;

    private ArrayList<Cluster> clusters = new ArrayList<>();

    private final static String TAG = "MapActivity";
    @Override  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.userLatLng = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
        setContentView(R.layout.map_activity);
        setupActionBar();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listView = (ListView) findViewById(R.id.lv_eventlist);
        new DownloadCitiesTask(this).execute("http://ec2-52-88-224-149.us-west-2.compute.amazonaws.com:3000/validCities/300");

    }

    public void ProcessCities(JSONArray json_ary){
        Set<String> city_ary = new HashSet<String>();

        for (int i=0; i<json_ary.length(); i++){
            try {
                JSONObject obj = json_ary.getJSONObject(i);
                city_ary.add(obj.getString("CityName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "City_ary size = " + String.valueOf(city_ary.size()));
        String baseUrl = "http://ec2-52-88-224-149.us-west-2.compute.amazonaws.com:3000/findEvents/";

        for(String city : city_ary){
            // Debug.
            Log.d(TAG, city);

            String eventUrl = baseUrl + city;
            Log.d(TAG, "eventUrl = " + eventUrl);

            // Request Events by city.
            new DownloadEventsTask(this).execute(eventUrl);
        }
    }

    public void ProcessEvents(JSONObject json_obj){
        Cluster cluster = new Cluster();

        try {
            // No events.
            String page_size = json_obj.getString("page_size");
            Log.d(TAG, "page_size = " + page_size);
            if (Integer.valueOf(page_size) == 0){
                return;
            }

            JSONArray json_ary = json_obj.getJSONObject("events").getJSONArray("event");
            Log.d(TAG, "json_ary size = " + json_ary.length());
            for (int i=0; i<json_ary.length(); i++){
                JSONObject obj = json_ary.getJSONObject(i);
                JSONObject image_obj = null;
                String image_url = "";

                image_obj = obj.optJSONObject("image");
                if (image_obj != null){
                    image_url = image_obj.getJSONObject("medium").getString("url");
                }

                Log.d(TAG, obj.getString("latitude"));
                Log.d(TAG, obj.getString("longitude"));
                Log.d(TAG, obj.getString("city_name"));

                // Set Cluster's location on first event's location.
                if (i == 0){
                    cluster.setLocation(obj.getString("latitude"), obj.getString("longitude"));
                }

                Log.d(TAG, "After if");

                cluster.AddEvent(
                        new Event(
                                obj.getString("latitude"),
                                obj.getString("longitude"),
                                obj.getString("city_name"),
                                obj.getString("url"),
                                obj.getString("title"),
                                obj.getString("description"),
                                obj.getString("start_time"),
                                obj.getString("stop_time"),
                                image_url
                        )
                );

                Log.d(TAG, "After AddEvent");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            Log.d(TAG, "NullPointerException");
        }


        if (cluster.length() != 0){
            clusters.add(cluster);
        }
        Log.d(TAG, "Clusters' Size = " + Integer.valueOf(clusters.size()));
        listView.setAdapter(new EventAdapter(MapActivity.this, cluster.events));
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
        hiddenPanel = findViewById(R.id.hidden_panel);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Animation bottomDown = AnimationUtils.loadAnimation(MapActivity.this,
                        R.anim.bottom_down);

                hiddenPanel.startAnimation(bottomDown);
                hiddenPanel.setVisibility(View.GONE);
            }
        });
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions().position(userLatLng).title("Marker"));
        map.setMyLocationEnabled(true);
        LatLng currLocation = new LatLng(userLatLng.latitude, userLatLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));
        mMap.addMarker(new MarkerOptions().position(currLocation));
    }

    public void slideUpDown(final View view) {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);

        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }
}
