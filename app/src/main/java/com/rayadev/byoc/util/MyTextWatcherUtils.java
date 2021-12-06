package com.rayadev.byoc.util;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.rayadev.byoc.util.ConverterUtil;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MyTextWatcherUtils {

    private EditText viewA, viewB;
    private ConverterUtil converter;
    private int userSelection;
    private double currencyValue;

    private String tempUserInput;
    private String[] tempCombos;

    private TextWatcher mTextWatcher;


    public MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, ConverterUtil converter) {

        this.userSelection = userSelection;
        this.converter = converter;

        this.viewA = viewA;
        this.viewB = viewB;

        //Favorites keyboard gets all weird when you mess with the flag here. Displays regular text
        viewA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        viewB.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }

    //Constructor for Converters + Currency
    public MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, double currencyValue) {

        this.userSelection = userSelection;
        this.viewA = viewA;
        this.viewB = viewB;
        this.currencyValue = currencyValue;

        //Favorites keyboard gets all weird when you mess with the flag here. Displays regular text
        viewA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        viewB.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }

    //Constructor for temp
    public MyTextWatcherUtils(int userSelection, EditText viewA, EditText viewB, String tempUserInput, String[] tempCombos) {
        this.userSelection = userSelection;
        this.viewA = viewA;
        this.viewB = viewB;
        this.tempUserInput = tempUserInput;
        this.tempCombos = tempCombos;

        //Allows for negative number on keyboard input
        viewA.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_SIGNED);
        viewB.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_SIGNED);


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

                    case 3:
                        if(viewA.isFocused()) {
                            runTempConversionAB();
                        }
                        break;
                    case 4:
                        if(viewB.isFocused()) {
                            runTempConversionBA();
                        }
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
        //Temp does as well.. Should be able to use same constructor
        final boolean b = !editTextAInputString.equals("") && !editTextAInputString.equals(".") && !editTextAInputString.equals("-");
        if(converter == null) {

            if (b) {
                double input = Double.parseDouble(viewA.getText().toString());

                double result = input * currencyValue;
//                String text_Result = String.valueOf(result);

                String text_Result = new DecimalFormat("########.###").format(result);
                viewB.setText(text_Result);

            } else {
                viewB.setText("");

            }

        }

        else {

            if (b) {
                double input = Double.parseDouble(viewA.getText().toString());
                double result;

                if(converter instanceof CustomConverterUtil) {

                    double unitAValue = ((CustomConverterUtil) converter).getUnitAValue();
                    double unitBValue = ((CustomConverterUtil) converter).getUnitBValue();

                    result = ((CustomConverterUtil) converter).convertABCustomUnits(unitAValue, unitBValue, input);
                    Log.i("CTAG", "AB Ran");

                }
                else {
                    result = converter.convert(input);

                }

                String result_String = String.valueOf(result);

                //Handles the text output for small results.
                if(result < 1) {
                    if(result_String.length() < 9)
                    {
                        result_String = new DecimalFormat("0.#######").format(result);
                    }
                    else {
                        result_String = new DecimalFormat("0.#######E00").format(result);
                    }

                }
                else {

                    result_String = new DecimalFormat("#########.###").format(result);
                }

                //Handles text output for large results
                if(result_String.length() > 12){
                    result_String = new DecimalFormat("#######.###E00").format(result);
                }
                viewB.setText(result_String);
            }


            else {
                viewB.setText("");

            }
        }

    }

    private void runConversionBA(ConverterUtil converter) {

        String editTextBInputString = String.valueOf(viewB.getText());

        //Currency provides a null converter
        boolean b = !editTextBInputString.equals("") && !editTextBInputString.equals(".") && !editTextBInputString.equals("-");
        if(converter == null) {
            if (b) {
                double input = Double.parseDouble(viewB.getText().toString());

                double result = input * (1/currencyValue);

                String text_Result = new DecimalFormat("########.###").format(result);
                viewA.setText(text_Result);

            } else {
                viewA.setText("");

            }

        }

        else {
            if (b) {
                double input = Double.parseDouble(viewB.getText().toString());
                double result;

                if(converter instanceof CustomConverterUtil) {

                    double unitAValue = ((CustomConverterUtil) converter).getUnitAValue(); //Should usually be 1.
                    double unitBValue = ((CustomConverterUtil) converter).getUnitBValue();

                    result = ((CustomConverterUtil) converter).convertBACustomUnits(unitAValue, unitBValue, input);
                    Log.i("CTAG", "BA Ran");

                }
                else {
                    result = converter.convert(input);

                }

                String result_String = String.valueOf(result);

                //Handles the text output for small results.
                if(result < 1) {
                    if(result_String.length() < 9)
                    {
                        result_String = new DecimalFormat("0.#######").format(result);
                    }
                    else {
                        result_String = new DecimalFormat("0.#######E00").format(result);
                    }

                }
                else {

                    result_String = new DecimalFormat("#########.###").format(result);
                }

                if(result_String.length() > 12){
                    result_String = new DecimalFormat("#######.###E00").format(result);
                }
                viewA.setText(result_String);


            } else {
                viewA.setText("");


            }
        }

    }

    private void runTempConversionAB() {
        Log.i("TW", "Inside TW AB");

        String editTextAInputString = String.valueOf(viewA.getText());

        if (!editTextAInputString.equals("") && !editTextAInputString.equals(".") && !editTextAInputString.equals("-")) {
            double input = Double.parseDouble(viewA.getText().toString());
            double result = getTempConversion(input, tempUserInput);

            String result_String = String.valueOf(result);

            //Handles the text output for small results.
            if(result < 1) {
                if(result_String.length() < 9)
                {
                    result_String = new DecimalFormat("0.#######").format(result);
                }
                else {
                    result_String = new DecimalFormat("0.#######E00").format(result);
                }

            }
            else {

                result_String = new DecimalFormat("#########.###").format(result);
            }

            //Handles text output for large results
            if(result_String.length() > 12){
                result_String = new DecimalFormat("#######.###E00").format(result);
            }
            viewB.setText(result_String);
        }
        else {
            viewB.setText("");

        }


    }

    private void runTempConversionBA() {
        Log.i("TW", "Inside TW BA");
        String editTextAInputString = String.valueOf(viewB.getText());

        if (!editTextAInputString.equals("") && !editTextAInputString.equals(".") && !editTextAInputString.equals("-")) {
            double input = Double.parseDouble(viewB.getText().toString());
            double result = getTempConversion(input, tempUserInput);

            String result_String = String.valueOf(result);

            //Handles the text output for small results.
            if(result < 1) {
                if(result_String.length() < 9)
                {
                    result_String = new DecimalFormat("0.#######").format(result);
                }
                else {
                    result_String = new DecimalFormat("0.#######E00").format(result);
                }

            }
            else {

                result_String = new DecimalFormat("#########.###").format(result);
            }

            //Handles text output for large results
            if(result_String.length() > 12){
                result_String = new DecimalFormat("#######.###E00").format(result);
            }
            viewA.setText(result_String);
        }
        else {
            viewA.setText("");

        }

    }

    private double getTempConversion(double input, String tempUserInput) {
        double result = 0.0;
        String foundTemp = "";
        for(int i = 0; i < tempCombos.length; i++){
            if(tempUserInput.equals(tempCombos[i])) {
                foundTemp = tempUserInput;
            }
        }

        //Fahrenheit to Celsius
        if(foundTemp.equals(tempCombos[0])) {
            result = (input - 32) * 0.5556;

        }
        //F to K
        if(foundTemp.equals(tempCombos[1])) {
            result = 0.556 * (input + 459.67);
        }
        //C to F
        if(foundTemp.equals(tempCombos[2])){
            result = (1.8*input) + 32;
        }
        //C to K
        if(foundTemp.equals(tempCombos[3])){
            result = input + 273.15;
        }
        //K to F
        if (foundTemp.equals(tempCombos[4])){
            result = ((input - 273.15) * 1.8) + 32;
        }
        //K to C
        if(foundTemp.equals(tempCombos[5])){
            result = input - 273.15;
        }

        return result;

    }


}
