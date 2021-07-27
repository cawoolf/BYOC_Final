package com.rayadev.byoc.ui.main;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.rayadev.byoc.model.ConverterUtil;

class MyTextWatcherUtils {

    private EditText viewA, viewB;
    private ConverterUtil converter;
    private int userSelection;

    private TextWatcher mTextWatcher;

    MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, ConverterUtil converter) {

        this.userSelection = userSelection;
        this.converter = converter;

        this.viewA = viewA;
        this.viewB = viewB;

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
        if(!editTextAInputString.equals("")) {
            double input = Double.parseDouble(viewA.getText().toString());

            double result = converter.convert(input);
            viewB.setText(String.valueOf(result));
        }
        else{
            viewB.setText(""); //This line is cuasing the loop

        }

    }

    private void runConversionBA(ConverterUtil converter) {

        String editTextBInputString = String.valueOf(viewB.getText());
        if(!editTextBInputString.equals("")) {
            double input = Double.parseDouble(viewB.getText().toString());

            double result = converter.convert(input);
            viewA.setText(String.valueOf(result));
        }
        else{
            viewA.setText("");


        }

    }


}
