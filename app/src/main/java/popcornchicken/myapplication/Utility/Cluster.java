package popcornchicken.myapplication.Utility;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by ianwind2 on 15/11/7.
 */
public class Cluster {
    public Location location = new Location("");
    public ArrayList<Event> events;

    public Cluster(){
        events = new ArrayList<>();
    }

    public int length(){
        return events.size();
    }

    public void setLocation(String latitude, String longitude){
        location.setLatitude(Double.valueOf(latitude));
        location.setLongitude(Double.valueOf(longitude));
    }

    public void AddEvent(Event event){
        events.add(event);
    }
}
