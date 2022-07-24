package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

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

public class Registration extends AppCompatActivity {

    EditText fn,mn,ln,ph,mid,adr;
    Button reg;
    String fname,mname,lname,phn,mail,addr,ipad;
    SharedPreferences ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fn=(EditText)findViewById(R.id.firstname);
        mn=(EditText)findViewById(R.id.middlename);
        ln=(EditText)findViewById(R.id.lastname);
        ph=(EditText)findViewById(R.id.phoneno);
        mid=(EditText)findViewById(R.id.mailid);
        adr=(EditText)findViewById(R.id.address);
        reg=(Button)findViewById(R.id.registration);
        ip= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ipad=ip.getString("ipadd","");

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                fname = fn.getText().toString();
                mname = mn.getText().toString();
                lname = ln.getText().toString();
                phn = ph.getText().toString();
                mail = mid.getText().toString();
                addr = adr.getText().toString();

                if (fname.equals("")) {
                    fn.setError("enter first name");
                } else if(!fname.matches("^[a-zA-Z]*$"))
                {
                    fn.setError("characters allowed");
                } else if(!mname.matches("^[a-zA-Z]*$"))
                {
                    mn.setError("characters allowed");
                }else if (lname.equals("")) {
                    ln.setError("enter last name");
                } else if(!lname.matches("^[a-zA-Z]*$"))
                {
                    ln.setError("characters allowed");
                } else if (phn.equals("")) {
                    ph.setError("enter phone number");
                } else if(phn.length()!=10)
                {
                    ph.setError("Invalid phoneno");
                }else if (mail.equals("")) {
                    mid.setError("enter email");
                } else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    mid.setError("Enter Valid Email");
                    mid.requestFocus();
                }else if (addr.equals("")) {
                    adr.setError("enter address");
                } else {

                    RequestQueue queue = Volley.newRequestQueue(Registration.this);
                    String url = "http://" + ipad + ":5000/signup";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("+++++++++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("success")) {
                                    Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                } else {
                                    Toast.makeText(getApplicationContext(), "The phone number or email provided is already registered ", Toast.LENGTH_LONG).show();

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
                            params.put("fname", fname);
                            params.put("mname", mname);
                            params.put("lname", lname);
                            params.put("phone", phn);
                            params.put("email", mail);
                            params.put("addr", addr);
                            params.put("un", mail);
                            params.put("pw", phn);

                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            }
        });

    }
}