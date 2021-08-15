package com.rayadev.byoc.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "currency_table")
public class Currency {

    @PrimaryKey
    @NonNull
    public String currencyPair;

    public String currencyValue; //Comes out of the JSON as a String

    public Currency(String currencyPair, String currencyValue) {

        this.currencyPair = currencyPair;
        this.currencyValue = currencyValue;

    }


}
