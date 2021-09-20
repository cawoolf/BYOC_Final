package com.rayadev.byoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rayadev.byoc.model.ConverterViewModel;
import com.rayadev.byoc.util.CurrencyUtil;
import com.rayadev.byoc.ui.main.PageAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private boolean mHomeTabChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeUI();
        setUpCurrency();

    }

    private void initializeUI() {

        keyBoardManager();
        setUpToolbar();
        setUpTabLayout();
        setUpPageAdapter(mTabLayout);
        mHomeTabChoice = checkFavoritesPrefs();
    }


    private void setUpCurrency() {

        //SharedPref and Date stuff to ensure that currency is only updated once a day
        //Saves on API request limits.
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        long oldDate = sharedPref.getLong("date", 0);
        int updates = sharedPref.getInt("updates", 0);

        //getting the current time in milliseconds, and creating a Date object from it:
        Date currentDate = new Date(System.currentTimeMillis()); //or simply new Date();

        //converting it back to a milliseconds representation:
        long currentDateMillis = currentDate.getTime();
        long currentDateSeconds = currentDateMillis/1000;

        long oldDateSeconds = oldDate/1000;

        long diff = currentDateSeconds - oldDateSeconds;

       if(diff > 86400) { //Basically if its been longer than a day since the last currency update.
           editor.putLong("date", currentDateMillis).apply();

           //Passes the ViewModel down to CurrencyUtil for Async Retrofit call. Inserts currency into database.
           ConverterViewModel converterViewModel = new ViewModelProvider(this).get(ConverterViewModel.class);
           loadCurrencyData(converterViewModel);

           updates = updates +1;
           editor.putInt("updates", updates).apply();
           Log.i("DTAG", "Update" + "\n" + "Old Date: " + oldDateSeconds + "\n" + "New Date: " + currentDateSeconds + "\n" +
                    "\n" + "Time Elapsed: " + diff + "\n" + "Total updates: " + updates);


       }
        else {
           Log.i("DTAG", "No Update" + "\n" + "Old Date: " + oldDateSeconds + "\n" + "New Date: " + currentDateSeconds
           + "\n" + "Time Elapsed: " + diff + "\n" + "Total updates: " + updates);

       }
    }

    private void loadCurrencyData(ConverterViewModel converterViewModel) {

        CurrencyUtil currencyUtil = new CurrencyUtil();

        try {
            currencyUtil.loadCurrencyData(converterViewModel);
        } catch (JSONException e) {

            Log.i("BTAG", e.toString());

        }

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

    private void setUpTabLayout() {

        // Create an instance of the tab layout from the view.
        mTabLayout = findViewById(R.id.tab_layout);

        // Set the title for each tab.
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_text_1));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_text_2));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_text_3)); //Just comment this out to remove setlist

        // Set the tabs to fill the entire layout.
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


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

    private boolean checkFavoritesPrefs() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean favPref = sharedPref.getBoolean("favorites_tab_default", false);

        TabLayout.Tab tab;
        if(favPref) {
            tab = mTabLayout.getTabAt(1);

        }
        else{
            tab = mTabLayout.getTabAt(0);

        }
        assert tab != null;
        tab.select();

        return favPref;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem itemFavorites = menu.findItem(R.id.homeFavoritesTrue_MenuItem);
        MenuItem itemConverter = menu.findItem(R.id.homeConverterTrue_MenuItem);

        if(mHomeTabChoice) {
            itemFavorites.setChecked(true);
        }
        else {
            itemConverter.setChecked(true);

        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {


        switch(item.getItemId()) {
            case R.id.homeFavoritesTrue_MenuItem:
                setFavoritesTab(1);
                item.setChecked(true);
                break;

            case R.id.homeConverterTrue_MenuItem:
                setFavoritesTab(0);
                item.setChecked(true);
                break;

            case R.id.info_MenuItem:
                Intent intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFavoritesTab(int i) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(i == 1) {
            editor.putBoolean("favorites_tab_default", true).apply();
        }
        else{
            editor.putBoolean("favorites_tab_default", false).apply();
        }
    }
}