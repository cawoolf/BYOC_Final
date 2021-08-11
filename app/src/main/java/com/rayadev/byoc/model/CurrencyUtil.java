package com.rayadev.byoc.model;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.ContentProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
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

public class CurrencyUtil{

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

        HashSet<String> currencySet = buildCurrencyHashSet();

        int i = 0;
        for(String pair: currencySet) {
            runCurrencyAPIRequest(pair);
            Log.i(TAG, pair);
            i++;
        }

        Log.i(TAG, "Number of requests: " + i);

        //Another good spot for testing;
        Log.i(TAG, "User Request: " + "\n");
        runCurrencyAPIRequest("USD_NZD,NZD_USD");



    }

    private HashSet<String> buildCurrencyHashSet() {

        //Max of two currencies pairs per request with free APIkey.
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

        Log.i(TAG, "Original request size: " + pairs.size() + ""); //Log the size

        //Convert to HashSet to ArrayList so that we can create unique pair doubles
        ArrayList<String> currencyPairList = new ArrayList<>();

        currencyPairList.addAll(pairs);

        //Run through the list and create a Set of unique double currencyPairs
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

        return currencyPairDoubles;
    }

    //Makes the API call from the given currency HashSet value.
    //The returned data is asynchronous. How to get it out.
    private void runCurrencyAPIRequest(String mCurrencyPair) {


        //Write here is where the thread needs to be more or less
        //Wait for the call to finish, and the return the JSON.
        final JSONObject[] jsonObject = {new JSONObject()};

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

                    //So basically pass the JSON to a SharedPreferneces.. Or use Hawk.
                    //Does all the serialization and stuff for you. And SharedPrefs is only for Strings.
                    String result = "Success: " + response.code() + "\n" +
                            c1 + ": " + currency.get(c1) + "\n" +
                            c2 + ": " + currency.get(c2);

                    Log.i(TAG, result);

                    //Need to build the JSON here as well. Each pair needs to be added to it.
                    //To create the final file with all the currency data.
                    writeJSON(currency.get(c1)+"");
                    jsonObject[0] = currency;


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

    private void writeJSON(String test) {

        //Best way is to pass this JSON back out of this classUtil, which is handled in its own thread.


        Log.i("BTAG",test);


//        SharedPreferences sharedPref = context.getSharedPreferences(
//                "JSON", Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();

    }

    private String getUrlString(String apiKey, String currencyPair) {
        return "https://free.currconv.com/api/v7/convert?q=" + currencyPair + "&compact=ultra&apiKey=" + apiKey;
    }
}
