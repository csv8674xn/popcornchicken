package popcornchicken.myapplication.Utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import popcornchicken.myapplication.activities.MapActivity;

/**
 * Created by ianwind2 on 15/11/7.
 */
public class DownloadCitiesTask extends AsyncTask<String, Integer, String> {
    private MapActivity myActivity;
    private static final String TAG = "DownloadCitiesTask";

    public DownloadCitiesTask(MapActivity activity){
        this.myActivity = activity;
    }

    protected String doInBackground(String... urls) {
        //... downloading
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urls[0]);
            urlConnection =
                    (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertStreamToString(in);

            return response;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return "";
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    protected void onPostExecute(String result) {
        Log.d(TAG, result);

        try {
            myActivity.ProcessCities(new JSONArray(result));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}