package com.example.admin.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.admin.database.database.Database;
import com.example.admin.database.database.DatabaseImpl;
import com.example.admin.database.database.Story;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="MainActivity";
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        database=new DatabaseImpl(this);

        Story s1=new Story();
        s1.setId(UUID.randomUUID().toString());
        s1.setTitle("Harry potter");
        s1.setContent("sdjhdfhn ");
        database.save(s1);

        Story s2=new Story();
        s2.setId(UUID.randomUUID().toString());
        s2.setTitle("Harry potter 2");
        s2.setContent("sdjhdfhn 2........ ");
        database.save(s2);

        ArrayList<Story> stories=database.getAllData();
        Log.d(TAG, "onCreate: "+stories.toString());

        s2.setTitle("Harry potter 2");
        database.upDate(s2);


        stories=database.getAllData();
        Log.d(TAG, "onCreate: "+stories.toString());
    }

}
