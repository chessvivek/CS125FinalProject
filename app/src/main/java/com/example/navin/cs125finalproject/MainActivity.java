package com.example.navin.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.*;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import java.lang.String;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.convertapi.*;

public class MainActivity extends AppCompatActivity {
    private Button button;

    private static RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rq = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.how);
        Button button2 = (Button) findViewById(R.id.Upload);
        Button button3 = (Button) findViewById(R.id.Download);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainActivity.this, Instructions.class);
                startActivity(int1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlfromuser;

                InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                EditText text = (EditText)findViewById(R.id.edit_text_input);
                String str = text.getText().toString();
                System.out.println(str);
                urlfromuser = "https://www." + str;

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Config.setDefaultSecret("HcGArfflL2m9nok3");
                        CompletableFuture<ConversionResult> result = ConvertApi.convert("url", "pdf",
                                new Param("Url", urlfromuser));
                        String url = null;
                        try {
                            url = result.get().getFile(0).getUrl();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(url);

                        CompletableFuture<ConversionResult> result2 = ConvertApi.convert("pdf", "png",
                                new Param("File", url));
                        String[] url2 = new String[100];
                        int fl = 0;
                        try {
                            fl = result2.get().fileCount();
                            for (int i = 0; i < fl; i++) {
                                url2[i] = result2.get().getFile(i).getUrl();
                                System.out.println("i = " + i + " url = " + url2[i]);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("Uploading to Imgur");

                        String wtf = "https://api.imgur.com/3/image";
                        JsonObjectRequest[] jsonobjectrequest = new JsonObjectRequest[fl];
                        for (int i = 0; i < fl; i++) {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("image", url2[i]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            jsonobjectrequest[i] = new JsonObjectRequest
                                    (Request.Method.POST, wtf, json, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                System.out.println(response.getJSONObject("data").getString("link"));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            System.out.println(error);
                                        }
                                    }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("Authorization", "Client-ID 942e15fe4a3ad6c");
                                    //                   params.put("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
                                    System.out.println(params);
                                    return params;
                                }
                            };
                        }

                        Thread t2 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < jsonobjectrequest.length; i++) {
                                    rq.add(jsonobjectrequest[i]);
                                }
                                try {
                                    Thread.sleep(jsonobjectrequest.length * 3000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        t2.start();
                        try {
                            t2.join();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("converting back to PDF!!");


                        for (int i = 0; i < fl; i++) {
                            CompletableFuture<ConversionResult> result3 = ConvertApi.convert("png", "pdf",
                                    new Param("File", url2[i]));
                            try {
                                url2[i] = result3.get().getFile(0).getUrl();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("i = " + i + " url = " + url2[i]);
                        }

                        Param[] lol = new Param[fl];
                        for (int i = 0; i < fl; i++) {
                            lol[i] = new Param("Files", url2[i]);
                        }
                        CompletableFuture<ConversionResult> result3 = ConvertApi.convert("pdf", "merge", lol);

                        String url3 = null;

                        try {
                            url3 = result3.get().getFile(0).getUrl();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("Finally, " + url3);
                    }
                });

                t.start();
  //              try {
  //                  t.join();
  //              } catch (Exception e) { }

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int3 = new Intent(MainActivity.this, Download.class);
                startActivity(int3);
            }
        });
    }
}

