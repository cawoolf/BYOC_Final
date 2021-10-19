package com.rayadev.byoc.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.rayadev.byoc.CustomConverterActivity;
import com.rayadev.byoc.R;
import com.rayadev.byoc.model.Converter;
import com.rayadev.byoc.util.ConverterUtil;
import com.rayadev.byoc.model.ConverterViewModel;
import com.rayadev.byoc.util.MyTextWatcherUtils;


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



    //Globals for Converter
    private ConverterUtil.Unit fromUnit, toUnit;
    private String unitAString, unitBString;
    private String unitCategory;
    private int mSwapUnits = 0;
    private boolean mFreshFragment = true;

    public ConverterTabFragment() {
        //Sets the initial spinner category to distance.
        this.spinnerID = R.layout.spinner_scrollview_distance;

    }

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

                if(unitAString != null && unitBString != null) {
                    switch (mSwapUnits) {

                        case 0:
                            Converter converterAB = new Converter(unitCategory, unitAString, unitBString);
                            mConverterViewModel.insert(converterAB);
                            Toast.makeText(getContext(), unitAString + " : " + unitBString + " --> Favorites", Toast.LENGTH_SHORT).show();
                            break;

                        case 1:
                            Converter converterBA = new Converter(unitCategory, unitBString, unitAString);
                            mConverterViewModel.insert(converterBA);
                            Toast.makeText(getContext(), unitBString + " : " + unitAString + " --> Favorites", Toast.LENGTH_SHORT).show();
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

                if (unitAString != null && unitBString != null) {
                    switch (mSwapUnits) {
                        case 0:
                            setConverterBoxLogic(toUnit, fromUnit);
                            setConverterBoxTitles(unitBString, unitAString);
                            mSwapUnits = 1;
                            break;

                        case 1:
                            setConverterBoxLogic(fromUnit, toUnit);
                            setConverterBoxTitles(unitAString, unitBString);
                            mSwapUnits = 0;
                            break;
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Select Units Before Swapping", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buildSpinner(View view) {

        String[] units = {getString(R.string.spinner_distance_title), getString(R.string.spinner_area_title), getString(R.string.spinner_time_title), getString(R.string.spinner_volume_title), getString(R.string.spinner_weight_title), getString(R.string.spinner_currency_title)};

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selected = parentView.getItemAtPosition(position).toString();
                Context context = parentView.getContext();
                CharSequence text = selected;
                int duration = Toast.LENGTH_SHORT;

                if (text.equals(getString(R.string.spinner_distance_title))) {
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_distance);
                    unitCategory = (String) text;

                    //This should be called after the user selects units off the spinner.
//                    setUpTargetConverter();
                }

                if (text.equals(getString(R.string.spinner_area_title))) {
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_area);
                    unitCategory = (String) text;
                }

                if (text.equals(getString(R.string.spinner_currency_title))) {
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_currency);
                    unitCategory = (String) text;

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

            @Override
            public void setUnitNames(String converterUnitAName, String converterUnitBName) {
                fromUnit = ConverterUtil.Unit.fromString(converterUnitAName);
                toUnit = ConverterUtil.Unit.fromString(converterUnitBName);

                setConverterBoxTitles(converterUnitAName, converterUnitBName);

                unitAString = converterUnitAName;
                unitBString = converterUnitBName;

                setConverterBoxLogic(fromUnit, toUnit);
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

        Toast.makeText(getContext(), "Select Units from drop down above.", Toast.LENGTH_LONG).show();


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

}
