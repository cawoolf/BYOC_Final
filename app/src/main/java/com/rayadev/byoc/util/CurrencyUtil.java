package com.rayadev.byoc.util;

import android.util.Log;

import com.google.gson.Gson;
import com.rayadev.byoc.model.ConverterViewModel;
import com.rayadev.byoc.model.Currency;
import com.rayadev.byoc.room.CurrencyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyUtil{

    private CurrencyAPI mCurrencyAPI;
    private final String mAPIKey = "882cc2509c2a6546a18c"; //Do not change API Key...
    private final String TAG = "ATAG";


    //Still need some kind of Java Date function that prevents Retrofit from running on every start.
    public void loadCurrencyData(ConverterViewModel converterViewModel) throws JSONException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://free.currconv.com/api/v7/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCurrencyAPI = retrofit.create(CurrencyAPI.class);

        HashSet<String> currencySet = buildCurrencyHashSet();

        for(String pair: currencySet) {
           runCurrencyAPIRequest(pair,converterViewModel);
            Log.i(TAG, pair);
        }

    }


    private HashSet<String> buildCurrencyHashSet() {

        //Max of two currencies pairs per request with free APIkey.
        //Cuts down on the total number of requests from 16 to 8 when using 4 currencies.
        //Build a HashSet with unique values that looks like "USD_NZD,NZD_USD"
        //Pass that argument into the String url for Retrofit.

        HashSet<String> pairs = new HashSet<>();
        String[] currencies = new String[]{"USD", "CAD", "EUR", "NZD"};

        //Create unique set of all currency pairs in currencies
        for (String c : currencies) {
            String c1 = c;
            for (String c2 : currencies) {
                String pair = c1 + "_" + c2;
                pairs.add(pair);
            }
        }

        //Convert to HashSet to ArrayList so that we can create unique pair of pairs (double pairs)
        ArrayList<String> currencyPairList = new ArrayList<>();

        currencyPairList.addAll(pairs);

        //Run through the list and create a Set of unique double pairs currencyPairs
        //This cuts down on the number of API requests. Free version only has max of two pairs per request.
        //This would be good code to learn and run testing on.
        HashSet<String> currencyPairDoubles = new HashSet<>();

        int I = 0;
        int J = 1;
        while (J < currencyPairList.size()) {

            String currencyA = currencyPairList.get(I);
            String currencyB = currencyPairList.get(J);
            String currencyAB = currencyA + "," + currencyB;
            currencyPairDoubles.add(currencyAB);

            //Iterates over the list skipping over two to ensure that each set of pairs is unqiue. This breaks
            //If the total size of the set is an odd number.
            I = I + 2;
            J = J + 2;

        }

        for(String pair : currencyPairDoubles) {
            Log.i(TAG, pair+"");

        }

        return currencyPairDoubles;
    }

    //Makes the API call from the given currency HashSet value.
    //The returned data is asynchronous. How to get it out.
    private void runCurrencyAPIRequest(String mCurrencyPair, ConverterViewModel converterViewModel) throws JSONException {

        Call call = mCurrencyAPI.getCurrency(getUrlString(mAPIKey, mCurrencyPair));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                     Log.i(TAG,"Fail: " + response.code());
                    return;
                }

                String body = new Gson().toJson(response.body());
                JSONObject currency = null;

                //Modify the string right here to separate out the currency pairs
                String c1 = mCurrencyPair.substring(0,7); //currency pair 1
                String c2 = mCurrencyPair.substring(8,15); //currency pair 2

                try {
                    currency = new JSONObject(body);

                    String result = "Success: " + response.code() + "\n" +
                            c1 + ": " + currency.get(c1) + "\n" +
                            c2 + ": " + currency.get(c2);

                    Log.i(TAG, result);

                    Currency currency1 = new Currency(c1, (Double)currency.get(c1));
                    Currency currency2 = new Currency(c2, (Double)currency.get(c2));

                    converterViewModel.insertCurrency(currency1);
                    converterViewModel.insertCurrency(currency2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                String fail = "Fail" + t.getMessage();
                Log.i(TAG, fail);

            }

        });
    }

    private String getUrlString(String apiKey, String currencyPair) {
        return "https://free.currconv.com/api/v7/convert?q=" + currencyPair + "&compact=ultra&apiKey=" + apiKey;
    }

}
