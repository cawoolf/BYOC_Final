package com.rayadev.byoc.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rayadev.byoc.CustomConverterActivity;
import com.rayadev.byoc.R;
import com.rayadev.byoc.model.Converter;
import com.rayadev.byoc.model.ConverterViewModel;
import com.rayadev.byoc.model.Currency;
import com.rayadev.byoc.util.ConverterUtil;
import com.rayadev.byoc.util.MyTextWatcherUtils;

import org.jetbrains.annotations.NotNull;


//The main fragment that allows the user to run conversions, and set up a converter to be saved to the HomeSetTab
public class ConverterTabFragment extends Fragment {


    private int spinnerID;
    private ConverterViewModel mConverterViewModel;

    //Views for the converter UI
    private TextView mUnitATitleTextView, mUnitBTitleTextView;
    private EditText mUnitAInputEditText, mUnitBInputEditText;
    private ImageButton mConverterInfoButton, mConverterSwapButton;

    //Bottom UI
    private ImageView mBuildConverterButton, mAddConverterButton;
    private LinearLayout mBottomUI;

    private SpinnerCategorySelection mSpinnerCategorySelection;

    //Globals for Converter
    private ConverterUtil.Unit fromUnit, toUnit;
    private String mUnitAString, mUnitBString;
    private String mUnitCategory;
    private int mSwapUnits = 0;
    private boolean mFreshFragment = true;

    public ConverterTabFragment() {
        //Sets the initial spinner category to distance.
//        this.spinnerID = R.layout.spinner_scrollview_distance;

    }

    @NotNull
    public static ConverterTabFragment newInstance() {
        ConverterTabFragment fragment = new ConverterTabFragment();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        //Ensures that we are creating an entirely fresh fragment.
        //savedInstanceState was causing bugs with the Spinner.
        //In the future can modify this for a better UX.

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_converter_tab, container, false);

        linkViews(view);
        setBottomUIOnClicks();
        buildSpinner(view);

        //SetUp View Model
        mConverterViewModel = new ViewModelProvider(this).get(ConverterViewModel.class);

        //For first create suppress keyboard until user selects units.

        if(mFreshFragment = true) {
            suppressKeyBoard();
        }

        return view;

    }

    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

//        setSpinnerScrollViewFragment(spinnerID);

    }

    private void linkViews(View view) {

        //SetUp View Model

        //Set Up Bottom UI
        mBuildConverterButton = view.findViewById(R.id.build_converter_button);
        mAddConverterButton = view.findViewById(R.id.add_converter_button);

        //Link ConverterBox Views
        View converterUILayout = view.findViewById(R.id.converter_cardlayout_include_converter_tab); // root View id from include

        mUnitATitleTextView = converterUILayout.findViewById(R.id.cardView_UnitATitle_TextView);
        mUnitBTitleTextView = converterUILayout.findViewById(R.id.cardView_UnitBTitle_TextView);

        mUnitAInputEditText = converterUILayout.findViewById(R.id.cardView_UnitAInput_EditText);
        mUnitBInputEditText = converterUILayout.findViewById(R.id.cardView_UnitBInput_EditText);

        mConverterInfoButton = converterUILayout.findViewById(R.id.cardView_InfoButton);
        mConverterSwapButton = converterUILayout.findViewById(R.id.cardView_SwapButton);

    }

    private void setBottomUIOnClicks() {
        mBuildConverterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CustomConverterActivity.class);
                startActivity(intent);
            }
        });

        mAddConverterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mUnitAString != null && mUnitBString != null) {
                    switch (mSwapUnits) {

                        case 0:
                            Converter converterAB = new Converter(mUnitCategory, mUnitAString, mUnitBString);
                            mConverterViewModel.insert(converterAB);
                            Toast.makeText(getContext(), mUnitAString + " : " + mUnitBString + " --> Favorites", Toast.LENGTH_SHORT).show();
                            break;

                        case 1:
                            Converter converterBA = new Converter(mUnitCategory, mUnitBString, mUnitAString);
                            mConverterViewModel.insert(converterBA);
                            Toast.makeText(getContext(), mUnitBString + " : " + mUnitAString + " --> Favorites", Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
                else {
                    Toast.makeText(getActivity(), "Select Units for Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mConverterSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUnitAString != null && mUnitBString != null) {
                    switch (mSwapUnits) {

                        case 0:

                            if(mUnitCategory.equals("Currency")) {
                                String currencyPair = mUnitBString + "_" + mUnitAString;
                                setUpTargetCurrency(currencyPair, mUnitBString, mUnitAString);
                                mSwapUnits = 1;

                            }

                            else {
                                setConverterBoxLogic(toUnit, fromUnit);
                                setConverterBoxTitles(mUnitBString, mUnitAString);
                            }
                            mSwapUnits = 1;
                            break;

                        case 1:

                            if(mUnitCategory.equals("Currency")) {
                                String currencyPair = mUnitAString + "_" + mUnitBString;
                                setUpTargetCurrency(currencyPair, mUnitAString, mUnitBString);
                                mSwapUnits = 0;

                            }

                            else {
                                setConverterBoxLogic(fromUnit, toUnit);
                                setConverterBoxTitles(mUnitAString, mUnitBString);
                                mSwapUnits = 0;
                                break;
                            }

                    }
                }
                else {
                    Toast.makeText(getActivity(), "Select Units Before Swapping", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buildSpinner(View view) {

//        String[] units = {getString(R.string.spinner_distance_title), getString(R.string.spinner_area_title), getString(R.string.spinner_time_title), getString(R.string.spinner_volume_title), getString(R.string.spinner_weight_title), getString(R.string.spinner_currency_title)};

        //Alphabetized
        String[]units = {getString(R.string.spinner_area_title), getString(R.string.spinner_currency_title), getString(R.string.spinner_distance_title),getString(R.string.spinner_temperature_title), getString(R.string.spinner_time_title), getString(R.string.spinner_volume_title), getString(R.string.spinner_weight_title)};

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        //Sets the initial spinner category to distance.
        if(mFreshFragment) {
            spinner.setSelection(2);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selected = parentView.getItemAtPosition(position).toString();
                Context context = parentView.getContext();
                CharSequence text = selected;
//                int duration = Toast.LENGTH_SHORT;
                Log.i("STAG", text.toString());



                if (text.equals(getString(R.string.spinner_area_title))) {
//                    Toast toast = Toast.makeText(context, "CATEGORY: " + text, duration);
//                    toast.show();
                    mSpinnerCategorySelection.unitSpinnerCategory((String)text);
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_area);
                    mUnitCategory = (String) text;
                }

                if (text.equals(getString(R.string.spinner_currency_title))) {
//                    Toast toast = Toast.makeText(context, "CATEGORY: " + text, duration);
//                    toast.show();

                    mSpinnerCategorySelection.unitSpinnerCategory((String)text);
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_currency);
                    mUnitCategory = (String) text;

                }


                if (text.equals(getString(R.string.spinner_distance_title))) {
//                    Toast toast = Toast.makeText(context, "CATEGORY: " + text, duration);
//                    toast.show();

                    mSpinnerCategorySelection.unitSpinnerCategory((String)text);
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_distance);
                    mUnitCategory = (String) text;

                }

                if(text.equals(getString(R.string.spinner_temperature_title))) {
                    mSpinnerCategorySelection.unitSpinnerCategory((String)text);
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_temperature);
                    mUnitCategory = (String) text;

                }

                if(text.equals(getString(R.string.spinner_time_title))) {
                    mSpinnerCategorySelection.unitSpinnerCategory((String)text);
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_time);
                    mUnitCategory = (String) text;

                }

                if(text.equals("Volume")) {
                    mSpinnerCategorySelection.unitSpinnerCategory((String)text);
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_volume);
                    mUnitCategory = (String) text;

                }

                if(text.equals("Weight")) {
                    mSpinnerCategorySelection.unitSpinnerCategory((String)text);
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_weight);
                    mUnitCategory = (String) text;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Replaces the ScrollViews for unit selection based on the spinner menu choice.
    private void setSpinnerScrollViewFragment(int layoutID) {
        SpinnerScrollViewFragment mFragment = new SpinnerScrollViewFragment(layoutID, new SpinnerScrollViewFragment.UserConverterSelection() {
            @Override
            public void sendConverterName(String converterName) {
//                Toast.makeText(getContext(), converterName, Toast.LENGTH_SHORT).show();

            }

            @Override //Add converter category here so that currency function can work.
            public void setUnitNames(String unitCategory, String converterUnitAName, String converterUnitBName) {
                fromUnit = ConverterUtil.Unit.fromString(converterUnitAName);
                toUnit = ConverterUtil.Unit.fromString(converterUnitBName);

                setConverterBoxTitles(converterUnitAName, converterUnitBName);

                mUnitAString = converterUnitAName;
                mUnitBString = converterUnitBName;

                if(unitCategory.equals(getString(R.string.spinner_currency_title))) {
                    String currencyPair = converterUnitAName +"_" + converterUnitBName;
                    setUpTargetCurrency(currencyPair, converterUnitAName, converterUnitBName);

                }
                else {

                    setConverterBoxLogic(fromUnit, toUnit);
                }
            }
        });

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.spinner_frame_layout, mFragment);

        // Complete the changes added above
        ft.commit();

    }

    //Will come from the SpinnerInterface


    private void setConverterBoxTitles(String unitAText, String unitBText) {

        mUnitATitleTextView.setText(unitAText);
        mUnitBTitleTextView.setText(unitBText);

    }

    //Here if unitCategory equals currency, need to use a different set of constructors.
    private void setConverterBoxLogic(ConverterUtil.Unit fromUnit, ConverterUtil.Unit toUnit) {

        //Restores the keyboard functionality for EditTexts that was disable in onCreateView.
        enableKeyboard();

        mUnitAInputEditText.clearFocus();
        mUnitBInputEditText.clearFocus();
        clearUserInput();


        //Theres definitely a more simple way to go about this, but I'm just solving the issue
        //Using objects instead of algorithms.. Just use lots of objects haha Probably not that efficient at big scales.
        ConverterUtil fromUnit_toUnit = new ConverterUtil(fromUnit, toUnit);
        ConverterUtil toUnit_fromUnit = new ConverterUtil(toUnit, fromUnit);

        final MyTextWatcherUtils[] myTextWatcherUtils = new MyTextWatcherUtils[2];

        myTextWatcherUtils[0] = new MyTextWatcherUtils(1, mUnitAInputEditText, mUnitBInputEditText, fromUnit_toUnit);
        myTextWatcherUtils[1] = new MyTextWatcherUtils(2, mUnitAInputEditText, mUnitBInputEditText, toUnit_fromUnit);

        myTextWatcherUtils[0].setUnitEditTextWatcher(mUnitAInputEditText);
        myTextWatcherUtils[1].setUnitEditTextWatcher(mUnitBInputEditText);



        mUnitBInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearUserInput();
            }
        });

        mUnitBInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearUserInput();
            }

        });

        //Enables first time display of the UI keyboard when setting Converter.
        // Prompts the user that the keyboard can be displayed by clicking.
        if(mFreshFragment) {
            displayFocusedKeyboard();
        }
        mFreshFragment = false;

    }


    private void clearUserInput() {

        if (mUnitAInputEditText.getText() != null) {
            mUnitAInputEditText.getText().clear();
        }

        if (mUnitBInputEditText.getText() != null) {
            mUnitBInputEditText.getText().clear();
        }

    }

    private void displayFocusedKeyboard() {

        mUnitAInputEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mUnitAInputEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void suppressKeyBoard() {
        mUnitAInputEditText.setShowSoftInputOnFocus(false);
        mUnitBInputEditText.setShowSoftInputOnFocus(false);

//        Toast.makeText(getContext(), "Select Units from drop down above.", Toast.LENGTH_LONG).show();


        mUnitAInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Select Units from drop down above.", Toast.LENGTH_SHORT).show();
            }
        });

        mUnitBInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Select Units from drop down above.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableKeyboard() {

        mUnitAInputEditText.setShowSoftInputOnFocus(true);
        mUnitBInputEditText.setShowSoftInputOnFocus(true);
        mUnitAInputEditText.setOnClickListener(null);
        mUnitBInputEditText.setOnClickListener(null);


    }


    private void setUpTargetCurrency(String currencyPair, String currencyA, String currencyB) {

        //Creating multiple instances of this view model just to access the database seems not good..
        //But that's not really whats happening!.. Right?
//        ConverterViewModel mConverterViewModel = new ViewModelProvider(this).get(ConverterViewModel.class);

        LiveData<Currency> currency = mConverterViewModel.getTargetCurrency(currencyPair);

        //This observer seems to be getting triggered from the add button, and other fragments.
        Observer<Currency> observer = new Observer<Currency>() {
            @Override
            public void onChanged(Currency currency) {
                Log.i("CTAG", "Observer Success" + currency.getCurrencyPair() + ":" + currency.getCurrencyValue());
                setConverterBoxTitles(currencyA, currencyB);
                setCurrencyLogic(currency.getCurrencyValue());

            }

        };

        currency.observe(getViewLifecycleOwner(), observer);

    }

    private void setCurrencyLogic(double currencyValue) {

        enableKeyboard();
        mUnitAInputEditText.clearFocus();
        mUnitBInputEditText.clearFocus();

        clearUserInput();

        MyTextWatcherUtils utilA = new MyTextWatcherUtils(1, mUnitAInputEditText, mUnitBInputEditText, 1, currencyValue);
        MyTextWatcherUtils utilB = new MyTextWatcherUtils(2,  mUnitAInputEditText, mUnitBInputEditText, 1, currencyValue);


        utilA.setUnitEditTextWatcher(mUnitAInputEditText);
        utilB.setUnitEditTextWatcher(mUnitBInputEditText);


        mUnitBInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearUserInput();
            }
        });

        mUnitBInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                clearUserInput();
            }
        });

    }

    public interface SpinnerCategorySelection {
        void unitSpinnerCategory(String unitCategory);
    }

    // lifecycle method in Fragment to capture the host Activity interface implementation:
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SpinnerCategorySelection) {
            mSpinnerCategorySelection = (SpinnerCategorySelection) context;
        } else {
            throw new ClassCastException(context.toString());
        }
    }

}
