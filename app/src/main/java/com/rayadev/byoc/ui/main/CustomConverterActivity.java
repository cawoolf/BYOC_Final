package com.rayadev.byoc.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rayadev.byoc.R;

/*Activity that gets triggered by the wrench on the ConverterTab

Allows the user to build their own converter, and save it to the HomeTab.
 */

public class CustomConverterActivity extends AppCompatActivity {

    //EditTexts for user input
    private EditText mUnitAName, mUnitAValue, mUnitBName, mUnitBValue;

    //Views for the converter UI
    private TextView mUnitATitleTextView, mUnitBTitleTextView;
    private EditText mUnitAInputEditText, mUnitBInputEditText;
    private ImageButton mConverterInfoButton, mConverterSwapButton;

    private Button mButton;

    //Bottom UI
    private ImageView mAddConverterButton;
    private LinearLayout mBottomUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_converter);
        setUpToolbar();
        linkViews();
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

        //Link ConverterBox Views
        View converterUILayout = findViewById( R.id.custom_converter_cardlayout_include_converter_tab ); // root View id from include

        mUnitATitleTextView = converterUILayout.findViewById(R.id.cardView_UnitATitle_TextView);
        mUnitBTitleTextView = converterUILayout.findViewById(R.id.cardView_UnitBTitle_TextView);

        mUnitAInputEditText = converterUILayout.findViewById(R.id.cardView_UnitAInput_EditText);
        mUnitBInputEditText = converterUILayout.findViewById(R.id.cardView_UnitBInput_EditText);

        mConverterInfoButton = converterUILayout.findViewById(R.id.cardView_InfoButton);
        mConverterSwapButton = converterUILayout.findViewById(R.id.cardView_SwapButton);

        //User inputs
        mUnitAName = findViewById(R.id.custom_converter_unitAName_EditText);
        mUnitAValue = findViewById(R.id.custom_converter_unitAValue_EditText);
        mUnitBName = findViewById(R.id.custom_converter_unitBName_EditText);
        mUnitBValue = findViewById(R.id.custom_converter_unitBValue_EditText);

        //Build Button click
        mButton = findViewById(R.id.build_custom_converter_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildConverter();
            }
        });
    }

    private void buildConverter() {

        mUnitATitleTextView.setText(mUnitAName.getText().toString());
        mUnitBTitleTextView.setText(mUnitBName.getText().toString());

    }
}