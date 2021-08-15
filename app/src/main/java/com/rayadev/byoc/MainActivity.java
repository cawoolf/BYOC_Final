package com.rayadev.byoc;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.rayadev.byoc.model.ConverterViewModel;
import com.rayadev.byoc.model.CurrencyAPI;
import com.rayadev.byoc.model.CurrencyUtil;
import com.rayadev.byoc.ui.main.PageAdapter;

import org.jetbrains.annotations.NotNull;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeUI();

        //Passes the ViewModel down to CurrencyUtil for Async Retrofit call. Inserts currency into database.
        ConverterViewModel converterViewModel = new ViewModelProvider( this).get(ConverterViewModel.class);
        loadCurrencyData(converterViewModel);

    }

    private void initializeUI() {

        keyBoardManager();
        setUpToolbar();
        TabLayout tabLayout = setUpTabLayout();
        setUpPageAdapter(tabLayout);
    }

    private void loadCurrencyData(ConverterViewModel converterViewModel) {

        //Need to make a SharedPref here so that you don't request the currency data from the API
        //Everytime you open the app. Make a SharedPref or something that resets everyday/hour. So
        //That the call is made only once per day/hour
        //With a Thread running out from here or most likely some kind of LiveData observer
        //Actually you should use the Repository
        CurrencyUtil currencyUtil = new CurrencyUtil();

        try {
            currencyUtil.loadCurrencyData(converterViewModel);
        } catch (JSONException e) {

            Log.i("BTAG", e.toString());

        }

        LiveData<JSONObject> test = new LiveData<JSONObject>() {
            @Override
            public void observe(@NonNull @NotNull LifecycleOwner owner, @NonNull @NotNull Observer<? super JSONObject> observer) {
                super.observe(owner, observer);
            }
        };



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





}