package com.rayadev.byoc.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "converter_favorites_table")
public class Converter {

    @PrimaryKey(autoGenerate = true)
    public int converterID;

    public String unitAString, unitBString;

    public String unitCategory;

    public Converter(String unitCategory, String unitAString, String unitBString) {
        this.unitCategory = unitCategory;
        this.unitAString = unitAString;
        this.unitBString = unitBString;
    }

    public int getConverterID() {
        return converterID;
    }

    public String getUnitAString() {
        return unitAString;
    }

    public String getUnitBString() {
        return unitBString;
    }

    public String getUnitCategory() {
        return unitCategory;
    }


}
