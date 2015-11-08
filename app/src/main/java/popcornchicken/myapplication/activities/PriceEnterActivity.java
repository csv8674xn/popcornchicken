package popcornchicken.myapplication.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import popcornchicken.myapplication.R;

public class PriceEnterActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private TextView totalPrice;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.MyNoActionBarShadowTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_enter_actitivty);
        setupActionBar();
        this.totalPrice = (TextView) findViewById(R.id.tv_price);
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API)
                .build();
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

    public void pushGo(View v){
        if(!displayGpsStatus()){
            Toast.makeText(getBaseContext(), "GPS off", Toast.LENGTH_SHORT).show();
            return ;
        }
        this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Intent intent = new Intent(PriceEnterActivity.this, MapActivity.class);
        intent.putExtra("latitude", mLastLocation.getLatitude());
        intent.putExtra("longitude", mLastLocation.getLongitude());
        if(totalPrice.getText().equals("$")){
            intent.putExtra("price", totalPrice.getText() + "0");
        } else {
            intent.putExtra("price", totalPrice.getText());
        }

        startActivity (intent);
    }
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }
    public void pushButton(View v){
        TextView currPush = (TextView)v;
        String priceString = this.totalPrice.getText().toString();

        if(priceString.length() >= 7){
            return;
        }
        if(priceString.equals("$") && currPush.getText().toString().equals("0")){
                return;
        }
        totalPrice.setText(priceString + ((TextView) v).getText());
    }

    public void pushBack(View v){
        if(totalPrice.getText().toString().equals("$")){
            return;
        }
        String priceString = this.totalPrice.getText().toString();
        totalPrice.setText(priceString.substring(0, priceString.length() - 1));
    }

    public void pushCE(View v){
        this.totalPrice.setText("$");
    }

    @Override
    public void onConnected(Bundle bundle) {
        this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
