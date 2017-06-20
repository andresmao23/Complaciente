package com.amcaicedo.sena.complaciente.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amcaicedo.sena.complaciente.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaludosFragment extends Fragment {


    public SaludosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saludos, container, false);
        return v;
    }

}
