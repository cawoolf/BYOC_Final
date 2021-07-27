package com.rayadev.byoc.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "converter_table")
public class Converter {

    @PrimaryKey(autoGenerate = true)
    public int converterID;

    public String unitAString, unitBString;

    public String unitCategory;

    public Converter(String fromUnit, String toUnit) {
        this.unitCategory = "distance";
        this.unitAString = fromUnit;
        this.unitBString = toUnit;
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
