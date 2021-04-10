package com.rayadev.byoc.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rayadev.byoc.R;


//The main fragment that allows the user to run conversions, and set up a converter to be saved to the HomeSetTab
public class ConverterTabFragment extends Fragment {


    private int spinnerID;

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

        buildSpinner(view);

        return view;


    }

    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


//        setSpinnerScrollViewFragment(spinnerID);



    }


    private void buildSpinner(View view) {
        String [] values =
                {"Time at Residence","Under 6 months","6-12 months","1-2 years","2-4 years","4-8 years","8-15 years","Over 15 years",};
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selected = parentView.getItemAtPosition(position).toString();
                Context context = parentView.getContext();
                CharSequence text = selected;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Replaces the ScrollViews for unit selection based on the spinner menu choice.
    private void setSpinnerScrollViewFragment(int layoutID) {
        SpinnerScrollViewFragment mFragment = new SpinnerScrollViewFragment(layoutID);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.spinner_frame_layout, mFragment);

        // Complete the changes added above
        ft.commit();

    }

}