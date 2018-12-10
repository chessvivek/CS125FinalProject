package com.example.navin.cs125finalproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.app.Activity;
import android.view.Menu;

import com.convertapi.ConversionResult;
import com.convertapi.ConvertApi;
import com.convertapi.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Download extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.list_view, MainActivity.listurls);

        ListView listView = (ListView) findViewById(R.id.list_url);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("converting back to PDF!!");

                if (MainActivity.listurls.get(position).link != null) {
                    System.out.println("Already converted");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MainActivity.listurls.get(position).link));
                    startActivity(browserIntent);
                    return;
                }

                List<String> url2 = new ArrayList<>();

                for (int i = 0; i < MainActivity.listurls.get(position).urls.size(); i++) {
                    CompletableFuture<ConversionResult> result3 = ConvertApi.convert("png", "pdf",
                            new Param("File", MainActivity.listurls.get(position).urls.get(i)));
                    try {
                        url2.add(result3.get().getFile(0).getUrl());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("i = " + i + " url = " + url2.get(i));
                }

                Param[] lol = new Param[url2.size()];
                for (int i = 0; i < url2.size(); i++) {
                    lol[i] = new Param("Files", url2.get(i));
                }
                CompletableFuture<ConversionResult> result3 = ConvertApi.convert("pdf", "merge", lol);

                String url3 = null;

                try {
                    url3 = result3.get().getFile(0).getUrl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Finally, " + url3);
                MainActivity.listurls.get(position).link = url3;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url3));
                startActivity(browserIntent);
            }
        });
    }
}