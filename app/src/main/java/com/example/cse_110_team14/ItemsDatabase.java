package com.example.cse_110_team14;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities= {CheckedName.class}, version=1)
public abstract class ItemsDatabase extends RoomDatabase {
    private static ItemsDatabase singleton = null;

    public abstract ItemsDao itemsDao();

    public synchronized static ItemsDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = ItemsDatabase.makeDatabase(context);
        }
        return singleton;
    }
    private static ItemsDatabase makeDatabase(Context context){
        return Room.databaseBuilder(context, ItemsDatabase.class, "items_app.db")
                .allowMainThreadQueries().build();
    }
}
