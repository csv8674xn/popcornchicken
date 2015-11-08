package popcornchicken.myapplication.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import popcornchicken.myapplication.R;

/**
 * Created by Yao-Jung on 2015/11/8.
 */
public class FlightSearchActivity extends ActionBarActivity {

    private static final float BLUR_RADIUS = 25f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.MyNoActionBarShadowTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_search_activity);
        ImageView imageView = (ImageView) findViewById(R.id.iv_cover);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.price_enter_background);
//        Bitmap blurredBitmap = blur(bitmap);
//        imageView.setImageBitmap(blurredBitmap);
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

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final android.renderscript.RenderScript renderScript = android.renderscript.RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}
