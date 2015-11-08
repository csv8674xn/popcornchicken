package popcornchicken.myapplication.Utility;

import android.location.Location;

/**
 * Created by ianwind2 on 15/11/7.
 */
public class Event {
    public Location location = new Location("");
    public String cityName;
    public String url;
    public String title;
    public String description;
    public String start_time;
    public String end_time;
    public String image_url;

    public Event(String latitude, String longitude, String cityName, String url, String title,
                 String description, String start_time, String end_time, String image_url){

        location.setLatitude(Double.valueOf(latitude));
        location.setLongitude(Double.valueOf(longitude));
        this.cityName = cityName;
        this.url = url;
        this.title = title;
        this.description = description;
        this.start_time = formatTime(start_time);
        this.end_time = formatTime(end_time);
        this.image_url = image_url;
    }

    private String formatTime(String time){
        String[] ary = time.split(" ");

        return ary[0];
    }
}
