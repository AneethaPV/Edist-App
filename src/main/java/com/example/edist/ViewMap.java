package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import static com.example.edist.LocationService.latitude;
import static com.example.edist.LocationService.longitude;

public class ViewMap extends AppCompatActivity implements OnMapReadyCallback {

    SharedPreferences sp;
    String url="",ip="";

    ArrayList<String> lat;
    ArrayList<String> lon,datetime;
    public static String latitude,longitude;
    private GoogleMap Mmap;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    String date="",share="",uid;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        b=(Button)findViewById(R.id.button);


        share=sp.getString("share","");
      //  Toast.makeText(getApplicationContext(),share,Toast.LENGTH_LONG).show();
        date=getIntent().getStringExtra("date");

        ip=sp.getString("ipadd","");
        if(share.equalsIgnoreCase("share") )
        {
            uid=sp.getString("sid","");
            b.setVisibility(View.INVISIBLE);
        }

        else  if(share.equalsIgnoreCase("all"))
        {
            uid=sp.getString("uid","");
            b.setVisibility(View.INVISIBLE);

        }
        else
        {
            uid=sp.getString("uid","");
            b.setVisibility(View.VISIBLE);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ShareLoc.class);
                i.putExtra("date",date);
                startActivity(i);
            }
        });

        url ="http://"+ip+":5000/view_rootmap1";

        // Create a Uri from an intent string. Use the result to create an Intent.
//        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=11.2571,75.7849");
//
//// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//        mapIntent.setPackage("com.google.android.apps.maps");
//
//// Attempt to start an activity that can handle the Intent
//        startActivity(mapIntent);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ViewMap.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    lat=new ArrayList<>();

                    lon=new ArrayList<>();
                    datetime=new ArrayList<>();
                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        lat.add(jo.getString("latitude"));
                        lon.add(jo.getString("longitude"));
                        datetime.add(jo.getString("datetime"));
                        LatLng loc=new LatLng( Double.parseDouble(jo.getString("latitude")),Double.parseDouble(jo.getString("longitude")));
                        arrayList.add(loc);
                    }

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync((OnMapReadyCallback) ViewMap.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("date", date);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // [START_EXCLUDE silent]
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        // [END_EXCLUDE]
        //LatLng sydney = new LatLng(-33.852, 151.211);

        Mmap = googleMap;

        for (int i = 0; i < arrayList.size(); i++) {
            Mmap.addMarker(new MarkerOptions()
                    .position(arrayList.get(i)).title(datetime.get(i)));
            // [START_EXCLUDE silent]
            Mmap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
            // [END_EXCLUDE]
        }
        // [END maps_marker_on_map_ready_add_marker]
    }
}
