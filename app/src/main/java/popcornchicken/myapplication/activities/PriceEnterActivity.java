package popcornchicken.myapplication.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import popcornchicken.myapplication.R;

public class PriceEnterActivity extends ActionBarActivity {
    private TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.MyNoActionBarShadowTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_enter_actitivty);
        setupActionBar();
        setupLocation();
        this.totalPrice = (TextView) findViewById(R.id.tv_price);
    }
    private void setupActionBar(){
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

    private void setupLocation(){
    }

    public void pushGo(View v){

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
}
