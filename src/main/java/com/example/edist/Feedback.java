package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.edist.R.id.feedbacksnd;

public class Feedback extends AppCompatActivity {
    EditText e1;
    Button b;
    String ipad="",text,uid;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        b=(Button)findViewById(R.id.bfeedback);
        e1=(EditText)findViewById(R.id.feedbacksnd);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ipad=sp.getString("ipadd","");
        uid=sp.getString("uid","");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                text = e1.getText().toString();

                if (text.equals("")) {
                    e1.setError("enter a feedback");
                } else {

                    RequestQueue queue = Volley.newRequestQueue(Feedback.this);
                    String url = "http://" + ipad + ":5000/add_feedback";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("+++++++++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");
                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), Userhome.class));
                                } else {
                                    startActivity(new Intent(getApplicationContext(), Feedback.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("feedback", text);
                            params.put("uid", sp.getString("uid", ""));

                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });
    }
}