package com.rayadev.byoc.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


//Links the data from the Repository to the Main Activity, in order to update the UI.
public class ConverterViewModel extends AndroidViewModel {

    //Add a private member variable to hold a reference to the Repository.
    private ConverterRepository mRepository;

    //Add a private LiveData member variable to cache the list of words.
    private LiveData<List<Converter>> mAllConverters;


    public ConverterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ConverterRepository(application);
        mAllConverters = mRepository.getAllConverters();
    }

    // The ViewModel implements all methods that will be available to the Main Activity.
    // This completely hides the implementation from the UI.
    // Pass these up the the Main Activity or whatever needs access.

    //Wrapper for the "getter" method that gets all the words.
    public LiveData<List<Converter>> getAllConverters() { return mAllConverters; }

    //Deletes a single Converters from the database
    public void deleteConverters(Converter converter) {mRepository.deleteConverter(converter);}

    public void insertConverter(Converter converter) {mRepository.insertConverter(converter);}

    public void getTargetConverter(String converterName) {mRepository.getTargetConverter(converterName);}

}
