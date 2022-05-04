package com.example.cse_110_team14;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class PlanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ArrayList<String> test = getIntent().getStringArrayListExtra("checked_animals");
        for(String s : test) System.out.println(s);
    }
}
