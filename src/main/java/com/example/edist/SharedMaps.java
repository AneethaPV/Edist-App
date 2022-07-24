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
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

public class SharedMaps extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv;
    SharedPreferences sp;
    String url="",ip="";

    ArrayList<String> name;
    ArrayList<String> date;
    ArrayList<String> sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_maps);

        lv=(ListView)findViewById(R.id.smlv);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipadd","");

        url ="http://"+ip+":5000/sharedmaps";


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(SharedMaps.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    date=new ArrayList<>();
                    name=new ArrayList<>();
                    sid=new ArrayList<>();
                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        date.add(jo.getString("date"));
                        sid.add(jo.getString("user_id"));
                        name.add(jo.getString("first_name")+" "+jo.getString("middle_name")+" "+jo.getString("last_name"));
                    }
                    //ArrayAdapter<String> ad=new ArrayAdapter<String>(News.this,android.R.layout.simple_list_item_1,news);
                    //lv.setAdapter(ad);

                    lv.setAdapter(new Custom2txt(SharedMaps.this,name,date));
                    lv.setOnItemClickListener((AdapterView.OnItemClickListener) SharedMaps.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error"+e, Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", sp.getString("uid", ""));

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i=new Intent(getApplicationContext(),ViewMap.class);

        SharedPreferences.Editor ed=sp.edit();
        ed.putString("share","share");
        ed.putString("sid",sid.get(position));

        ed.commit();
        i.putExtra("date",date.get(position));
       // i.putExtra("sid",sid.get(position));

        startActivity(i);


    }
}
