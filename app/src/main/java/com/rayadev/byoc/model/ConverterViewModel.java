package com.rayadev.byoc.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rayadev.byoc.model.Converter;
import com.rayadev.byoc.room.ConverterRepository;

import java.util.List;

public class ConverterViewModel extends AndroidViewModel {
    //Add a private member variable to hold a reference to the Repository.
    private ConverterRepository mRepository;

    //Add a private LiveData member variable to cache the list of words.
    private LiveData<List<Converter>> mConverterFavoritesList;

    public ConverterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ConverterRepository(application);
        mConverterFavoritesList = mRepository.getConverterFavoritesList();
    }

    // The ViewModel implements all methods that will be available to the Main Activity.
    // This completely hides the implementation from the UI.
    // Pass these up the the Main Activity or whatever needs access.

    //Wrapper for the "getter" method that gets all the words.
    public LiveData<List<Converter>> getConverterFavoritesList() {
        return mConverterFavoritesList;
    }

    //The insert method wrapper.
    public void insert(Converter converter) {
        mRepository.insert(converter);
    }

    public void insertCurrency(Currency currency){mRepository.insertCurrency(currency);}

    //Deletes a single word from the database
    public void delete(Converter converter) {
        mRepository.delete(converter);
    }

    public LiveData<Currency> getTargetCurrency(String pair) { return mRepository.getTargetCurrency(pair);}
}
