package com.rayadev.byoc.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
interface ConverterDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert();

    @Delete
    void delete(Converter converter);
}
