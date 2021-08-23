package com.rayadev.byoc.ui.main;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.rayadev.byoc.util.ConverterUtil;

class MyTextWatcherUtils {

    private EditText viewA, viewB;
    private ConverterUtil converter;
    private int userSelection;
    private double baseCurrency;
    private double currencyValue;

    private TextWatcher mTextWatcher;

    MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, ConverterUtil converter) {

        this.userSelection = userSelection;
        this.converter = converter;

        this.viewA = viewA;
        this.viewB = viewB;

    }


    MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, double baseCurrency, double currencyValue) {

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

        if(converter == null) {

            if (!editTextAInputString.equals("")) {
                double input = Double.parseDouble(viewA.getText().toString());

                double result = input * currencyValue;
                viewB.setText(String.valueOf(result));
            } else {
                viewB.setText("");

            }

        }

        else {


            if (!editTextAInputString.equals("")) {
                double input = Double.parseDouble(viewA.getText().toString());

                double result = converter.convert(input);
                viewB.setText(String.valueOf(result));
            } else {
                viewB.setText("");

            }
        }

    }

    private void runConversionBA(ConverterUtil converter) {

        //We are working with a currency now.
        String editTextBInputString = String.valueOf(viewB.getText());

        if(converter == null) {
            if (!editTextBInputString.equals("")) {
                double input = Double.parseDouble(viewB.getText().toString());

                double result = input * (baseCurrency/currencyValue);
                viewA.setText(String.valueOf(result));
            } else {
                viewA.setText("");

            }

        }

        else {
            if (!editTextBInputString.equals("")) {
                double input = Double.parseDouble(viewB.getText().toString());

                double result = converter.convert(input);
                viewA.setText(String.valueOf(result));
            } else {
                viewA.setText("");


            }
        }

    }


}
