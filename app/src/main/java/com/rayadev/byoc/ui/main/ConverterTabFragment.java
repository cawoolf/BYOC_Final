package com.rayadev.byoc.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
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

import com.rayadev.byoc.R;
import com.rayadev.byoc.model.Converter;

import java.util.List;


//The main fragment that allows the user to run conversions, and set up a converter to be saved to the HomeSetTab
public class ConverterTabFragment extends Fragment {


    private int spinnerID;


    //Views for the converter UI
    private TextView mUnitATitleTextView, mUnitBTitleTextView;
    private EditText mUnitAInputEditText, mUnitBInputEditText;
    private ImageButton mConverterInfoButton, mConverterSwapButton;

    //Bottom UI
    private ImageView mBuildConverterButton, mAddConverterButton;
    private LinearLayout mBottomUI;

    public ConverterTabFragment() {
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
        View converterUILayout = view.findViewById( R.id.converter_cardlayout_include_converter_tab ); // root View id from include

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

                Toast.makeText(getContext(), "Add clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buildSpinner(View view) {

        String[] units = {getString(R.string.spinner_distance_title), getString(R.string.spinner_area_title), getString(R.string.spinner_time_title), getString(R.string.spinner_volume_title), getString(R.string.spinner_weight_title)};

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
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

                if(text.equals(getString(R.string.spinner_distance_title))){
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_distance);

                    //This should be called after the user selects units off the spinner.
//                    setUpTargetConverter();

                }

                if(text.equals(getString(R.string.spinner_area_title))){
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    setSpinnerScrollViewFragment(R.layout.spinner_scrollview_area);
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
                Toast.makeText(getContext(), converterName, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void setUnitNames(String converterUnitAName, String converterUnitBName) {
                    Converter.Unit fromUnit = Converter.Unit.fromString(converterUnitAName);
                    Converter.Unit toUnit = Converter.Unit.fromString(converterUnitBName);

                    setConverterBoxTitles(converterUnitAName, converterUnitBName);

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


    private void setConverterBoxLogic(Converter.Unit fromUnit, Converter.Unit toUnit) {


        Converter fromUnit_toUnit = new Converter(fromUnit, toUnit);
        Converter toUnit_fromUnit = new Converter(toUnit, fromUnit);

        final MyTextWatcherUtils[] myTextWatcherUtils = new MyTextWatcherUtils[2];

        myTextWatcherUtils[0] = new MyTextWatcherUtils(1, mUnitAInputEditText, mUnitBInputEditText, fromUnit_toUnit);
        myTextWatcherUtils[1] = new MyTextWatcherUtils(2,  mUnitAInputEditText, mUnitBInputEditText, toUnit_fromUnit);

        mUnitAInputEditText.clearFocus();
        mUnitBInputEditText.clearFocus();

        clearUserInput();

        myTextWatcherUtils[0].setUnitEditTextWatcher(mUnitAInputEditText);
        myTextWatcherUtils[1].setUnitEditTextWatcher(mUnitBInputEditText);

        keyboardManager();

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

    private void clearUserInput() {

        if (mUnitAInputEditText.getText() != null) {
            mUnitAInputEditText.getText().clear();
        }

        if (mUnitBInputEditText.getText() != null) {
            mUnitBInputEditText.getText().clear();
        }

    }

    private void keyboardManager() {

        mUnitAInputEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mUnitAInputEditText, InputMethodManager.SHOW_IMPLICIT);
    }

}











//Other attemps at Threads and Async

//    //Need methods to set the data inside the converter_master_cardview
//    //Use LiveData for the List<Converter> with an observer. See HomeSetTab

//
//        mConverterViewModel.getTargetConverter(getConverterName()).observe(getViewLifecycleOwner(), new Observer<List<Converter>>() {
//            @Override
//            public void onChanged(List<Converter> converters) {
//               setConverterBoxData();
//
//            }
//        });



//    private static class getTargetConverterAsyncTask extends AsyncTask<String, Void, LiveData<List<Converter>>> {
//
//        private ConverterViewModel mConverterViewModel;
//
//        public getTargetConverterAsyncTask(ConverterViewModel converterViewModel){
//            this.mConverterViewModel = converterViewModel;
//        }
//        @Override
//        protected LiveData<List<Converter>> doInBackground(String... strings) {
//            LiveData<List<Converter>> converters = mConverterViewModel.getTargetConverter(strings[0]);
//            return converters;
//        }
//
//        @Override
//        protected void onPostExecute(LiveData<List<Converter>> converters) {
//            super.onPostExecute(converters);
//            Log.i("TAG", converters.getValue()+"success");
//
//        }
//    }
//
//    private class GetTargetConverterThread implements Runnable {
//
//        private String converterName;
//        private ConverterViewModel mConverterViewModel;
//
//        private GetTargetConverterThread(ConverterViewModel converterViewModel) {
//            this.mConverterViewModel = converterViewModel;
//        }
//
//        public GetTargetConverterThread(String converterName, ConverterViewModel converterViewModel) {
//            this.converterName = converterName;
//        }
//
//        @Override
//        public void run() {
//            mConverterViewModel.getTargetConverter(getConverterName()).observe(getViewLifecycleOwner(), new Observer<List<Converter>>() {
//                @Override
//                public void onChanged(List<Converter> converters) {
//                    //This is where you would update the Converter UI with the data.
//                    setConverterBoxData();
//                }
//            });
//        }
//
//    }