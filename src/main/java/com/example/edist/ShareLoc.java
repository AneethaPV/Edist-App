package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class ShareLoc extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lv;
    SharedPreferences sp;
    String url="",url2,ip="";
    ArrayList<String> name,idd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_loc);

        lv=(ListView)findViewById(R.id.lvs);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipadd","");

        url ="http://"+ip+":5000/shareloc";

        RequestQueue queue = Volley.newRequestQueue(ShareLoc.this);

        StringRequest string = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String respo) {

                        try {
                            JSONArray arr = new JSONArray(respo);
                            if (respo.length() > 0) {

                                name = new ArrayList<String>();
                                idd = new ArrayList<String>();


                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject ob = arr.getJSONObject(i);
                                    name.add(ob.getString("first_name")+" "+ob.getString("middle_name")+" "+ob.getString("last_name"));
                                    idd.add(ob.getString("user_id"));

                                }
                                ArrayAdapter ad =new ArrayAdapter(ShareLoc.this, android.R.layout.simple_list_item_1,name);
                                lv.setAdapter(ad);

                                //lv.setAdapter(new Custom3(chattlist.this,img,name));
                               lv.setOnItemClickListener(ShareLoc.this);

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "error"+e, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                }

                , new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("type", type);
                params.put("date", getIntent().getStringExtra("date"));

                params.put("uid", sp.getString("uid",""));
                return params;
            }

        };
        queue.add(string);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        url2 = "http://"+ip+":5000/shareloc1";

    final String sid=idd.get(position);


        RequestQueue queue = Volley.newRequestQueue(ShareLoc.this);
        String url = url2;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("+++++++++++++++++++++++", response);
                try {
                    JSONObject json = new JSONObject(response);
                    String res = json.getString("task");

                    if (res.equalsIgnoreCase("fail")) {
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                    } else {


                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),ShareLoc.class);
                        i.putExtra("date",getIntent().getStringExtra("date"));
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                                    Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fid", sp.getString("uid","0"));
                params.put("tid", idd.get(position));
                params.put("date", getIntent().getStringExtra("date"));

                return params;
            }
        };
        queue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),Userhome.class);
        startActivity(i);
    }
}






