package com.rayadev.byoc.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.rayadev.byoc.util.ConverterUtil;

public class MyTextWatcherUtils {

    private EditText viewA, viewB;
    private ConverterUtil converter;
    private int userSelection;
    private double baseCurrency;
    private double currencyValue;

    private TextWatcher mTextWatcher;

    public MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, ConverterUtil converter) {

        this.userSelection = userSelection;
        this.converter = converter;

        this.viewA = viewA;
        this.viewB = viewB;

    }


    public MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, double baseCurrency, double currencyValue) {

        this.userSelection = userSelection;
        this.viewA = viewA;
        this.viewB = viewB;
        this.baseCurrency = baseCurrency;
        this.currencyValue = currencyValue;

    }

    public void setUnitEditTextWatcher(EditText userInputEditText) {


        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                switch(userSelection) {
                    case 1:
                        if(viewA.isFocused()) {
                            runConversionAB(converter);
                        }

                        break;
                    case 2:
                        if(viewB.isFocused()) {
                            runConversionBA(converter);
                        }
                        break;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        userInputEditText.addTextChangedListener(mTextWatcher);
    }

    public void removeTextWatcher(EditText userInputEditText) {
        userInputEditText.removeTextChangedListener(mTextWatcher);
    }

    private void runConversionAB(ConverterUtil converter) {

        String editTextAInputString = String.valueOf(viewA.getText());

        //Currency provides a null converter
        if(converter == null) {

            if (!editTextAInputString.equals("") && !editTextAInputString.equals(".")) {
                double input = Double.parseDouble(viewA.getText().toString());

                double result = input * currencyValue;
                viewB.setText(String.valueOf(result));
                Log.i("CTAG", "AB Ran");
            } else {
                viewB.setText("");

            }

        }

        else {


            if (!editTextAInputString.equals("") && !editTextAInputString.equals(".")) {
                double input = Double.parseDouble(viewA.getText().toString());
                double result = 0.0;

                if(converter instanceof CustomConverterUtil) {

                    double unitAValue = ((CustomConverterUtil) converter).getUnitAValue();
                    double unitBValue = ((CustomConverterUtil) converter).getUnitBValue();

                    result = ((CustomConverterUtil) converter).convertCustomUnits(unitAValue, unitBValue);

                }
                else {
                    result = converter.convert(input);

                }
                viewB.setText(String.valueOf(result));
            }


            else {
                viewB.setText("");

            }
        }

    }

    private void runConversionBA(ConverterUtil converter) {

        String editTextBInputString = String.valueOf(viewB.getText());

        //Currency provides a null converter
        if(converter == null) {
            if (!editTextBInputString.equals("") && !editTextBInputString.equals(".")) {
                double input = Double.parseDouble(viewB.getText().toString());

                double result = input * (1/currencyValue);
                viewA.setText(String.valueOf(result));
                Log.i("CTAG", "BA Ran");
            } else {
                viewA.setText("");

            }

        }

        else {
            if (!editTextBInputString.equals("") && !editTextBInputString.equals(".")) {
                double input = Double.parseDouble(viewB.getText().toString());
                double result = 0.0;

                if(converter instanceof CustomConverterUtil) {

                    double unitAValue = ((CustomConverterUtil) converter).getUnitAValue();
                    double unitBValue = ((CustomConverterUtil) converter).getUnitBValue();

                    result = ((CustomConverterUtil) converter).convertCustomUnits(unitBValue, unitAValue);

                }
                else {
                    result = converter.convert(input);

                }
                viewA.setText(String.valueOf(result));
            } else {
                viewA.setText("");


            }
        }

    }


}
