package com.rayadev.byoc.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rayadev.byoc.model.Converter;

@Database(entities = {Converter.class}, version = 1, exportSchema = false)
public abstract class ConverterDatabase extends RoomDatabase {

    public abstract ConverterDAO getConverterDAO();

    private static ConverterDatabase INSTANCE;

    public static ConverterDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ConverterDatabase.class) {
                if (INSTANCE == null) {

                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ConverterDatabase.class, "converter_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
