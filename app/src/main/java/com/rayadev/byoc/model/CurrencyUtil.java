package com.rayadev.byoc.model;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.rayadev.byoc.R;

import org.json.JSONException;
import org.json.JSONObject;

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
    private final String TAG = "ATAG";;


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

        String[] currencies = new String[] {"USD", "CAD", "EUR", "NZD"};


        for(String c : currencies) {
            String c1 = c;
            for(String c2: currencies) {
                String pair = c1+"_"+c2;
                pairs.add(pair);
            }
        }

        for(String c1 : pairs) {
            Log.i(TAG, c1);
        }

        Log.i(TAG, pairs.size()+"");

        String mCurrencyPair = "USD_CAD";

        for(String pair : pairs) {
            getCurrency(pair);
        }


    }

    //Can make get request for currency at once.
    private void getCurrency(String mCurrencyPair) {

//        Call call = mCurrencyAPI.getCurrencyPair(pair);
        Call call = mCurrencyAPI.getCurrency(getUrlString(mAPIKey, mCurrencyPair));
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

//                String code = "Success Code:" + response.code();
//                String body = new Gson().toJson(response.body());
//                textViewResult.setText(code + "\n" + body);

                String body = new Gson().toJson(response.body());
                JSONObject currency = null;
                try {
                    currency = new JSONObject(body);

                    String result = "Code: " + response.code() +"\n" +
                            mCurrencyPair + ": "+ currency.getString(mCurrencyPair);

//                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, result);


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

                String fail = "Fail"+ t.getMessage();
                Log.i(TAG, fail);

            }
        });

    }

    private String getUrlString(String apiKey, String currencyPair) {
        return "https://free.currconv.com/api/v7/convert?q=" + currencyPair + "&compact=ultra&apiKey=" + apiKey;
    }
}
