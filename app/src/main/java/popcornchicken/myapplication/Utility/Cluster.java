package popcornchicken.myapplication.Utility;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by ianwind2 on 15/11/7.
 */
public class Cluster {
    public Location location = new Location("");
    public ArrayList<Event> events;
    public String cityName;
    public boolean isValid = true;
    public Cluster(){
        events = new ArrayList<>();
    }

    public int length(){
        return events.size();
    }

    public void setLocation(String latitude, String longitude, String city){
        try{
            location.setLatitude(Double.valueOf(latitude));
            location.setLongitude(Double.valueOf(longitude));
        } catch(Exception e){
            this.isValid = false;
            return;
        }

        this.cityName = city;
    }

    public void AddEvent(Event event){
        events.add(event);
    }
}
