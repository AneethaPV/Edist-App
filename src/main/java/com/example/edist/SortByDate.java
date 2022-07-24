package com.example.edist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class SortByDate extends AppCompatActivity {

    Button b1,b2;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_by_date);

        b1=(Button)findViewById(R.id.all);
        b2=(Button)findViewById(R.id.date);
        sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("share","all");
                ed.commit();

                Intent i=new Intent(getApplicationContext(),ViewMap.class);
                i.putExtra("date","na");
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("share","date");
                ed.commit();

                Intent i=new Intent(getApplicationContext(),DateMap.class);


                startActivity(i);
            }
        });
    }
}