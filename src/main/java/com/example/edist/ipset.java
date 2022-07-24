package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.time.Instant;

public class ipset extends AppCompatActivity {

    EditText e1;
    Button b;
    String ip="";
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipset);

        e1=(EditText)findViewById(R.id.editTextTextPersonName3);
        b=(Button)findViewById(R.id.button);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 0x123450);
            }
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ip=e1.getText().toString();
              if(ip.equals(""))
              {
                  e1.setError("enter ip");
              }
              else
              {
                  SharedPreferences.Editor ed=sh.edit();
                  ed.putString("ipadd", ip);
                  ed.commit();

                  Intent i=new Intent(getApplicationContext(),MainActivity.class);
                  startActivity(i);
              }
            }
        });
    }
}