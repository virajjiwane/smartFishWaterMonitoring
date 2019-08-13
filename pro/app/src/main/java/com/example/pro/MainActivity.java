package com.example.pro;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference reff;


    TextView tempv, dov, phv, alert;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        tempv = (TextView) findViewById(R.id.tempv);
        dov = (TextView) findViewById(R.id.dov);
        phv = (TextView) findViewById(R.id.phv);
        alert = (TextView) findViewById(R.id.alert);
        //reff = FirebaseDatabase.getInstance().getReference();
        try {
            dov.setText("Fetching data from server. Please wait...");
            new FetchThingspeakTask().execute();

        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }

       /* reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               *//* for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if (dataSnapshot.hasChild("/Device1/Status")) {
                        String d1 = dataSnapshot.getValue(String.class);
                        Toast.makeText(device.this, d1, Toast.LENGTH_SHORT).show();

                    }*//*
                Long v1 = dataSnapshot.child("Sensor").child("temp").getValue(Long.class);
                Long v2 = dataSnapshot.child("Sensor").child("DO").getValue(Long.class);

                Long v3 = dataSnapshot.child("Sensor").child("Ph").getValue(Long.class);



                String str = String.valueOf(v1);
                tempv.setText(str + " °C");
                dov.setText(String.valueOf(v2 + " ppm"));
                phv.setText(String.valueOf(v3));
                if (v3 <= 7 || v3 >= 9.5) {
                    alert.setText("ph is at critical");
                    phv.setTextColor(Color.parseColor("#FF0000"));
                    tempv.setTextColor(Color.parseColor("#000000"));
                    dov.setTextColor(Color.parseColor("#000000"));
                }
                if (v2 <= 5 || v2 >= 9) {
                    alert.setText("DO is at critical");
                    dov.setTextColor(Color.parseColor("#FF0000"));
                    tempv.setTextColor(Color.parseColor("#000000"));
                    phv.setTextColor(Color.parseColor("#000000"));

                }
                if (v1 <= 15 || v1 >= 35) {
                    alert.setText("Temperature is at critical");
                    tempv.setTextColor(Color.parseColor("#FF0000"));
                    dov.setTextColor(Color.parseColor("#000000"));
                    phv.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/

    }
    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            alert.setText("");
        }
        protected String doInBackground(Void... urls) {
            try {
                /*URL url = new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "");
                */
                URL url = new URL("https://api.thingspeak.com/channels/802670/feeds.json?api_key=PO00EF8ZJB83LQNJ&results=2");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        protected void onPostExecute(String response) {
            if(response == null) {
                Toast.makeText(MainActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
               /* JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                double v1 = channel.getDouble(THINGSPEAK_FIELD1);
                if(v1>=90)
                    t1.setText("HI ALL  ");
                else
                    t1.setText("NO VALUES");*/
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
                Log.e("response",channel.toString());
                Log.e("channel",channel.getJSONObject("channel").toString());
                Log.e("feed",channel.getJSONArray("feeds").toString());
                Log.e("last",channel.getJSONArray("feeds").get(channel.getJSONArray("feeds").length()-1).toString());
                Log.e("ph",channel.getJSONArray("feeds").getJSONObject(channel.getJSONArray("feeds").length()-1).getString("field2").toString());

                double temp = channel.getJSONArray("feeds").getJSONObject(channel.getJSONArray("feeds").length()-1).getDouble("field1");
                double ph = channel.getJSONArray("feeds").getJSONObject(channel.getJSONArray("feeds").length()-1).getDouble("field2");
                double dissolvedOxygen = channel.getJSONArray("feeds").getJSONObject(channel.getJSONArray("feeds").length()-1).getDouble("field3");
                alert.setText("");
                String str = String.valueOf(temp);
                tempv.setText(temp + " °C");
                dov.setText(dissolvedOxygen+" ppm");
                phv.setText(ph+"");
                if (ph < 4 || ph > 10) {
                    alert.setText("ph is at critical");
                    phv.setTextColor(Color.parseColor("#FF0000"));
                    tempv.setTextColor(Color.parseColor("#000000"));
                    dov.setTextColor(Color.parseColor("#000000"));
                }
                if (dissolvedOxygen < 4) {
                    alert.setText("DO is at critical");
                    dov.setTextColor(Color.parseColor("#FF0000"));
                    tempv.setTextColor(Color.parseColor("#000000"));
                    phv.setTextColor(Color.parseColor("#000000"));

                }
                if (temp < 15 || temp > 35) {
                    alert.setText("Temperature is at critical");
                    tempv.setTextColor(Color.parseColor("#FF0000"));
                    dov.setTextColor(Color.parseColor("#000000"));
                    phv.setTextColor(Color.parseColor("#000000"));
                }
                else{
                    alert.setText("");
                    tempv.setTextColor(Color.parseColor("#000000"));
                    dov.setTextColor(Color.parseColor("#000000"));
                    phv.setTextColor(Color.parseColor("#000000"));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                new FetchThingspeakTask().execute();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.device) {
            Intent i = new Intent(this, device.class);
            startActivity(i);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

