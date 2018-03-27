package cz.numsolution.cfdpal.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.numsolution.cfdpal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculationFragment extends Fragment {


    public CalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculation, container, false);
    }

}
