package com.rayadev.byoc.model;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ConverterRepository {

    private ConverterDAO mConverterDAO;
    LiveData<List<Converter>> mConverterFavoritesList;


}
