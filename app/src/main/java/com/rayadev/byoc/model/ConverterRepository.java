package com.rayadev.byoc.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ConverterRepository {

    private ConverterDAO mConverterDAO;
    LiveData<List<Converter>> mConverterFavoritesList;

    //Add a constructor that gets a handle to the database and initializes the member variables.
    public ConverterRepository(Application application) {
        ConverterDatabase db = ConverterDatabase.getDatabase(application);
        mConverterDAO = db.getConverterDAO();
        mConverterFavoritesList = mConverterDAO.getFavoriteConverterList();
    }

    //All accessible methods
    public LiveData<List<Converter>> getConverterFavoritesList() {return  mConverterFavoritesList;}

    public void insert (Converter converter) {
        new insertAsyncTask(mConverterDAO).execute(converter);
    }

    public void delete (Converter converter) {
        new deleteAsyncTask(mConverterDAO).execute(converter);}

    //AsyncTasks for database methods.
    private static class insertAsyncTask extends AsyncTask<Converter, Void, Void> {

        private final ConverterDAO mAsyncTaskDao;

        insertAsyncTask(ConverterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Converter... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Converter, Void, Void> {

        private final ConverterDAO mAsyncTaskDao;

         deleteAsyncTask(ConverterDAO asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(final Converter... converters) {

            mAsyncTaskDao.delete(converters[0]);
            return null;
        }
    }


}
