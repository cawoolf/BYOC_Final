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

        if(layoutID == R.layout.spinner_scrollview_distance) {

            int[] distanceScrollViewA_ViewIDs = new int[]{R.id.DA1, R.id.DA2, R.id.DA3, R.id.DA4,
                    R.id.DA5, R.id.DA6, R.id.DA7, R.id.DA8};

            int[] distanceScrollViewB_ViewIDs = new int[]{R.id.DB1, R.id.DB2, R.id.DB3, R.id.DB4,
                    R.id.DB5, R.id.DB6, R.id.DB7, R.id.DB8};

            linkSpinnerViews(view, distanceScrollViewA_ViewIDs, distanceScrollViewB_ViewIDs);

        }

        if(layoutID == R.layout.spinner_scrollview_currency) {
            int[] currencyScrollViewA_ViewIDs = new int[] {R.id.CURA1, R.id.CURA2, R.id.CURA3,
            R.id.CURA4};

            int[] currencyScrollViewB_ViewIds = new int[] {R.id.CURB1, R.id.CURB2, R.id.CURB3, R.id.CURB3,
            R.id.CURB4};

            linkSpinnerViews(view, currencyScrollViewA_ViewIDs, currencyScrollViewB_ViewIds);
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
            mUserConverterSelection.setUnitNames (mConverterUnitAName, mConverterUnitBName);

            //Toast to prompt user that the Converter is set and ready for use.
            Toast.makeText(getActivity(), mConverterUnitAName + " : " + mConverterUnitBName, Toast.LENGTH_SHORT).show();

            Log.i("TAGS", converterName);
            mConverterUnitAName ="";
            mConverterUnitBName="";


        }
    }

    public interface UserConverterSelection {

        void sendConverterName(String converterName);
        void setUnitNames (String converterUnitAName, String converterUnitBName); //Use this more to interact with the enums

    }

}