package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.json.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment this line of code out if u want to go back to search activity since I have not
        // implemented a way to go back to the start yet
        // ActivityData.setActivity(this, "activity.json", "SearchActivity");
        String activityName = ActivityData.getActivity(this, "activity.json");
        Intent intent = new Intent(this, SearchActivity.class);;

        Log.d("MainActivity", "activityName: " + activityName);

        if(activityName.equals("SearchActivity")) {
            intent = new Intent(this, SearchActivity.class);
        }

        else if(activityName.equals("PlanActivity")) {
            Log.d("MainActivity", "activityName: " + activityName);
            ItemsDao itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
            List<CheckedName> checkedNames = itemsDao.getAll();
            ArrayList<String> test = new ArrayList<>();
            for(CheckedName name : checkedNames) {
                test.add(name.name);
            }

            Log.d("MainActivity", "exhibitList: " + test);

            intent = new Intent(this, PlanActivity.class);
            intent.putExtra("checked_animals", test);

        }

        else if(activityName.equals("VisitAnimalActivity")) {


            intent = new Intent(this, VisitAnimalActivity.class);
            intent.putExtra("animal_order", ActivityData.getAnimals(this,
                    "animals.json"));
            intent.putExtra("exhibit_id_order", ActivityData.getIds(this,
                    "ids.json"));
            intent.putExtra("directions", ActivityData.getDirections
                    (this,"directions.json"));
            intent.putExtra("index", ActivityData.getDirectionsIndex
                    (this, "index.json"));
        }

//        List<String> animals = new ArrayList<>();
//        animals.add("dog");
//        animals.add("cat");
//        animals.add("bird");
//        animals.add("fish");
//        animals.add("snake");
//
//        ActivityData.setIds(this, "ids.json", animals);
//        List<String> animalsFromFile = ActivityData.getIds(this, "ids.json");
//        Log.d("MainActivity", "animalsFromFile: " + animalsFromFile.get(0));

        startActivity(intent);
    }
}