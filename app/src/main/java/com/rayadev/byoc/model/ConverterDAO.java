package com.rayadev.byoc.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
interface ConverterDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert();

    @Delete
    void delete(Converter converter);

    @Query("SELECT * from converter_table")
    void getFavoriteConverterList();
}
