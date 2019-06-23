package com.example.sololearn_newsfeed.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.sololearn_newsfeed.persistence.entity.Result;

import java.util.List;

@Dao
public interface NewsDAO {

    @Query("SELECT * FROM result")
    LiveData<List<Result>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Result result);

}
