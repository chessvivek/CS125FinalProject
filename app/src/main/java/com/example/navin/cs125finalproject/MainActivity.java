package com.example.navin.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.CompletableFuture;

import com.convertapi.*;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("*/*");
/*                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                String path = null;

                @Override
                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                    if (requestCode == RESULT_LOAD_IMAGE) {
                        if (resultCode == RESULT_OK) {
                            Uri fileUri = data.getData();
                            path = fileUri.getPath();
                        }
                    }
                }
*/
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Config.setDefaultSecret("HcGArfflL2m9nok3");
                        CompletableFuture<ConversionResult> result = ConvertApi.convert("url", "pdf",
                                new Param("Url", "https://faculty.math.illinois.edu/~pballen/teaching/241/exam1.html"));
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
                try {
                    t.join();
                } catch (Exception e) { }
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

