package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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

public class FeedbackMain extends AppCompatActivity {
    ListView lv;
    SharedPreferences sp;
    String url="",ip="";
    Button b1;

    ArrayList<String> date;
    ArrayList<String> feedback;
    ArrayList<String> rply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_main);

        lv=(ListView)findViewById(R.id.fdlv);
        b1=(Button)findViewById(R.id.bfd);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipadd","");

        url ="http://"+ip+":5000/viewfeedback";


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(FeedbackMain.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    date=new ArrayList<>();
                    feedback=new ArrayList<>();
                    rply=new ArrayList<>();
                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        date.add(jo.getString("date"));
                        feedback.add(jo.getString("feedback"));
                        rply.add(jo.getString("status"));
                    }
                    //ArrayAdapter<String> ad=new ArrayAdapter<String>(News.this,android.R.layout.simple_list_item_1,news);
                    //lv.setAdapter(ad);

                    lv.setAdapter(new Custom3txt(FeedbackMain.this,date,feedback,rply));

                } catch (JSONException e) {
                    e.printStackTrace();
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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Feedback.class);
                startActivity(i);
            }
        });



    }
}
