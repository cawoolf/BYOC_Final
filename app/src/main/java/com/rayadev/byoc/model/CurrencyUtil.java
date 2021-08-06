package com.rayadev.byoc.model;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.rayadev.byoc.MainActivity;
import com.rayadev.byoc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyUtil {

    private CurrencyAPI mCurrencyAPI;
    private final String mAPIKey = "882cc2509c2a6546a18c";
    private final String TAG = "ATAG";



    //Might need to be in its own thread, and class..
    //Watch coding in flows video about Handler and Looper to update UI thread from Currency Thread.
    //For sure. And set it for double pair requests, and for only once a day.
    public void loadCurrencyData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://free.currconv.com/api/v7/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCurrencyAPI = retrofit.create(CurrencyAPI.class);

        HashMap<String, Double> currencyPairs = new HashMap<>();
        HashSet<String> pairs = new HashSet<>();

//        String[] currencies = new String[]{getString(R.string.currency_USD), getString(R.string.currency_CAD),
//                getString(R.string.currency_EUR), getString(R.string.currency_NZD)};

        String[] currencies = new String[]{"USD", "CAD", "EUR", "NZD"};

        //Create unique set of all currency pairs in currencies
        for (String c : currencies) {
            String c1 = c;
            for (String c2 : currencies) {
                String pair = c1 + "_" + c2;
                pairs.add(pair);
            }
        }

        Log.i(TAG, pairs.size() + ""); //Log the size

        for (String c1 : pairs) {
            Log.i(TAG, c1); //Show each pair
        }

        //Convert to HashSet to ArrayList so that we can create unique pair doubles
        ArrayList<String> currencyPairList = new ArrayList<>();

        currencyPairList.addAll(pairs);

        //Run through the list and create a Set of unique double currencyPairs
        HashSet<String> currencyPairDoubles = new HashSet<>();

        int I = 0;
        int J = 1;
        while (J < currencyPairList.size()) {

            String currencyA = currencyPairList.get(I);
            String currencyB = currencyPairList.get(J);
            String currencyAB = currencyA + "," + currencyB;
            currencyPairDoubles.add(currencyAB);

            I = I + 2;
            J = J + 2;

            //getCurrency(pair);
        }


        for (String c1 : currencyPairDoubles) {
            Log.i(TAG, c1); //Show each pair

        }

        //Just an issue with getting the data out of the JSON
        String test= "USD_PHP,USD_CAD";
        getCurrency(test);




    }

    //Can make get request for currency at once.
    private void getCurrency(String mCurrencyPair) {

//        Call call = mCurrencyAPI.getCurrencyPair(pair);
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
                try {
                    currency = new JSONObject(body);

                    String result = "Code: " + response.code() + "\n" +
                            mCurrencyPair + ": " + currency.getString(mCurrencyPair);

                    Log.i(TAG, result);


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
