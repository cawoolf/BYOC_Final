package com.rayadev.byoc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.rayadev.byoc.model.Converter;
import com.rayadev.byoc.model.ConverterViewModel;
import com.rayadev.byoc.util.CustomConverterUtil;
import com.rayadev.byoc.util.KeyboardUtils;
import com.rayadev.byoc.util.MyTextWatcherUtils;

/*Activity that gets triggered by the wrench on the ConverterTab

Allows the user to build their own converter, and save it to the HomeTab.
 */

public class CustomConverterActivity extends AppCompatActivity {

    //Master custom layout
    private LinearLayout mMasterCustomLayout;

    //EditTexts for user input
    private EditText mUnitANameInput, mUnitAValueInput, mUnitBNameInput, mUnitBValueInput;

    //Views for the converter UI
    private View mConverterUI;
    private TextView mUnitATitleTextView, mUnitBTitleTextView, mUnitAQuestionTextView, mUnitBQuestionTextView, mValueUnitAQuestionTextView;
    private EditText mUnitAInputEditText, mUnitBInputEditText;
    private ImageButton mConverterInfoButton, mConverterSwapButton;
    private ImageView mSwapInputNamesButton, mClearInputFieldsButton;
    private int mSwapUnits = 0;
    private boolean mSwapClicked = false;

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

    private ConverterViewModel mConverterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_converter);

        setUpToolbar();
        linkViews();
        setOnClicks();
        setChangeListeners();
        keyboardManager();
        suppressKeyBoard();

        Toast.makeText(this, "1) Input Values For Units" + "\n" + "2) Click Build!", Toast.LENGTH_LONG).show();
        mUnitATitleTextView.setText("Enter Unit Name Above");
        mUnitBTitleTextView.setText("Enter Unit Name Above");

        mConverterViewModel = new ViewModelProvider(this).get(ConverterViewModel.class);
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
        mUnitANameInput = findViewById(R.id.custom_converter_unitAName_EditText);
        mUnitAValueInput = findViewById(R.id.custom_converter_unitAValue_EditText);
        mUnitBNameInput = findViewById(R.id.custom_converter_unitBName_EditText);
        mUnitBValueInput = findViewById(R.id.custom_converter_unitBValue_EditText);
        mUnitAQuestionTextView = findViewById(R.id.custom_converter_unitAQuestion_TextView);
        mUnitBQuestionTextView = findViewById(R.id.custom_converter_unitBQuestion_TextView);
        mValueUnitAQuestionTextView = findViewById(R.id.custom_converter_valueAQuestion_TextView);

        //Bottom UI
        mBottomUI = findViewById(R.id.custom_convertertab_BottomUI_LinearLayout);
        mAddConverterButton = findViewById(R.id.custom_add_converter_button);

        //Build Buttons
        mBuildButton = findViewById(R.id.custom_converter_buildButton_LinearLayout);
        mSwapInputNamesButton = findViewById(R.id.custom_converter_swapInputNames_button);
        mClearInputFieldsButton = findViewById(R.id.custom_converter_clearAllInputs_Button);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void setOnClicks() {
        mConverterSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSwapClicked = true;
                if(allConverterDataValid()) {
                    switch (mSwapUnits) {
                        case 0:
                            mSwapUnits = 1;
                            buildConverter();

                            break;
                        case 1:
                            mSwapUnits = 0;
                            buildConverter();
                            break;

                    }

                }

            }
        });

        mAddConverterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid = allConverterDataValid();
                //Example add
                //Get data from edit texts.
                if(valid) {

                    Converter converter = new Converter("Custom", mUnitAConverterName, mUnitBConverterName, mUnitAConverterValue, mUnitBConverterValue);
                    mConverterViewModel.insert(converter);

                Toast.makeText(CustomConverterActivity.this, mUnitAConverterName + " : " + mUnitBConverterName + "--> Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mClearInputFieldsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUnitANameInput.setText("");
                mUnitBNameInput.setText("");
                mUnitAValueInput.setText("1");
                mUnitBValueInput.setText("");

                //Resets the text.
                mUnitAQuestionTextView.setText("Unit A");
                mUnitBQuestionTextView.setText("Unit B's");
                mValueUnitAQuestionTextView.setText(mUnitAValueInput.getText().toString());

//                Toast.makeText(CustomConverterActivity.this, "Inputs Cleared", Toast.LENGTH_SHORT).show();

            }
        });

        mSwapInputNamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitANameHolder = mUnitANameInput.getText().toString();
                String unitBNameHolder = mUnitBNameInput.getText().toString();

                mUnitANameInput.setText(unitBNameHolder);
                mUnitBNameInput.setText(unitANameHolder);

                mUnitAQuestionTextView.setText(unitBNameHolder);
                mUnitBQuestionTextView.setText(unitANameHolder);

                //Clears and rests values as well.
                mUnitAValueInput.setText("1");
                mUnitBValueInput.setText("");
                mValueUnitAQuestionTextView.setText(mUnitAValueInput.getText().toString());




            }
        });
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
        mUnitANameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {

                    if(firstAClick[0] == 0) {
                        mUnitANameInput.setText("");
                        firstAClick[0] = 1;
                    }
                    mBottomUI.setVisibility(View.INVISIBLE);
                    mConverterUI.setVisibility(View.INVISIBLE);

                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(6);
                    mUnitANameInput.setFilters(filterArray);
                }

                else {
                    mBottomUI.setVisibility(View.VISIBLE);
                    mConverterUI.setVisibility(View.VISIBLE);
                    mUnitAQuestionTextView.setText((mUnitANameInput.getText().toString()));
                }
            }
        });

        final int[] firstBClick = {0};
        mUnitBNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if(firstBClick[0] == 0) {
                        mUnitBNameInput.setText("");
                        firstBClick[0] = 1;
                    }
                    mBottomUI.setVisibility(View.INVISIBLE);
                    mConverterUI.setVisibility(View.INVISIBLE);

                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(6);
                    mUnitBNameInput.setFilters(filterArray);
                }

                else {
                    mBottomUI.setVisibility(View.VISIBLE);
                    mConverterUI.setVisibility(View.VISIBLE);
                    mUnitBQuestionTextView.setText((mUnitBNameInput.getText().toString()));
                }
            }
        });

        mUnitAValueInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mUnitAValueInput.setText("1");
                    mBottomUI.setVisibility(View.INVISIBLE);
                    mConverterUI.setVisibility(View.INVISIBLE);
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(8);
                    mUnitAValueInput.setFilters(filterArray);
                }

                else {
                    mBottomUI.setVisibility(View.VISIBLE);
                    mConverterUI.setVisibility(View.VISIBLE);
                    mValueUnitAQuestionTextView.setText(mUnitAValueInput.getText().toString());
                }
            }
        });

        mUnitBValueInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
//                    mUnitBValue.setText("");
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
        mUnitBValueInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) ||(actionId == EditorInfo.IME_ACTION_NEXT)) {


//                    //mMasterCustomLayout.setVisibility(View.VISIBLE);
                    mUnitAInputEditText.clearFocus();
                    mUnitBInputEditText.clearFocus();

//                    //Needed so that the main UI returns when converter UI loses focus.
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

//                    if(allConverterDataValid()) {
//                        buildConverter();
//                    }

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
        boolean a = mUnitANameInput.getText().toString().equals("Unit Name A");
        boolean a_b = mUnitANameInput.getText().toString().equals("");
        boolean av = mUnitAValueInput.getText().toString().equals("");
        boolean b = mUnitBNameInput.getText().toString().equals("Unit Name B");
        boolean b_b = mUnitBNameInput.getText().toString().equals("");
        boolean bv = mUnitBValueInput.getText().toString().equals("");

        Log.i("CTAG",mUnitANameInput.getText().toString() + "" + a);

        if(a || a_b || av || b || b_b || bv) {
            Toast.makeText(CustomConverterActivity.this, "Input all fields for the Converter.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }

    }

    private void buildConverter() {

        enableKeyboard();

        try {

            if(mSwapUnits == 0) {
                mUnitAConverterName = mUnitANameInput.getText().toString();
                mUnitBConverterName = mUnitBNameInput.getText().toString();
                mUnitAConverterValue = Double.parseDouble(mUnitAValueInput.getText().toString());
                mUnitBConverterValue = Double.parseDouble(mUnitBValueInput.getText().toString());

            }
            else if (mSwapUnits == 1) {
                mUnitAConverterName = mUnitBNameInput.getText().toString();
                mUnitBConverterName = mUnitANameInput.getText().toString();
                mUnitAConverterValue = Double.parseDouble(mUnitBValueInput.getText().toString());
                mUnitBConverterValue = Double.parseDouble(mUnitAValueInput.getText().toString());
            }

            Log.i("CTAG", mUnitAConverterValue + "");
            mUnitATitleTextView.setText(mUnitAConverterName);
            mUnitBTitleTextView.setText(mUnitBConverterName);

            mCustomConverter = new Converter(mUnitAConverterName, mUnitBConverterName, mUnitAConverterValue, mUnitBConverterValue);
            setConverterBoxLogic();

            //If swapped was not clicked.. Vibrate
            if(!mSwapClicked) {
                // this effect creates the vibration of default amplitude for 1000ms(1 sec)
                mVibrationEffect = VibrationEffect.createOneShot(75, 75);
                // it is safe to cancel other vibrations currently taking place
                mVibrator.cancel();
                mVibrator.vibrate(mVibrationEffect);

                //Resets so that vibrate will occur on next buildConverter()
                mSwapClicked = false;

                //Toast.makeText(this, "Converter Built..", Toast.LENGTH_SHORT).show();
            }
            else {
                mSwapClicked = false;
            }


        }


        catch(Exception e) {
            Toast.makeText(this, "Input Values for Units", Toast.LENGTH_SHORT).show();

        }

    }

    private void keyboardManager() {

        //Keyboard opens when Converter Icon is clicked
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

//        mUnitAInputEditText.clearFocus();
//        mUnitBInputEditText.clearFocus();
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