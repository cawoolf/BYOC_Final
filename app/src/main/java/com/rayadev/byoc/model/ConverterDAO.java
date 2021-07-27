package com.rayadev.byoc.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
interface ConverterDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Converter param);

    @Delete
    void delete(Converter converter);

    @Query("SELECT * from converter_table") //Maybe try to order by primary key, or make it so that you can drag and rearrange the order of the converters.
    LiveData<List<Converter>> getFavoriteConverterList();
}
