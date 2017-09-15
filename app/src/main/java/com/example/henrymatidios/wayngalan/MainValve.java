package com.example.henrymatidios.wayngalan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainValve.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainValve#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainValve extends Fragment {
    private LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_valve, (ViewGroup) container, false);
    }
}
