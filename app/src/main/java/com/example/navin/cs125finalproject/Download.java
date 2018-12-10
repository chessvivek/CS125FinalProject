package com.example.navin.cs125finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.app.Activity;
import android.view.Menu;

public class Download extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.list_view, MainActivity.listurls);

        ListView listView = (ListView) findViewById(R.id.list_url);
        listView.setAdapter(adapter);

        

    }
}