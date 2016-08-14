package com.example.bhall.pugetsoundwind;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ObsStations extends AppCompatActivity {

    public class BobJSONObject extends JSONObject {

        public String getStringWithDefault(String key, String default_result) {
            String result;
            try {
                result = super.getString(key);
            } catch (JSONException e) {
                result = default_result;
            }
            return result;
        }
    }

    public class BackgroundAsyncTask extends AsyncTask<Void, Void, String>{
        Context context;

        private static final String TAG = "BackgroundAsyncTask";

        private static final String WIND_DIRECTION = "wind_direction";
        private static final String DATE_TIME = "time";
        private static final String WIND_SPEED = "wind_speed";
        private static final String STATION_NAME = "station_name";

        protected String getStringFromJSONObject(JSONObject row, String key, String default_value) {
            String result;
            try {
                result = row.getString(key);
            } catch (JSONException e){
                result = default_value;
            }
            return result;
        }

        @Override
        protected void onPreExecute(){
            return;
        }
        @Override
        protected String doInBackground(Void... params){
            StringBuilder builder = new StringBuilder();
            try {
                URL url = new URL("http://b.obhall.com/obs");
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String result){
            Toast.makeText(getApplicationContext(),"Got it!",Toast.LENGTH_SHORT).show();

            JSONArray jsonArray = new JSONArray();
            ArrayList<StationItem> stationItems = new ArrayList<StationItem>();

            try {
                jsonArray = new JSONArray(result);
                for (int ix=0; ix<jsonArray.length(); ix++){

                    JSONObject row = jsonArray.getJSONObject(ix);

                    String weather = getStringFromJSONObject(row, WIND_SPEED, "null") + "kts " +
                         getStringFromJSONObject(row, WIND_DIRECTION, "(no dir)");

                    stationItems.add(new StationItem(getStringFromJSONObject(row, STATION_NAME, "unknown station"),
                                                     weather,
                                                     getStringFromJSONObject(row, DATE_TIME, "time unknown")));
                }
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
                Toast.makeText(getApplicationContext(),"Could not parse JSON",Toast.LENGTH_LONG).show();
            }
            ListView listView = (ListView)findViewById(R.id.listView);
            Log.i(TAG, "station items length: " + stationItems.toArray().length);
            listView.setAdapter(new StationAdapter(getApplicationContext(),stationItems));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_stations);

        Button button = (Button)findViewById(R.id.button);
        final Context mContext = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundAsyncTask bat = new BackgroundAsyncTask();
                bat.execute();
            }
        });
    }
}
