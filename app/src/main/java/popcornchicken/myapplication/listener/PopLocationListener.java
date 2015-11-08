package popcornchicken.myapplication.listener;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yao-Jung on 2015/11/7.
 */
public class PopLocationListener implements android.location.LocationListener {
    private Context mContext;
    private String longitude;
    private String latitude;
    private String cityName;
    public PopLocationListener(Context context){
        this.mContext = context;
    }
    @Override
    public void onLocationChanged(Location loc) {
        this.longitude = "" + loc.getLongitude();
        this.latitude = "" + loc.getLatitude();

        String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            cityName = addresses.get(0).getLocality();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getLongitude(){
        return this.longitude;
    }

    public String getLatitude(){
        return  this.latitude;
    }

    public String getCityName(){
        return cityName;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
