package com.example.cse_110_team14;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemsDao {
    @Insert
    long insert(CheckedName checkedName);

    @Query("SELECT * FROM `checked_names` WHERE `id`=:id")
    CheckedName get (long id);

    @Query("SELECT * FROM `checked_names`")
    List<CheckedName> getAll();

    @Query("DELETE FROM `checked_names` WHERE name = :name")
    void delete(String name);

    @Query("DELETE FROM `checked_names`")
    void deleteAll();
}
