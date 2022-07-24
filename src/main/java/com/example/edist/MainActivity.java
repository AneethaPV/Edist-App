package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
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

public class MainActivity extends AppCompatActivity {

    EditText e1,e2;
    Button b1,b2;
    String user,pwd,ipad;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1=(EditText)findViewById(R.id.user);
        e2=(EditText)findViewById(R.id.pwd);
        b1=(Button)findViewById(R.id.log);
        b2=(Button)findViewById(R.id.reg);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ipad=sp.getString("ipadd","");


//        ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                1);
//        ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                1);


        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user = e1.getText().toString();
                pwd = e2.getText().toString();

                if (user.equals("")) {
                    e1.setError("username can't be empty");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(user).matches())
                {
                    e1.setError("Enter Valid Email");
                    e1.requestFocus();
                }
                else if (pwd.equals("")) {
                    e2.setError("password can't be empty");
                }
                else if(pwd.length()!=10) {
                    e2.setError("Invalid phoneno");
                }
                else {

                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String url = "http://" + ipad + ":5000/login";                    //call webservice function login

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("+++++++++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("fail")) {
                                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
                                } else {
                                    SharedPreferences.Editor ed = sp.edit();
                                    ed.putString("uid", res);
                                    ed.commit();
                                    startActivity(new Intent(getApplicationContext(), Userhome.class));
                                    startService(new Intent(getApplicationContext(), LocationService.class));
                                    startService(new Intent(getApplicationContext(), SocialDistanceAlert.class));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //  Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("un", user);
                            params.put("pw", pwd);
                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}