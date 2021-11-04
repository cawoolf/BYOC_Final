package com.rayadev.byoc.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rayadev.byoc.R;

import java.util.ArrayList;

public class SpinnerScrollViewFragment extends Fragment {

    private final int layoutID;
    private UserConverterSelection mUserConverterSelection;
    private String mConverterUnitAName = "";
    private String mConverterUnitBName = "";
    private String storedUnitAName ="";
    private String storedUnitBName = "";
    private String mUnitCategory = "";


//    public SpinnerScrollViewFragment() {
//        // Required empty public constructor
//    }

    public SpinnerScrollViewFragment(int layoutID, UserConverterSelection userConverterSelection) {

        this.mUserConverterSelection = userConverterSelection;
        this.layoutID = layoutID;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(layoutID, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Handle all view interactions here.
        //Handle user selection of units, based on layout ID
        createScrollViewLogic(view);

    }

    private void createScrollViewLogic(View view) {

        //Get the set of unit View IDs based on the layout being used.
        //Declare all of this in an arrays resource file.
        Log.i("STAG", layoutID +"");

        if(layoutID == R.layout.spinner_scrollview_area) {
            mUnitCategory = getString(R.string.spinner_area_title);

            int[] areaScrollViewA_ViewIDs = {R.id.AA1, R.id.AA2, R.id.AA3, R.id.AA4, R.id.AA5,
            R.id.AA6, R.id.AA7, R.id.AA8};

            int[] areaScrollViewB_ViewIDs = {R.id.AB1, R.id.AB2, R.id.AB3, R.id.AB4, R.id.AB5,
            R.id.AB6, R.id.AB7,R.id.AB8};

            linkSpinnerViews(view, areaScrollViewA_ViewIDs, areaScrollViewB_ViewIDs);

        }


        if(layoutID == R.layout.spinner_scrollview_distance) {

            mUnitCategory = getString(R.string.spinner_distance_title);

            int[] distanceScrollViewA_ViewIDs = {R.id.DA1, R.id.DA2, R.id.DA3, R.id.DA4,
                    R.id.DA5, R.id.DA6, R.id.DA7, R.id.DA8};

            int[] distanceScrollViewB_ViewIDs = {R.id.DB1, R.id.DB2, R.id.DB3, R.id.DB4,
                    R.id.DB5, R.id.DB6, R.id.DB7, R.id.DB8};

            linkSpinnerViews(view, distanceScrollViewA_ViewIDs, distanceScrollViewB_ViewIDs);

        }

        if(layoutID == R.layout.spinner_scrollview_currency) {

            mUnitCategory = getString(R.string.spinner_currency_title);

            int[] currencyScrollViewA_ViewIDs = {R.id.CURA1, R.id.CURA2, R.id.CURA3,
            R.id.CURA4};

            int[] currencyScrollViewB_ViewIds = {R.id.CURB1, R.id.CURB2, R.id.CURB3, R.id.CURB3,
            R.id.CURB4};

            linkSpinnerViews(view, currencyScrollViewA_ViewIDs, currencyScrollViewB_ViewIds);
        }

        if(layoutID == R.layout.spinner_scrollview_temperature) {

            mUnitCategory = getString(R.string.spinner_temperature_title);

            int[] scrollViewA_IDs = {R.id.Temp_A1, R.id.Temp_A2, R.id.Temp_A3};
            int[] scrollViewB_IDs = {R.id.Temp_B1, R.id.Temp_B2, R.id.Temp_B3};

            linkSpinnerViews(view, scrollViewA_IDs, scrollViewB_IDs);
        }

        if(layoutID == R.layout.spinner_scrollview_time) {
            mUnitCategory = getString(R.string.spinner_time_title);
            int[] scrollViewA_IDs = {R.id.TA1, R.id.TA2, R.id.TA3, R.id.TA4, R.id.TA5,
                    R.id.TA6, R.id.TA7, R.id.TA8};
            int[] scrollViewB_IDs = {R.id.TB1, R.id.TB2, R.id.TB3, R.id.TB4,
                    R.id.TB5, R.id.TB6, R.id.TB7, R.id.TB8};

            linkSpinnerViews(view, scrollViewA_IDs, scrollViewB_IDs);

        }

        if(layoutID == R.layout.spinner_scrollview_volume) {
            mUnitCategory = getString(R.string.spinner_volume_title);

            int[] scrollViewA_IDs = {R.id.VA1, R.id.VA2, R.id.VA3, R.id.VA4, R.id.VA5,
            R.id.VA6, R.id.VA8, R.id.VA9};
            int[] scrollViewB_IDs = {R.id.VB1, R.id.VB2, R.id.VB3, R.id.VB4, R.id.VB5, R.id.VB6,
            R.id.VB7, R.id.VB8};

            linkSpinnerViews(view, scrollViewA_IDs, scrollViewB_IDs);

        }

        if(layoutID == R.layout.spinner_scrollview_weight) {
            mUnitCategory = getString(R.string.spinner_weight_title);

            int [] weightScrollViewA_ViewIDs = {R.id.WA1, R.id.WA2, R.id.WA3, R.id.WA4,
                R.id.WA5, R.id.WA6};

            int[] weightScrollViewB_ViewIDs = {R.id.WB1, R.id.WB2, R.id.WB3, R.id.WB4, R.id.WB5, R.id.WB6};

            linkSpinnerViews(view, weightScrollViewA_ViewIDs, weightScrollViewB_ViewIDs);

        }

//        if(layoutID == R.layout.spinner_scrollview_area) {
//            int areaScrollViewA_ViewIDs = int int[] {R.id.D}
//        }


    }

    private void linkSpinnerViews(View view, int[]unitAIDs, int[]unitBIDs) {

        final ArrayList<TextView> mTextViewAList = new ArrayList<>();
        final ArrayList<TextView> mTextViewBList = new ArrayList<>();

        int i = 0;
        while(i < unitAIDs.length) {
           TextView unitATextView = view.findViewById(unitAIDs[i]);
           TextView unitBTextView = view.findViewById(unitBIDs[i]);

           mTextViewAList.add(unitATextView);
           mTextViewBList.add(unitBTextView);
           i++;
        }

        buildSpinnerUI(view, mTextViewAList, mTextViewBList);

    }

    private void buildSpinnerUI(View view, ArrayList<TextView> mTextViewAList, ArrayList<TextView> mTextViewBList){


        Log.i("TAG", mTextViewAList.size()+"");
        for(final TextView textView : mTextViewAList) {
            Log.i("TAG", textView.toString());

            final int[] a = new int[1];
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                 // This is for removing the color of the other selected units.
                    for(TextView textView : mTextViewAList) {
                        textView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.cardViewBackGroundColor, null));
                    }

                    textView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_border_square, null));

                    mConverterUnitAName = textView.getText().toString();
                    storedUnitAName = mConverterUnitAName;
                    Log.i("TAGS",textView.getText().toString());

                    sendConverterName();

                }

            });
        }

        for(final TextView textView : mTextViewBList) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(TextView tv2 : mTextViewBList) {
                        tv2.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.cardViewBackGroundColor, null));
                    }


                    textView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.edit_text_border_square, null));

                    mConverterUnitBName = textView.getText().toString();
                    storedUnitBName = mConverterUnitBName;

                    Log.i("TAGS",textView.getText().toString());
                    sendConverterName();

                }

            });
        }

    }

    private void sendConverterName() {

            //This makes the converter automatically select both units, and show the keyboard.
            if (mConverterUnitAName.equals("")) {
                mConverterUnitAName = storedUnitAName;
            }

            if (mConverterUnitBName.equals("")) {
                mConverterUnitBName = storedUnitBName;
            }


        Log.i("CTAG", mConverterUnitAName + " CA" + "\n" + mConverterUnitBName + " CB");

        if(!mConverterUnitAName.equals("") && !mConverterUnitBName.equals("")) {
            String converterName = mConverterUnitAName + mConverterUnitBName;

            mUserConverterSelection.sendConverterName(converterName);
            mUserConverterSelection.setUnitNames (mUnitCategory, mConverterUnitAName, mConverterUnitBName);

            //Toast to prompt user that the Converter is set and ready for use.
            Toast.makeText(getActivity(), mConverterUnitAName + " : " + mConverterUnitBName, Toast.LENGTH_SHORT).show();

            Log.i("TAGS", converterName);
            mConverterUnitAName ="";
            mConverterUnitBName="";


        }
    }

    public interface UserConverterSelection {

        void sendConverterName(String converterName);
        void setUnitNames (String converterCategory, String converterUnitAName, String converterUnitBName); //Use this more to interact with the enums

    }

}