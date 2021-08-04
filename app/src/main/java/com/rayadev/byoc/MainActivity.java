package com.rayadev.byoc;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.rayadev.byoc.model.CurrencyAPI;
import com.rayadev.byoc.ui.main.PageAdapter;

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

public class MainActivity extends AppCompatActivity {

    private CurrencyAPI mCurrencyAPI;
    private final String mAPIKey = "882cc2509c2a6546a18c";
    private final String TAG = "ATAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeUI();
        loadCurrencyData();

    }

    private void initializeUI() {

        keyBoardManager();
        setUpToolbar();
        TabLayout tabLayout = setUpTabLayout();
        setUpPageAdapter(tabLayout);
    }


    //Might need to be in its own thread, and class..
    //Watch coding in flows video about Handler and Looper to update UI thread from Currency Thread.
    //For sure. And set it for double pair requests, and for only once a day.
    private void loadCurrencyData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://free.currconv.com/api/v7/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCurrencyAPI = retrofit.create(CurrencyAPI.class);

        HashMap<String, Double> currencyPairs = new HashMap<>();
        HashSet<String> pairs = new HashSet<>();

        String[] currencies = {getString(R.string.currency_USD), getString(R.string.currency_CAD),
                getString(R.string.currency_EUR), getString(R.string.currency_NZD)};


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

//        for(String pair : pairs) {
//            getCurrency(pair);
//        }


    }



    private void keyBoardManager() {

        //Keeps the keyboard from auto-popping up after onCreate()
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setUpToolbar() {

        //Links the toolbar over the app Actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private TabLayout setUpTabLayout() {

        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        // Set the title for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_2));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_3)); //Just comment this out to remove setlist

        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        return tabLayout;

    }

    private void setUpPageAdapter(TabLayout tabLayout) {

        // Use PagerAdapter to manage page views in fragments.

        // Each page is represented by its own fragment
        //PageAdapter populates the screens that the ViewPager passes to the TabLayout
        final ViewPager viewPager = findViewById(R.id.pager);
        final PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        // Setting a listener for tab clicks
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition()); //Important for clicking over to new tabs.
                Log.i("TAG", String.valueOf(tab.getPosition()));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);


        return super.onCreateOptionsMenu(menu);
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