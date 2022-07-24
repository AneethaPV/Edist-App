package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class Containment extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv;
    SharedPreferences sp;
    String url="",ip="";

    ArrayList<String> place;
    ArrayList<String> corporation;
    ArrayList<String> date;
    ArrayList<String> lat ;
    ArrayList<String> lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_containment);

        lv=(ListView)findViewById(R.id.conlist);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipadd","");

        url ="http://"+ip+":5000/viewcontainment_zone";


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(Containment.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    place=new ArrayList<>();
                    corporation=new ArrayList<>();
                    date=new ArrayList<>();
                    lat=new ArrayList<>();
                    lon=new ArrayList<>();

                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        place.add(jo.getString("place_name"));
                        corporation.add(jo.getString("panchayath"));
                        date.add(jo.getString("date"));
                        lat.add(jo.getString("latitude"));
                        lon.add(jo.getString("longitude"));
                    }
                    //ArrayAdapter<String> ad=new ArrayAdapter<String>(News.this,android.R.layout.simple_list_item_1,news);
                    //lv.setAdapter(ad);

                    lv.setAdapter(new Custom3txt(Containment.this,place,corporation,date));
                lv.setOnItemClickListener((AdapterView.OnItemClickListener) Containment.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q="+lat.get(position)+","+lon.get(position)));
        startActivity(intent);


    }
}
