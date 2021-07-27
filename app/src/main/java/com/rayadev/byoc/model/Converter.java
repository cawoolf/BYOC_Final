package com.rayadev.byoc.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "converter_table")
public class Converter {

    @PrimaryKey(autoGenerate = true)
    public int converterID;

    public String fromUnit, toUnit;


    public Converter(String fromUnit, String toUnit) {
        this.fromUnit = fromUnit;
        this.toUnit = toUnit;
    }



}
