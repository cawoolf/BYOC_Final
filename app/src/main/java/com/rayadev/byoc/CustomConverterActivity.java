package com.rayadev.byoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.rayadev.byoc.R;
import com.rayadev.byoc.model.Converter;
import com.rayadev.byoc.util.ConverterUtil;
import com.rayadev.byoc.util.CustomConverterUtil;
import com.rayadev.byoc.util.KeyboardUtils;
import com.rayadev.byoc.util.MyTextWatcherUtils;

import org.jetbrains.annotations.NotNull;

/*Activity that gets triggered by the wrench on the ConverterTab

Allows the user to build their own converter, and save it to the HomeTab.
 */

public class CustomConverterActivity extends AppCompatActivity {

    //Master custom layout
    private LinearLayout mMasterCustomLayout;

    //EditTexts for user input
    private EditText mUnitAName, mUnitAValue, mUnitBName, mUnitBValue;

    //Views for the converter UI
    private View mConverterUI;
    private TextView mUnitATitleTextView, mUnitBTitleTextView, mUnitAQuestionTextView, mUnitBQuestionTextView, mValueUnitAQuestionTextView;
    private EditText mUnitAInputEditText, mUnitBInputEditText;
    private ImageButton mConverterInfoButton, mConverterSwapButton;

    private LinearLayout mBuildButton;

    //Bottom UI
    private ImageView mAddConverterButton;
    private LinearLayout mBottomUI;

    //Custom Converter object
    private Converter mCustomConverter;
    private String mUnitAConverterName, mUnitBConverterName;
    private double mUnitAConverterValue, mUnitBConverterValue;

    private Vibrator mVibrator;
    private VibrationEffect mVibrationEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_converter);

        setUpToolbar();
        linkViews();
        setChangeListeners();
        keyboardManager();
        suppressKeyBoard();

        Toast.makeText(this, "1) Input Values For Units" + "\n" + "2) Click Build!", Toast.LENGTH_LONG).show();
    }

    private void setUpToolbar() {

        //Links the toolbar over the app Actionbar
        Toolbar toolbar = findViewById(R.id.custom_converter_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Converter Workshop");
    }

    private void linkViews() {

        mMasterCustomLayout = findViewById(R.id.custom_converter_master_linear);

        //Link ConverterBox Views
        mConverterUI = findViewById( R.id.custom_converter_cardlayout_include_converter_tab ); // root View id from include

        mUnitATitleTextView = mConverterUI.findViewById(R.id.cardView_UnitATitle_TextView);
        mUnitBTitleTextView = mConverterUI.findViewById(R.id.cardView_UnitBTitle_TextView);

        mUnitAInputEditText = mConverterUI.findViewById(R.id.cardView_UnitAInput_EditText);
        mUnitBInputEditText = mConverterUI.findViewById(R.id.cardView_UnitBInput_EditText);

        mConverterInfoButton = mConverterUI.findViewById(R.id.cardView_InfoButton);
        mConverterSwapButton = mConverterUI.findViewById(R.id.cardView_SwapButton);

        //User inputs
        mUnitAName = findViewById(R.id.custom_converter_unitAName_EditText);
        mUnitAValue = findViewById(R.id.custom_converter_unitAValue_EditText);
        mUnitBName = findViewById(R.id.custom_converter_unitBName_EditText);
        mUnitBValue = findViewById(R.id.custom_converter_unitBValue_EditText);
        mUnitAQuestionTextView = findViewById(R.id.custom_converter_unitAQuestion_TextView);
        mUnitBQuestionTextView = findViewById(R.id.custom_converter_unitBQuestion_TextView);
        mValueUnitAQuestionTextView = findViewById(R.id.custom_converter_valueAQuestion_TextView);

        //Bottom UI
        mBottomUI = findViewById(R.id.custom_convertertab_BottomUI_LinearLayout);
        mAddConverterButton = findViewById(R.id.custom_add_converter_button);

        //Build Button click
        mBuildButton = findViewById(R.id.custom_converter_buildButton_LinearLayout);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setChangeListeners() {


        mBuildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(allConverterDataValid()){
                    hideKeyboard(CustomConverterActivity.this);
                    buildConverter();
                }

            }
        });

        final int[] firstAClick = {0};
        mUnitAName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {

                    if(firstAClick[0] == 0) {
                        mUnitAName.setText("");
                        firstAClick[0] = 1;
                    }
                    mBottomUI.setVisibility(View.INVISIBLE);
                    mConverterUI.setVisibility(View.INVISIBLE);

                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(6);
                    mUnitAName.setFilters(filterArray);
                }

                else {
                    mBottomUI.setVisibility(View.VISIBLE);
                    mConverterUI.setVisibility(View.VISIBLE);
                    mUnitAQuestionTextView.setText((mUnitAName.getText().toString()));
                }
            }
        });

        final int[] firstBClick = {0};
        mUnitBName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if(firstBClick[0] == 0) {
                        mUnitBName.setText("");
                        firstBClick[0] = 1;
                    }
                    mBottomUI.setVisibility(View.INVISIBLE);
                    mConverterUI.setVisibility(View.INVISIBLE);

                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(6);
                    mUnitBName.setFilters(filterArray);
                }

                else {
                    mBottomUI.setVisibility(View.VISIBLE);
                    mConverterUI.setVisibility(View.VISIBLE);
                    mUnitBQuestionTextView.setText((mUnitBName.getText().toString()));
                }
            }
        });

        mUnitAValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mUnitAValue.setText("1");
                    mBottomUI.setVisibility(View.INVISIBLE);
                    mConverterUI.setVisibility(View.INVISIBLE);
                }

                else {
                    mBottomUI.setVisibility(View.VISIBLE);
                    mConverterUI.setVisibility(View.VISIBLE);
                    mValueUnitAQuestionTextView.setText(mUnitAValue.getText().toString());
                }
            }
        });

        mUnitBValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mUnitBValue.setText("");
                    mBottomUI.setVisibility(View.INVISIBLE);
                    mConverterUI.setVisibility(View.INVISIBLE);
                }

                else {
                    mBottomUI.setVisibility(View.VISIBLE);
                    mConverterUI.setVisibility(View.VISIBLE);
                }
            }
        });


        //Last field to input, and runs build Converter on Done click.
        mUnitBValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) ||(actionId == EditorInfo.IME_ACTION_NEXT)) {


//                    //mMasterCustomLayout.setVisibility(View.VISIBLE);
                    mUnitAInputEditText.clearFocus();
                    mUnitBInputEditText.clearFocus();

//                    //Needed so that the main UI returns when converter UI loses focus.
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if(allConverterDataValid()) {
                        buildConverter();
                    }

                }
                return false;
            }
        });


       //Closes keyboard, and clears focus on DONE click for converter UI
        mUnitAInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    mMasterCustomLayout.setVisibility(View.VISIBLE);
                    mUnitAInputEditText.clearFocus();
                    mUnitBInputEditText.clearFocus();

                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    clearUserInput();
                }
                return false;
            }
        });

        //Closes keyboard, and clears focus on DONE click for converter UI
        mUnitBInputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                   mMasterCustomLayout.setVisibility(View.VISIBLE);
                    mUnitAInputEditText.clearFocus();
                    mUnitBInputEditText.clearFocus();

                    //Needed so that the main UI returns when converter UI loses focus.
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    clearUserInput();

                }
                return false;
            }
        });


    }

    private boolean allConverterDataValid(){

        //Checks if the user has input all data for the Converter
        boolean a = mUnitAName.getText().toString().equals("Unit A Name");
        boolean av = mUnitAValue.getText().toString().equals("");
        boolean b = mUnitBName.getText().toString().equals("Unit B Name");
        boolean bv = mUnitBValue.getText().toString().equals("");

        Log.i("CTAG",mUnitAName.getText().toString() + "" );

        if(a || av || b || bv) {
            Toast.makeText(CustomConverterActivity.this, "Input all fields for the Converter.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }

    }

    private void buildConverter() {

        enableKeyboard();

        mUnitATitleTextView.setText(mUnitAName.getText().toString());
        mUnitBTitleTextView.setText(mUnitBName.getText().toString());

        mUnitAConverterName = mUnitAInputEditText.getText().toString();
        mUnitBConverterName = mUnitBInputEditText.getText().toString();

        try {
            mUnitAConverterValue = Double.parseDouble(mUnitAValue.getText().toString());
            mUnitBConverterValue = Double.parseDouble(mUnitBValue.getText().toString());
            mCustomConverter = new Converter(mUnitAConverterName, mUnitBConverterName, mUnitAConverterValue, mUnitBConverterValue);
            setConverterBoxLogic();

            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            mVibrationEffect = VibrationEffect.createOneShot(75,75);

            // it is safe to cancel other vibrations currently taking place
            mVibrator.cancel();
            mVibrator.vibrate(mVibrationEffect);
        }
        catch (Exception e) {
            Toast.makeText(this, "Input Values for Units", Toast.LENGTH_SHORT).show();
        }



    }

    private void keyboardManager() {

        //Keyboard opens when Converter Icon is clicked
//        mUnitAInputEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mUnitAInputEditText, InputMethodManager.SHOW_IMPLICIT);

        //When keyboard is closed, Shows the converter UI
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {

            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {

                if (isVisible && !mUnitAInputEditText.hasFocus() && !mUnitBInputEditText.hasFocus()) {
                    mConverterUI.setVisibility(View.INVISIBLE);
                    mBottomUI.setVisibility(View.INVISIBLE);
                } else {
                    mConverterUI.setVisibility(View.VISIBLE);
                    mBottomUI.setVisibility(View.VISIBLE);

                    if(mUnitAInputEditText.hasFocus() || mUnitBInputEditText.hasFocus() && isVisible) {
                        mMasterCustomLayout.setVisibility(View.GONE);
                    }
                    else{
                        mMasterCustomLayout.setVisibility(View.VISIBLE);

                    }
                }

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

    //Here if unitCategory equals currency, need to use a different set of constructors.
    private void setConverterBoxLogic() {

        mUnitAInputEditText.clearFocus();
        mUnitBInputEditText.clearFocus();
        clearUserInput();


        //Theres definitely a more simple way to go about this, but I'm just solving the issue
        //Using objects instead of algorithms.. Just use lots of objects haha Probably not that efficient at big scales.
//        ConverterUtil fromUnit_toUnit = new ConverterUtil(fromUnit, toUnit);
//        ConverterUtil toUnit_fromUnit = new ConverterUtil(toUnit, fromUnit);

        CustomConverterUtil fromUnit_toUnit = new CustomConverterUtil(mCustomConverter.getUnitAValue(), mCustomConverter.getUnitBValue());
        CustomConverterUtil toUnit_fromUnit = new CustomConverterUtil(mCustomConverter.getUnitAValue(), mCustomConverter.getUnitBValue());

        final MyTextWatcherUtils[] myTextWatcherUtils = new MyTextWatcherUtils[2];

        myTextWatcherUtils[0] = new MyTextWatcherUtils(1, mUnitAInputEditText, mUnitBInputEditText, fromUnit_toUnit);
        myTextWatcherUtils[1] = new MyTextWatcherUtils(2, mUnitAInputEditText, mUnitBInputEditText, toUnit_fromUnit);

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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void suppressKeyBoard() {
        mUnitAInputEditText.setShowSoftInputOnFocus(false);
        mUnitBInputEditText.setShowSoftInputOnFocus(false);

//        Toast.makeText(getContext(), "Select Units from drop down above.", Toast.LENGTH_LONG).show();


        mUnitAInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomConverterActivity.this, "Input all fields for the Converter.", Toast.LENGTH_SHORT).show();
            }
        });

        mUnitBInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomConverterActivity.this, "Input all fields for the Converter.", Toast.LENGTH_SHORT).show();
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