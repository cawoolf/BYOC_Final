package com.rayadev.byoc.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.rayadev.byoc.model.Converter;
import com.rayadev.byoc.model.Currency;

import java.util.List;

public class ConverterRepository{

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

    public void insertCurrency(Currency currency) {
       new insertCurrencyAsyncTask(mConverterDAO).execute(currency);
    }

    public void delete (Converter converter) {
        new deleteAsyncTask(mConverterDAO).execute(converter);}

    //Needs to be Async Task, and probably LiveData
    public Currency getTargetCurrency(String converterName) {
        new getCurrencyAsyncTask(mConverterDAO).execute(converterName);

//        return new Currency(new getCurrencyAsyncTask(mConverterDAO).equals(converterName));
        return null;
   }

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

    private static class insertCurrencyAsyncTask extends AsyncTask<Currency, Void, Void> {

        private final ConverterDAO mAsyncTaskDao;

        insertCurrencyAsyncTask(ConverterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Currency... params) {
            mAsyncTaskDao.insertCurrency(params[0]);
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

    public static class getCurrencyAsyncTask extends AsyncTask<String, Void, Currency> {

        private final ConverterDAO mAsyncTaskDao;
        private getAsyncCurrency mCurrencyInterface;

        getCurrencyAsyncTask(ConverterDAO asyncTaskDao) {
            mAsyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Currency doInBackground(String... strings) {
            Currency currency = mAsyncTaskDao.getTargetCurrency(strings[0]);
            return currency;
        }

        @Override
        protected void onPostExecute(Currency currency) {
            super.onPostExecute(currency);
            mCurrencyInterface.getCurrency(currency);

        }

        interface getAsyncCurrency{
            Currency getCurrency(Currency currency);
        }


    }



}
