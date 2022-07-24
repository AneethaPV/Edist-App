package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DateMap extends AppCompatActivity{
    EditText e1;
    Button b;
    String ipad="",date,uid;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_map);

        b=(Button)findViewById(R.id.bd);
        e1=(EditText)findViewById(R.id.editTextDate);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ipad=sp.getString("ipadd","");
        uid=sp.getString("uid","");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                date = e1.getText().toString();

                if (date.equals(""))
                {
                    e1.setError("Provide date");
                }
                else
                    {
                    Intent i=new Intent(getApplicationContext(),ViewMap.class);
                    i.putExtra("date",date);
                    startActivity(i);


                }
            }
        });
    }
}