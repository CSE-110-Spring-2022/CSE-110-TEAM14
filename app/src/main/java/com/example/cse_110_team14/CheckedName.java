package com.example.cse_110_team14;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checked_names")
public class CheckedName {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name;

    CheckedName(@NonNull String name){
        this.name = name;
    }
}
