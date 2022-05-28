package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String activityName = "";
        Intent intent = new Intent(this, SearchActivity.class);;

//        try {
//            Reader reader = Files.newBufferedReader(Paths.get("app/activityState.json"));
//            JsonObject parser = JsonParser.parseReader(reader).getAsJsonObject();
//            activityName = parser.get("activity").getAsString();
//            Log.d("json", activityName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Log.d("test", "please work");


        activityName = ActivityData.getActivity(this, "activity.json");
        Log.d("MainActivity", "activityName: " + activityName);

        if(activityName.equals("SearchActivity")) {
            intent = new Intent(this, SearchActivity.class);
        }

        else if(activityName.equals("PlanActivity")) {
            intent = new Intent(this, PlanActivity.class);
        }
        else if(activityName.equals("VisitAnimalActivity")) {
            intent = new Intent(this, VisitAnimalActivity.class);
        }

        startActivity(intent);
    }
}