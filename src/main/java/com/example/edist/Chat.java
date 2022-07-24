 package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class Chat extends AppCompatActivity {

    LinearLayout lt;
    EditText ed;
    Button b1;
    TextView t1;

    String url="";
    String fid="";
    String id="";
    String ip="";
    Handler hd;
    static String prv="";

    String lastid;

    public static String ur="",url2;
    TelephonyManager tm;
    SharedPreferences sp;

    public static ArrayList<String> From_id,Toid,Message,Date ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ip=sp.getString("ipadd","");

        url ="http://"+ip+":5000/viewuser";

//        ed=(EditText)findViewById(R.id.);
//        lt=(LinearLayout)findViewById(R.id.);
//        b1=(Button)findViewById(R.id.);
//        t1=(TextView)findViewById(R.id.);


        lastid="0";
        t1.setText(getIntent().getStringExtra("name"));

        fid=getIntent().getStringExtra("fid");


        id=sp.getString("uid", "");

        hd=new Handler();
        hd.post(r);


        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final String message=ed.getText().toString();
                ed.setText("");
                if(message.equals(""))
                {
                    ed.setError("Enter message");
                    ed.requestFocus();
                }
                else {
                    ur = "http://"+ip+":5000/chatuser";

                    final RequestQueue res = Volley.newRequestQueue(Chat.this);
                    StringRequest string = new StringRequest(Request.Method.POST, ur,
                            new Response.Listener<String>() {
                                public void onResponse(String respo) {

                                    if (respo.equals("success")) {
                                        ed.setText("");

                                        res.stop();


                                    } else {

//                                    Toast.makeText(getApplicationContext(), "invalid",Toast.LENGTH_LONG ).show();

                                    }
                                }
                            }

                            , new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "error detected"+error, Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    })

                    {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("From_id", id);
                            params.put("To_id", fid);
                            params.put("Message", message);


                            return params;
                        }
                    };
                    res.add(string);
                }

            }
        });


    }


    public final Runnable r = new Runnable() {

        @Override
        public void run() {

            url2 = "http://" + ip + ":5000/viewchat";
            final RequestQueue mqueu = Volley.newRequestQueue(Chat.this);

            StringRequest string = new StringRequest(Request.Method.POST, url2,
                    new Response.Listener<String>() {
                        public void onResponse(String respo) {

                            try {
                                JSONArray arr = new JSONArray(respo);
                                if (respo.length() > 0) {


                                    From_id = new ArrayList<String>();
                                    Toid = new ArrayList<String>();
                                    Message = new ArrayList<String>();
                                    Date = new ArrayList<String>();
                                    lt.removeAllViews();
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject c = arr.getJSONObject(i);

                                        From_id.add(c.getString("Fromid"));
                                        Toid.add(c.getString("Toid"));
                                        Message.add(c.getString("Message"));
                                        Date.add(c.getString("Date"));

                                        TextView tv = new TextView(getApplicationContext());
                                        TextView tv1 = new TextView(getApplicationContext());
                                        if (!c.getString("Date").equals(prv)) {
                                            //Toast.makeText(getApplicationContext(), "result is"+prv, Toast.LENGTH_LONG).show();
                                            tv1.setText(c.getString("Date"));
                                            tv1.setGravity(Gravity.CENTER);
                                            prv = c.getString("Date");
                                        }

                                        if (From_id.get(i).equalsIgnoreCase(id)) {
                                            tv.setTextColor(Color.BLACK);
                                            tv.setText(Message.get(i));
                                            tv.setTextSize((float) 15.0);

                                            tv.setGravity(Gravity.RIGHT);

                                            tv.setBackgroundColor(Color.WHITE);

                                            //tv1.setTextColor(Color.RED);
                                            //tv1.setText(date.get(i)+"");


                                            tv1.setBackgroundColor(Color.WHITE);


                                        } else {
                                            tv.setTextColor(Color.BLUE);
                                            tv.setText(Message.get(i));
                                            tv.setTextSize((float) 15.0);
                                            tv.setGravity(Gravity.LEFT);

                                            tv.setBackgroundColor(Color.WHITE);

                                            //tv1.setTextColor(Color.BLACK);
                                            //tv1.setText(date.get(i));
                                            //tv1.setGravity(Gravity.CENTER);

                                            tv1.setBackgroundColor(Color.WHITE);
                                        }

                                        lt.addView(tv);
                                        lt.addView(tv1);


                                    }

                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Toast.makeText(getApplicationContext(), "error" + e, Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            hd.postDelayed(r, 2000);
                        }
                    }

                    , new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "error" + error, Toast.LENGTH_LONG).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Fromid", id);
                    params.put("Toid", fid);


                    return params;
                }
            };
            mqueu.add(string);

        }

    };



}
